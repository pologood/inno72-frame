package com.point72.msg.center.model;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Rule {

	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime start;
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime end;
	private List<Integer> workDay;
}