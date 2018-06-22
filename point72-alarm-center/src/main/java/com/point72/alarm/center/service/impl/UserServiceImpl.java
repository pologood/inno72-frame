package com.point72.alarm.center.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.point72.alarm.center.mapper.DepartmentMapper;
import com.point72.alarm.center.mapper.UserMapper;
import com.point72.alarm.center.model.Department;
import com.point72.alarm.center.service.UserService;
import com.point72.common.Result;
import com.point72.common.Results;
import com.point72.config.client.MemcachedKeysProperties;
import com.point72.ddtalk.user.UserHandler;
import com.point72.ddtalk.user.model.User;
import com.point72.memcached.client.ClientXmemcached;

@Component
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	ClientXmemcached clientXmemcached;

	@Autowired
	MemcachedKeysProperties memcachedKeysProperties;

	@Autowired
	UserMapper userMapper;

	@Autowired
	DepartmentMapper deptMapper;

	@Override
	public Result<JSONObject> get(String token) {

		String info = (String) clientXmemcached.get(token);
		JSONObject obj = JSON.parseObject(info);
		String ddToken = (String) clientXmemcached.get(memcachedKeysProperties.getDdtoken());

		String id = obj.getJSONObject("user").getString("id");
		com.point72.alarm.center.model.User userMap = userMapper.selectByPrimaryKey(id);
		String userId = userMap.getUserid();

		User user = UserHandler.get(ddToken, userId);
		if (info != null) {
			logger.info("token info : {}", info);
			obj.put("user", user);
			return Results.success(obj);
		} else {
			return Results.failure("");
		}
	}

	@Override
	public Result<List<Department>> getDepartmentTree() {
		List<Department> result = deptMapper.findTree();
		return Results.success(result);
	}

	@Override
	public Result<JSONArray> generateDepartmentNgxBootstrapTree() {
		List<Department> data = this.getDepartmentTree().getData();
		JSONArray deptJson = new JSONArray();
		data.forEach(dept -> {
			deptJson.add(generateNode(dept));
		});
		return Results.success(deptJson);
	}

	private JSONObject generateNode(Department department) {
		JSONObject json = new JSONObject();
		json.put("id", department.getId());
		json.put("name", department.getName());
		json.put("isUser", false);
		JSONArray children = new JSONArray();
		if (department.getChildren().size() > 0) {
			department.getChildren().forEach(dept -> {
				children.add(generateNode(dept));
			});
		}
		department.getUser().forEach(user -> {
			JSONObject userJson = new JSONObject();
			userJson.put("id", user.getUserid());
			userJson.put("name", user.getName());
			userJson.put("isUser", true);
			children.add(userJson);
		});
		json.put("children", children);
		return json;
	}

}
