package com.inno72.msg.center.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.inno72.config.client.AbstractProperties;

@ConfigurationProperties(value = "inno72.task")
public class QyWeChatProperties extends AbstractProperties {

}
