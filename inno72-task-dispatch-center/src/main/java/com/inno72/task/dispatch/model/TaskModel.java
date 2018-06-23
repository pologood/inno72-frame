package com.inno72.task.dispatch.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

import lombok.Data;

@Data
public class TaskModel implements Serializable {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 177706684331611609L;

	private String id;

	/**
	 * 任务名
	 */
	private String name;

	/**
	 * 任务描述
	 */
	private String detail;

	/**
	 * 执行时间策略
	 */
	private String policy;

	/**
	 * 执行jar路径
	 */
	private String filePath;

	/**
	 * 执行文件名
	 */
	private String fileName;

	/**
	 * 是否可执行
	 */
	private Boolean executable;

	/**
	 * 下次执行时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime nextTime;

	/**
	 * 删除标识
	 */
	private Boolean delete;

	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime created;

	@Transient
	private String key;

	@Transient
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;

	@Transient
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	public Map<String, Object> generateUpdate() {
		Map<String, Object> updateMap = new HashMap<>();
		Class<?> clazz = this.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Transient t = field.getAnnotation(Transient.class);
			if (t == null) {
				try {
					String key = field.getName();
					Object value = field.get(this);
					updateMap.put(key, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return updateMap;

	}

}
