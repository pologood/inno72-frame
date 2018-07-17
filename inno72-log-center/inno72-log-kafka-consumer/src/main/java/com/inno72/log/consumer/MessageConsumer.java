package com.inno72.log.consumer;
import com.alibaba.fastjson.JSON;
import com.inno72.log.util.SysLogRepository;
import com.inno72.log.util.TopicEnum;
import com.inno72.log.vo.OtherLog;
import com.inno72.log.vo.SysLog;
import com.inno72.mongo.MongoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 消费日志消息
 */
@Service
public class MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Resource
    private MongoUtil mongoUtil;

    @Autowired
    private SysLogRepository sysLogRepository;

    @KafkaListener(topics = TopicEnum.BIZ.topic)
    public void onBizMessage(String message) {
		OtherLog otherLog = JSON.parseObject(message, OtherLog.class);
		mongoUtil.save(otherLog);
    }


    @KafkaListener(topics = TopicEnum.PRODUCT.topic)
    public void onProductMessage(String message) {
		OtherLog otherLog = JSON.parseObject(message, OtherLog.class);
		mongoUtil.save(otherLog);
    }


    @KafkaListener(topics = TopicEnum.SYS.topic)
    public void onSysMessage(String message) {
		SysLog sysLog = JSON.parseObject(message, SysLog.class);
		sysLogRepository.save(sysLog);
    }
}