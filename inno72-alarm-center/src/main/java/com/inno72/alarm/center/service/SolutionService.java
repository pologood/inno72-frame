package com.inno72.alarm.center.service;

import java.util.List;

import com.inno72.alarm.center.model.Solution;
import com.inno72.common.Result;

/**
 * 解决方案Service
 * 
 * @author Houkm
 *
 *         2017年8月11日
 */
public interface SolutionService {

	/**
	 * 新增解决方案
	 * 
	 * @param solution
	 * @author Houkm 2017年8月11日
	 */
	void save(Solution solution);

	/**
	 * 删除解决方案
	 * 
	 * @param id
	 * @author Houkm 2017年8月11日
	 */
	void remove(String id);

	/**
	 * 获取解决方案详情
	 * 
	 * @param code
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	Result<Solution> get(String code);

	/**
	 * 获取解决方案列表
	 * 
	 * @param solution
	 * @return
	 * @author Houkm 2017年8月11日
	 */
	Result<List<Solution>> getList(Solution solution);

}
