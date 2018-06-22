package com.point72.task.dispatch.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.point72.common.Result;
import com.point72.task.dispatch.model.TaskModel;

public interface TaskModelService {

	/**
	 * 获取所有任务
	 * 
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<Page<TaskModel>> getAll(TaskModel taskModel, Pageable pageable);

	/**
	 * 获取非暂停切未删除的任务
	 * 
	 * @param start
	 * @param end
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<List<TaskModel>> getExecuteList();

	/**
	 * 获取任务详情
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<TaskModel> getById(String id);

	/**
	 * 保存任务
	 * 
	 * @param model
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<String> save(TaskModel model);

	/**
	 * 删除任务
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<String> delete(String id);

	/**
	 * 保存文件
	 * 
	 * @param file
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<String> saveFile(MultipartFile file, String id);

	/**
	 * 立即执行
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<String> run(String id);

	/**
	 * 开始任务
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<String> start(String id);

	/**
	 * 暂停任务
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月26日
	 */
	Result<String> pause(String id);

	/**
	 * 下载日志
	 * 
	 * @param date
	 * @author Houkm 2017年8月4日
	 */
	ResponseEntity<byte[]> downloadLog(LocalDate date, String taskName);

}
