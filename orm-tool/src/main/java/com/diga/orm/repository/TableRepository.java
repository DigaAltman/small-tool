package com.diga.orm.repository;

import com.diga.db.core.DB;
import com.diga.generic.utils.StringUtils;
import com.diga.orm.annotation.SetDB;
import com.diga.orm.pojo.mysql.column.ColumnComment;
import com.diga.orm.pojo.mysql.column.ColumnIndex;
import com.diga.orm.pojo.mysql.column.ColumnStructure;
import com.diga.orm.pojo.mysql.table.TableDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TableRepository {

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
    @SetDB
    public List<ColumnStructure> getTableStructure(String tableName) {
        List<ColumnStructure> columnStructureList = db.selectList(String.format("SHOW COLUMNS FROM `%s`", tableName), ColumnStructure.class);
        return columnStructureList;
    }

    /**
     * 获取数据表的索引信息
     *
     * mysql> SHOW INDEX FROM db1.food_user;
     */
    @SetDB
    public List<ColumnIndex> getTableIndex(String tableName) {
        List<ColumnIndex> tableIndexList = db.selectList(String.format("SHOW INDEX FROM `%s`", tableName), ColumnIndex.class);
        return tableIndexList;
    }

    /**
     * 获取数据表中字段的备注
     *
     * mysql> SELECT COLUMN_NAME AS column_name, column_comment, column_type, column_key FROM information_schema.COLUMNS WHERE table_name = 'food_user';
     * 12 rows in set (0.00 sec)
     */
    @SetDB
    public List<ColumnComment> getTableFieldComment(String tableName) {
        List<ColumnComment> columnCommentList = db.selectList(String.format("SELECT COLUMN_NAME AS column_name, column_comment, column_type, column_key FROM information_schema.COLUMNS WHERE table_name = '%s'", tableName), ColumnComment.class);
        return columnCommentList;
    }

    @SetDB
    public TableDetail getTableDetail(String tableName) {
        TableDetail tableDetail = db.selectOne("SELECT TABLE_SCHEMA, TABLE_NAME, ENGINE, VERSION, ROW_FORMAT, TABLE_ROWS, AVG_ROW_LENGTH, DATA_LENGTH, MAX_DATA_LENGTH, INDEX_LENGTH, DATA_FREE, AUTO_INCREMENT, CREATE_TIME, UPDATE_TIME, CHECK_TIME, TABLE_COLLATION, TABLE_COMMENT, CREATE_OPTIONS  FROM information_schema.tables WHERE TABLE_SCHEMA != 'information_schema' AND TABLE_NAME=?", TableDetail.class, tableName);
        tableDetail.setEntityName(StringUtils.humpFirstUpper(tableDetail.getTableName()));
        return tableDetail;
    }

    @SetDB
    public List<String> getTableList() {
        return db.selectList("show tables", String.class);
    }
}
