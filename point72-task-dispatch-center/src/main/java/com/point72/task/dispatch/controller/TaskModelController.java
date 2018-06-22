package com.point72.task.dispatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.point72.common.Result;
import com.point72.common.Results;
import com.point72.task.dispatch.model.Task;
import com.point72.task.dispatch.model.TaskModel;
import com.point72.task.dispatch.service.TaskModelService;
import com.point72.task.dispatch.service.TaskService;

@RestController
@RequestMapping("/taskModel")
public class TaskModelController {

	@Autowired
	private TaskModelService taskModelService;

	@Autowired
	private TaskService taskService;

	/**
	 * 获取所有任务
	 * 
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	@GetMapping
	public Result<Page<TaskModel>> getAll(TaskModel taskModel,
			@PageableDefault(page = 0, size = 10, sort = "nextTime", direction = Direction.DESC) Pageable pageable) {
		return taskModelService.getAll(taskModel, pageable);
	}

	/**
	 * 新增任务
	 * 
	 * @param taskModel
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	@PostMapping
	public Result<String> post(@RequestBody TaskModel taskModel) {
		return this.taskModelService.save(taskModel);
	}

	/**
	 * 修改任务
	 * 
	 * @param taskModel
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	@PutMapping
	public Result<String> put(@RequestBody TaskModel taskModel) {
		return this.taskModelService.save(taskModel);
	}

	/**
	 * 获取任务信息
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	@GetMapping("/{id}")
	public Result<TaskModel> getById(@PathVariable("id") String id) {
		return this.taskModelService.getById(id);
	}

	/**
	 * 获取执行历史
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	@GetMapping("/history/{id}")
	public Result<Page<Task>> getHistory(@PathVariable("id") String id,
			@PageableDefault(page = 0, size = 10, sort = "startTime", direction = Direction.DESC) Pageable pageable) {
		return this.taskService.getByTaskModel(id, pageable);
	}

	/**
	 * 删除任务
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	@DeleteMapping("/{id}")
	public Result<String> delete(@PathVariable("id") String id) {
		return this.taskModelService.delete(id);
	}

	/**
	 * 上传执行文件
	 * 
	 * @param file
	 * @param response
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	@PostMapping("/upload/{id}")
	public Result<String> upload(@RequestParam("file") MultipartFile file, @PathVariable("id") String id) {
		this.taskModelService.saveFile(file, id);
		return Results.success();
	}

	/**
	 * 立即执行任务
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	@PostMapping("/run/{id}")
	public Result<String> run(@PathVariable("id") String id) {
		return this.taskModelService.run(id);
	}

	/**
	 * 开始任务
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	@PostMapping("/start/{id}")
	public Result<String> start(@PathVariable("id") String id) {
		return this.taskModelService.start(id);
	}

	/**
	 * 暂停任务
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月27日
	 */
	@PostMapping("/pause/{id}")
	public Result<String> pause(@PathVariable("id") String id) {
		return this.taskModelService.pause(id);
	}

}
