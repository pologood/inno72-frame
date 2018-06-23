package com.inno72.config.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Dubbo配置属性
 * 
 * @author Houkm
 *
 *         2017年5月18日
 */
@ConfigurationProperties(prefix = "yyxk.dubbo")
public class DubboProperties {

	private String registryAddress;

	private String registryProtocol;

	private String monitorProtocol;

	private Integer orderPort;

	/**
	 * 需在自己工程中配置, 不配置时使用spring.application.name
	 */
	private String applicationName;

	public Integer getOrderPort() {
		return orderPort;
	}

	public void setOrderPort(Integer orderPort) {
		this.orderPort = orderPort;
	}

	public String getMonitorProtocol() {
		return monitorProtocol;
	}

	public void setMonitorProtocol(String monitorProtocol) {
		this.monitorProtocol = monitorProtocol;
	}

	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public String getRegistryProtocol() {
		return registryProtocol;
	}

	public void setRegistryProtocol(String registryProtocol) {
		this.registryProtocol = registryProtocol;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

}
