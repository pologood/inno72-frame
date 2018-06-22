package com.point72.task.dispatch.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.point72.common.Result;
import com.point72.task.dispatch.model.TaskExecutorModel;

public interface TaskExecutorService {

	Result<TaskExecutorModel> get(String id);

	Result<String> create();

	Result<String> start(String id);

	Result<String> stop(String id);

	Result<Page<TaskExecutorModel>> get(TaskExecutorModel taskExecutorModel, Pageable pageable);

}
