package com.diga.orm.handler.properties.ext;

import com.diga.generic.utils.StringUtils;
import com.diga.orm.handler.dao.ext.MybatisDaoHandler;
import com.diga.orm.handler.properties.AbstractAppHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;

import java.util.Map;

public class MybatisAppHandler extends AbstractAppHandler {
    public MybatisAppHandler(TableDetail tableDetail) {
        super(tableDetail);
        super.generateHandler = new MybatisDaoHandler(tableDetail);
    }

    @Override
    protected void applicationExtend(Map<String, Object> vm) {
        StringUtils.SBuilder sb = StringUtils.to();
        sb.to("mybatis:\n");
        sb.to("    mapper-locations: classpath:mybatis/*.xml\n");
        sb.to("    type-aliases-package: org.example.pojo\n");
        sb.to("\n");
        sb.to("pagehelper:\n");
        sb.to("    helperDialect: MYSQL\n");
        sb.to("    reasonable: true\n");
        sb.to("    supportMethodsArguments: true\n");
        sb.to("    params: count=countSql\n");
        sb.to("    returnPageInfo: check\n");

        vm.put("applicationExtend", sb.toString());

    }
}
