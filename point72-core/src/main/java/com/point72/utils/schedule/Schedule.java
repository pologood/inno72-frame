package com.point72.utils.schedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

/**
 * 排班
 * 
 * @author Houkm
 *
 *         2017年9月28日
 */
@Data
public class Schedule {

	/**
	 * include包含的时间为 true
	 */
	private Rule include;

	/**
	 * exclude包含的时间为false
	 */
	private Rule exclude;

	public boolean check(LocalDateTime time) {
		// 拒绝规则
		if (exclude != null) {
			return !caluRule(exclude, time);
		}
		// 接收规则
		if (include != null) {
			return caluRule(include, time);
		}
		return false;
	}

	private boolean caluRule(Rule rule, LocalDateTime datetime) {
		DayOfWeek dayOfWeek = datetime.getDayOfWeek();
		boolean weekend = dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY);
		boolean isWeekend = rule.isWeekend();
		List<DayOfWeek> repeat = rule.getRepeat();
		List<LocalDate> days = rule.getDays();
		Scope scope = rule.getScope();
		// 按是否周末
		if (isWeekend && weekend) {
			return true;
		}

		// 按周几
		if (repeat != null && repeat.contains(dayOfWeek)) {
			return true;
		}

		// 按日期
		if (days != null && days.contains(datetime.toLocalDate())) {
			return true;
		}
		LocalTime time = datetime.toLocalTime();
		// 按范围
		if (scope != null && time.isAfter(scope.getStart()) && time.isBefore(scope.getEnd())) {
			return true;
		}
		return false;
	}

}
