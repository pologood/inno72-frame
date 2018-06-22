package com.point72.msg.center.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.point72.common.Result;
import com.point72.config.client.MqProperties;
import com.point72.core.dto.MsgDTO;
import com.point72.mq.core.AbstractListener;
import com.point72.mq.util.ConverterUtil;
import com.point72.msg.center.service.MsgFacedeService;
import com.point72.redis.StringUtil;

/**
 * 消息中心队列消息处理
 * 
 * @author Houkm
 *
 *         2017年6月16日
 */
@Component
public class MsgListener implements AbstractListener {

	private Logger logger = LoggerFactory.getLogger(MsgListener.class);

	@Autowired
	private MqProperties queueProp;

	@Autowired
	private MsgFacedeService msgFacedeService;

	@Autowired
	private StringUtil stringUtil;

	@Override
	public Result<Object> handleMessage(byte[] arg0) {
		MsgDTO msg = ConverterUtil.toObj(arg0, MsgDTO.class);
		logger.info("已接收到消息key【{}】，但是不保证后续发送成功，此key将从redis中删除。", msg.getMsgTimestamp());
		stringUtil.delete(msg.getMsgTimestamp());
		return msgFacedeService.transmit(msg);
	}

	@Override
	public String getQueueName() {
		return queueProp.getMsg().getName();
	}

}
