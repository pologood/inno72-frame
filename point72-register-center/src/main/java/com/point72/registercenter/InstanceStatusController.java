package com.point72.registercenter;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

/**
 * 注册中心实例管理
 * 
 * @author Houkm
 *
 *         2017年5月25日
 */
@RestController
public class InstanceStatusController {

	private Logger logger = LoggerFactory.getLogger("注册中心实例管理接口");

	/**
	 * 将applicationName的version的实例状态改为OUT_OF_SERVICE
	 * 
	 * @param applicationName
	 * @param version
	 * @return
	 * @author Houkm 2017年5月24日
	 */
	@RequestMapping(value = "/takeOut", method = RequestMethod.POST)
	public Collection<String> takeOut(String applicationName, String instanceId) {
		logger.error("准备将服务 【{}】 实例ID 【{}】 移出注册中心", applicationName, instanceId);
		return getRegister().getSortedApplications().stream().filter(hasApplication(applicationName))
				.flatMap(app -> app.getInstances().stream()).filter(hasInstance(instanceId))
				.map(instance -> updateInstanceStatus(instance, InstanceStatus.OUT_OF_SERVICE))
				.collect(Collectors.toList());
	}

	/**
	 * 将applicationName的version的实例状态改为OUT_OF_SERVICE
	 * 
	 * @param applicationName
	 * @param version
	 * @return
	 * @author Houkm 2017年5月24日
	 */
	@RequestMapping(value = "/getBack", method = RequestMethod.POST)
	public Collection<String> getBack(String applicationName, String instanceId) {
		logger.error("准备将服务 【{}】 实例ID 【{}】 移回注册中心", applicationName, instanceId);
		return getRegister().getSortedApplications().stream().filter(hasApplication(applicationName))
				.flatMap(app -> app.getInstances().stream()).filter(hasInstance(instanceId))
				.map(instance -> updateInstanceStatus(instance, InstanceStatus.UP)).collect(Collectors.toList());
	}

	/**
	 * 获取所有app
	 * 
	 * @return
	 * @author Houkm 2017年5月25日
	 */
	@RequestMapping(value = "/getapps", method = RequestMethod.GET)
	public List<Application> getApps() {
		return getRegister().getSortedApplications();
	}

	/**
	 * 查看实例
	 * 
	 * @param applicationNameCriteria
	 * @param instanceId
	 * @return
	 * @author Houkm 2017年5月25日
	 */
	@RequestMapping(value = "/instancesQuery", method = RequestMethod.POST)
	public Collection<String> queryInstancesByMetaData(@RequestParam("applicationName") String applicationNameCriteria,
			@RequestParam("instanceId") String instanceId) {
		return getRegister().getSortedApplications().stream().filter(hasApplication(applicationNameCriteria))
				.flatMap(app -> app.getInstances().stream()).filter(hasInstance(instanceId))
				.map(info -> info.getAppName() + " - " + info.getId() + " - " + info.getStatus())
				.collect(Collectors.toList());
	}

	private String updateInstanceStatus(InstanceInfo instanceInfo, InstanceStatus status) {
		boolean isSuccess = getRegister().statusUpdate(instanceInfo.getAppName(), instanceInfo.getId(), status,
				String.valueOf(System.currentTimeMillis()), true);
		logger.error("instanceId【{}】状态修改 【{}】 【{}】", instanceInfo.getId(), status, isSuccess ? "成功" : "失败");
		return (instanceInfo.getAppName() + " - " + instanceInfo.getId() + " result: " + isSuccess);

	}

	/**
	 * 查看是否对应名称的实例
	 * 
	 * @param applicationName
	 * @return
	 * @author Houkm 2017年5月24日
	 */
	private Predicate<Application> hasApplication(final String applicationName) {
		return application -> application.getName().toUpperCase().equals(applicationName);
	}

	/**
	 * 是否有对应ID的实例
	 * 
	 * @param version
	 * @return
	 * @author Houkm 2017年5月24日
	 */
	private Predicate<InstanceInfo> hasInstance(final String instanceId) {
		return instanceInfo -> instanceInfo.getId().equals(instanceId);
	}

	/**
	 * 获取注册器实例
	 * 
	 * @return
	 * @author Houkm 2017年5月24日
	 */
	private PeerAwareInstanceRegistry getRegister() {
		return EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
	}

}
