package com.diga.orm.configuration;

import com.diga.db.core.DB;
import com.diga.db.core.factory.ResultMapFactory;
import com.diga.db.factory.DBFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DBFactoryBean implements FactoryBean<DB> {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResultMapFactoryBean resultMapFactoryBean;

    @Override
    public DB getObject() throws Exception {
        DBFactory dbFactory = new DBFactory(dataSource.getConnection());
        dbFactory.setResultMapFactory(resultMapFactoryBean.getResultMapFactory());
        return dbFactory.getDB();
    }

    @Override
    public Class<?> getObjectType() {
        return DB.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
