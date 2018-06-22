package com.point72.alarm.center.service;

import java.util.List;

import com.point72.alarm.center.model.Follow;
import com.point72.alarm.center.model.PageInfo;
import com.point72.common.Result;

/**
 * 报错Service
 * 
 * @author Houkm
 *
 *         2017年8月11日
 */
public interface FollowService {

	/**
	 * 新增报错
	 * 
	 * @param follow
	 * @author Houkm 2017年8月11日
	 */
	void save(Follow follow);

	/**
	 * 删除报错
	 * 
	 * @param id
	 * @author Houkm 2017年8月11日
	 */
	void remove(String id);

	/**
	 * 获取报错详情
	 * 
	 * @param code
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	Result<Follow> get(String code);

	/**
	 * 获取报错列表
	 * 
	 * @param follow
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	Result<PageInfo<List<Follow>>> getList(Follow follow, int pageNum, int pageSize);
}
