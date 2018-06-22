package com.point72.alarm.center.service.impl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.point72.alarm.center.model.LogException;
import com.point72.alarm.center.model.LogSystemException;
import com.point72.alarm.center.model.PageInfo;
import com.point72.alarm.center.service.LogExceptionService;
import com.point72.annotation.ExceptionNotify;
import com.point72.common.Result;
import com.point72.common.Results;
import com.point72.common.datetime.LocalDateTimeUtil;
import com.point72.config.client.DomainProperties;
import com.point72.config.client.YyxkConfigProperties;
import com.point72.core.aop.WithOutAfterThrowingCut;
import com.point72.core.dto.LogExceptionDTO;
import com.point72.exception.SystemException;
import com.point72.mongo.MongoUtil;
import com.point72.msg.MsgUtil;

@Service
public class LogExceptionServiceImpl implements LogExceptionService {

	private Logger logger = LoggerFactory.getLogger(LogExceptionServiceImpl.class);

	@Autowired
	private MongoUtil mongo;

	@Autowired
	private DomainProperties domainProp;

	@Autowired
	private YyxkConfigProperties yyxkProp;

	@Autowired
	private MsgUtil msgUtil;

	@Override
	public Result<LogSystemException> getSystemException(String id) {
		LogSystemException ex = mongo.findById(id, LogSystemException.class);
		return Results.success(ex);
	}

	@Override
	public Result<LogException> getBusinessException(String id) {
		LogException ex = mongo.findById(id, LogException.class);
		return Results.success(ex);
	}

	@Override
	public Result<PageInfo<List<LogSystemException>>> getSystemExceptionList(LogSystemException ex, int pageNum,
			int pageSize) {
		Query query = generateQuery(ex.getKeyword(), ex.getStartTime(), ex.getEndTime());
		int rows = (int) mongo.count(query, LogSystemException.class);
		query.skip((pageNum - 1) * pageSize).limit(pageSize);
		List<LogSystemException> data = mongo.find(query, LogSystemException.class);
		PageInfo<List<LogSystemException>> pageInfo = new PageInfo<List<LogSystemException>>(data, rows, pageNum,
				pageSize);
		return Results.success(pageInfo);
	}

	@Override
	public Result<PageInfo<List<LogException>>> getBusinessExceptionList(LogException ex, int pageNum, int pageSize) {
		Query query = generateQuery(ex.getKeyword(), ex.getStartTime(), ex.getEndTime());
		int rows = (int) mongo.count(query, LogException.class);
		query.skip((pageNum - 1) * pageSize).limit(pageSize);
		List<LogException> data = mongo.find(query, LogException.class);
		PageInfo<List<LogException>> pageInfo = new PageInfo<List<LogException>>(data, rows, pageNum, pageSize);
		return Results.success(pageInfo);
	}

	@Override
	@WithOutAfterThrowingCut
	public void log(LogExceptionDTO dto) {

		ExceptionNotify en = dto.getExceptionNotify();
		String detailUri = null;
		Exception exception = dto.getEx();

		// dto转为model
		LogException logEx = new LogException(dto);
		logger.info("接收到{}的异常", logEx.toString());
		String message = logEx.getMessage();

		// 记录systemexception
		if (dto.getEx().getClass().equals(SystemException.class)) {
			logger.info("此异常为SystemException");
			LogSystemException logSysEx = new LogSystemException(logEx);
			SystemException sysEx = (SystemException) exception;
			logSysEx.setMsgId(sysEx.getMsgId());
			this.mongo.save(logSysEx);
			detailUri = MessageFormat.format(yyxkProp.get("sysExDetailUri"), logSysEx.getId());
		} else {// 记录exception
			this.mongo.save(logEx);
			detailUri = MessageFormat.format(yyxkProp.get("exDetailUri"), logEx.getId());
		}

		// 报警信息
		String messageUrl = domainProp.getAlarm() + detailUri;
		String picUrl = "";
		String title = exception.getClass().getSimpleName();
		Map<String, String> params = new HashMap<>();
		params.put("message", message);
		params.put("app", logEx.getApplicationName());
		String env = System.getenv("spring_profiles_active");
		params.put("env", env == null || env.equals("") ? "dev" : env);
		if (env != null && env.equals("prod")) {
			if (en != null) {// 报警
				params.put("owner", en.owner());
				if (en.notifyGroup() != null && !en.notifyGroup().equals("")) {
					// 通过指定群通知
					String groupId = en.notifyGroup();
					logger.info("报警到群:{}", groupId);
					title = en.title();
					msgUtil.sendDDLinkByGroup("exception_alarm_by_group", params, groupId, title, messageUrl, picUrl,
							"报警中心");
				}
				if (en.notifyUser().length > 0) {
					// 通过微应用通知指定用户
					String miniApp = yyxkProp.get("alarmMiniApp");
					logger.info("报警到微应用【{}】给开发者【{}】", miniApp, en.notifyUser());
					msgUtil.sendDDLinkByMiniApp("exception_alarm_by_miniapp", params, miniApp,
							Arrays.asList(en.notifyUser()), en.title(), messageUrl, picUrl, "报警中心");
				}
			}
			// 所有异常都将报警到开发群
			msgUtil.sendDDLinkByGroup("exception_alarm", params, title, messageUrl, picUrl, "报警中心");
		}
	}

	private Query generateQuery(String keyword, LocalDateTime startTime, LocalDateTime endTime) {
		Query query = new Query();
		if (keyword != null && !"".equals(keyword)) {
			Pattern pattern = Pattern.compile("^.*" + keyword + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(new Criteria().orOperator(Criteria.where("message").regex(pattern),
					Criteria.where("project").regex(pattern), Criteria.where("applicationName").regex(pattern),
					Criteria.where("instanceName").regex(pattern), Criteria.where("exceptionClass").regex(pattern),
					Criteria.where("msgId").regex(pattern)));
		}

		// 时间区间
		if (startTime != null) {
			Criteria timeParam = Criteria.where("caughtTime")
					.gte(LocalDateTimeUtil.toDate(startTime, ZoneOffset.ofHours(8)));
			if (endTime == null) {
				endTime = LocalDateTime.now();
			}
			timeParam.lt(LocalDateTimeUtil.toDate(endTime, ZoneOffset.ofHours(8)));
			query.addCriteria(timeParam);
		}
		query.with(new Sort(Direction.DESC, "caughtTime")); // 排序
		return query;
	}

}
