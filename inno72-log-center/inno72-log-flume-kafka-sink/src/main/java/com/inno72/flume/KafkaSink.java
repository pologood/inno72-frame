package com.inno72.flume;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;

public class KafkaSink extends AbstractSink implements Configurable {

	Producer<String, String> producer;
	String topic = "test";

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
				status = Status.BACKOFF;
				return status;
			}
			byte[] body = event.getBody();
			final String msg = new String(body);
			final KeyedMessage<String, String> message = new KeyedMessage<String, String>(topic, msg);
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
		Properties prop = new Properties();
		prop.put("zookeeper.connect", "192.168.33.243:2181");
		prop.put("metadata.broker.list", "192.168.33.243:9092");

//		prop.put("zookeeper.connect", PropertiesUtil.getProperty("zookeeper.connect"));
//		prop.put("metadata.broker.list", PropertiesUtil.getProperty("metadata.broker.list"));
		prop.put("serializer.class", StringEncoder.class.getName());
		producer = new Producer<String, String>(new ProducerConfig(prop));
	}
}
