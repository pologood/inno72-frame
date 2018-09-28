package com.inno72.log.vo;

import lombok.Data;

@Data
public class MachineVisitorCount {
	/**
	 * @MongoDB\Id(strategy="UUID")
	 */
	private String $id;
	/**
	 * String
	 * 	否	日期	yyyy-MM-dd
	 */
	private String date;
	/**
	 * String
	 * 	否	机器Code
	 */
	private String machineCode;

	private Integer visitor;

}
