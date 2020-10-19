package com.diga.orm.service;

import com.diga.orm.pojo.mysql.column.ColumnComment;
import com.diga.orm.pojo.mysql.column.ColumnIndex;
import com.diga.orm.pojo.mysql.column.ColumnStructure;
import com.diga.orm.vo.ColumnDetail;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITableService {
    List<ColumnDetail> getTableColumnList(String tableName);

    void generateEntity(String tableName);

    void generateRepository(String tableName);
}
