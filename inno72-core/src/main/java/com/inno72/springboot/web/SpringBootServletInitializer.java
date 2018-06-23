package com.inno72.springboot.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 继承自org.springframework.boot.web.support.SpringBootServletInitializer<br>
 * 通过环境变量spring_profiles_active来指定active,并通过setAppNameForLog()来指定log4j2的日志子目录
 * 
 * @author Houkm
 *
 *         2017年8月11日
 */
public abstract class SpringBootServletInitializer
		extends org.springframework.boot.web.support.SpringBootServletInitializer {

	Logger logger = LoggerFactory.getLogger(SpringBootServletInitializer.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		String active = System.getenv("spring_profiles_active");
		logger.info("获取spring_profiles_active：{}", active);
		if (active == null || active.equals("")) {
			logger.info("未读取到spring_profiles_active的环境变量,使用默认值: dev");
			active = "dev";
		}
		builder.profiles(active);
		System.setProperty("application_name", setAppNameForLog());
		return builder.sources(this.getClass());
	}

	public abstract String setAppNameForLog();

}
