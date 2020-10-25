package com.diga.orm.handler.dao;

import java.util.*;
import java.util.stream.Collectors;

import com.diga.generic.utils.CollectionUtils;
import com.diga.orm.common.SqlTypeCommon;
import com.diga.orm.handler.GenerateHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.Code;
import com.diga.orm.vo.ColumnDetail;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractDaoHandler implements GenerateHandler {
    protected GenerateHandler generateHandler;
    protected TableDetail tableDetail;

    // 主键
    protected List<ColumnDetail> primaryIndex = Lists.newArrayList();

    // 唯一索引
    protected List<ColumnDetail> onlyIndex = Lists.newArrayList();

    // 组合索引|普通索引
    protected Map<String, List<ColumnDetail>> mulIndex = Maps.newLinkedHashMap();

    // 包管理部分 | TODO 日后修改
    protected String mapperPackage = "org.example.mapper";

    // 包管理部分 | TODO 日后修改
    protected String entityPackage = "org.example.pojo";


    public AbstractDaoHandler(TableDetail tableDetail) {
        this.tableDetail = tableDetail;

        // 子类实例化前必定执行
        init();
    }

    private void init() {
        // 索引字段
        List<ColumnDetail> keyIndex = tableDetail.getColumnDetailList().stream().filter(columnDetail -> !StringUtils.isBlank(columnDetail.getKey())).collect(Collectors.toList());

        // 主键字段
        primaryIndex = keyIndex.stream().filter(columnDetail -> SqlTypeCommon.IndexType.valueOf(columnDetail.getKey()) == SqlTypeCommon.IndexType.PRI).collect(Collectors.toList());

        // 唯一索引
        onlyIndex = keyIndex.stream().filter(columnDetail -> SqlTypeCommon.IndexType.valueOf(columnDetail.getKey()) == SqlTypeCommon.IndexType.UNI).collect(Collectors.toList());

        // 组合索引或者普通索引
        keyIndex.stream().forEach(columnDetail -> {
            SqlTypeCommon.IndexType indexType = SqlTypeCommon.IndexType.valueOf(columnDetail.getKey());
            if (indexType != SqlTypeCommon.IndexType.PRI && indexType != SqlTypeCommon.IndexType.UNI) {
                List<ColumnDetail> columnDetailList = mulIndex.get(columnDetail.getKeyName());
                if (columnDetailList == null) {
                    columnDetailList = Lists.newLinkedList();
                    mulIndex.put(columnDetail.getKeyName(), columnDetailList);
                }
                columnDetailList.add(columnDetail);
            }
        });
    }

    public void handle(List<Code> codeList) {
        generateCode(codeList);
        generateHandler.handle(codeList);
    }

    public abstract void generateCode(List<Code> codeList);

    /**
     * 快速生成接口方法模板的方法
     *
     * @param returnType 方法的返回值类型
     * @param methodName 方法名称
     * @param paramList  方法参数列表
     * @param methodDesc 方法的简介
     * @param returnDesc 返回结果说明
     * @return
     */
    protected String generateInterfaceMethod(String returnType, String methodName, List<StringParam> paramList, String methodDesc, String returnDesc) {
        com.diga.generic.utils.StringUtils.SBuilder sb = com.diga.generic.utils.StringUtils.to();
        sb.to("    /**\n");
        sb.to("     * ", methodDesc, "\n");

        paramList.forEach(param -> sb.to("     * @param ", param.paramName, "\n"));
        sb.to("     * @return ", returnDesc, "\n");
        sb.to("     */\n");

        sb.to("    ", returnType, " ", methodName, "(");

        CollectionUtils.forEach(paramList, (index, item, itemList) -> {
            sb.to(item.build());
            if (index < itemList.size() - 1) {
                sb.to(",");
            }
        });
        sb.to(");\n");

        return sb.toString();
    }


    /**
     * 参数对象的模型表达
     */
    protected static class StringParam {
        // 参数名称
        private String paramName;

        // 注解文本
        private String annotationText;

        // 参数类型
        private String paramType;

        // 泛型类型
        private String[] genericTypes;

        public StringParam(String annotationText, String paramName, String paramType, String[] genericTypes) {
            this.annotationText = annotationText;
            this.paramName = paramName;
            this.paramType = paramType;
            this.genericTypes = genericTypes;
        }

        private String build() {
            com.diga.generic.utils.StringUtils.SBuilder sb = new com.diga.generic.utils.StringUtils.SBuilder();

            if (!StringUtils.isBlank(annotationText)) {
                sb.to("@" + annotationText);
            }

            sb.to(paramType);
            if (genericTypes != null && genericTypes.length > 0) {
                sb.to("<");
                for (int i = 0; i < genericTypes.length; i++) {
                    sb.to(genericTypes[i]);
                    if (i != genericTypes.length - 1) {
                        sb.to(",");
                    }
                }
                sb.to(">");
            }

            sb.to(" ", paramName);
            return sb.toString();
        }
    }


}
