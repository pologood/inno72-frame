package com.point72.msg.center;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.point72.config.client.MemcachedKeysProperties;
import com.point72.config.client.SMSProperties;
import com.point72.springboot.web.SpringApplicationBuilder;
import com.point72.springboot.web.SpringBootServletInitializer;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = { "com.point72" })
@EnableEurekaClient
@EnableConfigurationProperties({ MemcachedKeysProperties.class, SMSProperties.class })
public class MsgCenterApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MsgCenterApplication.class, "msg-center", args);
	}

	@Override
	public String setAppNameForLog() {
		return "msg-center";
	}
}
