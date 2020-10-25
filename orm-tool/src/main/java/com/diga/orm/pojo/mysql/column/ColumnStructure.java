package com.diga.orm.pojo.mysql.column;

import com.diga.db.annotation.ResultBean;
import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import com.diga.orm.common.SqlTypeCommon;
import com.diga.orm.vo.ColumnDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 表结构信息, 通过调用 show columns from #{tableName} 得到的结果对应的实体类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColumnStructure implements Serializable {
    /**
     * 字段名称
     */
    private String field;

    /**
     * 字段的 SQL 类型
     */
    private String type;

    /**
     * 字段是否为 NULL
     */
    private String isNull;

    /**
     * 字段是否为 主键
     */
    private String key;

    /**
     * 字段默认值
     */
    private String defaultValue;

    /**
     * 字段扩展补充
     */
    private String extra;

    @ResultBean(id = "com.diga.orm.pojo.mysql.column.ColumnStructure")
    public ResultMap tableStructure() {
        ResultMap resultMap = new ResultMap(null, ColumnStructure.class);
        List<Result> resultList = resultMap.getResultList();
        resultList.add(new Result().setColumn("Field").setProperty("field"));
        resultList.add(new Result().setColumn("Type").setProperty("type"));
        resultList.add(new Result().setColumn("Null").setProperty("isNull"));
        resultList.add(new Result().setColumn("Key").setProperty("key"));
        resultList.add(new Result().setColumn("Default").setProperty("defaultValue"));
        resultList.add(new Result().setColumn("Extra").setProperty("extra"));
        return resultMap;
    }

    /**
     * 将字段转换为 VO 对象
     *
     * @param columnIndexList   字段索引
     * @param columnCommentList 字段注释
     * @return
     */
    public ColumnDetail ToVO(List<ColumnIndex> columnIndexList, List<ColumnComment> columnCommentList) {
        ColumnDetail detailVo = new ColumnDetail();
        Class javaType = null;


        for (SqlTypeCommon.mysqlEnum value : SqlTypeCommon.mysqlEnum.values()) {
            if (StringUtils.upperCase(type).startsWith(value.name())) {
                javaType = value.getJavaType();
                break;
            }
        }

        detailVo.setColumn(field);
        detailVo.setSqlType(type);
        detailVo.setJavaType(javaType);
        detailVo.setProperty(com.diga.generic.utils.StringUtils.hump(field));
        detailVo.setAllowNull(isNull.equals("YES"));
        detailVo.setKey(key);
        detailVo.setExtra(extra);
        detailVo.setDefaultValue(defaultValue);

        for (ColumnIndex index : columnIndexList) {
            if (StringUtils.equalsIgnoreCase(index.getColumnName(), field)) {
                detailVo.setKeyName(index.getKeyName());
                detailVo.setSeqInIndex(index.getSeqInIndex());
                detailVo.setCollation(index.getCollation());
                detailVo.setIndexType(index.getIndexType());
                detailVo.setIndexComment(index.getIndexComment());
                break;
            }
        }

        for (ColumnComment comment : columnCommentList) {
            if (StringUtils.equalsIgnoreCase(comment.getColumnName(), field)) {
                detailVo.setComment(comment.getColumnComment());
                break;
            }
        }

        detailVo.setProperty(com.diga.generic.utils.StringUtils.hump(field));
        return detailVo;
    }
}


