package com.diga.orm.repository;

import com.diga.orm.pojo.mysql.column.ColumnComment;
import com.diga.orm.pojo.mysql.column.ColumnIndex;
import com.diga.orm.pojo.mysql.column.ColumnStructure;
import com.diga.orm.pojo.mysql.table.TableDetail;

import java.util.List;

public interface TableRepository {
    TableDetail getTableDetail(String tableName);

    List<ColumnStructure> getTableStructure(String tableName);

    List<ColumnIndex> getTableIndex(String tableName);

    List<ColumnComment> getTableFieldComment(String tableName);
}
