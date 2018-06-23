package com.inno72.alarm.center.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.inno72.config.client.AbstractProperties;

@ConfigurationProperties(prefix = "yyxk.msg")
@Configuration
public class MsgCodeProperties extends AbstractProperties {

}
