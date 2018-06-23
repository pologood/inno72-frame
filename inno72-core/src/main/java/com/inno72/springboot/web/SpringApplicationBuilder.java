package com.inno72.springboot.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 继承自org.springframework.boot.builder.SpringApplicationBuilder<br>
 * 直接通过环境变量spring_profiles_active来指定active，并设置log4j2的日志目录<br>
 * 构造方法中将直接启动org.springframework.boot.builder.SpringApplicationBuilder
 * 
 * @author Houkm
 *
 *         2017年8月11日
 */
public class SpringApplicationBuilder extends org.springframework.boot.builder.SpringApplicationBuilder {

	Logger logger = LoggerFactory.getLogger(SpringApplicationBuilder.class);

	public SpringApplicationBuilder(Class<?> mainClass, String logName, String[] args, boolean web) {
		System.setProperty("application_name", logName);
		new org.springframework.boot.builder.SpringApplicationBuilder(mainClass).profiles(getActive()).web(web)
				.run(args);
	}

	public SpringApplicationBuilder(Class<?> mainClass, String logName, String[] args) {
		System.setProperty("application_name", logName);
		new org.springframework.boot.builder.SpringApplicationBuilder(mainClass).profiles(getActive()).web(true)
				.run(args);
	}

	@Bean
	public FilterRegistrationBean crossFilter() {
		Filter filter = new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Credentials", "true");
				response.setHeader("Access-Control-Allow-Methods", "POST, PUT, DELETE, OPTIONS, HEAD");
				response.setHeader("Access-Control-Allow-Headers",
						"User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,lf-None-Matoh");
				response.setHeader("Access-Control-Max-Age", "1209600");
				response.setHeader("Access-Control-Expose-Headers", "lf-None-Matoh");
				response.setHeader("Access-Control-Request-Headers", "lf-None-Matoh");
				response.setHeader("Expires", "-1");
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("pragma", "no-cache");
				filterChain.doFilter(request, response);
			}
		};

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.addUrlPatterns("/*");
		registration.setName("crossFilter");
		registration.setOrder(1);
		return registration;
	}

	private String getActive() {
		String active = System.getenv("spring_profiles_active");
		logger.info("获取spring_profiles_active：{}", active);
		if (active == null || active.equals("")) {
			logger.info("未读取到spring_profiles_active的环境变量,使用默认值: dev");
			active = "dev";
		}
		return active;
	}

}
