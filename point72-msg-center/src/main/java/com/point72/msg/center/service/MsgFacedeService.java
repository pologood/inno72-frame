package com.point72.msg.center.service;

import com.point72.common.Result;
import com.point72.core.dto.MsgDTO;

/**
 * 消息转发接口
 * 
 * @author Houkm
 *
 *         2017年6月16日
 */
public interface MsgFacedeService {

	/**
	 * 转发
	 * 
	 * @param msg
	 * @return
	 * @author Houkm 2017年6月16日
	 */
	Result<Object> transmit(MsgDTO msg);

}
