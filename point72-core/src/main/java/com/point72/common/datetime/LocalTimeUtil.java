package com.point72.common.datetime;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalTimeUtil {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	public static String transfer2str(LocalTime localTime) {
		return localTime.format(formatter);
	}

	public static LocalTime transfer(String time) {
		return LocalTime.parse(time, formatter);
	}

	public static LocalTime transfer(Date date) {
		return LocalDateTimeUtil.toLocalDateTime(date).toLocalTime();
	}

	public static LocalTime transfer(LocalDateTime dateTime) {
		return dateTime.toLocalTime();
	}

	public static String transfer2str(Date date) {
		return transfer2str(transfer(date));
	}

	public static String transfer2str(LocalDateTime dateTime) {
		return transfer2str(transfer(dateTime));
	}
}
