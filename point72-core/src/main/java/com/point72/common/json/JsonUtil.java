package com.point72.common.json;

import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	public static <T> T toObj(byte[] bts, Class<T> clazz) {

		ObjectMapper objMapper = new ObjectMapper();
		T t = null;
		try {
			t = objMapper.readValue(bts, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}

	public static String toJson(Object object) {
		return toJson(object, "yyyy-MM-dd HH:mm:ss");

	}

	/**
	 * 
	 * @param json
	 *            json串
	 * @param clazz
	 *            生成的bean class
	 * @return 生成对象
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

	/**
	 * 
	 * @param json
	 *            json串
	 * @param clazz
	 *            生成的List的元素class
	 * @return
	 */
	public static <T> List<T> toArray(String json, Class<T> clazz) {

		return JSON.parseArray(json, clazz);
	}

	/**
	 * 
	 * @param object
	 *            对象
	 * @param dateFormat
	 *            日期字符串
	 * @return
	 */
	public static String toJson(Object object, String dateFormat) {
		return JSON.toJSONStringWithDateFormat(object, dateFormat, SerializerFeature.WriteDateUseDateFormat);

	}
}
