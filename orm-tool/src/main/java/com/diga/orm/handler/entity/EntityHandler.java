package com.diga.orm.handler.entity;

import com.diga.generic.utils.DateTimeUtils;
import com.diga.generic.utils.StringUtils;
import com.diga.orm.common.CodeEnum;
import com.diga.orm.common.DataBaseEnum;
import com.diga.orm.common.RepositoryEnum;
import com.diga.orm.handler.GenerateHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.Code;
import com.diga.orm.vo.ColumnDetail;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityHandler implements GenerateHandler {
    private TableDetail tableDetail;
    private RepositoryEnum repositoryType;

    public EntityHandler(RepositoryEnum repositoryEnum, TableDetail tableDetail) {
        this.repositoryType = repositoryEnum;
        this.tableDetail = tableDetail;
    }

    @Override
    public void handle(List<Code> codeList) {
        Code code = new Code(CodeEnum.JAVA, tableDetail.getEntityName(), null);
        // TODO package xxx.xxx.entity;
        StringUtils.SBuilder PACK = StringUtils.to("\npackage xxx.xxx.entity;\n\n");
        Set<Class> IMPORT_CLASS_SET = new HashSet();
        StringUtils.SBuilder IMPORT = StringUtils.to("import lombok.Data;\n\nimport java.io.Serializable;\n");

        if (repositoryType != null) {
            switch (repositoryType) {
                case SPRING_DATA_JPA:
                    IMPORT.to("import javax.persistence.Column;\nimport javax.persistence.Entity;\nimport javax.persistence.Id;\nimport javax.persistence.Table;\n");
                    break;

                case MYBATIS_PLUS:
                    IMPORT.to("import com.baomidou.mybatisplus.annotation.IdType;\nimport com.baomidou.mybatisplus.annotation.TableField;\nimport com.baomidou.mybatisplus.annotation.TableId;\nimport com.baomidou.mybatisplus.annotation.TableName;\n");
                    break;

                case MYBATIS:
                case JDBC:
                    break;

                case DB:
                    IMPORT.to("import com.diga.db.annotation.Column;\nimport com.diga.db.annotation.Id;\n");
                    break;
            }
        }

        String TITLE = "/**\n" +
                " * @date        {date}\n" +
                " * @description {description}\n" +
                " */\n";

        String now = DateTimeUtils.getNowDate("yyyy-MM-dd HH:mm:ss");
        String description = !org.apache.commons.lang3.StringUtils.isEmpty(tableDetail.getComment()) ? tableDetail.getComment() : tableDetail.getTableName();
        description = description + ",存储引擎:" + tableDetail.getEngine() + ",字符集:" + tableDetail.getCharset();
        TITLE = TITLE.replace("{date}", now).replace("{description}", description);

        StringUtils.SBuilder body = StringUtils.to(TITLE);

        if (repositoryType != null) {
            switch (repositoryType) {
                case SPRING_DATA_JPA:
                    body.to("@Entity\n@Table(name = \"", tableDetail.getTableName(), "\")");
                    break;

                case MYBATIS_PLUS:
                    body.to("@TableName(\"", tableDetail.getTableName(), "\")");
                    break;

            }
        }

        body.to("@Data\npublic class ", StringUtils.firstUpper(tableDetail.getEntityName()), " implements Serializable {\n");

        String FIELD = "\t/**{comment}\n" +
                "\t */";

        // 循环遍历字段
        for (ColumnDetail columnDetail : tableDetail.getColumnDetailList()) {
            StringUtils.SBuilder sb = StringUtils.to();

            if (!org.apache.commons.lang3.StringUtils.isEmpty(columnDetail.getKey())) {
                sb.to("\n\t * 所属索引[", columnDetail.getKeyName(), "] {'类型': ", columnDetail.getKey(), ",");
                sb.to("'结构':", columnDetail.getIndexType(), ",");
                sb.to("'顺序':", columnDetail.getSeqInIndex().toString(), ",");
                sb.to("'备注':", columnDetail.getIndexComment(), "}");

            }

            if (!org.apache.commons.lang3.StringUtils.isEmpty(columnDetail.getComment())) {
                sb.to("\n\t * ", columnDetail.getComment());
            }

            if (!org.apache.commons.lang3.StringUtils.isEmpty(sb.toString())) {
                body.to("\n", FIELD.replace("{comment}", sb.toString()));
            }

            Class fieldType = columnDetail.getJavaType();
            IMPORT_CLASS_SET.add(fieldType);

            if (repositoryType != null) {
                switch (repositoryType) {
                    case MYBATIS_PLUS:
                        if (org.apache.commons.lang3.StringUtils.equals(columnDetail.getKey(), "PRI")) {
                            body.to("@TableId\n");
                        }
                        body.to("@TableField(name = \"", columnDetail.getColumn(), "\")");
                        break;

                    case DB:
                    case SPRING_DATA_JPA:
                        if (org.apache.commons.lang3.StringUtils.equals(columnDetail.getKey(), "PRI")) {
                            body.to("@Id\n");
                        }
                        body.to("@Column(name = \"", columnDetail.getColumn(), "\")");
                        break;
                }
            }

            body.to("\n\tprivate\t", fieldType.getSimpleName(), "\t", columnDetail.getProperty());
            if (!org.apache.commons.lang3.StringUtils.isBlank(columnDetail.getDefaultValue()) && !Date.class.isAssignableFrom(fieldType)) {
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

        codeList.add(code);
    }

}
