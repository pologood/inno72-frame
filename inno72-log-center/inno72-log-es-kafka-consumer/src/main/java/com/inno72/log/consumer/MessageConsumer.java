package com.inno72.log.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.log.repository.SysLog;
import com.inno72.log.repository.SysLogRepository;
import com.inno72.log.util.TopicEnum;

/**
 * 消费日志消息
 */
@Service
public class MessageConsumer {

    private static Logger LOGEGR = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private SysLogRepository sysLogRepository;

    @KafkaListener(topics = TopicEnum.SYS.topic)
    public void onSysMessage(String message) {
		SysLog sysLog = JSON.parseObject(message, SysLog.class);
		LOGEGR.info("SYS topic【{}】接受消息 【{}】",TopicEnum.SYS.topic ,JSON.toJSONString(sysLog));
		SysLog save = sysLogRepository.save(sysLog);
		LOGEGR.info("SYS 消费结果 ===> ", JSON.toJSONString(save));
    }
}