package com.diga.orm.service;

import com.diga.orm.pojo.mysql.column.ColumnComment;
import com.diga.orm.pojo.mysql.column.ColumnIndex;
import com.diga.orm.pojo.mysql.column.ColumnStructure;
import com.diga.orm.repository.mysql.TableRepository;
import com.diga.orm.vo.ColumnDetail;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {

    @Autowired
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

    public void generateEntity(String tableName) {
    }

    public void generateRepository(String tableName) {
    }
}
