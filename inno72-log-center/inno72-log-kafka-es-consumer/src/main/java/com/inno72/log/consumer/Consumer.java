package com.inno72.log.consumer;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.inno72.log.repository.SysLogRepository;


@Component
public class Consumer implements CommandLineRunner {

	@Resource
	private SysLogRepository sysLogRepository;

	@Resource
	private ConsumerInit consumerInit;

	@Override
	public void run(String... args) {
		new Main(sysLogRepository, consumerInit).start();
	}
}
