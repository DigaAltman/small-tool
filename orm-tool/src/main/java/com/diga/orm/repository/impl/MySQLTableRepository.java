package com.diga.orm.repository.impl;

import com.diga.db.core.DB;
import com.diga.orm.pojo.mysql.column.ColumnComment;
import com.diga.orm.pojo.mysql.column.ColumnIndex;
import com.diga.orm.pojo.mysql.column.ColumnStructure;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mySQLTableRepository")
public class MySQLTableRepository implements TableRepository {

    @Autowired
    private DB db;

    /**
     * 获取数据表的字段信息
     *
     * mysql> SHOW COLUMNS FROM db1.food_user;
     * +-------------+---------------+------+-----+-------------------+-------+
     * | Field       | Type          | Null | Key | Default           | Extra |
     * +-------------+---------------+------+-----+-------------------+-------+
     * | id          | varchar(64)   | NO   | PRI | NULL              |       |
     * | username    | varchar(32)   | YES  | UNI | NULL              |       |
     * | password    | varchar(64)   | NO   |     | NULL              |       |
     * | nickname    | varchar(32)   | YES  |     | NULL              |       |
     * | realname    | varchar(128)  | YES  |     | NULL              |       |
     * | face        | varchar(1024) | YES  |     | NULL              |       |
     * | mobile      | varchar(32)   | YES  |     | NULL              |       |
     * | email       | varchar(32)   | YES  |     | NULL              |       |
     * | sex         | int(11)       | YES  |     | 2                 |       |
     * | birthday    | date          | YES  |     | NULL              |       |
     * | create_time | datetime      | YES  |     | CURRENT_TIMESTAMP |       |
     * | update_time | datetime      | YES  |     | CURRENT_TIMESTAMP |       |
     * +-------------+---------------+------+-----+-------------------+-------+
     * 12 rows in set (0.00 sec)
     */
    @Override
    public List<ColumnStructure> getTableStructure(String tableName) {
        List<ColumnStructure> columnStructureList = db.selectList(String.format("SHOW COLUMNS FROM %s", tableName), ColumnStructure.class);
        return columnStructureList;
    }

    /**
     * 获取数据表的索引信息
     *
     * mysql> SHOW INDEX FROM db1.food_user;
     * +-----------+------------+--------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+
     * | Table     | Non_unique | Key_name     | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment |
     * +-----------+------------+--------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+
     * | food_user |          0 | PRIMARY      |            1 | id          | A         |           2 |     NULL | NULL   |      | BTREE      |         |               |
     * | food_user |          0 | uni_username |            1 | username    | A         |           2 |     NULL | NULL   | YES  | BTREE      |         |               |
     * +-----------+------------+--------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+
     * 2 rows in set (0.01 sec)
     */
    @Override
    public List<ColumnIndex> getTableIndex(String tableName) {
        List<ColumnIndex> tableIndexList = db.selectList(String.format("SHOW INDEX FROM %s", tableName), ColumnIndex.class);
        return tableIndexList;
    }

    /**
     * 获取数据表中字段的备注
     *
     * mysql> SELECT COLUMN_NAME AS column_name, column_comment, column_type, column_key FROM information_schema.COLUMNS WHERE table_name = 'food_user';
     * +-------------+---------------------------+---------------+------------+
     * | column_name | column_comment            | column_type   | column_key |
     * +-------------+---------------------------+---------------+------------+
     * | id          | 主键id                    | varchar(64)   | PRI        |
     * | username    | 用户名                    | varchar(32)   | UNI        |
     * | password    | 密码                      | varchar(64)   |            |
     * | nickname    | 昵称                      | varchar(32)   |            |
     * | realname    | 真实姓名                  | varchar(128)  |            |
     * | face        | 头像图片URL地址           | varchar(1024) |            |
     * | mobile      | 手机号                    | varchar(32)   |            |
     * | email       | 邮箱地址                  | varchar(32)   |            |
     * | sex         | 性别, 0:男,1:女,2:无      | int(11)       |            |
     * | birthday    | 日期                      | date          |            |
     * | create_time | 创建时间                  | datetime      |            |
     * | update_time | 最后一次更新时间          | datetime      |            |
     * +-------------+---------------------------+---------------+------------+
     * 12 rows in set (0.00 sec)
     */
    @Override
    public List<ColumnComment> getTableFieldComment(String tableName) {
        List<ColumnComment> columnCommentList = db.selectList(String.format("SELECT COLUMN_NAME AS column_name, column_comment, column_type, column_key FROM information_schema.COLUMNS WHERE table_name = '%s'", tableName), ColumnComment.class);
        return columnCommentList;
    }

    @Override
    public TableDetail getTableDetail(String tableName) {
        TableDetail tableDetail = db.selectOne("SELECT TABLE_SCHEMA, TABLE_NAME, ENGINE, VERSION, ROW_FORMAT, TABLE_ROWS, AVG_ROW_LENGTH, DATA_LENGTH, MAX_DATA_LENGTH, INDEX_LENGTH, DATA_FREE, AUTO_INCREMENT, CREATE_TIME, UPDATE_TIME, CHECK_TIME, TABLE_COLLATION, TABLE_COMMENT, CREATE_OPTIONS  FROM information_schema.tables WHERE TABLE_SCHEMA != 'information_schema' AND TABLE_NAME=?", TableDetail.class, tableName);
        return tableDetail;
    }
}
