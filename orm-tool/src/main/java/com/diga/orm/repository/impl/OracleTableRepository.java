package com.diga.orm.repository.impl;

import com.diga.orm.pojo.mysql.column.ColumnComment;
import com.diga.orm.pojo.mysql.column.ColumnIndex;
import com.diga.orm.pojo.mysql.column.ColumnStructure;
import com.diga.orm.repository.TableRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("oracleTableRepository")
public class OracleTableRepository implements TableRepository {

    @Override
    public List<ColumnStructure> getTableStructure(String tableName) {
        return null;
    }

    @Override
    public List<ColumnIndex> getTableIndex(String tableName) {
        return null;
    }

    @Override
    public List<ColumnComment> getTableFieldComment(String tableName) {
        return null;
    }
}
