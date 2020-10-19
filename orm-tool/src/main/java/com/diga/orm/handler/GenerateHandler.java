package com.diga.orm.handler;

import com.diga.orm.common.CodeTypeEnum;
import com.diga.orm.common.DataBaseEnum;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.CodeNode;

public interface GenerateHandler {
    /**
     * 能处理的数据库类型
     *
     * @return
     */
    DataBaseEnum handleDataBaseEnum();

    /**
     * 处理器核心方法
     *
     * @param codeNode    具体代码
     * @param tableDetail 表详情信息
     */
    void handle(CodeNode codeNode, TableDetail tableDetail);
}
