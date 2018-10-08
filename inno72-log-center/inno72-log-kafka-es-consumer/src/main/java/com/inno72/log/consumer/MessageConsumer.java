package com.inno72.log.consumer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.datetime.LocalDateTimeUtil;
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

	@KafkaListener(topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"0"})}, containerFactory = "batchFactory")
	public void onSysMessage0(List<ConsumerRecord<?,?>> record) {
		save(record);
	}

	@KafkaListener(topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"1"})}, containerFactory = "batchFactory")
	public void onSysMessage1(List<ConsumerRecord<?,?>> record) {
		save(record);
	}

	@KafkaListener(topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"2"})}, containerFactory = "batchFactory")
	public void onSysMessage2(List<ConsumerRecord<?,?>> record) {
		save(record);
	}

	@KafkaListener(topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"3"})}, containerFactory = "batchFactory")
	public void onSysMessage3(List<ConsumerRecord<?,?>> record) {
		save(record);
	}


	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
	private void save(List<ConsumerRecord<?,?>> records){
		fixedThreadPool.execute(() -> {
			LOGEGR.info("线程{}开始时间{},共处理{}条数据", Thread.currentThread().getName(), LocalDateTimeUtil.transfer(LocalDateTime.now(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS")), records.size());
			List<SysLog> sysLogs = new ArrayList<>();
			for (ConsumerRecord record : records){
				Optional optional = Optional.ofNullable(record.value());
				if (optional.isPresent()){
					sysLogs.add(JSON.parseObject(record.value().toString(), SysLog.class));
				}
			}
			Iterable<SysLog> save = sysLogRepository.save(sysLogs);
			LOGEGR.info("线程{}结束时间{}", Thread.currentThread().getName(), LocalDateTimeUtil.transfer(LocalDateTime.now(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS")));

		});

	}
}