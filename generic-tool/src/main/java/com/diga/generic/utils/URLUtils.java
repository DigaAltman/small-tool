package com.diga.generic.utils;

import java.io.File;

public class URLUtils {

	/**
	 * @param path classpath的路径
	 * @return 返回classpath路径对应的具体路径
	 */
	public static String classpath(String path) {
		return URLUtils.class.getResource("/" + path).getPath();
	}

	/**
	 * @param path classpath 的路径
	 * @return 返回classpath路径对应的文件
	 */
	public static File filepath(String path) {
		return new File(classpath(path));
	}

}