package com.inno72.log.consumer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
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

	@KafkaListener(id = "id0", topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"0"})}, containerFactory = "batchFactory")
	public void onSysMessage0(List<ConsumerRecord<?,?>> record, Acknowledgment acknowledgment) {
		try {
			save(record);
		}finally {
//			acknowledgment.acknowledge();
		}


	}

	@KafkaListener(id = "id1",topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"1"})}, containerFactory = "batchFactory")
	public void onSysMessage1(List<ConsumerRecord<?,?>> record, Acknowledgment acknowledgment) {
		try {
			save(record);
		}finally {
//			acknowledgment.acknowledge();
		}
	}

	@KafkaListener(id = "id2", topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"2"})}, containerFactory = "batchFactory")
	public void onSysMessage2(List<ConsumerRecord<?,?>> record, Acknowledgment acknowledgment) {
		try {
			save(record);
		}finally {
//			acknowledgment.acknowledge();
		}
	}

	@KafkaListener(id = "id3", topicPartitions = {@TopicPartition( topic = TopicEnum.SYS.topic,partitions = {"3"})}, containerFactory = "batchFactory")
	public void onSysMessage3(List<ConsumerRecord<?,?>> record, Acknowledgment acknowledgment) {
		try {
			save(record);
		}finally {
//			acknowledgment.acknowledge();
		}
	}

	private static int count = 0;

	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	private void save(List<ConsumerRecord<?,?>> records){
		fixedThreadPool.execute(() -> {
			LOGEGR.info("线程{}开始时间{},共处理{}条数据", Thread.currentThread().getName(), LocalDateTimeUtil.transfer(LocalDateTime.now(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS")), records.size());
			List<SysLog> sysLogs = new ArrayList<>();
			for (ConsumerRecord record : records){
				Optional optional = Optional.ofNullable(record.value());
				LOGEGR.info("key - {}, offset - {}, partition - {}",record.key(),record.offset(),record.partition());
				if (optional.isPresent()){
					sysLogs.add(JSON.parseObject(record.value().toString(), SysLog.class));
				}
			}
			Iterable<SysLog> save = sysLogRepository.save(sysLogs);
			LOGEGR.info("线程{}结束时间{}", Thread.currentThread().getName(), LocalDateTimeUtil.transfer(LocalDateTime.now(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS")));
			count+=records.size();
			System.out.println(count);
		});

	}
}