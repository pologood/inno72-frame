package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.Inno72LogTaskServiceProperties;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.FileUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.log.LogAllContext;
import com.inno72.log.LogContext;
import com.inno72.log.vo.LogType;
import com.inno72.mapper.Inno72AppLogMapper;
import com.inno72.model.Inno72AppLog;
import com.inno72.service.ImportAppLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Service("importAppLogService")
public class ImportAppLogServiceImpl implements ImportAppLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportAppLogService.class);

	@Resource
	private Inno72AppLogMapper inno72AppLogMapper;

	@Autowired
	private FileUtil fileUtil;

	@Override
	public void execute() throws Exception {

		List<Inno72AppLog> inno72AppLogs = inno72AppLogMapper.selectAllByStatus(1);
		if ( inno72AppLogs.size() == 0 ){
			LOGGER.info("ImportAppLogServiceImpl.execute app没有需要上报的日志记录");
			return;
		}
		List<Future<Inno72AppLog>> futures = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (Inno72AppLog log : inno72AppLogs){
			boolean download = fileUtil.download(log.getLogUrl());
			if (download){
				ParseLog parseLog = new ParseLog(log);
				Future<Inno72AppLog> submit = executorService.submit(parseLog);
				futures.add(submit);
			}
		}
		for (Future<Inno72AppLog> futureTask : futures){
			Inno72AppLog log = futureTask.get();
			inno72AppLogMapper.updateByPrimaryKeySelective(log);

		}
		executorService.shutdown();
	}


	class DownloadThread extends Thread {

		private Inno72AppLog inno72AppLog;

		public DownloadThread(Inno72AppLog inno72AppLog){
			this.inno72AppLog = inno72AppLog;
		}
		@Override
		public void run() {
			try {

				boolean download = true;
				//				boolean download = FileUtil.download(inno72AppLog.getLogUrl());
				if (download){
					//					futures.add(new FutureTask(new ParseLog(inno72AppLog)));
				}
			}catch (Exception e){
				System.out.println(e.getMessage());
			}


		}
	}

	//	@Autowired
	//	private Inno72LogTaskServiceProperties inno72LogTaskServiceProperties;

	class ParseLog implements Callable<Inno72AppLog>{

		private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

		private Inno72AppLog inno72AppLog;

		ParseLog(Inno72AppLog inno72AppLog){
			this.inno72AppLog = inno72AppLog;
		}

		@Override
		public Inno72AppLog call() throws Exception {

			//			String localBasePath = inno72LogTaskServiceProperties.get("log_local0_path");
			String localBasePath = "/tmp/";
			String logUrl = inno72AppLog.getLogUrl();
			String localPath = localBasePath + logUrl.substring(0, logUrl.lastIndexOf("."));
			File file=new File(localPath);

			inno72AppLog.setReciveTime(LocalDateTime.now());

			if (!file.exists()){
				inno72AppLog.setStatus(Inno72AppLog.Inno72AppLog_status.FAIL.status());
				inno72AppLog.setErrorLog("文件不存在!");
				return inno72AppLog;
			}

			if (file.isDirectory()){
				File[] files = file.listFiles();
				assert files != null;
				if (files.length == 0){
					inno72AppLog.setStatus(Inno72AppLog.Inno72AppLog_status.SUCC.status());
					inno72AppLog.setErrorLog("压缩文件包没有文件!");
					return inno72AppLog;
				}
				LOGGER.info("{}上报日志,{}", JSON.toJSONString(inno72AppLog), JSON.toJSONString(files));

				for ( File curFile : files ){
					BufferedReader reader=null;
					String temp=null;
					int line=1;
					try{
						reader=new BufferedReader(new FileReader(curFile));
						while((temp=reader.readLine())!=null){
							line++;
							if (StringUtil.isEmpty(temp)){
								continue;
							}
							LOGGER.info("当前行 ： {}" , temp);

							String logType = FastJsonUtils.getString(temp, "logType");
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
							.tag("产品日志")
							.activityId(FastJsonUtils.getString(temp,"activityId"))
							.appName(FastJsonUtils.getString(temp,"appName"))
							.detail(FastJsonUtils.getString(temp,"detail"))
							.instanceName(FastJsonUtils.getString(temp,"instanceName"))
							.level(FastJsonUtils.getString(temp,"level"))
							.operatorId(FastJsonUtils.getString(temp,"operatorId"))
							.platform(FastJsonUtils.getString(temp,"platform"))
							.time(FastJsonUtils.getString(temp,"time"))
//							.userId(FastJsonUtils.getString(temp,"userId"))
							.userId("我是测试userId")
							.bulid();
							LOGGER.info("安卓产品上报日志! === {} ", temp);

							if (line % 1000 == 0){
								try {
									Thread.sleep(2000);
								}catch (Exception e){
									LOGGER.error("线程睡眠异常 {}", e.getMessage());
								}
							}
						}
					}catch(Exception e){
						inno72AppLog.setErrorLog("解析数据不完整");
						inno72AppLog.setStatus(Inno72AppLog.Inno72AppLog_status.FAIL.status());
						return inno72AppLog;
					}
					finally{
						if(reader!=null){
							try{
								reader.close();
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}
				inno72AppLog.setErrorLog("Completion");
				inno72AppLog.setStatus(Inno72AppLog.Inno72AppLog_status.SUCC.status());
				return inno72AppLog;
			}else {
				inno72AppLog.setErrorLog("文件路径错误");
				inno72AppLog.setStatus(Inno72AppLog.Inno72AppLog_status.FAIL.status());
				return inno72AppLog;
			}
		}
	}
}
