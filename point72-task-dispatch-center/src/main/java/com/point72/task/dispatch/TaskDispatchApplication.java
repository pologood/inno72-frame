package com.point72.task.dispatch;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = { "com.point72" })
@EnableEurekaClient
public class TaskDispatchApplication extends com.point72.springboot.web.SpringBootServletInitializer {

	public static void main(String[] args) {
		new com.point72.springboot.web.SpringApplicationBuilder(TaskDispatchApplication.class, "task_dispatch", args);
	}

	@Override
	public String setAppNameForLog() {
		return "task_dispatch";
	}

}
