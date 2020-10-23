package com.diga.orm.handler.properties.ext;

import com.diga.orm.handler.properties.AbstractAppHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;

import java.util.Map;

public class JdbcAppHandler extends AbstractAppHandler {
    public JdbcAppHandler(TableDetail tableDetail) {
        super(tableDetail);
    }

    @Override
    protected void applicationExtend(Map<String, Object> vm) {

    }
}
