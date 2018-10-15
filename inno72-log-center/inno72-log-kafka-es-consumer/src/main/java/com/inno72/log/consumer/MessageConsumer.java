package com.inno72.log.consumer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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

	@Resource
	private SysLogRepository sysLogRepository;

	@KafkaListener(topics = TopicEnum.SYS.topic, containerFactory = "batchFactory")
	public void onSysMessage(List<String> message) {
		LOGEGR.info("************************************ {} ************************************", message.size());
		List<SysLog> sysLogs = new ArrayList<>();
		for (String msg : message){
			sysLogs.add(JSON.parseObject(msg, SysLog.class));
		}
		Iterable<SysLog> save = sysLogRepository.save(sysLogs);
		LOGEGR.info("SYS topic【{}】消费结果 ===> {}",TopicEnum.SYS.topic , JSON.toJSONString(save));

	}
}