package com.inno72.log.consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 消费日志消息
 */
@Service
public class MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @KafkaListener(topics = TopicConst.PAY_TOPIC)
    public void onMessage(String payMessage) {
		System.out.println(payMessage);
    }
}