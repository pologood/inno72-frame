package com.inno72.alarm.center.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.alarm.center.service.TestService;
import com.inno72.common.Result;
import com.inno72.common.Results;

@CrossOrigin
@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestController {

	@Autowired
	TestService testService;

	/**
	 * 异常报警
	 * 
	 * @param token
	 * @return
	 * @author Houkm 2017年6月27日
	 */
	@RequestMapping(value = "/exception", method = RequestMethod.GET)
	public Result<Object> query() {
		testService.notifyException();
		return Results.success();
	}

	/**
	 * 异常报警2
	 * 
	 * @param token
	 * @return
	 * @author Houkm 2017年6月27日
	 */
	@RequestMapping(value = "/exception2", method = RequestMethod.GET)
	public Result<Object> logException() {
		testService.logException();
		return Results.success();
	}

	/**
	 * 发送消息测试
	 * 
	 * @return
	 * @author Houkm 2017年7月11日
	 */
	@RequestMapping(value = "/msg", method = RequestMethod.GET)
	public Result<Object> msg() {
		testService.sendMsg();
		return Results.success();
	}

}
