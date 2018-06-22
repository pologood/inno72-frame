package com.point72.utils.schedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

/**
 * 排班规则
 * 
 * @author Houkm
 *
 *         2017年9月28日
 */
@Data
public class Rule {

	private boolean weekend; // 是否周末
	private List<DayOfWeek> repeat; // 按周重复
	private List<LocalDate> days; // 指定日期
	private Scope scope; // 范围日期

}
