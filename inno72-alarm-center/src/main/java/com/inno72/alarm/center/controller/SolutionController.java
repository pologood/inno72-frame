package com.inno72.alarm.center.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.alarm.center.model.Solution;
import com.inno72.alarm.center.service.SolutionService;
import com.inno72.common.Result;
import com.inno72.common.Results;

/**
 * 解决方案接口
 * 
 * @author Houkm
 *
 *         2017年8月11日
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/solution", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SolutionController {

	@Autowired
	SolutionService solutionService;

	/**
	 * 解决方案列表
	 * 
	 * @param solution
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Result<List<Solution>> query(Solution solution) {
		return solutionService.getList(solution);
	}

	/**
	 * 新增解决方案
	 * 
	 * @param solution
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Result<Boolean> save(@RequestBody Solution solution) {
		solutionService.save(solution);
		return Results.success(true);
	}

	/**
	 * 解决方案详情
	 * 
	 * @param code
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public Result<Solution> query(@PathVariable("code") String code) {
		return solutionService.get(code);
	}

	/**
	 * 修改解决方案
	 * 
	 * @param solution
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public Result<Boolean> modify(@RequestBody Solution solution) {
		solutionService.save(solution);
		return Results.success(true);
	}

	/**
	 * 删除解决方案
	 * 
	 * @param code
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.DELETE)
	public Result<Boolean> remove(@PathVariable("code") String code) {
		solutionService.remove(code);
		return Results.success(true);
	}

}
