package com.point72.alarm.center.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.point72.alarm.center.model.LogException;
import com.point72.alarm.center.model.PageInfo;
import com.point72.alarm.center.service.LogExceptionService;
import com.point72.common.Result;

/**
 * 程序异常
 * 
 * @author Houkm
 *
 *         2017年7月11日
 */
@RestController
@RequestMapping("/ex")
@CrossOrigin
public class ExceptionController {

	@Autowired
	private LogExceptionService exService;

	/**
	 * 获取异常列表
	 * 
	 * @param ex
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@GetMapping("")
	public Result<PageInfo<List<LogException>>> getList(LogException ex,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		return exService.getBusinessExceptionList(ex, pageNo, pageSize);
	}

	/**
	 * 获取异常详情
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@GetMapping("/{id}")
	public Result<LogException> get(@PathVariable("id") String id) {
		return exService.getBusinessException(id);
	}

}
