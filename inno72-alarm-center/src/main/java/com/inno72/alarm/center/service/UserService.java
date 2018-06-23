package com.inno72.alarm.center.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inno72.alarm.center.model.Department;
import com.inno72.common.Result;

public interface UserService {

	/**
	 * 根据token获取memcached中的信息
	 * 
	 * @param token
	 * @return
	 * @author Houkm 2017年6月27日
	 */
	public Result<JSONObject> get(String token);

	/**
	 * 获取所有部门用户数据
	 * 
	 * @return
	 * @author Houkm 2017年6月27日
	 */
	public Result<List<Department>> getDepartmentTree();

	public Result<JSONArray> generateDepartmentNgxBootstrapTree();
}
