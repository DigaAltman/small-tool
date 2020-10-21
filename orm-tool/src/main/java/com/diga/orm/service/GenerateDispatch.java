package com.diga.orm.service;

import com.diga.orm.common.DataBaseEnum;
import com.diga.orm.common.RepositoryEnum;
import com.diga.orm.handler.GenerateHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.Code;
import lombok.Data;

@Data
public class GenerateDispatch {
    private DataBaseEnum dataBaseEnum;
    private RepositoryEnum repositoryEnum;
    private GenerateHandler generateHandler;

    public GenerateDispatch(DataBaseEnum dataBaseEnum, RepositoryEnum repositoryEnum) {
        this.dataBaseEnum = dataBaseEnum;
        this.repositoryEnum = repositoryEnum;
    }

    public void dispatch(Code codeNode, TableDetail tableDetail) {

    }
}
