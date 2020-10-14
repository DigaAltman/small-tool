package com.diga.db.factory;

import lombok.Data;

import java.sql.Connection;

@Data
public class DBFactory {
    private Connection connection;

    public DBFactory(Connection connection) {
        this.connection = connection;
    }


}
