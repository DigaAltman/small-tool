package com.diga.orm.service;

import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.Code;
import org.springframework.stereotype.Service;

@Service
public interface ITableService {
    Code generateEntity(String tableName);

    Code generateRepository(String tableName);

    TableDetail getTableDetail(String tableName);
}
