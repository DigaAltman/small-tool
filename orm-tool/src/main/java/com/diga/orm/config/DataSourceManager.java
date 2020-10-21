package com.diga.orm.config;

import javax.sql.DataSource;

public class DataSourceManager {
    private static ThreadLocal<DataSource> dataSourceThreadLocal=new ThreadLocal();

    public static void setDataSource(DataSource dataSource) {
        dataSourceThreadLocal.set(dataSource);
    }

    public static DataSource getDataSource() {
        return dataSourceThreadLocal.get();
    }

}
