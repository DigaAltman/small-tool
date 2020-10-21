package com.diga.orm.handler.pom;

import com.diga.orm.common.DataBaseEnum;
import com.diga.orm.handler.GenerateHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.CodeNode;

public class AbstractPomHandler implements GenerateHandler {
    /**
     * 能处理的数据库类型
     *
     * @return
     */
    @Override
    public DataBaseEnum handleDataBaseEnum() {
        return null;
    }

    /**
     * 处理器核心方法
     *
     * @param codeNode    具体代码
     * @param tableDetail 表详情信息
     */
    @Override
    public void handle(CodeNode codeNode, TableDetail tableDetail) {

    }
}
