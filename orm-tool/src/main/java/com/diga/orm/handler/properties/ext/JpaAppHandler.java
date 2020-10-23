package com.diga.orm.handler.properties.ext;

import com.diga.generic.utils.StringUtils;
import com.diga.orm.handler.properties.AbstractAppHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;

import java.util.Map;

public class JpaAppHandler extends AbstractAppHandler {
    public JpaAppHandler(TableDetail tableDetail) {
        super(tableDetail);
    }

    @Override
    protected void applicationExtend(Map<String, Object> vm) {
        StringUtils.SBuilder sb = StringUtils.to();
        sb.to("  jpa:\n");
        sb.to("      database: MySQL\n");
        sb.to("      database-platform: org.hibernate.dialect.MySQL5InnoDBDialect\n");
        sb.to("      show-sql: true\n");
        sb.to("      hibernate:\n");
        sb.to("        ddl-auto: update\n");

        vm.put("applicationExtend", sb.toString());
    }
}