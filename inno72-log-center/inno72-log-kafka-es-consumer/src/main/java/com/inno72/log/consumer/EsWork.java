package com.inno72.log.consumer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.inno72.log.repository.SysLog;
import com.inno72.log.repository.SysLogRepository;

public class EsWork implements Callable<String>{

	private static Logger logger = LoggerFactory.getLogger(EsWork.class);

	private ConsumerRecords<String, String> consumerRecord;
	private Semaphore semaphore;

	private Map<TopicPartition, OffsetAndMetadata> offsetsMap = new HashMap<>();

	private SysLogRepository sysLogRepository;

	EsWork(ConsumerRecords<String, String> consumerRecord, Semaphore semaphore, SysLogRepository sysLogRepository){
		this.consumerRecord = consumerRecord;
		this.semaphore = semaphore;
		this.sysLogRepository = sysLogRepository;
	}

	@Override
	public String call() {
		try {
			logger.info("当前线程 {{{{}}}} 开始处理 「『{}』」分区， semaphore「{}」", Thread.currentThread().getName(), consumerRecord.partitions(),semaphore);
			semaphore.acquire();
			List<SysLog> sysLogs = new ArrayList<>();
			for (final ConsumerRecord<String, String> record : consumerRecord) {
				offsetsMap.clear();
				//记录当前 TopicPartition和OffsetAndMetadata
				TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
				OffsetAndMetadata offset = new OffsetAndMetadata(record.offset());
				offsetsMap.put(topicPartition, offset);
				sysLogs.add(JSON.parseObject(record.value(), SysLog.class));
			}
			sysLogRepository.save(sysLogs);
			logger.info(JSON.toJSONString(sysLogs));
		} catch (Exception e) {
			logger.error("错误 {}, 堆栈 {} \n 当前处理集合 {}, 出错消息 {}, ",e.getMessage(), e, JSON.toJSONString(consumerRecord), JSON.toJSON(offsetsMap));
		} finally{
			semaphore.release();
		}
		return "succ";
	}

}
