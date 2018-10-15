package com.inno72;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com.inno72"})
@EnableDiscoveryClient
public class KafkaPointConsumerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(KafkaPointConsumerApplication.class, "inno72-log-kafka-point-consumer", args);
	}

	@Override
	public String setAppNameForLog() {
		return "inno72-kafka-consumer-service";
	}
}
