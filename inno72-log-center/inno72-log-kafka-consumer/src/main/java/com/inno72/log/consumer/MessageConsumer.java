package com.inno72.log.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.log.util.TopicEnum;
import com.inno72.log.vo.OtherLog;
import com.inno72.log.vo.PointLog;

/**
 * 消费日志消息
 */
@Service
public class MessageConsumer {

	private static Logger LOGEGR = LoggerFactory.getLogger(MessageConsumer.class);

	@Autowired
	private MongoOperations mongoTpl;

	@KafkaListener(topics = TopicEnum.PRODUCT.topic)
	public void onBizMessage(String message) {

		OtherLog otherLog = JSON.parseObject(message, OtherLog.class);
		LOGEGR.info("BIZ topic【{}】接受消息 【{}】",TopicEnum.BIZ.topic, JSON.toJSONString(otherLog));
		// 获取日志是否具有指定类型，分别存储
		mongoTpl.save(otherLog);
	}

}