package com.diga.orm.handler.dao.ext;

import com.diga.generic.utils.StringUtils;
import com.diga.orm.common.CodeEnum;
import com.diga.orm.handler.dao.AbstractDaoHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.Code;

import java.util.List;

public class MybatisDaoHandler extends AbstractDaoHandler {

    public MybatisDaoHandler(TableDetail tableDetail) {
        super(tableDetail);
    }

    @Override
    public void generateCode(List<Code> codeList) {
        Code javaCode = new Code(CodeEnum.JAVA, tableDetail.getEntityName(), null);
        Code xmlCode = new Code(CodeEnum.XML, tableDetail.getEntityName(), null);

        StringUtils.SBuilder sb = StringUtils.to();



    }

}
