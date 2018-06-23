package com.inno72.msg.center.model;

import java.time.LocalTime;

import lombok.Data;

/**
 * 发送时间规则
 * 
 * @author Houkm
 *
 *         2017年6月15日
 */
@Data
public class SendTime {

	private Rule include;
	private Rule exclude;

	public boolean check() {
		// LocalDateTime now = LocalDateTime.now();
		// System.out.println(JSON.toJSONString(this));
		// if (now.isAfter(this.getInclude().getStart()) &&
		// now.isBefore(this.getInclude().getEnd())) {
		// System.out.println(now.getDayOfWeek().getValue());
		// Integer dayOfWeek = now.getDayOfWeek().getValue();
		// if (this.getInclude().getWorkDay().contains(dayOfWeek)) {
		// if (now.isAfter(this.getExclude().getStart()) &&
		// now.isBefore(this.getExclude().getEnd())) {
		// System.out.println("不让发送");
		// } else {
		// if (this.getExclude().getWorkDay().contains(dayOfWeek)) {
		// System.out.println("不让发送");
		// } else {
		// System.out.println("lalala");
		// }
		// }
		//
		// } else {
		// System.out.println("不是工作日");
		// }
		// } else {
		// System.out.println("不在发送范围");
		// }

		LocalTime now = LocalTime.now();
		boolean send = now.isAfter(include.getStart()) ? include.getEnd().isAfter(now) ? true : false : false;
		return send;
	}
}