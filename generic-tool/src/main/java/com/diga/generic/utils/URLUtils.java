package com.diga.generic.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class URLUtils {

	/**
	 * @param path classpath的路径
	 * @return 返回classpath路径对应的具体路径
	 */
	public static InputStream classpath(String path) {
		ClassPathResource classPathResource = new ClassPathResource(path);
		try {
			InputStream is = classPathResource.getInputStream();
			return is;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}