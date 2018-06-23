package com.inno72.task.dispatch.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.config.client.ExceptionProperties;
import com.inno72.config.client.YyxkConfigProperties;
import com.inno72.exception.ExceptionBuilder;
import com.inno72.mongo.MongoUtil;
import com.inno72.task.dispatch.model.TaskModel;
import com.inno72.task.dispatch.service.TaskModelService;

@Service
public class TaskModelServiceImpl implements TaskModelService {

	private Logger logger = LoggerFactory.getLogger(TaskModelServiceImpl.class);

	@Autowired
	private MongoUtil mongoUtil;

	@Autowired
	ExceptionProperties exeProp;

	@Autowired
	private YyxkConfigProperties yyxkProp;

	@Override
	public Result<Page<TaskModel>> getAll(TaskModel taskModel, Pageable pageable) {
		Query query = new Query(Criteria.where("delete").is(false));

		if (taskModel.getKey() != null && !"".equals(taskModel.getKey())) {
			Pattern pattern = Pattern.compile("^.*" + taskModel.getKey() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(new Criteria().orOperator(
					//@formatter:off
					new Criteria("name").regex(pattern), 
					new Criteria("policy").is(taskModel.getKey()),
					new Criteria("fileName").regex(pattern)
					//@formatter:on
			));
		}

		if (taskModel.getStartTime() != null) {
			Criteria timeParam = Criteria.where("nextTime")
					.gte(LocalDateTimeUtil.toDate(taskModel.getStartTime(), ZoneOffset.ofHours(8)));
			if (taskModel.getEndTime() != null) {
				timeParam.lt(LocalDateTimeUtil.toDate(taskModel.getEndTime(), ZoneOffset.ofHours(8)));
			}
			query.addCriteria(timeParam);
		}

		if (taskModel.getExecutable() != null) {
			query.addCriteria(Criteria.where("executable").is(taskModel.getExecutable()));
		}

		long count = mongoUtil.count(query, TaskModel.class);
		query.with(pageable);
		List<TaskModel> data = mongoUtil.find(query, TaskModel.class);
		Page<TaskModel> page = new PageImpl<>(data, pageable, count);
		return Results.success(page);
	}

	@Override
	public Result<List<TaskModel>> getExecuteList() {
		List<TaskModel> data = mongoUtil.find(new Query(Criteria.where("executable").is(true).and("delete").is(false)),
				TaskModel.class);
		return Results.success(data);
	}

	@Override
	public Result<TaskModel> getById(String id) {
		// TODO Auto-generated method stub
		TaskModel taskModel = mongoUtil.findById(id, TaskModel.class);
		return Results.success(taskModel);
	}

	@Override
	public Result<String> save(TaskModel model) {
		if (model.getId() == null || model.getId().equals("")) {
			model.setId(StringUtil.uuid());
			this.mongoUtil.save(model);
		} else {
			TaskModel example = new TaskModel();
			example.setId(model.getId());
			this.mongoUtil.update(example, model.generateUpdate(), TaskModel.class);
		}
		return Results.success();
	}

	@Override
	public Result<String> delete(String id) {
		TaskModel taskModel = this.getById(id).getData();
		taskModel.setDelete(true);
		TaskModel example = new TaskModel();
		example.setId(id);
		this.mongoUtil.update(example, taskModel.generateUpdate(), TaskModel.class);
		return Results.success();
	}

	@Override
	public Result<String> saveFile(MultipartFile file, String id) {
		File dest = new File(yyxkProp.get("taskFilePath") + file.getOriginalFilename());
		try {
			file.transferTo(dest);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		TaskModel taskModel = this.getById(id).getData();
		taskModel.setFileName(file.getOriginalFilename());
		taskModel.setFilePath(dest.getAbsolutePath());
		this.save(taskModel);
		return Results.success();
	}

	@Override
	public Result<String> run(String id) {
		TaskModel taskModel = this.getById(id).getData();
		if (taskModel != null) {
			if (checkLegality(taskModel)) {
				taskModel.setNextTime(LocalDateTime.now());
				this.save(taskModel);
				logger.info("run");
			}
		}
		return Results.success();
	}

	@Override
	public Result<String> start(String id) {
		TaskModel taskModel = this.getById(id).getData();
		if (taskModel != null) {
			if (checkLegality(taskModel)) {
				taskModel.setExecutable(true);
				this.save(taskModel);
			}
		}
		return Results.success();
	}

	@Override
	public Result<String> pause(String id) {
		TaskModel taskModel = this.getById(id).getData();
		if (taskModel != null) {
			taskModel.setExecutable(false);
			this.save(taskModel);
		}
		return Results.success();
	}

	/**
	 * 检查任务是否合法并设置下一次执行时间
	 * 
	 * @param taskModel
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	private boolean checkLegality(TaskModel taskModel) {
		CronTriggerImpl trigger = new CronTriggerImpl();
		boolean legal = false;
		try {
			trigger.setCronExpression(taskModel.getPolicy());
			LocalDateTime nextTime = analyze(taskModel.getPolicy());
			// 检查cron合法性
			legal = nextTime != null && nextTime.isAfter(LocalDateTime.now());
			if (legal) {

				// 设置下一次执行时间
				taskModel.setNextTime(nextTime);
				String filePath = taskModel.getFilePath();
				// 检查是否有执行文件
				if (filePath != null) {
					File file = new File(filePath);
					if (!file.exists()) {
						legal = false;
						throw ExceptionBuilder.build(exeProp).format("illegalFile", taskModel.getPolicy()).create();
					}
				} else {
					legal = false;
					throw ExceptionBuilder.build(exeProp).format("illegalFile", taskModel.getPolicy()).create();
				}
			} else {
				legal = false;
				throw ExceptionBuilder.build(exeProp).format("illegalPolicy", taskModel.getPolicy()).create();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			throw ExceptionBuilder.build(exeProp).format("illegalPolicy", taskModel.getPolicy()).create();
		}
		return legal;
	}

	private LocalDateTime analyze(String cron) {
		CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
		try {
			cronTriggerImpl.setCronExpression(cron);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (dates != null && dates.size() > 0) {
			return LocalDateTimeUtil.transfer(df.format(dates.get(0)));
		}
		return null;
	}

	@Override
	public ResponseEntity<byte[]> downloadLog(LocalDate date, String taskName) {
		ResponseEntity<byte[]> response = null;

		String filePath = "/usr/local/Tomcat/Logs/task_dispatch";
		if (date.isEqual(LocalDate.now())) {
			filePath += "/" + taskName + ".log";
		} else {
			String year = String.valueOf(date.getYear());
			String month = String
					.valueOf(date.getMonthValue() < 10 ? "0" + date.getMonthValue() : date.getMonthValue());
			filePath += "/" + year + "-" + month + "/" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-"
					+ taskName + ".log";
		}
		File logFile = new File(filePath);

		if (logFile.exists()) {
			// StreamingResponseBody stream = new StreamingResponseBody() {
			// @Override
			// public void writeTo(OutputStream outputStream) throws IOException
			// {
			BufferedInputStream bis = null;
			ByteArrayOutputStream bos = null;
			try {
				bos = new ByteArrayOutputStream();
				bis = new BufferedInputStream(new FileInputStream(logFile));
				byte[] b = new byte[2048];
				while (bis.read(b) != -1) {
					bos.write(b);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// }
			// };

			response = ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment;filename=" + logFile.getName()).body(bos.toByteArray());

		} else {
			try {
				response = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
						.body(Results.failure("日志文件不存在").json().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

}
