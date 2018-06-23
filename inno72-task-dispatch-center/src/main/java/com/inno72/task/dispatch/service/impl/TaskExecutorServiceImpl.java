package com.inno72.task.dispatch.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.FileCopyUtils;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.config.client.YyxkConfigProperties;
import com.inno72.mongo.MongoUtil;
import com.inno72.process.ExecutorRunner;
import com.inno72.task.dispatch.model.TaskExecutorModel;
import com.inno72.task.dispatch.model.TaskExecutorModel.ExecutorStatus;
import com.inno72.task.dispatch.model.TaskModel;
import com.inno72.task.dispatch.service.TaskExecutorService;

//@Service
public class TaskExecutorServiceImpl implements TaskExecutorService {

	@Autowired
	private YyxkConfigProperties yyxkProp;

	@Autowired
	private MongoUtil mongoUtil;

	@Autowired
	private ExecutorRunner runner;

	@Override
	public Result<String> create() {
		File in = new File(yyxkProp.get("taskExecutorPath") + "/executor.jar");
		String outPath = generateCopyExecutorPath();
		File out = new File(outPath);
		try {
			FileCopyUtils.copy(in, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		TaskExecutorModel taskExecutor = new TaskExecutorModel();
		taskExecutor.setId(StringUtil.uuid());
		taskExecutor.setCreateTime(LocalDateTime.now());
		taskExecutor.setFilePath(outPath);
		taskExecutor.setStatus(ExecutorStatus.STOP);
		this.mongoUtil.save(taskExecutor);
		return Results.success();
	}

	@Override
	public Result<String> start(String id) {
		TaskExecutorModel executor = this.get(id).getData();
		this.runner.start(executor.getId(), executor.getFilePath());
		executor.setStatus(ExecutorStatus.RUNNING);
		this.mongoUtil.save(executor);
		return Results.success();
	}

	@Override
	public Result<String> stop(String id) {
		TaskExecutorModel executor = this.get(id).getData();
		this.runner.stop(id);
		executor.setStatus(ExecutorStatus.STOP);
		this.mongoUtil.save(executor);
		return Results.success();
	}

	@Override
	public Result<TaskExecutorModel> get(String id) {
		TaskExecutorModel executor = this.mongoUtil.findById(id, TaskExecutorModel.class);
		return Results.success(executor);
	}

	@Override
	public Result<Page<TaskExecutorModel>> get(TaskExecutorModel taskExecutorModel, Pageable pageable) {
		Query query = new Query();
		if (taskExecutorModel.getStatus() != null) {
			query.addCriteria(Criteria.where("status").is(taskExecutorModel.getStatus()));
		}
		long count = mongoUtil.count(query, TaskModel.class);
		query.with(pageable);
		List<TaskExecutorModel> data = this.mongoUtil.find(query, TaskExecutorModel.class);
		Page<TaskExecutorModel> page = new PageImpl<>(data, pageable, count);
		return Results.success(page);
	}

	private String generateCopyExecutorPath() {
		return yyxkProp.get("taskExecutorPath") + "/executor-" + System.currentTimeMillis() + ".jar";
	}

}
