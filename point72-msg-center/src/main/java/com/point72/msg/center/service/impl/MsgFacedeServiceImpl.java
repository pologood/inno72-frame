package com.point72.msg.center.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.point72.common.Result;
import com.point72.common.Results;
import com.point72.config.client.ExceptionProperties;
import com.point72.core.dto.MsgDTO;
import com.point72.exception.ExceptionBuilder;
import com.point72.msg.center.model.MsgTemplateModel;
import com.point72.msg.center.service.MsgFacedeService;
import com.point72.msg.center.service.MsgModelService;
import com.point72.msg.center.service.MsgTemplateModelService;

/**
 * 消息转发
 * 
 * @author Houkm
 *
 *         2017年8月14日
 */
@Component
public class MsgFacedeServiceImpl implements MsgFacedeService {

	private Logger logger = LoggerFactory.getLogger(MsgFacedeServiceImpl.class);

	@Autowired
	private MsgModelService msgModelService;

	@Autowired
	private MsgTemplateModelService msgTplModelService;

	@Autowired
	ExceptionProperties exceptionProp;

	@Override
	public Result<Object> transmit(MsgDTO msg) {
		Result<MsgTemplateModel> tplModel = msgTplModelService.get(msg.getCode());
		if (tplModel == null || tplModel.getData() == null) {
			throw ExceptionBuilder.build(exceptionProp).format("msg_code_not_exist", msg.getCode()).create();
		}
		logger.info("消息参数: {}", msg.getParams());
		logger.info("消息接收方: {}", msg.getReceiver());
		MsgTemplateModel model = tplModel.getData();
		switch (model.getMessageType()) {
		case 1:// 微信
			logger.info("发送微信消息");
			msgModelService.sendWeChat(msg);
			break;
		case 2:// 钉钉群
			logger.info("发送钉钉消息");
			msgModelService.sendDingDing(msg);
			break;
		case 3:// 短信
			logger.info("发送短信");
			msgModelService.sendSms(msg);
			break;
		case 4:// 推送
			logger.info("发送推送消息");
			msgModelService.sendPush(msg);
			break;
		case 5:// 邮件
			logger.info("发送邮件");
			msgModelService.sendMail(msg);
			break;
		case 6:// 钉钉机器人
			logger.info("发送机器人消息");
			msgModelService.sendDingDingRobot(msg);
			break;
		case 7:// 钉钉微应用
			logger.info("发送微应用消息");
			msgModelService.sendDingDingMiniApp(msg);
			break;
		default:
			break;
		}
		return Results.success();
	}

}
