package com.diga.orm.handler.properties;

import com.diga.generic.utils.FileUtils;
import com.diga.generic.utils.ModelUtils;
import com.diga.generic.utils.URLUtils;
import com.diga.orm.common.CodeEnum;
import com.diga.orm.handler.GenerateHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.Code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractAppHandler implements GenerateHandler {
    protected GenerateHandler generateHandler;
    protected TableDetail tableDetail;

    public AbstractAppHandler(TableDetail tableDetail) {
        this.tableDetail = tableDetail;
    }

    @Override
    public void handle(List<Code> codeList) {
        String model = FileUtils.readFile(URLUtils.classpath("model/application.model"));

        Map<String, Object> vm = new HashMap();
        vm.put("port", 8080);
        vm.put("url", "jdbc:mysql://localhost:3306/mysql");
        vm.put("username", "root");
        vm.put("password", "1234");
        vm.put("driverClassName", "com.mysql.jdbc.Driver");

        applicationExtend(vm);

        String body = ModelUtils.render(model, vm);
        Code code = new Code(CodeEnum.YAML, "application", body);
        codeList.add(code);

        generateHandler.handle(codeList);
    }

    protected abstract void applicationExtend(Map<String, Object> vm);
}
