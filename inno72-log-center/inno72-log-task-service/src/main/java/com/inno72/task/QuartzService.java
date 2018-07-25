package com.inno72.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inno72.service.ImportAppLogService;

@Component
public class QuartzService {

	@Autowired
	private ImportAppLogService importAppLogService;

	@Scheduled(cron = "0 10 * * * ?")
	public void tast() throws Exception {
		importAppLogService.execute();
	}

}
