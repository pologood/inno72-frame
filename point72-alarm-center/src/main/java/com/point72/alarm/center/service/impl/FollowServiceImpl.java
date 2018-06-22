package com.point72.alarm.center.service.impl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.point72.alarm.center.config.MsgCodeProperties;
import com.point72.alarm.center.model.Follow;
import com.point72.alarm.center.model.PageInfo;
import com.point72.alarm.center.model.Question;
import com.point72.alarm.center.service.FollowService;
import com.point72.common.Result;
import com.point72.common.Results;
import com.point72.config.client.ExceptionProperties;
import com.point72.config.client.MemcachedKeysProperties;
import com.point72.config.client.YyxkConfigProperties;
import com.point72.exception.ExceptionBuilder;
import com.point72.mongo.MongoUtil;
import com.point72.msg.MsgUtil;

@Component
public class FollowServiceImpl implements FollowService {
	@Autowired
	private MongoUtil mongoUtil;

	@Autowired
	private MsgUtil msgUtil;

	@Autowired
	MemcachedKeysProperties memKeys;

	@Autowired
	YyxkConfigProperties yyxkProp;

	@Autowired
	ExceptionProperties exProp;

	@Autowired
	MsgCodeProperties codeProp;

	@Override
	public void save(Follow follow) {

		Question question = this.mongoUtil.findById(follow.getQuestionId(), Question.class);
		String messageUrl = MessageFormat.format(yyxkProp.get("followUrl"), question.getId());
		if (follow.getId() != null && follow.getId().length() != 0) {
			Follow old = this.get(follow.getId()).getData();
			follow.setCreateBy(old.getCreateBy());
			follow.setCreateTime(old.getCreateTime());
			follow.setState(2);// 解决状态
			follow.setSolutionTime(LocalDateTime.now());

			if (question.getPersonLiable() == null || question.getPersonLiable().size() < 1) {
				throw ExceptionBuilder.build(exProp).format("no_personLiable", question.getId(), question.getContent())
						.create();
			} else {
				msgUtil.sendDDLinkByMiniApp(codeProp.get("fixedError"), null, yyxkProp.get("errorMsgAgentId"),
						question.getPersonLiable(), question.getTitle(), messageUrl, yyxkProp.get("alarmLinkPicUrl"),
						"错误中心/解决报错");
			}
		} else {
			follow.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			follow.setCreateTime(LocalDateTime.now());
			follow.setState(1); // 报错状态
			if (question.getPersonLiable() == null || question.getPersonLiable().size() < 1) {
				throw ExceptionBuilder.build(exProp).format("no_personLiable", question.getId(), question.getContent())
						.create();
			} else {
				msgUtil.sendDDLinkByMiniApp(codeProp.get("alarmError"), null, yyxkProp.get("errorMsgAgentId"),
						question.getPersonLiable(), question.getTitle(), messageUrl, yyxkProp.get("alarmLinkPicUrl"),
						"错误中心/新增报错");
			}

		}
		mongoUtil.save(follow);
	}

	@Override
	public void remove(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoUtil.delete(query, Follow.class);

	}

	@Override
	public Result<Follow> get(String code) {
		Follow follow = mongoUtil.findById(code, Follow.class);
		return Results.success(follow);
	}

	@Override
	public Result<PageInfo<List<Follow>>> getList(Follow follow, int pageNum, int pageSize) {
		Query query = new Query();
		query.addCriteria(Criteria.where("questionId").is(follow.getQuestionId()));
		query.with(new Sort(Direction.DESC, "createTime")); // 排序
		int rows = (int) mongoUtil.count(query, Follow.class);
		query.skip((pageNum - 1) * pageSize).limit(pageSize);
		List<Follow> list = mongoUtil.find(query, Follow.class);

		PageInfo<List<Follow>> pageInfo = new PageInfo<List<Follow>>(list, rows, pageNum, pageSize);
		return Results.success(pageInfo);
	}

}
