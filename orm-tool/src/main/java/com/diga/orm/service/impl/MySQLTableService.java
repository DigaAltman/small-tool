package com.diga.orm.service.impl;

import com.diga.orm.common.CodeTypeEnum;
import com.diga.orm.common.DataBaseEnum;
import com.diga.orm.handler.GenerateHandler;
import com.diga.orm.pojo.mysql.column.ColumnComment;
import com.diga.orm.pojo.mysql.column.ColumnIndex;
import com.diga.orm.pojo.mysql.column.ColumnStructure;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.repository.DataBaseRepository;
import com.diga.orm.repository.TableRepository;
import com.diga.orm.service.GenerateDispatch;
import com.diga.orm.service.ITableService;
import com.diga.orm.vo.CodeNode;
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
    private List<ColumnDetail> getTableColumnList(String tableName) {

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
    public CodeNode generateEntity(String tableName) {
        TableDetail tableDetail = getTableDetail(tableName);
        CodeNode codeNode = new CodeNode();

        GenerateDispatch generateDispatch = new GenerateDispatch(DataBaseEnum.MYSQL, null);
        generateDispatch.dispatch(codeNode, tableDetail);
        return codeNode;
    }

    // 生成持久层
    public CodeNode generateRepository(String tableName) {
        CodeNode codeNode = new CodeNode();
        return codeNode;
    }

    // 获取数据表的详细信息
    public TableDetail getTableDetail(String tableName) {
        TableDetail tableDetail = tableRepository.getTableDetail(tableName);
        tableDetail.setColumnDetailList(getTableColumnList(tableName));
        return tableDetail;
    }
}
