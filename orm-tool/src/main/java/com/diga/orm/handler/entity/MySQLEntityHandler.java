package com.diga.orm.handler.entity;

import com.diga.generic.utils.DateTimeUtils;
import com.diga.generic.utils.StringUtils;
import com.diga.orm.common.CodeTypeEnum;
import com.diga.orm.common.DataBaseEnum;
import com.diga.orm.common.RepositoryEnum;
import com.diga.orm.handler.GenerateHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.CodeNode;
import com.diga.orm.vo.ColumnDetail;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySQLEntityHandler implements GenerateHandler {
    private GenerateHandler nextHandler;
    private RepositoryEnum repositoryType;

    public MySQLEntityHandler(GenerateHandler nextHandler, RepositoryEnum repositoryEnum) {
        this.nextHandler = nextHandler;
        this.repositoryType = repositoryEnum;
    }

    /**
     * 能处理的数据库类型
     *
     * @return
     */
    @Override
    public DataBaseEnum handleDataBaseEnum() {
        return DataBaseEnum.MYSQL;
    }

    /**
     * 处理器核心方法
     *
     * @param codeNode    具体代码
     * @param tableDetail 表详情信息
     */
    @Override
    public void handle(CodeNode codeNode, TableDetail tableDetail) {
        codeNode.setCodeTypeEnum(CodeTypeEnum.JAVA);
        List<CodeNode.Code> codeList = codeNode.getCodeList();
        CodeNode.Code code = new CodeNode.Code(tableDetail.getEntityName(), null);
        codeList.add(code);

        // TODO package xxx.xxx.entity;
        StringUtils.SBuilder PACK = StringUtils.to("package xxx.xxx.entity;\n\n");
        Set<Class> IMPORT_CLASS_SET = new HashSet();
        StringUtils.SBuilder IMPORT = StringUtils.to("import lombok.Data;\n\nimport java.io.Serializable;\n");


        String TITLE =
                "/**\n" +
                " * @date        {date}\n" +
                " * @description {description}\n" +
                " */\n";

        String now = DateTimeUtils.getNowDate("yyyy-MM-dd HH:mm:ss");
        String description = org.apache.commons.lang3.StringUtils.isEmpty(tableDetail.getComment()) ? tableDetail.getComment() : tableDetail.getTableName();
        description = description + ",存储引擎:" + tableDetail.getEngine() + ",字符集:" + tableDetail.getCharset();
        TITLE = TITLE.replace("{date}", now).replace("{description}", description);

        StringUtils.SBuilder body = StringUtils.to(TITLE);

        body.to("public class ", StringUtils.firstUpper(tableDetail.getEntityName()), " implements Serializable {\n");

        String FIELD =
                "\t/**{comment}\n" +
                "\t */";

        // 循环遍历字段
        for (ColumnDetail columnDetail : tableDetail.getColumnDetailList()) {
            StringUtils.SBuilder sb = StringUtils.to();

            if (!org.apache.commons.lang3.StringUtils.isEmpty(columnDetail.getKey())) {
                sb.to("\n\t * 所属索引[", columnDetail.getKeyName(),"] {'类型': ", columnDetail.getKey(), ",");
                sb.to("'结构':", columnDetail.getIndexType(), ",");
                sb.to("'顺序':", columnDetail.getSeqInIndex().toString(), ",");
                sb.to("'备注':", columnDetail.getIndexComment(), "}\n");

            }

            if (!org.apache.commons.lang3.StringUtils.isEmpty(columnDetail.getComment())) {
                sb.to("\n\t * ", columnDetail.getComment());
            }

            if (!org.apache.commons.lang3.StringUtils.isEmpty(sb.toString())) {
                body.to("\n", FIELD.replace("{comment}", sb.toString()), "\n");
            }

            Class fieldType = columnDetail.getJavaType();
            IMPORT_CLASS_SET.add(fieldType);

            body.to("\tprivate\t", fieldType.getSimpleName(), "\t", columnDetail.getProperty());
            if (org.apache.commons.lang3.StringUtils.isBlank(columnDetail.getDefaultValue()) && !Date.class.isAssignableFrom(fieldType)) {
                body.to(" = ");
                if (String.class.isAssignableFrom(fieldType)) {
                    body.to("\"", columnDetail.getDefaultValue(), "\"");
                } else if (Number.class.isAssignableFrom(fieldType)) {
                    body.to(columnDetail.getDefaultValue());
                }
            }
            body.to(";\n");
        }

        body.to("}");

        // 解决导入的类的问题
        for (Class importClass : IMPORT_CLASS_SET) {
            if (!importClass.getName().startsWith("java.lang")) {
                IMPORT.to("import ", importClass.getName(), ";\n");
            }
        }

        String codeValue = StringUtils.to(PACK.toString(), IMPORT.toString(), "\n", body.toString(), "\n").toString();
        code.setBody(codeValue);

        CodeNode nextCode = new CodeNode();
        codeNode.setNext(nextCode);
        nextHandler.handle(nextCode, tableDetail);
    }

}
