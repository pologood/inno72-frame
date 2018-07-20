package com.inno72.log.consumer;

import com.alibaba.fastjson.JSON;
import com.inno72.log.repository.SysLogRepository;
import com.inno72.log.util.TopicEnum;
import com.inno72.log.vo.OtherLog;
import com.inno72.log.repository.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 消费日志消息
 */
@Service
public class MessageConsumer {

    private static Logger LOGEGR = LoggerFactory.getLogger(MessageConsumer.class);

	@Autowired
	private MongoOperations mongoTpl;

    @Autowired
    private SysLogRepository sysLogRepository;

    @KafkaListener(topics = "test")
    public void onMessage(String message) {
		OtherLog otherLog = JSON.parseObject(message, OtherLog.class);
		LOGEGR.info("BIZ topic【{}】接受消息 【{}】",TopicEnum.BIZ.topic, JSON.toJSONString(otherLog));
		mongoTpl.save(otherLog);
    }
    @KafkaListener(topics = TopicEnum.BIZ.topic)
    public void onBizMessage(String message) {
    	OtherLog otherLog = JSON.parseObject(message, OtherLog.class);
    	LOGEGR.info("BIZ topic【{}】接受消息 【{}】",TopicEnum.BIZ.topic, JSON.toJSONString(otherLog));
    	mongoTpl.save(otherLog);
    }


    @KafkaListener(topics = TopicEnum.PRODUCT.topic)
    public void onProductMessage(String message) {
		OtherLog otherLog = JSON.parseObject(message, OtherLog.class);
		LOGEGR.info("PRODUCT topic【{}】接受消息 【{}】",TopicEnum.PRODUCT.topic ,JSON.toJSONString(otherLog));
		mongoTpl.save(otherLog);
    }


    @KafkaListener(topics = TopicEnum.SYS.topic)
    public void onSysMessage(String message) {
		SysLog sysLog = JSON.parseObject(message, SysLog.class);
		LOGEGR.info("SYS topic【{}】接受消息 【{}】",TopicEnum.SYS.topic ,JSON.toJSONString(sysLog));
		sysLogRepository.save(sysLog);
    }
}