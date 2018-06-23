package com.inno72.alarm.center.service;

import java.util.List;

import com.inno72.alarm.center.model.LogException;
import com.inno72.alarm.center.model.LogSystemException;
import com.inno72.alarm.center.model.PageInfo;
import com.inno72.common.Result;
import com.inno72.core.dto.LogExceptionDTO;

/**
 * 异常记录
 * 
 * @author Houkm
 *
 *         2017年7月10日
 */
public interface LogExceptionService {

	/**
	 * 获取系统异常列表
	 * 
	 * @param ex
	 * @param pageable
	 * @return
	 * @author Houkm 2017年7月12日
	 */
	public Result<PageInfo<List<LogSystemException>>> getSystemExceptionList(LogSystemException ex, int pageNum,
			int pageSize);

	/**
	 * 获取系统异常
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月11日
	 */
	public Result<LogSystemException> getSystemException(String id);

	/**
	 * 获取业务异常列表
	 * 
	 * @param keyword
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author Houkm 2017年7月11日
	 */
	public Result<PageInfo<List<LogException>>> getBusinessExceptionList(LogException ex, int pageNum, int pageSize);

	/**
	 * 获取业务异常
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年7月11日
	 */
	public Result<LogException> getBusinessException(String id);

	/**
	 * 记录异常
	 * 
	 * @param dto
	 * @author Houkm 2017年7月10日
	 */
	public void log(LogExceptionDTO dto);

}
