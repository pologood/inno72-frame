package com.inno72.log.consumer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.inno72.log.repository.SysLogRepository;

public class ConsumerThrad extends Thread{
	private static Logger logger = LoggerFactory.getLogger(ConsumerThrad.class);

	private ExecutorService workerExecutorService;

	private Semaphore semaphore;

	private Map<TopicPartition, OffsetAndMetadata> offsetsMap = new HashMap<>();

	private KafkaConsumer<String, String> consumer;

	private List<Future<String>> taskList = new ArrayList<>();

	private SysLogRepository sysLogRepository;

	ConsumerThrad(KafkaConsumer<String, String> consumer, ExecutorService workerExecutorService, Semaphore semaphore,
			SysLogRepository sysLogRepository){
		this.workerExecutorService = workerExecutorService;
		this.semaphore = semaphore;
		this.consumer = consumer;
		this.sysLogRepository = sysLogRepository;
	}

	@Override
	public void run() {

		while (ConsumerInit.isRun) {
			try {
				ConsumerRecords<String, String> records = consumer.poll(500);
				if ( records.isEmpty() ) {
					logger.info("本次无记录 ->>>> {}", JSON.toJSONString(records));
					Thread.sleep(399);
					continue;
				}

				taskList.add(workerExecutorService.submit(new EsWork(records, semaphore, sysLogRepository)));

				for(Future<String> task : taskList){
					String string = task.get();
					if (!string.equals("succ")) {
						logger.error("失败记录 -> {}", JSON.toJSONString(offsetsMap));
						//TODO 是否需要重复发送消息 继续消费？
					}
				}
				consumer.commitSync();

			} catch (Exception e) {
				logger.error("TopicPartitionThread run error.", e.getMessage());
			} finally{
				taskList.clear();
			}
		}
		consumer.close();
		
	}
}
