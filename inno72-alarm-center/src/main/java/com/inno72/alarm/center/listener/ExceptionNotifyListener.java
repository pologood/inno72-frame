package com.inno72.alarm.center.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inno72.alarm.center.service.LogExceptionService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.config.client.MqProperties;
import com.inno72.core.dto.LogExceptionDTO;
import com.inno72.mq.core.AbstractListener;
import com.inno72.mq.util.ConverterUtil;

/**
 * 接收报警记录
 * 
 * @author Houkm
 *
 *         2017年7月10日
 */
@Component
public class ExceptionNotifyListener implements AbstractListener {

	@Autowired
	private MqProperties queueProp;

	@Autowired
	LogExceptionService logExceptionService;

	@Override
	public Result<Object> handleMessage(byte[] bytes) {
		LogExceptionDTO exDto = ConverterUtil.toObj(bytes, LogExceptionDTO.class);
		logExceptionService.log(exDto);
		return Results.success();
	}

	@Override
	public String getQueueName() {
		return queueProp.getLogException().getName();
	}

}
