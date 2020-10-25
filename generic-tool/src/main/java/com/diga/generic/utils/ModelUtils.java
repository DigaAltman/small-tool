package com.diga.generic.utils;

import java.util.Map;

public class ModelUtils {

	/**
	 * 模板渲染方法
	 */
	public static String render(String model, Map<String, Object> vm) {
		for (Map.Entry<String, Object> kv : vm.entrySet()) {
			model = model.replaceAll("\\{\\{" + kv.getKey() + "}}", kv.getValue().toString());
		}
		return model;
	}

}