package com.inno72.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.log.LogAllContext;
import com.inno72.log.vo.LogType;
import com.inno72.service.ImportAppLogService;


@RestController
public class TaskLogContoller {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskLogContoller.class);

	@Autowired
	private ImportAppLogService importAppLogService;

	@GetMapping("/task/insert")
	public void testInsert(){
		new LogAllContext(LogType.PRODUCT)
				.tag("产品日志")
				.activityId("activityId")
				.appName("appName")
				.detail("detail")
				.instanceName("instanceName")
				.level("dev")
				.operatorId("operatorId")
				.platform("platform")
				.time("2018-01-01 10:09:22")
				.userId("123321231")
				.bulid();
		
		LOGGER.info("安卓产品上报日志! === {} ", "");
	}

	@GetMapping("/task/run")
	public void testTask(){
		try {
			importAppLogService.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
