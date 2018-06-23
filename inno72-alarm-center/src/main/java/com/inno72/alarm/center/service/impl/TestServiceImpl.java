package com.inno72.alarm.center.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inno72.alarm.center.service.TestService;
import com.inno72.annotation.ExceptionNotify;
import com.inno72.config.client.ExceptionProperties;
import com.inno72.exception.ExceptionBuilder;
import com.inno72.msg.MsgUtil;

@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private ExceptionProperties exProp;

	@Autowired
	private MsgUtil msgUtil;

	@Override
	@ExceptionNotify(owner = "哈哈哈", notifyUser = { "houkemian" }, title = "test", project = "报警中心", function = "测试异常记录")
	public void notifyException() {
		if (LocalDateTime.now().isAfter(LocalDateTime.of(2017, 6, 5, 1, 1))) {
			throw ExceptionBuilder.build(exProp).format("send_msg_failed", "xxx").create();
		}
	}

	@Override
	public void sendMsg() {
		Map<String, String> params = new HashMap<>();
		params.put("goodsName", "营养发育基础检查");
		msgUtil.sendPush("WC_APPOINTMENT_SUCC_DOCTOR_AND", params, "3924", "test/msg", "预约成功通知", "预约成功通知");
		msgUtil.sendPush("WC_APPOINTMENT_SUCC_DOCTOR_IOS", params, "4005", "test/msg", "预约成功通知", "预约成功通知");

	}

	@Override
	public void logException() {
		String[] aaa = { "" };
		System.out.println(aaa[10]);
	}

}
