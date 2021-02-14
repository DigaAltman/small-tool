package com.diga.generic.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class URLUtils {

    /**
     * @param path classpath的路径
     * @return 返回classpath路径对应的具体路径
     */
    public static InputStream classpath(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        try {
            InputStream is = resource.getInputStream();
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 基于项目路径来获取文件流
     *
     * @param path
     * @return
     */
    public static InputStream projectPath(String path) {
        String url = System.getProperty("user.dir");
        FileSystemResource resource = new FileSystemResource(url + File.separator + path);
        try {
            InputStream is = resource.getInputStream();
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}