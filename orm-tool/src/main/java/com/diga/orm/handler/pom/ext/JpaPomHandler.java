package com.diga.orm.handler.pom.ext;

import com.diga.generic.utils.StringUtils;
import com.diga.orm.handler.pom.AbstractPomHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;

import java.util.Map;

public class JpaPomHandler extends AbstractPomHandler {
    public JpaPomHandler(TableDetail tableDetail) {
        super(tableDetail);
    }

    @Override
    protected void mapperDependencies(Map<String, Object> vm) {
        StringUtils.SBuilder sb = StringUtils.to();
        sb.to("        <dependency>\n");
        sb.to("            <groupId>org.springframework.boot</groupId>\n");
        sb.to("            <artifactId>spring-boot-starter-data-jpa</artifactId>\n");
        sb.to("        </dependency>\n");

        vm.put("mapperDependencies", sb.toString());
    }
}
