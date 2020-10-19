package com.diga.orm.service;

import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.CodeNode;
import org.springframework.stereotype.Service;

@Service
public interface ITableService {
    CodeNode generateEntity(String tableName);

    CodeNode generateRepository(String tableName);

    TableDetail getTableDetail(String tableName);
}
