package com.diga.orm.handler.pom.ext;

import com.diga.generic.utils.StringUtils;
import com.diga.orm.handler.pom.AbstractPomHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;

import java.util.Map;

public class MybatisPomHandler extends AbstractPomHandler {

    public MybatisPomHandler(TableDetail tableDetail) {
        super(tableDetail);
    }

    @Override
    protected void mapperDependencies(Map<String, Object> vm) {
        StringUtils.SBuilder sb = StringUtils.to();
        sb.to("        <dependency>\n");
        sb.to("            <groupId>org.mybatis.spring.boot</groupId>\n");
        sb.to("            <artifactId>mybatis-spring-boot-starter</artifactId>\n");
        sb.to("            <version>1.3.2</version>\n");
        sb.to("        </dependency>\n");
        sb.to("        <dependency>\n");
        sb.to("            <groupId>com.github.pagehelper</groupId>\n");
        sb.to("            <artifactId>pagehelper-spring-boot-starter</artifactId>\n");
        sb.to("            <version>1.2.5</version>\n");
        sb.to("        </dependency>\n");

        vm.put("mapperDependencies", sb.toString());
    }
}
