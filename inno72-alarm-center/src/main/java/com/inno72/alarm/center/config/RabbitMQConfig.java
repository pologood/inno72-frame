package com.inno72.alarm.center.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.inno72.alarm.center.listener.ExceptionNotifyListener;
import com.inno72.mq.core.AbstractListenerContainer;
import com.inno72.mq.core.AbstractTaskExecutor;

@Configuration
public class RabbitMQConfig {

	/**
	 * 实例线程池实例
	 * 
	 * @return
	 * @Date 2017年6月16日
	 * @Author Houkm
	 */
	@Bean
	public AbstractTaskExecutor taskExecutor() {
		AbstractTaskExecutor taskExecutor = new AbstractTaskExecutor();
		return taskExecutor;
	}

	@Autowired
	ExceptionNotifyListener exceptionNotifyListener;

	/**
	 * 报警记录队列监听器
	 * 
	 * @param connectionFactory
	 * @param taskExecutor
	 * @return
	 * @Date 2017年6月16日
	 * @Author Houkm
	 */
	@Bean
	AbstractListenerContainer msgContainer(ConnectionFactory connectionFactory, ThreadPoolTaskExecutor taskExecutor) {
		AbstractListenerContainer container = new AbstractListenerContainer(connectionFactory, taskExecutor,
				exceptionNotifyListener);
		return container;
	}

}
