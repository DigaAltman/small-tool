package com.diga.db.core;

import lombok.Data;

import java.sql.*;
import java.util.*;

@Data
public class DB {

    /**
     * 用于与数据库交互的 Connection 对象
     */
    private Connection connection;


    /**
     * 关闭连接对象
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建 PreparedStatement 的方法
     *
     * @param sql
     * @param args
     * @return
     */
    private PreparedStatement buildPrepareStatement(String sql, Object... args) {
        if (sql != null && !sql.trim().equals("")) {
            throw new IllegalArgumentException("无效的SQL语句");
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= args.length; i++) {
                preparedStatement.setObject(i, args[i - 1]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }


    /**
     * 将当前行的 resultSet 转换为 Map
     *
     * @param resultSet
     * @return
     */
    private LinkedHashMap resultSetToMap(ResultSet resultSet) throws SQLException {
        LinkedHashMap<String, Object> map = new LinkedHashMap();
        ResultSetMetaData metaData = resultSet.getMetaData();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnLabel(i);
            Object value = resultSet.getObject(columnName);
            map.put(columnName, value);
        }

        return map;
    }

    /**
     * 执行sql, 返回是否执行成功
     *
     * @param sql
     * @param args
     * @return
     */
    public boolean execute(String sql, Object... args) {
        try {
            boolean res = buildPrepareStatement(sql, args).execute();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 执行sql, 返回数据库影响条数
     *
     * @param sql
     * @param args
     * @return
     */
    public int executeUpdate(String sql, Object... args) {
        int res = -1;
        try {
            res = buildPrepareStatement(sql, args).executeUpdate();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 执行查询语句, 返回原生 resultSet
     *
     * @param sql  sql语句
     * @param args 参数
     * @return
     */
    public ResultSet select(String sql, Object... args) {
        ResultSet resultSet = null;
        try {
            resultSet = buildPrepareStatement(sql, args).executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}
