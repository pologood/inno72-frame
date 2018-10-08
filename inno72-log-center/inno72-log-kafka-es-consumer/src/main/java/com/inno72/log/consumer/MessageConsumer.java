package com.inno72.log.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
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

	@KafkaListener(id = "id0", topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"0"})}, containerFactory = "batchFactory")
	public void onSysMessage0(List<ConsumerRecord<?,?>> records) {
		List<SysLog> sysLogs = new ArrayList<>();
		for (ConsumerRecord<?, ?> record : records){
			Optional optional = Optional.ofNullable(record.value());
			if (optional.isPresent()){
				sysLogs.add(JSON.parseObject(record.value().toString(), SysLog.class));
			}
		}
		Iterable<SysLog> save = sysLogRepository.save(sysLogs);
		LOGEGR.debug("SYS partition 0 - topic【{}】消费结果 ===> {}",TopicEnum.SYS.topic , JSON.toJSONString(save));

	}

	@KafkaListener(id = "id1", topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"1"})}, containerFactory = "batchFactory")
	public void onSysMessage1(List<ConsumerRecord<?,?>> records) {
		List<SysLog> sysLogs = new ArrayList<>();
		for (ConsumerRecord<?, ?> record : records){
			Optional optional = Optional.ofNullable(record.value());
			if (optional.isPresent()){
				sysLogs.add(JSON.parseObject(record.value().toString(), SysLog.class));
			}
		}
		Iterable<SysLog> save = sysLogRepository.save(sysLogs);
		LOGEGR.debug("SYS partition 1 - topic【{}】消费结果 ===> {}",TopicEnum.SYS.topic , JSON.toJSONString(save));
	}

	@KafkaListener(id = "id2", topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"2"})}, containerFactory = "batchFactory")
	public void onSysMessage2(List<ConsumerRecord<?,?>> records) {
		List<SysLog> sysLogs = new ArrayList<>();
		for (ConsumerRecord<?, ?> record : records){
			Optional optional = Optional.ofNullable(record.value());
			if (optional.isPresent()){
				sysLogs.add(JSON.parseObject(record.value().toString(), SysLog.class));
			}
		}
		Iterable<SysLog> save = sysLogRepository.save(sysLogs);
		LOGEGR.debug("SYS partition 2 - topic【{}】消费结果 ===> {}",TopicEnum.SYS.topic , JSON.toJSONString(save));
	}

	@KafkaListener(id = "id3", topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"3"})}, containerFactory = "batchFactory")
	public void onSysMessage3(List<ConsumerRecord<?,?>> records) {
		List<SysLog> sysLogs = new ArrayList<>();
		for (ConsumerRecord<?, ?> record : records){
			Optional optional = Optional.ofNullable(record.value());
			if (optional.isPresent()){
				sysLogs.add(JSON.parseObject(record.value().toString(), SysLog.class));
			}
		}
		Iterable<SysLog> save = sysLogRepository.save(sysLogs);
		LOGEGR.debug("SYS partition 3 - topic【{}】消费结果 ===> {}",TopicEnum.SYS.topic , JSON.toJSONString(save));
	}
}