package com.diga.orm.service;

import com.diga.orm.common.DataBaseEnum;
import com.diga.orm.common.RepositoryEnum;
import com.diga.orm.handler.GenerateHandler;
import com.diga.orm.handler.entity.EntityHandler;
import com.diga.orm.handler.pom.ext.JdbcPomHandler;
import com.diga.orm.handler.pom.ext.JpaPomHandler;
import com.diga.orm.handler.pom.ext.MybatisPlusPomHandler;
import com.diga.orm.handler.pom.ext.MybatisPomHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.Code;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public class GenerateDispatch {
    private DataBaseEnum dataBaseEnum;
    private RepositoryEnum repositoryEnum;
    private GenerateHandler generateHandler;

    @Getter
    private List<Code> codeList = new ArrayList();

    public GenerateDispatch(DataBaseEnum dataBaseEnum, RepositoryEnum repositoryEnum) {
        this.dataBaseEnum = dataBaseEnum;
        this.repositoryEnum = repositoryEnum;
    }

    public void dispatch(TableDetail tableDetail) {

        // 基于持久层类型进行划分
        switch (repositoryEnum) {
            case MYBATIS:
                generateHandler = new MybatisPomHandler(tableDetail);
                break;

            case JDBC:
                generateHandler = new JdbcPomHandler(tableDetail);
                break;

            case SPRING_DATA_JPA:
                generateHandler = new JpaPomHandler(tableDetail);
                break;

            case MYBATIS_PLUS:
                generateHandler = new MybatisPlusPomHandler(tableDetail);
                break;
        }

        generateHandler.handle(codeList);
    }

    public void dispatchEntity(TableDetail tableDetail) {
        generateHandler = new EntityHandler(null, tableDetail);
        generateHandler.handle(codeList);
    }

}
