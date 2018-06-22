package com.point72.task.dispatch.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSON;
import com.point72.common.datetime.LocalDateTimeUtil;
import com.point72.config.client.YyxkConfigProperties;
import com.point72.redis.ListUtil;
import com.point72.task.dispatch.model.Task;
import com.point72.task.dispatch.model.TaskModel;
import com.point72.task.dispatch.service.TaskModelService;
import com.point72.task.dispatch.service.TaskService;

//@Component
public class TaskScanner {

	@Autowired
	private TaskModelService taskModelService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ListUtil listUtil;

	@Autowired
	private YyxkConfigProperties yyxkProp;

	@Scheduled(fixedRate = 1000 * 60, initialDelay = 2000)
	public void scan() {

		List<TaskModel> taskModels = taskModelService.getExecuteList().getData();
		List<Task> tasks = new ArrayList<>();
		//@formatter:off
		taskModels.stream()
			.filter
				(taskModel -> //筛选掉不在下一分钟内执行的任务，并设置下一次执行时间
				parseCron(taskModel).isBefore(LocalDateTime.now().plusMinutes(1)))
			.sorted(//根据下一次执行时间来排序
				new Comparator<TaskModel>() {
				@Override
				public int compare(TaskModel o1, TaskModel o2) {
					return o1.getNextTime().compareTo(o2.getNextTime());
				}
			})
			.forEach
				(taskModel -> {//创建任务并推送至队列
					Task task = taskService.create(taskModel).getData();
					tasks.add(task);
					listUtil.rightPush(yyxkProp.get("taskKey"), JSON.toJSONString(task));
				});
		//@formatter:on
	}

	private LocalDateTime parseCron(TaskModel taskModel) {

		CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
		try {
			cronTriggerImpl.setCronExpression(taskModel.getPolicy());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (dates != null && dates.size() > 0) {
			LocalDateTime analyzeTime = LocalDateTimeUtil.transfer(df.format(dates.get(0)));
			taskModel.setNextTime(analyzeTime);
			this.taskModelService.save(taskModel);
			return LocalDateTimeUtil.transfer(df.format(dates.get(0)));
		}
		return null;
	}

}
