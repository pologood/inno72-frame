package com.point72.msg.center.model;

import java.lang.reflect.Field;
import java.util.Arrays;

import com.alibaba.fastjson.JSON;

public class AbstractMsgModel {

	public String text() {
		return JSON.toJSONString(this);
	}

	public <T> T transfer(Class<T> targetClass) throws InstantiationException, IllegalAccessException {

		Class<?> clazz = this.getClass();
		T target = targetClass.newInstance();
		Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
			Field f;
			try {
				f = targetClass.getDeclaredField(field.getName());
				if (f != null) {
					f.set(target, field.get(this));
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		return target;

	}

}
