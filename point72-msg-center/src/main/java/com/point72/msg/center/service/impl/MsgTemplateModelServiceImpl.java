package com.point72.msg.center.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.point72.common.Result;
import com.point72.common.Results;
import com.point72.common.datetime.LocalDateTimeUtil;
import com.point72.config.client.ExceptionProperties;
import com.point72.mongo.MongoUtil;
import com.point72.msg.center.StateType;
import com.point72.msg.center.model.MsgTemplateModel;
import com.point72.msg.center.service.MsgTemplateModelService;

@Component
public class MsgTemplateModelServiceImpl implements MsgTemplateModelService {

	@Autowired
	private MongoUtil mongoUtil;

	@Autowired
	private ExceptionProperties exceptionProp;

	@Override
	public void save(MsgTemplateModel msgTemplateModel) {
		String code = msgTemplateModel.getCode();
		msgTemplateModel.setId(code);
		if (this.checkExist(code).getData()) { // 存在，更新模板
			if (msgTemplateModel.getModifyTime() == null) {
				msgTemplateModel.setModifyTime(LocalDateTime.now()); // 设置更新时间
			}
			MsgTemplateModel old = this.get(code).getData();
			msgTemplateModel.setCreateBy(old.getCreateBy());
			msgTemplateModel.setCreateTime(old.getCreateTime());
			msgTemplateModel.setState(old.getState());

		} else { //
			if (msgTemplateModel.getCreateTime() == null) {
				msgTemplateModel.setCreateTime(LocalDateTime.now()); // 设置创建时间
			}
			msgTemplateModel.setState(StateType.NOMAL.getV()); // 设置状态
		}
		mongoUtil.save(msgTemplateModel);
	}

	@Override
	public void remove(String code) {
		MsgTemplateModel msgTemplateModel = this.get(code).getData();
		if (msgTemplateModel != null) {
			msgTemplateModel.setState(StateType.DEL.getV());
			mongoUtil.save(msgTemplateModel);
		}
	}

	@Override
	public Result<MsgTemplateModel> get(String code) {
		MsgTemplateModel msgTemplateModel = mongoUtil.findById(code, MsgTemplateModel.class);
		return Results.success(msgTemplateModel);
	}

	@Override
	public Result<Boolean> checkExist(String code) {
		MsgTemplateModel msgTemplateModel = this.get(code).getData();
		if (msgTemplateModel != null) {
			return Results.success(true);
		}
		return Results.success(false);
	}

	@Override
	public Result<Page<MsgTemplateModel>> getList(MsgTemplateModel msgTemplateModel, Pageable pageable) {

		Query query = new Query();
		if (msgTemplateModel.getKey() != null && !"".equals(msgTemplateModel.getKey())) {
			Pattern pattern = Pattern.compile("^.*" + msgTemplateModel.getKey() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(new Criteria().orOperator(Criteria.where("code").regex(pattern),
					Criteria.where("name").regex(pattern)));

		}
		if (msgTemplateModel.getStartTime() != null) {
			Criteria timeParam = Criteria.where("createTime")
					.gte(LocalDateTimeUtil.toDate(msgTemplateModel.getStartTime(), ZoneOffset.ofHours(8)));
			if (msgTemplateModel.getEndTime() != null) {
				timeParam.lt(LocalDateTimeUtil.toDate(msgTemplateModel.getEndTime(), ZoneOffset.ofHours(8)));
			}
			query.addCriteria(timeParam);
		}
		query.addCriteria(Criteria.where("state").is(StateType.NOMAL.getV()));
		query.with(new Sort(Direction.DESC, "createTime")); // 排序
		if (msgTemplateModel.getMessageType() != null && msgTemplateModel.getMessageType() != 0) {
			query.addCriteria(Criteria.where("messageType").is(msgTemplateModel.getMessageType()));
		}
		if (msgTemplateModel.getMessageChildType() != null && msgTemplateModel.getMessageChildType() != 0) {
			query.addCriteria(Criteria.where("messageChildType").is(msgTemplateModel.getMessageChildType()));
		}

		int rows = (int) mongoUtil.count(query, MsgTemplateModel.class);
		query.with(pageable);
		List<MsgTemplateModel> list = mongoUtil.find(query, MsgTemplateModel.class);
		Page<MsgTemplateModel> page = new PageImpl<>(list, pageable, rows);
		return Results.success(page);
	}

}
