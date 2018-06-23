package com.inno72.alarm.center.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.inno72.alarm.center.model.PageInfo;
import com.inno72.alarm.center.model.Question;
import com.inno72.alarm.center.service.QuestionService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.mongo.MongoUtil;

@Component
public class QuestionServiceImpl implements QuestionService {
	@Autowired
	private MongoUtil mongoUtil;

	@Override
	public void save(Question question) {
		if (question.getId() != null) {
			Question old = this.get(question.getId()).getData();
			question.setCreateBy(old.getCreateBy());
			question.setCreateTime(old.getCreateTime());
			question.setModifyTime(LocalDateTime.now());
		} else {
			question.setCreateTime(LocalDateTime.now());
			question.setNum(mongoUtil.getNextId(Question.class.getName()).toString());
		}
		mongoUtil.save(question);
	}

	@Override
	public void remove(String id) {

	}

	@Override
	public Result<Question> get(String code) {
		Question question = mongoUtil.findById(code, Question.class);
		return Results.success(question);
	}

	@Override
	public Result<PageInfo<List<Question>>> getList(Question question, int pageNum, int pageSize) {

		Query query = new Query();

		if (question.getTitle() != null && !"".equals(question.getTitle())) {
			Pattern pattern = Pattern.compile("^.*" + question.getTitle() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(new Criteria().orOperator(Criteria.where("title").regex(pattern),
					Criteria.where("questionSource").regex(pattern)));
		}
		// 时间区间
		if (question.getStartTime() != null) {
			Criteria timeParam = Criteria.where("createTime")
					.gte(LocalDateTimeUtil.toDate(question.getStartTime(), ZoneOffset.ofHours(8)));
			if (question.getEndTime() != null) {
				timeParam.lt(LocalDateTimeUtil.toDate(question.getEndTime(), ZoneOffset.ofHours(8)));
			}
			query.addCriteria(timeParam);
		}

		query.with(new Sort(Direction.DESC, "createTime")); // 排序
		if (question.getTermilType() != 0) {
			query.addCriteria(Criteria.where("termilType").is(question.getTermilType()));
		}

		int rows = (int) mongoUtil.count(query, Question.class);
		query.skip((pageNum - 1) * pageSize).limit(pageSize);
		List<Question> list = mongoUtil.find(query, Question.class);

		PageInfo<List<Question>> pageInfo = new PageInfo<List<Question>>(list, rows, pageNum, pageSize);
		return Results.success(pageInfo);

	}

}
