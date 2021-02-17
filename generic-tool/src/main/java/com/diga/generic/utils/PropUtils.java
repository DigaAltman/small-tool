package com.diga.generic.utils;

import com.diga.generic.base.KV;
import com.google.common.collect.Sets;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.*;


/**
 * created by TMT
 */
public class PropUtils {

    /**
     * 根据流来获取 properties 对象
     *
     * @param is
     * @return
     */
    public static synchronized KV load(InputStream is) {
        return new KV(new HashMap<String, String>((Map) get(is)));
    }

    /**
     * 根据文件来获取 properties 对象
     *
     * @param file
     * @return
     */
    public static KV load(File file) {
        try {
            return load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("get KV errors, by FileNotFoundException..");
    }

    /**
     * 根据 相对路径 来获取 properties 对象
     *
     * @param classpath
     * @return
     */
    public static KV load(String classpath) {
        return load(URLUtils.classpath(classpath));
    }


    public static Properties get(InputStream is) {
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
