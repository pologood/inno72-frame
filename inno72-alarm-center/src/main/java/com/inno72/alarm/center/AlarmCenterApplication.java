package com.inno72.alarm.center;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.inno72.config.client.MemcachedKeysProperties;
import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = { "com.inno72" })
@EnableEurekaClient
@EnableConfigurationProperties({ MemcachedKeysProperties.class })
public class AlarmCenterApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AlarmCenterApplication.class, "alarm-center", args);
	}

	@Override
	public String setAppNameForLog() {
		return "alarm-center";
	}
}
