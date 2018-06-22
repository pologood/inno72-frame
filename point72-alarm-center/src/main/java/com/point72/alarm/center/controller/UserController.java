package com.point72.alarm.center.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.point72.alarm.center.service.UserService;
import com.point72.common.Result;
import com.point72.common.Results;

@CrossOrigin
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * 根据token换取信息
	 * 
	 * @param token
	 * @return
	 * @author Houkm 2017年6月27日
	 */
	@RequestMapping(value = "/{token}", method = RequestMethod.GET)
	public Result<Object> query(@PathVariable("token") String token) {
		return Results.success(userService.get(token));
	}

	/**
	 * 根据parentId获取部门列表
	 * 
	 * @param parentId
	 * @return
	 * @author Houkm 2017年6月27日
	 */
	@GetMapping("/tree")
	public Result<JSONArray> getDepartment() {
		return this.userService.generateDepartmentNgxBootstrapTree();
	}

}
