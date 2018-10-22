package com.inno72.consumer;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.utils.StringUtil;
import com.inno72.service.MachineDataCountService;
import com.inno72.util.TopicEnum;
import com.inno72.model.PointLog;


/**
 * 消费日志消息
 */
@Service
public class MessageConsumer {

	private static Logger LOGEGR = LoggerFactory.getLogger(MessageConsumer.class);

	@Autowired
	private MongoOperations mongoTpl;

	@Autowired
	private MachineDataCountService machineDataCountService;

	private static final List<String> countSet = Arrays.asList("31", "32", "33", "34","1","36");

	@KafkaListener(topics = TopicEnum.POINT.topic)
	public void onPointMessage(String message) {
		PointLog pointLog = JSON.parseObject(message, PointLog.class);
		LOGEGR.info("Point topic【{}】接受消息 【{}】",TopicEnum.POINT.topic, JSON.toJSONString(pointLog));
		// 获取日志是否具有指定类型，分别存储
		mongoTpl.insert(pointLog, "PointLog");
		String type = pointLog.getType();
		if (StringUtil.isNotEmpty(type) && countSet.contains(type)){
			machineDataCountService.countLog(pointLog);
		}
	}

}