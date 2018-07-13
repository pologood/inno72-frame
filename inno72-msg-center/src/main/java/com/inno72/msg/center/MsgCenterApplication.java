package com.inno72.msg.center;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.inno72.config.client.MemcachedKeysProperties;
import com.inno72.config.client.SMSProperties;
import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = { "com.inno72" })
@EnableEurekaClient
@EnableConfigurationProperties({ SMSProperties.class })
public class MsgCenterApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(MsgCenterApplication.class, "msg-center", args);
	}

	@Override
	public String setAppNameForLog() {
		return "msg-center";
	}
}
