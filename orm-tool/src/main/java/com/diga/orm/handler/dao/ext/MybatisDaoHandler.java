package com.diga.orm.handler.dao.ext;

import com.diga.generic.utils.FileUtils;
import com.diga.generic.utils.ModelUtils;
import com.diga.generic.utils.StringUtils;
import com.diga.generic.utils.URLUtils;
import com.diga.orm.common.CodeEnum;
import com.diga.orm.common.SqlTypeCommon;
import com.diga.orm.handler.dao.AbstractDaoHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.Code;
import com.diga.orm.vo.ColumnDetail;
import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;

import java.util.*;

@EqualsAndHashCode
public class MybatisDaoHandler extends AbstractDaoHandler {

    public MybatisDaoHandler(TableDetail tableDetail) {
        super(tableDetail);
    }

    @Override
    public void generateCode(List<Code> codeList) {
        Code javaCode = new Code(CodeEnum.JAVA, tableDetail.getEntityName(), null);
        Code xmlCode = new Code(CodeEnum.XML, tableDetail.getEntityName(), null);

        javaCode.setBody(generateJavaCode());
        xmlCode.setBody(generateXmlCode());
    }

    /**
     * 生成 <select> 标签的方法
     *
     * @param statementId   标签名称
     * @param parameterType 参数类型
     * @param resultType    返回结果
     * @param sql           具体查询的 sql
     * @param isType        区分返回类型是 resultMap 还是 resultType
     * @return
     */
    private String generateSelectStatement(String statementId, String parameterType, String resultType, String sql, boolean isType) {
        StringUtils.SBuilder sb = StringUtils.to();

        sb.to("\t<select id=\"", statementId, "\"");
        if (isType) {
            sb.to(" resultType=\"", resultType, "\"");
        } else {
            sb.to(" resultMap=\"", resultType, "\"");
        }
        sb.to(" parameterType=\"", parameterType, "\" >\n");

        sb.to("\t\t", sql, "\n");

        sb.to("\t</select>\n\n");
        return sb.toString();
    }

    /**
     * 生成 <update> 标签的方法
     *
     * @param statementId   标签名称
     * @param parameterType 参数类型
     * @param sql           具体查询的 sql
     * @return
     */
    private String generateUpdateStatement(String statementId, String parameterType, String sql) {
        StringUtils.SBuilder sb = StringUtils.to();

        sb.to("\t<update id=\"", statementId, "\" resultType=\"int\"");
        sb.to(" parameterType=\"", parameterType, "\" >\n");

        sb.to("\t\t", sql, "\n");

        sb.to("\t</update>\n\n");
        return sb.toString();
    }

    /**
     * 生成 <insert> 标签的方法
     *
     * @param statementId 标签名称
     * @param sql         具体执行的 sql
     */
    private String generateInsertStatement(String statementId, String sql) {
        StringUtils.SBuilder sb = StringUtils.to();
        sb.to("\t<insert id=\"", statementId, "\" resultType=\"int\"");
        sb.to(" parameterType=\"", StringUtils.lowerFirstCase(tableDetail.getEntityName()), "\" >\n");

        sb.to("\t\t", sql, "\n");

        sb.to("\t</insert>\n\n");
        return sb.toString();
    }


    /**
     * 生成 <delete> 标签的方法
     *
     * @param statementId   标签名称
     * @param parameterType 参数类型
     * @param sql           具体执行的 sql
     */
    private String generateDeleteStatement(String statementId, String parameterType, String sql) {
        StringUtils.SBuilder sb = StringUtils.to();
        sb.to("\t<delete id=\"", statementId, "\" resultType=\"int\"");
        sb.to(" parameterType=\"", parameterType, "\" >\n");

        sb.to("\t\t", sql, "\n");

        sb.to("\t</delete>\n\n");
        return sb.toString();
    }


    /**
     * 根据参数列表生成方法名称
     *
     * @param methodType 方法类型
     * @param columnList 参数列表
     * @return
     */
    private String generateMethodNameByParameterList(String methodType, List<ColumnDetail> columnList) {
        String s = "";
        for (int i = 0; i < columnList.size(); i++) {
            s += StringUtils.firstUpper(columnList.get(i).getProperty());
            if (i != columnList.size() - 1) {
                s += "And";
            }
        }

        switch (methodType) {
            case "SELECT":
                return "selectBy" + s;

            case "UPDATE":
                return "updateBy" + s;

            case "DELETE":
                return "deleteBy" + s;
        }

        return null;
    }

    /**
     * 根据参数列表生成条件表达式
     *
     * @param columnList 参数列表
     * @return
     */
    private String generateExprByParameterList(List<ColumnDetail> columnList) {
        String s = "";
        for (int i = 0; i < columnList.size(); i++) {
            s += (columnList.get(i).getColumn() + " = #{" + columnList.get(i).getProperty() + "}");
            if (i != columnList.size() - 1) {
                s += " and ";
            }
        }
        return s;
    }

    /**
     * 生成 mybatis 对应的 xml 代码
     *
     * @return
     */
    private String generateXmlCode() {
        StringUtils.SBuilder sb = StringUtils.to();

        // 生成 result-map
        sb.to(generateResultMap());
        // 生成 sql 片段
        sb.to(generateSqlSection());

        // 实体类别名
        String typeAliasEntity = StringUtils.lowerFirstCase(tableDetail.getEntityName());


        // 生成基于主键的字段查询
        if (primaryIndex.size() > 0) {
            // selectByPrimary
            String primaryExpr = generateExprByParameterList(primaryIndex);
            String primarySelect = "SELECT <include refid=\"generalSql\"/> FROM " + tableDetail.getTableName() + " WHERE " + primaryExpr;


            // updateByPrimary
            StringBuilder primaryUpdate = new StringBuilder("UPDATE ");
            primaryUpdate.append(tableDetail.getTableName()).append("\n");
            primaryUpdate.append("\t\t<set>\n");
            tableDetail.getColumnDetailList().forEach(column -> {
                if (!org.apache.commons.lang3.StringUtils.isBlank(column.getKey()) && SqlTypeCommon.IndexType.PRI == SqlTypeCommon.IndexType.valueOf(column.getKey())) {
                    primaryUpdate.append("\t\t\t<if test=\"").append(column.getProperty()).append(" != null\">").append(column.getColumn()).append(" = #{").append(column.getProperty()).append("},</if>\n");
                }
            });
            primaryUpdate.append("\t\t</set>\n");
            primaryUpdate.append("\t\tWHERE ").append(primaryExpr);

            // deleteByPrimary 这里我们加一个 LIMIT 1 是为了保险
            String primaryDelete = "DELETE FROM " + tableDetail.getTableName() + " WHERE " + primaryExpr + " LIMIT 1";


            if (primaryIndex.size() > 1) {
                sb.to(generateSelectStatement("selectByPrimary", "map", "generalMap", primarySelect, false));
                sb.to(generateUpdateStatement("updateByPrimary", "map", primaryUpdate.toString()));
                sb.to(generateDeleteStatement("deleteByPrimary", "map", primaryDelete));
            } else {
                sb.to(generateSelectStatement("selectByPrimary", primaryIndex.get(0).getJavaType().getName(), "generalMap", primarySelect, false));
                sb.to(generateUpdateStatement("updateByPrimary", primaryIndex.get(0).getJavaType().getName(), primaryUpdate.toString()));
                sb.to(generateDeleteStatement("deleteByPrimary", primaryIndex.get(0).getJavaType().getName(), primaryDelete));
            }
        }


        StringUtils.SBuilder insert = StringUtils.to("INSERT INTO ", tableDetail.getTableName(), "(\n");
        StringUtils.SBuilder insertSelective = StringUtils.to("INSERT INTO ", tableDetail.getTableName(), "(\n");


        StringUtils.SBuilder selectNames = StringUtils.to();
        StringUtils.SBuilder names = StringUtils.to();

        StringUtils.SBuilder selectValues = StringUtils.to();
        StringUtils.SBuilder values = StringUtils.to();


        for (int i = 0; i < tableDetail.getColumnDetailList().size(); i++) {
            ColumnDetail column = tableDetail.getColumnDetailList().get(i);
            names.to(column.getColumn());
            selectNames.to("\t\t\t<if test=\"", column.getProperty(), " != null\" >\n");
            selectValues.to("\t\t\t<if test=\"", column.getProperty(), " != null\" >\n");

            values.to("#{", column.getProperty(), "}");
            if (i != tableDetail.getColumnDetailList().size() - 1) {
                names.to(",");
                selectNames.to("\t\t\t\t", column.getColumn(), "\n");

                values.to(",");
                selectValues.to("\t\t\t\t#{", column.getProperty(), "},\n");
            }

            selectNames.to("\t\t\t</if>\n");
            selectValues.to("\t\t\t</if>\n");
        }

        insert.to(names.toString(), ") VALUES(", values.toString(), ")");
        insertSelective.to("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\n");
        insertSelective.to(selectNames.toString());
        insertSelective.to("\t\t</trim>\n");

        insertSelective.to("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\n");
        insertSelective.to(selectValues.toString());
        insertSelective.to("\t\t</trim>\n");

        sb.to(generateInsertStatement("insert", insert.toString()));
        sb.to(generateInsertStatement("insertSelective", insertSelective.toString()));

        // 基于唯一索引查询
        onlyIndex.forEach(column -> {
            String selectStatementId = "selectBy" + StringUtils.firstUpper(column.getProperty());
            String containStatementId = "containBy" + StringUtils.firstUpper(column.getProperty());

            String parameterType = column.getJavaType().getName();
            String expr = column.getColumn() + " = #{" + column.getProperty() + "}";

            sb.to(generateSelectStatement(selectStatementId, parameterType, "generalMap", "SELECT <include refid=\"generalSql\"/> FROM " + tableDetail.getTableName() + " WHERE " + expr, false));
            sb.to(generateSelectStatement(containStatementId, parameterType, "int", "SELECT COUNT(1) FROM " + tableDetail.getTableName() + " WHERE " + expr, false));
        });

        // 基于普通索引或组合索引查询
        mulIndex.forEach((indexName, columnList) -> {
            String expr = generateExprByParameterList(columnList);
            String statementId = "selectBy" + generateMethodNameByParameterList("SELECT", columnList);
            String sql = "SELECT <include refid=\"generalSql\"/> FROM " + tableDetail.getTableName() + " WHERE " + expr;
            if (columnList.size() > 1) {
                sb.to(generateSelectStatement(statementId, "map", "generalMap", sql, false));
            } else {
                sb.to(generateSelectStatement(statementId, columnList.get(0).getJavaType().getName(), "generalMap", sql, false));
            }
        });

        // 基于当前实体类进行查询
        StringUtils.SBuilder selectByExampleEntitySql = StringUtils.to("SELECT <include refid=\"generalSql\"/> FROM ", tableDetail.getTableName(), "\n");
        selectByExampleEntitySql.to("\t\t<where>\n");

        for (int i = 0; i < tableDetail.getColumnDetailList().size(); i++) {
            ColumnDetail column = tableDetail.getColumnDetailList().get(i);
            selectByExampleEntitySql.to("\t\t\t<if test=\"", column.getProperty(), "!=null\">", column.getColumn(), "=#{", column.getProperty(), "}</if>\n");
        }

        selectByExampleEntitySql.to("\t\t</where>\n");

        sb.to(generateSelectStatement("selectByExampleEntity", typeAliasEntity, "generalMap", selectByExampleEntitySql.toString(), false));


        String mapperXml = FileUtils.readFile(URLUtils.filepath("model/mapper.model"));
        Map<String, Object> vm = new HashMap();
        vm.put("statementList", sb.toString());
        return ModelUtils.render(mapperXml, vm);
    }

    /**
     * 生成 sql 片段
     *
     * @return
     */
    private String generateSqlSection() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t<sql id=\"generalSql\">\n");

        sb.append("\t\t");

        for (int i = 0; i < tableDetail.getColumnDetailList().size(); i++) {
            ColumnDetail column = tableDetail.getColumnDetailList().get(i);
            sb.append(column.getColumn());
            if (i != tableDetail.getColumnDetailList().size() - 1) {
                sb.append(",");
            }
        }

        sb.append("\n");

        sb.append("\t</sql>");
        return sb.toString();
    }

    /**
     * 生成实体类对应的结果映射
     *
     * @return
     */
    private String generateResultMap() {
        StringUtils.SBuilder sb = new StringUtils.SBuilder();

        sb.to("\t<resultMap id=\"generalMap\" type=\"", StringUtils.lowerFirstCase(tableDetail.getEntityName()), "\">\n");

        primaryIndex.forEach(column -> {
            sb.to("\t\t<!-- 主键索引, 建议使用这个字段作为条件进行查询 -->\n");
            sb.to("\t\t<id property=\"", column.getProperty(), "\" column=\"", column.getColumn(), "\"/>\n");
            sb.to("\n");
        });

        onlyIndex.forEach(column -> {
            sb.to("\t\t<!-- 唯一索引, 建议使用这个字段作为条件进行查询 -->\n");
            sb.to("\t\t<result property=\"", column.getProperty(), "\" column=\"", column.getColumn(), "\"/>\n");
            sb.to("\n");
        });

        mulIndex.forEach((indexName, columnList) -> {
            if (columnList.size() == 1) {
                sb.to("\t\t<!-- 普通索引, 建议使用这个字段作为条件进行查询 -->\n");
                sb.to("\t\t<result property=\"", columnList.get(0).getProperty(), "\" column=\"", columnList.get(0).getColumn(), "\"/>\n");
                sb.to("\n");
            } else {
                sb.to("\t\t<!-- 组合索引, 建议使用组合索引的全部字段作为条件或者组合索引的第一个字段进行查询 -->\n");
                for (ColumnDetail columnDetail : columnList) {
                    sb.to("\t\t<!-- 组合索引中的顺序[\"", columnDetail.getSeqInIndex().toString(), "\"] -->\n");
                    sb.to("\t\t<result property=\"", columnDetail.getProperty(), "\" column=\"", columnDetail.getColumn(), "\"/>\n");
                }
                sb.to("\n");
            }
        });

        // 普通字段遍历
        tableDetail.getColumnDetailList().forEach(column -> {
            if (org.apache.commons.lang3.StringUtils.isBlank(column.getKey())) {
                sb.to("\t\t<result property=\"", column.getProperty(), "\" column=\"", column.getColumn(), "\"/>\n");
            }
        });

        sb.to("\t</resultMap>\n");

        return sb.toString();
    }


    /**
     * 生成 mybatis 对应的 mapper 代码, 生成的方法如下:
     * <p>
     * selectByPrimary
     * updateByPrimary
     * deleteByPrimary
     * insert
     * insertSelective
     * <p>
     * selectByExampleEntity
     *
     * @return
     */
    private String generateJavaCode() {
        // 依赖包部分
        StringUtils.SBuilder importBuilder = StringUtils.to();

        // 核心代码部分
        StringUtils.SBuilder bodyBuilder = StringUtils.to();

        Set<String> importBody = new HashSet<String>();

        bodyBuilder.to("public class ", tableDetail.getEntityName(), "Mapper {\n");

        String methodDesc = "";
        List<StringParam> paramList = Lists.newLinkedList();

        String returnEntity = super.entityPackage + "." + tableDetail.getEntityName();
        importBody.add(returnEntity);

        // 基于主键查询
        boolean primaryLengthStatus = super.primaryIndex.size() > 1;
        for (int i = 0; i < super.primaryIndex.size(); i++) {
            ColumnDetail columnDetail = primaryIndex.get(i);
            // 字段备注, 不存在取字段名称
            String fieldDesc = org.apache.commons.lang3.StringUtils.isBlank(columnDetail.getComment()) ? columnDetail.getColumn() : columnDetail.getComment();
            methodDesc += fieldDesc;

            importBody.add(columnDetail.getJavaType().getName());
            if (primaryLengthStatus) {
                importBody.add("org.apache.ibatis.annotations.Param");
                paramList.add(new StringParam("Param(\"" + columnDetail.getProperty() + "\")", columnDetail.getProperty(), columnDetail.getJavaType().getSimpleName(), null));
            } else {
                paramList.add(new StringParam(null, columnDetail.getProperty(), columnDetail.getJavaType().getSimpleName(), null));
            }
        }

        bodyBuilder.to(super.generateInterfaceMethod(returnEntity, "selectByPrimary", paramList, "基于主键字段 [" + methodDesc + "] 查询", "返回" + methodDesc + "的结果"));
        bodyBuilder.to("\n\n");

        // 基于主键的其他操作
        bodyBuilder.to(super.generateInterfaceMethod("int", "updateByPrimary", paramList, "基于主键字段 [" + methodDesc + "] 进行修改操作", "返回数据库影响条数"));
        bodyBuilder.to("\n\n");

        bodyBuilder.to(super.generateInterfaceMethod("int", "deleteByPrimary", paramList, "基于主键字段 [" + methodDesc + "] 进行删除操作", "返回数据库影响条数"));
        bodyBuilder.to("\n\n");

        bodyBuilder.to(super.generateInterfaceMethod("int", "insertSelective", Arrays.asList(new StringParam(null, StringUtils.lowerFirstCase(tableDetail.getEntityName()), returnEntity, null)), "选择性插入对应字段的数据", "返回数据库影响条数"));
        bodyBuilder.to("\n\n");

        bodyBuilder.to(super.generateInterfaceMethod("int", "insert", Arrays.asList(new StringParam(null, StringUtils.lowerFirstCase(tableDetail.getEntityName()), returnEntity, null)), "插入全部字段对应的数据", "返回数据库影响条数"));
        bodyBuilder.to("\n\n");


        // 基于唯一索引查询
        for (int i = 0; i < super.onlyIndex.size(); i++) {
            ColumnDetail columnDetail = onlyIndex.get(i);
            // 字段备注
            String fieldDesc = org.apache.commons.lang3.StringUtils.isBlank(columnDetail.getComment()) ? columnDetail.getColumn() : columnDetail.getComment();
            importBody.add(columnDetail.getJavaType().getName());
            List<StringParam> onlyParamList = Arrays.asList(new StringParam(null, columnDetail.getProperty(), columnDetail.getJavaType().getSimpleName(), null));

            String onlyColumn = StringUtils.humpFirstUpper(columnDetail.getColumn());
            // 生成 selectByUsername
            bodyBuilder.to(super.generateInterfaceMethod(returnEntity, "selectBy" + onlyColumn, onlyParamList, "基于 [" + fieldDesc + "] 字段查询,此方法走唯一索引", "返回符合条件的唯一一条结果"));
            bodyBuilder.to("\n\n");

            // 生成 containByUsername
            bodyBuilder.to(super.generateInterfaceMethod("int", "containBy" + onlyColumn, onlyParamList, "是否包含 [" + fieldDesc + "] 字段对应的结果,此方法走唯一索引", "返回结果 > 0表示数据已经存在"));
        }

        // 基于组合索引或普通索引进行查询
        mulIndex.forEach((indexName, columnDetailList) -> {
            StringBuilder byName = new StringBuilder();
            StringBuilder byDesc = new StringBuilder();
            List<StringParam> mulParamList = new ArrayList();

            boolean columnLengthStatus = columnDetailList.size() > 1;
            for (int i = 0; i < columnDetailList.size(); i++) {
                ColumnDetail columnDetail = columnDetailList.get(i);
                String fieldDesc = org.apache.commons.lang3.StringUtils.isBlank(columnDetail.getComment()) ? columnDetail.getColumn() : columnDetail.getComment();

                byName.append(StringUtils.humpFirstUpper(columnDetail.getColumn()));
                byDesc.append(fieldDesc);

                importBody.add(columnDetail.getJavaType().getName());
                if (columnLengthStatus) {
                    mulParamList.add(new StringParam("Param(\"" + columnDetail.getProperty() + "\")", columnDetail.getProperty(), columnDetail.getJavaType().getSimpleName(), null));
                } else {
                    mulParamList.add(new StringParam(null, columnDetail.getProperty(), columnDetail.getJavaType().getSimpleName(), null));
                }

                if (i != columnDetailList.size() - 1) {
                    byName.append("And");
                    byDesc.append(",");
                }
            }

            bodyBuilder.to(super.generateInterfaceMethod(returnEntity, "selectBy" + byName, mulParamList, "基于字段 [" + byDesc + "] 进行查询, 走索引", "返回符合条件的一条或多条数据"));
            bodyBuilder.to("\n\n");
        });

        // selectByExampleEntity
        importBody.add("java.util.List");
        ArrayList<StringParam> entityParamList = Lists.newArrayList(new StringParam(null, StringUtils.lowerFirstCase(tableDetail.getEntityName()), tableDetail.getEntityName(), null));
        bodyBuilder.to(super.generateInterfaceMethod("List<" + returnEntity + ">", "selectByExampleEntity", entityParamList, "基于实体类进行条件查询", "返回符合条件的一条或多条数据"));

        bodyBuilder.to("}\n");

        importBody.forEach(e -> {
            if (!e.startsWith("java.lang")) {
                importBuilder.to(e);
            }
        });

        return StringUtils.to("package ", super.mapperPackage, ";\n\n").to(importBuilder.toString(), "\n\n").to(bodyBuilder.toString()).toString();
    }


}
