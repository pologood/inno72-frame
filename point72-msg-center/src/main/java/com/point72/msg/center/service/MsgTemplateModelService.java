package com.point72.msg.center.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.point72.common.Result;
import com.point72.msg.center.model.MsgTemplateModel;

public interface MsgTemplateModelService {

	/**
	 * 保存和修改消息模板
	 * 
	 * @param msgTemplateModel
	 */
	void save(MsgTemplateModel msgTemplateModel);

	/**
	 * 删除消息模板
	 * 
	 * @param id
	 */
	void remove(String code);

	/**
	 * 根据消息模板code获取，消息模板
	 * 
	 * @param code
	 */
	Result<MsgTemplateModel> get(String code);

	/**
	 * 检查code是否存在
	 * 
	 * @param code
	 */
	Result<Boolean> checkExist(String code);

	/**
	 * 获取消息模板分页列表
	 * 
	 * @param pageNum
	 *            TODO
	 * @param pageSize
	 *            TODO
	 * @param msgModel
	 * @return
	 */
	Result<Page<MsgTemplateModel>> getList(MsgTemplateModel msgTemplateModel, Pageable pageable);
}
