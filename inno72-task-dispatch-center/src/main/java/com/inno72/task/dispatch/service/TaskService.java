package com.inno72.task.dispatch.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inno72.common.Result;
import com.inno72.task.dispatch.model.Task;
import com.inno72.task.dispatch.model.TaskModel;

public interface TaskService {

	/**
	 * 保存任务
	 * 
	 * @param tasks
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<String> save(List<Task> tasks);

	/**
	 * 保存任务
	 * 
	 * @param task
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<String> save(Task task);

	/**
	 * 根据任务模型ID获取任务
	 * 
	 * @param taskModelId
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<Page<Task>> getByTaskModel(String taskModelId, Pageable pageable);

	/**
	 * 根据任务模型创建一个任务
	 * 
	 * @param taskModel
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	Result<Task> create(TaskModel taskModel);

	/**
	 * 批量根据任务模型创建任务
	 * 
	 * @param taskModelList
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	Result<List<Task>> create(List<TaskModel> taskModelList);

}
