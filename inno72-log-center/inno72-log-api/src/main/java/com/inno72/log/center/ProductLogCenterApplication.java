package com.inno72.log.center;

import com.inno72.springboot.web.SpringApplicationBuilder;
import com.inno72.springboot.web.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = { "com.inno72" })
@EnableEurekaClient
public class ProductLogCenterApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ProductLogCenterApplication.class, "product-log-center", args);
	}

	@Override
	public String setAppNameForLog() {
		return "product-log-center";
	}
}
