package com.point72.alarm.center.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.point72.alarm.center.model.Follow;
import com.point72.alarm.center.model.PageInfo;
import com.point72.alarm.center.service.FollowService;
import com.point72.common.Result;
import com.point72.common.Results;

/**
 * 问题报错接口
 * 
 * @author Houkm
 *
 *         2017年8月11日
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/follow", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FollowController {

	@Autowired
	FollowService followService;

	/**
	 * 报错列表
	 * 
	 * @param follow
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Result<PageInfo<List<Follow>>> query(Follow follow,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		return followService.getList(follow, pageNo, pageSize);
	}

	/**
	 * 新增报错
	 * 
	 * @param follow
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Result<Boolean> save(@RequestBody Follow follow) {
		followService.save(follow);
		return Results.success(true);
	}

	/**
	 * 报错详情
	 * 
	 * @param code
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public Result<Follow> query(@PathVariable("code") String code) {
		return followService.get(code);
	}

	/**
	 * 修改报错
	 * 
	 * @param follow
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public Result<Boolean> modify(@RequestBody Follow follow) {
		followService.save(follow);
		return Results.success(true);
	}

	/**
	 * 删除报错
	 * 
	 * @param code
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.DELETE)
	public Result<Boolean> remove(@PathVariable("code") String code) {
		followService.remove(code);
		return Results.success(true);
	}

}
