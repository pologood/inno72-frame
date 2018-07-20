package com.inno72.log;

import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication(scanBasePackages = {"com.inno72"})
@EnableDiscoveryClient
@EnableElasticsearchRepositories(basePackages = "com.inno72.log.vo")
public class KafkaConsumerApplication extends SpringBootServletInitializer {

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(KafkaConsumerApplication.class);
//	}
	public static void main(String[] args) {
		new SpringApplicationBuilder(KafkaConsumerApplication.class, "inno72-game-service", args);
	}

	@Override
	public String setAppNameForLog() {
		return "inno72-kafka-service";
	}
}
