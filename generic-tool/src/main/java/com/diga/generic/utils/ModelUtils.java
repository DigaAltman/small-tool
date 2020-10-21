package com.diga.generic.utils;

import java.util.Map;

public class ModelUtils {

	/**
	 * 模板渲染方法
	 */
	public static String render(String model, Map<String, Object> vm) {
		vm.forEach((k, v) -> {
			model.replaceAll("{{" + k + "}}", v.toString());
		});
		return model;
	}

}