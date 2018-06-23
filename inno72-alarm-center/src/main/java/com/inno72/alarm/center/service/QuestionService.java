package com.inno72.alarm.center.service;

import java.util.List;

import com.inno72.alarm.center.model.PageInfo;
import com.inno72.alarm.center.model.Question;
import com.inno72.common.Result;

/**
 * 问题Service
 * 
 * @author Houkm
 *
 *         2017年8月11日
 */
public interface QuestionService {

	/**
	 * 新增问题
	 * 
	 * @param question
	 * @author Houkm 2017年8月11日
	 */
	void save(Question question);

	/**
	 * 删除问题
	 * 
	 * @param id
	 * @author Houkm 2017年8月11日
	 */
	void remove(String id);

	/**
	 * 获取问题详情
	 * 
	 * @param code
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	Result<Question> get(String code);

	/**
	 * 获取问题列表
	 * 
	 * @param question
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	Result<PageInfo<List<Question>>> getList(Question question, int pageNum, int pageSize);

}
