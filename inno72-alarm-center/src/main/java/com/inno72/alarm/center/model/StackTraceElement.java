package com.inno72.alarm.center.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StackTraceElement {

	private String declaringClass;
	private String methodName;
	private String fileName;
	private Integer lineNumber;

	public StackTraceElement(java.lang.StackTraceElement stack) {
		this.declaringClass = stack.getClassName();
		this.methodName = stack.getMethodName();
		this.fileName = stack.getFileName();
		this.lineNumber = stack.getLineNumber();
	}

	public static List<StackTraceElement> transfer(java.lang.StackTraceElement[] stack) {
		List<StackTraceElement> data = new ArrayList<>();
		Arrays.asList(stack).forEach(s -> {
			data.add(new StackTraceElement(s));
		});
		return data;
	}

}
