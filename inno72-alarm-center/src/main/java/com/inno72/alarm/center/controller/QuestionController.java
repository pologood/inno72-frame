package com.inno72.alarm.center.controller;

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

import com.inno72.alarm.center.model.PageInfo;
import com.inno72.alarm.center.model.Question;
import com.inno72.alarm.center.service.QuestionService;
import com.inno72.common.Result;
import com.inno72.common.Results;

/**
 * 问题接口
 * 
 * @author Houkm
 *
 *         2017年8月11日
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/question", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class QuestionController {

	@Autowired
	QuestionService questionService;

	/**
	 * 问题列表
	 * 
	 * @param question
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Result<PageInfo<List<Question>>> query(Question question,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		return questionService.getList(question, pageNo, pageSize);
	}

	/**
	 * 新增问题
	 * 
	 * @param question
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Result<Boolean> save(@RequestBody Question question) {
		questionService.save(question);
		return Results.success(true);
	}

	/**
	 * 问题详情
	 * 
	 * @param code
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public Result<Question> query(@PathVariable("code") String code) {
		return questionService.get(code);
	}

	/**
	 * 修改问题
	 * 
	 * @param question
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public Result<Boolean> modify(@RequestBody Question question) {
		questionService.save(question);
		return Results.success(true);
	}

	/**
	 * 删除问题
	 * 
	 * @param code
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.DELETE)
	public Result<Boolean> remove(@PathVariable("code") String code) {
		questionService.remove(code);
		return Results.success(true);
	}

}
