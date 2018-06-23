package com.inno72.msg.center.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data

@Configuration
@ConfigurationProperties(prefix = "yyxk.getui.pro")
public class GetuiProProperties {

	private String appid;
	private String appkey;
	private String appsecret;

}
