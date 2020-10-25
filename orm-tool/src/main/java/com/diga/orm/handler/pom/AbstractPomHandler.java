package com.diga.orm.handler.pom;

import com.diga.generic.utils.FileUtils;
import com.diga.generic.utils.ModelUtils;
import com.diga.generic.utils.StringUtils;
import com.diga.generic.utils.URLUtils;
import com.diga.orm.common.CodeEnum;
import com.diga.orm.handler.GenerateHandler;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.vo.Code;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.UrlResource;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPomHandler implements GenerateHandler {
    protected GenerateHandler generateHandler;
    protected TableDetail tableDetail;

    public AbstractPomHandler(TableDetail tableDetail) {
        this.tableDetail = tableDetail;
    }

    @Override
    public void handle(List<Code> codeList) {
        String model = FileUtils.readFile(URLUtils.classpath("model/pom.model"));
        Map<String, Object> vm = new HashMap();
        vm.put("groupId", "org.example");
        vm.put("artifactId", "wdnmd");
        vm.put("version", "0.0.1.RELEASE");
        vm.put("packageType", "jar");

        mapperDependencies(vm);

        String app = ModelUtils.render(model, vm);
        codeList.add(new Code(CodeEnum.XML,"pom", app));
        
        generateHandler.handle(codeList);
    }


    protected abstract void mapperDependencies(Map<String, Object> vm);
}
