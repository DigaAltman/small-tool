package com.diga.generic.utils;

import com.diga.generic.base.KV;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * 基于 yml 的配置文件工具类
 */
public class YmlUtils {
    private static Map<String, String> allMap = new HashMap<String, String>();

    public static void clear() {
        allMap.clear();
    }

    public static synchronized KV load(InputStream is) {
        Yaml yaml = new Yaml();
        Iterator<Object> iterator = yaml.loadAll(is).iterator();

        Map<String, String> map;
        while (iterator.hasNext()) {
            map = (Map) iterator.next();
            iteratorYml(map, null);
        }

        return new KV(allMap);
    }

    private static void iteratorYml(Map<String, String> map, String key) {
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object k = entry.getKey();
            Object v = entry.getValue();

            // 还没遍历到具体值
            if (v instanceof LinkedHashMap) {
                if (key == null) {
                    iteratorYml((Map) v, k.toString());
                } else {
                    iteratorYml((Map) v, key + "." + k.toString());
                }

            } else if (v instanceof ArrayList) {
                if (key == null) {
                    allMap.put(k.toString(), v.toString());
                    Iterator iterator1 = ((ArrayList) v).iterator();
                    while (iterator1.hasNext()) {
                        Object next = iterator1.next();
                        if (next instanceof LinkedHashMap) {
                            iteratorYml((Map) next, key + "." + k.toString());
                        }
                    }
                } else {
                    allMap.put(key + "." + k.toString(), v.toString());
                    Iterator iterator1 = ((ArrayList) v).iterator();
                    while (iterator1.hasNext()) {
                        Object next = iterator1.next();
                        if (next instanceof LinkedHashMap) {
                            iteratorYml((Map) next, key + "." + k.toString());
                        }
                    }
                }
            } else {
                if (key == null) {
                    allMap.put(k.toString(), v.toString());
                } else {
                    allMap.put(key + "." + k.toString(), v.toString());
                }
            }
        }
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
            throw new IllegalArgumentException("get KV errors, by FileNotFoundException..");
        }
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

}
