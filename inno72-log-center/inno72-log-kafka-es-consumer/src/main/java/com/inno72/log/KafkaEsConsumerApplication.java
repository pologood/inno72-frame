package com.inno72.log;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com.inno72"})
@EnableEurekaClient
@EnableElasticsearchRepositories(basePackages = "com.inno72.log.repository")
public class KafkaEsConsumerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(KafkaEsConsumerApplication.class, "inn72-kafka-es-consumer-service", args);
	}

	@Override
	public String setAppNameForLog() {
		return "inn72-kafka-es-consumer-service";
	}

}
