package com.diga.orm.service.impl;

import com.diga.orm.pojo.mysql.column.ColumnComment;
import com.diga.orm.pojo.mysql.column.ColumnIndex;
import com.diga.orm.pojo.mysql.column.ColumnStructure;
import com.diga.orm.repository.TableRepository;
import com.diga.orm.service.ITableService;
import com.diga.orm.vo.ColumnDetail;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MySQLTableService implements ITableService {

    @Autowired
    @Qualifier("mySQLTableRepository")
    private TableRepository tableRepository;

    // 基于表名称获取数据表的字段和索引结构
    public List<ColumnDetail> getTableColumnList(String tableName) {

        // 拿到表结构
        List<ColumnStructure> columnStructureList = tableRepository.getTableStructure(tableName);

        // 拿到表的索引信息
        List<ColumnIndex> columnIndexList = tableRepository.getTableIndex(tableName);

        // 表字段注释
        List<ColumnComment> columnCommentList = tableRepository.getTableFieldComment(tableName);

        List<ColumnDetail> columnDetailList = Lists.newLinkedList();
        for (ColumnStructure column : columnStructureList) {
            columnDetailList.add(column.ToVO(columnIndexList, columnCommentList));
        }

        return columnDetailList;
    }

    // 生成实体类
    public void generateEntity(String tableName) {

    }

    // 生成持久层
    public void generateRepository(String tableName) {

    }
}
