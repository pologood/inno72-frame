package com.point72.msg.center.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data

@Configuration
@ConfigurationProperties(prefix = "yyxk.getui.android")
public class GetuiAndroidProperties {

	private String appid;
	private String appkey;
	private String appsecret;

}
