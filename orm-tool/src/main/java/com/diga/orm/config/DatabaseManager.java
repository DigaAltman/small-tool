package com.diga.orm.config;

import java.sql.Connection;

public class DatabaseManager {
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal();

    public static void set(Connection connection) {
        connectionThreadLocal.set(connection);
    }

    public static Connection get() {
        return connectionThreadLocal.get();
    }

}
