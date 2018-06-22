package com.point72.zuul;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Hello world!
 *
 */

@SpringBootApplication(scanBasePackages = { "com.point72" })
@EnableZuulProxy
@EnableEurekaClient
@EnableHystrixDashboard
@EnableTurbine
public class Zuul extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		System.setProperty("application_name", "zuul");
		String active = System.getenv("spring_profiles_active");
		active = active == null || active.equals("") ? "dev" : active;
		return builder.profiles(active).sources(Zuul.class);
	}

	public static void main(String[] args) {
		System.setProperty("application_name", "zuul");
		String active = System.getenv("spring_profiles_active");
		active = active == null || active.equals("") ? "dev" : active;
		new SpringApplicationBuilder(Zuul.class).profiles(active).web(true).run(args);
	}
}
