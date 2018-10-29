package com.inno72.log.consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ConsumerInit {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;
	@Value("${spring.kafka.consumer.max-poll-records}")
	private String maxPollRecords;
	private Integer patition;

	public static boolean isRun = true;

	public ConsumerInit getInit(){
		return this;
	}

	public List<KafkaConsumer<String,String>> getConsumerClient() {
		List<KafkaConsumer<String,String>> consumers = new ArrayList<>();
		String topic = "log-sys";
		if (patition == null) {
			KafkaConsumer<String,String> consumerInit = this.getConsumerInit(topic, patition);
			consumers.add(consumerInit);
		}else {
			if (patition >= 0) {
				for (int i = 0; i < patition; i++) {
					KafkaConsumer<String,String> consumerInit = this.getConsumerInit(topic, i);
					consumers.add(consumerInit);
				}
			}
		}
		return consumers;
	}

	public KafkaConsumer<String, String> getConsumerInit(String topic, Integer patitionNum)
	{
		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapServers);
		props.put("group.id", groupId);
		props.put("auto.offset.reset", "latest");//latest
		props.put("enable.auto.commit", "false");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		if (patitionNum != null) {
			TopicPartition partition = new TopicPartition(topic, patitionNum);
			consumer.assign(Collections.singletonList(partition));
		}else {
			consumer.subscribe(Collections.singletonList(topic));
		}

		return consumer;
	}

	public Long getMaxPollRecords() {
		return Long.parseLong(maxPollRecords);
	}

	public void setMaxPollRecords(String maxPollRecords) {
		this.maxPollRecords = maxPollRecords;
	}

}
