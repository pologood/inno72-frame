package com.inno72.flume;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.util.PropertiesUtil;
import com.inno72.util.TopicEnum;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@Component
public class KafkaSink extends AbstractSink implements Configurable {

	private Producer<String, String> producer;



	private static String zkAddress;
	private static String kafkaAddress;
	private static String logKey;


	@PostConstruct
	public void init() throws IOException {

		Properties logProperties = new PropertiesUtil().load("/base.propersties");
		zkAddress = Optional.ofNullable(logProperties.get("zookeeper_connect")).map(Object::toString).orElse("");
		kafkaAddress = Optional.ofNullable(logProperties.get("kafka_metadata_broker_list")).map(Object::toString).orElse("");
		logKey = Optional.ofNullable(logProperties.get("logKey")).map(Object::toString).orElse("");

		Properties prop = new Properties();
		prop.put("zookeeper.connect", zkAddress);
		prop.put("metadata.broker.list", kafkaAddress);
		prop.put("serializer.class", StringEncoder.class.getName());
		producer = new Producer<String, String>(new ProducerConfig(prop));
	}

	@Override
	public Status process() throws EventDeliveryException {
		Status status = null;
		Channel channel = getChannel();
		Transaction transaction = channel.getTransaction();
		transaction.begin();
		try {
			Event event = channel.take();
			if (event == null) {
				transaction.rollback();
				return Status.BACKOFF;
			}
			byte[] body = event.getBody();
			final String msg = new String(body);
			if (StringUtils.isEmpty( msg)){
				return Status.BACKOFF;
			}
			JSONObject msgJsonObject = JSON.parseObject(msg);
			String logType = Optional.ofNullable(msgJsonObject.get(logKey)).map(Object::toString).orElse("");

			if ( StringUtils.isEmpty(logType) ){
				return Status.BACKOFF;
			}

			TopicEnum byType = TopicEnum.findByType(logType);

			if ( byType == null ){
				return Status.BACKOFF;
			}

			final KeyedMessage<String, String> message = new KeyedMessage<String, String>(byType.getTopic(), msg);
			producer.send(message);
			transaction.commit();
			status = Status.READY;
		} catch (Exception e) {
			transaction.rollback();
			status = Status.BACKOFF;
		} finally {
			transaction.close();
		}

		return status;
	}

	@Override
	public void configure(Context arg0) {
		// todo 设置到配置文件
//		Properties prop = new Properties();
//		prop.put("zookeeper.connect", "192.168.33.243:2181");
//		prop.put("metadata.broker.list", "192.168.33.243:9092");
//		prop.put("zookeeper.connect", PropertiesUtil.getProperty("zookeeper.connect"));
//		prop.put("metadata.broker.list", PropertiesUtil.getProperty("metadata.broker.list"));
//		prop.put("serializer.class", StringEncoder.class.getName());
//		producer = new Producer<String, String>(new ProducerConfig(prop));
	}
}
