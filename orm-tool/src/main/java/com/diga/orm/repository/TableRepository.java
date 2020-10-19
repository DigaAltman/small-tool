package com.diga.orm.repository;

import com.diga.orm.pojo.mysql.column.ColumnComment;
import com.diga.orm.pojo.mysql.column.ColumnIndex;
import com.diga.orm.pojo.mysql.column.ColumnStructure;

import java.util.List;

public interface TableRepository {

    List<ColumnStructure> getTableStructure(String tableName);

    List<ColumnIndex> getTableIndex(String tableName);

    List<ColumnComment> getTableFieldComment(String tableName);
}
