package com.inno72.alarm.center.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.inno72.alarm.center.model.Solution;
import com.inno72.alarm.center.service.SolutionService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mongo.MongoUtil;

@Component
public class SolutionServiceImpl implements SolutionService {
	@Autowired
	private MongoUtil mongoUtil;
	@Override
	public void save(Solution solution) {
		solution.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		solution.setCreateTime(LocalDateTime.now());
		mongoUtil.save(solution);
	}

	@Override
	public void remove(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoUtil.delete(query, Solution.class);
	}

	@Override
	public Result<Solution> get(String code) {
		Solution solution =  mongoUtil.findById(code, Solution.class);
		return Results.success(solution);
	}

	@Override
	public Result<List<Solution>> getList(Solution solution) {
		Query query = new Query();
		query.addCriteria(Criteria.where("questionId").is(solution.getQuestionId()));
		query.with(new Sort(Direction.ASC, "num")); //排序
		List<Solution> list = mongoUtil.find(query, Solution.class);
		return Results.success(list);
	}

}
