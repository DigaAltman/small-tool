package com.diga.db.factory;

import com.diga.db.core.DB;
import com.diga.db.core.factory.DefaultResultMapFactory;
import com.diga.db.core.factory.ResultMapFactory;
import com.diga.db.result.DefaultResultRowHandler;
import com.diga.db.result.ResultRowHandler;
import com.sun.xml.internal.txw2.output.ResultFactory;
import lombok.Data;

import java.sql.Connection;

@Data
public class DBFactory {
    private Connection connection;
    private ResultMapFactory resultMapFactory = new DefaultResultMapFactory();

    public DBFactory(Connection connection) {
        this.connection = connection;
    }

    /**
     * 暂时通过这种方式获取 DB 连接
     *
     * @return
     */
    public DB getDB() {
        DB db = new DB();
        db.setConnection(connection);
        ResultRowHandler resultRowHandler = new DefaultResultRowHandler(resultMapFactory);
        db.setResultRowHandler(resultRowHandler);
        return db;
    }
}
