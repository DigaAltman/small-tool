package com.diga.orm.aspect;

import com.diga.db.core.DB;
import com.diga.db.factory.DBFactory;
import com.diga.generic.utils.ReflexUtils;
import com.diga.orm.config.DatabaseManager;
import com.diga.orm.configuration.ResultMapFactoryBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class WorkAspect {

    @Autowired
    private ResultMapFactoryBean resultMapFactoryBean;


    @Around("@annotation(com.diga.orm.annotation.SetDB)")
    public Object monitorAround(ProceedingJoinPoint pjp) throws Throwable {
        Object target = pjp.getTarget();
        Field[] declaredFields = target.getClass().getDeclaredFields();
        List<Field> dbFieldList = Arrays.stream(declaredFields).filter(f -> DB.class.isAssignableFrom(f.getType())).collect(Collectors.toList());

        // 取出会话级别的 Connection, 设置会话级别的DB
        Connection connection = DatabaseManager.get();
        DBFactory threadLocalDBFactory = new DBFactory(connection);
        threadLocalDBFactory.setResultMapFactory(resultMapFactoryBean.getResultMapFactory());
        DB threadLocalDB = threadLocalDBFactory.getDB();

        // 循环赋值
        dbFieldList.forEach(dbField -> ReflexUtils.set(target, dbField.getName(), threadLocalDB));

        Object result = pjp.proceed();
        return result;
    }



}
