package com.diga.orm.service;

import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.Code;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITableService {
    List<Code> generateEntity(String tableName);

    List<Code> generateRepository(String tableName);

    TableDetail getTableDetail(String tableName);
}
