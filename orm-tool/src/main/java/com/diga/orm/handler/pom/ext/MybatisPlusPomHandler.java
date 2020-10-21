package com.diga.orm.handler.pom.ext;

import com.diga.generic.utils.StringUtils;
import com.diga.orm.handler.pom.AbstractPomHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;

import java.util.Map;

public class MybatisPlusPomHandler extends AbstractPomHandler {
    public MybatisPlusPomHandler(TableDetail tableDetail) {
        super(tableDetail);
    }

    @Override
    protected void mapperDependencies(Map<String, Object> vm) {
        StringUtils.SBuilder sb = StringUtils.to();
        sb.to("        <dependency>\n");
        sb.to("            <groupId>com.baomidou</groupId>\n");
        sb.to("            <artifactId>mybatis-plus-boot-starter</artifactId>\n");
        sb.to("            <version>3.1.0</version>\n");
        sb.to("        </dependency>\n");
        sb.to("        <dependency>\n");
        sb.to("            <groupId>com.github.pagehelper</groupId>\n");
        sb.to("            <artifactId>pagehelper</artifactId>\n");
        sb.to("            <version>5.1.4</version>\n");
        sb.to("        </dependency>\n");

        vm.put("mapperDependencies", sb.toString());
    }
}
