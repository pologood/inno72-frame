package com.point72.msg.center;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.point72.common.Result;
import com.point72.core.dto.MsgDTO;
import com.point72.msg.center.service.MsgFacedeService;

@RestController
@RequestMapping("/test")
public class Test {

	@Autowired
	private MsgFacedeService facedeService;

	@RequestMapping("v")
	public void v() {
		String json = "{\"code\":\"WC_FAST_ORDER_AND\",\"msgTimestamp\":\"mq_msg_1504672911572\",\"params\":{\"code\":12340,\"data\":{\"desp\":\"病情描述：大夫您好，我的宝宝（男），2岁6个月零3天，流涕大于4天\",\"patientName\":\"测试\",\"patientNum\":\"11463330265109\",\"urls\":[],\"doctorId\":1104,\"createTime\":\"2017-09-06T12:41:51\",\"newOrderId\":\"e69bad6303024f43af628c68789fafde\",\"orderNum\":\"10113961709100655\",\"birthdayStr\":\"2岁6个月零3天\"},\"createTime\":1504672911534,\"text\":\"您有一条新的速诊订单\",\"title\":\"通知\"},\"receiver\":\"1104\",\"sentBy\":\"SOCKET-SocketIOServiceImpl\"}";
		MsgDTO dto = JSON.parseObject(json, MsgDTO.class);
		Result<Object> result = facedeService.transmit(dto);
		System.out.println(result.json());
	}
}
