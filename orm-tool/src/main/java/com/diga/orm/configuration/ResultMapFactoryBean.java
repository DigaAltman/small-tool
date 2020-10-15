package com.diga.orm.configuration;

import com.diga.db.annotation.ResultBean;
import com.diga.db.core.ResultMap;
import com.diga.db.core.factory.DefaultResultMapFactory;
import com.diga.db.core.factory.ResultMapFactory;
import com.diga.generic.utils.ClassUtils;
import com.diga.generic.utils.CollectionUtils;
import com.diga.generic.utils.ReflexUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class ResultMapFactoryBean {

    private String resultMapLocation;

    public ResultMapFactoryBean(String mapperLocation) {
        this.resultMapLocation = mapperLocation;
        this.buildResultMapFactory();
    }

    private ResultMapFactory resultMapFactory;

    private void buildResultMapFactory() {
        if (resultMapFactory == null) {

            ResultMapFactory resultMapFactory = new DefaultResultMapFactory();

            // 拿到包下的所有类
            Set<Class<?>> locationClassSet = ClassUtils.getClasses(resultMapLocation);

            // 对数据集进行编译
            CollectionUtils.forEach(locationClassSet, (index, clazz, classSet) -> {
                if (clazz.getAnnotation(ResultBean.class) != null && clazz instanceof Serializable) {
                    ResultMap resultMap = resultMapFactory.build((Class<? extends Serializable>) clazz, false);
                    ResultBean resultBean = clazz.getAnnotation(ResultBean.class);

                    if (!StringUtils.isEmpty(resultBean.id()) && !resultBean.id().equals(clazz.getName())) {
                        resultMapFactory.putResultMap(resultBean.id(), resultMap);
                    } else {
                        resultMapFactory.putResultMap(clazz.getName(), resultMap);
                    }

                } else {
                    Method[] methods = clazz.getMethods();
                    for (Method method : methods) {
                        ResultBean resultBean = method.getAnnotation(ResultBean.class);
                        if (resultBean != null && ResultMap.class.isAssignableFrom(method.getReturnType())) {
                            String id = resultBean.id();
                            Object bean = ReflexUtils.tryInstance(clazz);
                            if (bean == null) {
                                throw new IllegalArgumentException("@ResultBean 所属的类" + clazz.getName() + "不能被实例化");
                            }
                            try {
                                ResultMap resultMap = (ResultMap) method.invoke(bean);
                                if (StringUtils.isEmpty(id)) {
                                    resultMap.setId(StringUtils.isEmpty(resultMap.getId()) ? method.getName() : resultMap.getId());
                                } else {
                                    resultMap.setId(id);
                                }
                                resultMapFactory.putResultMap(resultMap.getId(), resultMap);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            this.resultMapFactory = resultMapFactory;
        }
    }

    public ResultMapFactory getResultMapFactory() {
        return this.resultMapFactory;
    }
}
