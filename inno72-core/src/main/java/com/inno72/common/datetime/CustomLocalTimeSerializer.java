package com.inno72.common.datetime;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 格式化Jackson返回{@linkplain LocalTime}格式.
 * 属性加注解@JsonSerialize(using=CustomLocalTimeSerializer.class)
 * 
 * @author Houkm
 *
 *         2017年5月5日
 */
public class CustomLocalTimeSerializer extends JsonSerializer<LocalTime> {

	@Override
	public void serialize(LocalTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String str = value.format(formatter);
		jgen.writeString(str);
	}

}
