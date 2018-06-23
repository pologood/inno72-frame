package com.inno72.task.dispatch.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.mongo.MongoUtil;
import com.inno72.task.dispatch.model.Task;
import com.inno72.task.dispatch.model.TaskModel;
import com.inno72.task.dispatch.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private MongoUtil mongoUtil;

	@Override
	public Result<String> save(List<Task> tasks) {
		tasks.forEach(task -> {
			mongoUtil.save(task);
		});
		return Results.success();
	}

	@Override
	public Result<String> save(Task task) {
		mongoUtil.save(task);
		return Results.success();
	}

	@Override
	public Result<Page<Task>> getByTaskModel(String taskModelId, Pageable pageable) {
		Query query = new Query(Criteria.where("taskModelId").is(taskModelId));
		long count = mongoUtil.count(query, Task.class);
		query.with(pageable);
		List<Task> data = mongoUtil.find(query, Task.class);
		Page<Task> page = new PageImpl<>(data, pageable, count);
		return Results.success(page);
	}

	@Override
	public Result<Task> create(TaskModel taskModel) {
		Task task = new Task();
		task.setId(StringUtil.uuid());
		task.setTaskModel(taskModel);
		task.setTaskModelId(taskModel.getId());
		task.setAnalyzeTime(taskModel.getNextTime());
		task.setRegisteTime(LocalDateTime.now());
		this.save(task);
		return Results.success(task);
	}

	@Override
	public Result<List<Task>> create(List<TaskModel> taskModelList) {
		List<Task> taskList = new ArrayList<>();
		taskModelList.forEach(taskModel -> {
			taskList.add(this.create(taskModel).getData());
		});
		return Results.success(taskList);
	}

}
