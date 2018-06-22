package com.point72.alarm.center.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.point72.alarm.center.model.LogSystemException;
import com.point72.alarm.center.model.PageInfo;
import com.point72.alarm.center.service.LogExceptionService;
import com.point72.common.Result;

/**
 * 系统异常
 * 
 * @author Houkm
 *
 *         2017年7月11日
 */
@RestController
@RequestMapping("/ex/system")
@CrossOrigin
public class SystemExController {

	@Autowired
	private LogExceptionService exService;

	/**
	 * 系统异常列表
	 * 
	 * @param ex
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@GetMapping("")
	public Result<PageInfo<List<LogSystemException>>> getList(LogSystemException ex,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		return exService.getSystemExceptionList(ex, pageNo, pageSize);
	}

	/**
	 * 系统异常详情
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@GetMapping("/{id}")
	public Result<LogSystemException> get(@PathVariable("id") String id) {
		return exService.getSystemException(id);
	}

}
