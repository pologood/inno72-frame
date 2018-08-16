package com.inno72.config.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "multiple.datasource.inno72_saas")
@Data
public class DataSourceSAASProperties extends AbstractProperties {
	private String url;
	private String username;
	private String password;
	private String driverClassName;
}
