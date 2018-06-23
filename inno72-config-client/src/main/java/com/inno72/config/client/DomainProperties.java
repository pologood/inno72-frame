package com.inno72.config.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data

@ConfigurationProperties(prefix = "yyxk.domain")
public class DomainProperties extends AbstractProperties {

	private String msg;
	private String alarm;

}
