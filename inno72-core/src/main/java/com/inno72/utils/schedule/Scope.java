package com.inno72.utils.schedule;

import java.time.LocalTime;

import lombok.Data;

/**
 * 时间范围
 * 
 * @author Houkm
 *
 *         2017年9月28日
 */
@Data
public class Scope {
	private LocalTime start;
	private LocalTime end;
}
