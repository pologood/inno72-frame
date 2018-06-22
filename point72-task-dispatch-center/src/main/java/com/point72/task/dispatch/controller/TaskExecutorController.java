package com.point72.task.dispatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.point72.common.Result;
import com.point72.task.dispatch.model.TaskExecutorModel;
import com.point72.task.dispatch.service.TaskExecutorService;

//@RestController
@RequestMapping("/executor")
public class TaskExecutorController {

	@Autowired
	private TaskExecutorService taskExecutorService;

	/**
	 * 任务执行器列表数据
	 * 
	 * @param model
	 * @param pageable
	 * @return
	 * @author Houkm 2017年8月9日
	 */
	@GetMapping
	public Result<Page<TaskExecutorModel>> get(TaskExecutorModel model,
			@PageableDefault(page = 0, size = 10, sort = "status", direction = Direction.ASC) Pageable pageable) {
		return this.taskExecutorService.get(model, pageable);
	}

	/**
	 * 创建任务执行器
	 * 
	 * @return
	 * @author Houkm 2017年8月9日
	 */
	@GetMapping("/new")
	public Result<String> create() {
		return taskExecutorService.create();
	}

	/**
	 * 执行任务执行器
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年8月9日
	 */
	@GetMapping("/start/{id}")
	public Result<String> start(@PathVariable("id") String id) {
		return taskExecutorService.start(id);
	}

	/**
	 * 停止运行任务执行器
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年8月9日
	 */
	@GetMapping("/stop/{id}")
	public Result<String> stop(String id) {
		return taskExecutorService.stop(id);
	}

}
