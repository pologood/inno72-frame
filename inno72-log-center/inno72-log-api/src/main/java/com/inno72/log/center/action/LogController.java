package com.inno72.log.center.action;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.log.LogAllContext;
import com.inno72.log.center.util.FastJsonUtils;
import com.inno72.log.vo.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping(value = "/log/")
@CrossOrigin
public class LogController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);


	/**
	 *
	 * @param file 日志文件
	 * @param pushId 推送ID
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public @ResponseBody
	Result uploadImg(@RequestParam("file") MultipartFile file, String pushId) throws IOException {

		// 解压缩文件
		InputStream is = file.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		String line;

		for (; ; ) {

			line = reader.readLine();
			if (null != line) {

				String logType = FastJsonUtils.getString(line, "logType");
				LogType logTypeE = null;

				switch (logType){

					case "product" :
						logTypeE = LogType.PRODUCT;

					case "biz" :
						logTypeE = LogType.BIZ;

					case "sys" :
						logTypeE = LogType.SYS;

				}

				if ( logTypeE == null ){
					continue;
				}

				new LogAllContext(logTypeE)
						.tag(FastJsonUtils.getString(line,"tag"))
						.activityId(FastJsonUtils.getString(line,"activityId"))
						.appName(FastJsonUtils.getString(line,"appName"))
						.detail(FastJsonUtils.getString(line,"detail"))
						.instanceName(FastJsonUtils.getString(line,"instanceName"))
						.level(FastJsonUtils.getString(line,"level"))
						.operatorId(FastJsonUtils.getString(line,"operatorId"))
						.platform(FastJsonUtils.getString(line,"platform"))
						.time(FastJsonUtils.getString(line,"time"))
						.userId(FastJsonUtils.getString(line,"userId"))
						.bulid();
				LOGGER.info("上报日志! === {} ", line);

				// 解析
			} else {
				break;
			}
		}
		// 返回json
		return Results.success();
	}
}
