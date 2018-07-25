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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@Component
public class KafkaSink extends AbstractSink implements Configurable {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSink.class);

	private Producer<String, String> producer;

	private static String zkAddress;
	private static String kafkaAddress;
	private static String logKey;


	public KafkaSink() throws IOException {
		Properties logProperties = new PropertiesUtil().load("/home/logUser/services/flume/conf/base.propersties");
		zkAddress = Optional.ofNullable(logProperties.get("inno72.log.zookeeper_connect")).map(Object::toString).orElse("");
		kafkaAddress = Optional.ofNullable(logProperties.get("inno72.log.kafka_metadata_broker_list")).map(Object::toString).orElse("");
		logKey = Optional.ofNullable(logProperties.get("inno72.log.logKey")).map(Object::toString).orElse("");
		System.out.println("zkAddress:"+zkAddress);
		System.out.println("kafkaAddress:"+kafkaAddress);
		System.out.println("logKey:"+logKey);
		Properties prop = new Properties();
		prop.put("zookeeper.connect", zkAddress);
		prop.put("metadata.broker.list", kafkaAddress);
		prop.put("serializer.class", StringEncoder.class.getName());
		producer = new Producer<String, String>(new ProducerConfig(prop));
	}

	@Override
	public Status process() throws EventDeliveryException {
		Status status = Status.READY;
		Channel channel = getChannel();
		Transaction transaction = channel.getTransaction();

		Event event = channel.take();
		LOGGER.info("event ===> {}", JSON.toJSONString(event));
		if (event == null) {
			return Status.BACKOFF;
		}
		try {
			transaction.begin();
		} catch (Exception e) {
			LOGGER.info("开启事物异常 =========> {}", e.getMessage(), e);
		}

		try {

			System.out.println("进入自定义sink");

			byte[] body = event.getBody();
			LOGGER.info("body ===> {}", JSON.toJSONString(body));
			String msg = new String(body);
			System.out.println("自定义sink msg" + msg);
			if (StringUtils.isBlank(msg)){
				try {
					transaction.close();
				} catch (Exception e2) {
					LOGGER.info("关闭事物异常" , e2.getMessage(), e2 );
				}
				return Status.BACKOFF;
			}
			System.out.println("开始解析 =================================");
			JSONObject msgJsonObject = JSON.parseObject(msg);
			System.out.println("解析后的日志 ===> "+JSON.toJSONString(msgJsonObject));
			String logType = Optional.ofNullable(msgJsonObject.get(logKey)).map(Object::toString).orElse("");
			System.out.println("自定义sink logType:" + logType);
			if ( StringUtils.isEmpty(logType) ){
				status = Status.BACKOFF;
			}

			TopicEnum byType = TopicEnum.findByType(logType);
			if ( byType == null ){
				status = Status.BACKOFF;
			}

			LOGGER.info("kafka 分发topic {}", byType.getTopic());
			if ( !status.equals(Status.BACKOFF) ) {
				System.out.println("创建message ======> "+JSON.toJSONString(byType.getTopic()) +" ；；；；；；；  msg =====> "+msg);
				final KeyedMessage<String, String> message = new KeyedMessage<String, String>(byType.getTopic(), msg);
				System.out.println("message ======> "+JSON.toJSONString(message));
				producer.send(message);
			}
			transaction.commit();
		} catch (Exception e) {
			LOGGER.info("进入异常 {}  -----> ",e.getMessage(), e);
			try {
				transaction.rollback();
			} catch (Exception e2) {
				LOGGER.info("回滚事物异常 =========>{} ===》getMessage {}", e2.getMessage(), e2);
			}

			status = Status.BACKOFF;
		} finally {
			try {
				transaction.close();
			} catch (Exception e2) {
				System.out.println("关闭事物异常" + e2);
			}
		}
		System.out.println("自定义sink status 结束" + status);
		return Status.READY;
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
