package com.inno72.task;

import com.inno72.service.ImportAppLogService;
import com.inno72.service.SuperQuartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class QuartzService {

	@Autowired
	private ImportAppLogService importAppLogService;

	@Scheduled(cron = "0 10 * * * ?")
	public void tast() throws Exception {
		importAppLogService.execute();
	}

}
