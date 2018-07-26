package com.inno72.log;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com.inno72"})
@EnableDiscoveryClient
public class KafkaConsumerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(KafkaConsumerApplication.class, "inno72-kafka-consumer-service", args);
	}

	@Override
	public String setAppNameForLog() {
		return "inno72-kafka-consumer-service";
	}
}
