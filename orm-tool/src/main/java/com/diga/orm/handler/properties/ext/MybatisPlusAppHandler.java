package com.diga.orm.handler.properties.ext;

import com.diga.generic.utils.StringUtils;
import com.diga.orm.handler.properties.AbstractAppHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;

import java.util.Map;

public class MybatisPlusAppHandler extends AbstractAppHandler {
    public MybatisPlusAppHandler(TableDetail tableDetail) {
        super(tableDetail);
    }

    @Override
    protected void applicationExtend(Map<String, Object> vm) {
        StringUtils.SBuilder sb = StringUtils.to();
        sb.to("mybatis-plus:\n");
        sb.to("    mapper-locations: classpath:mybatis/*.xml\n");
        sb.to("    typeAliasesPackage: org.example.pojo\n");
        sb.to("    configuration:\n");
        sb.to("        map-underscore-to-camel-case: true\n");
        sb.to("        cache-enabled: false\n");
        sb.to("        call-setters-on-nulls: true\n");
        sb.to("\n");
        sb.to("    global-config:\n");
        sb.to("        refresh: true\n");
        sb.to("        banner: false\n");
        sb.to("\n");  
        sb.to("    db-config:\n");  
        sb.to("        db-type: mysql\n");
        sb.to("        id-type: UUID\n");
        sb.to("        field-strategy: not_empty\n");
        sb.to("        capital-mode: true\n");
        sb.to("        logic-delete-value: 1\n");
        sb.to("        logic-not-delete-value: 0\n");  

        vm.put("applicationExtend", sb.toString());

    }
}
