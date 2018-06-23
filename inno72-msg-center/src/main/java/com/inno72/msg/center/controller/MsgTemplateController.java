package com.inno72.msg.center.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.msg.center.MessageChildType;
import com.inno72.msg.center.MessageType;
import com.inno72.msg.center.model.AbstractMsgModel;
import com.inno72.msg.center.model.MsgTemplateModel;
import com.inno72.msg.center.model.PushModel;
import com.inno72.msg.center.model.TextModel;
import com.inno72.msg.center.model.WechatTemplateMsgModel;
import com.inno72.msg.center.service.MsgTemplateModelService;

@RestController
@RequestMapping(value = "/msgTemplate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin
public class MsgTemplateController {

	@Autowired
	MsgTemplateModelService msgTemplateModelService;

	/**
	 * 查询消息模板列表
	 * 
	 * @param msgTemplateModel
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Result<Page<MsgTemplateModel>> query(MsgTemplateModel msgTemplateModel,
			@PageableDefault(page = 0, size = 10, sort = "createTime", direction = Direction.DESC) Pageable pageable) {
		return msgTemplateModelService.getList(msgTemplateModel, pageable);
	}

	/**
	 * 保存消息模板
	 * 
	 * @param msgTemplateModel
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Result<Boolean> save(@RequestBody String json) {
		JSONObject jsonObj = JSON.parseObject(json);
		Integer messageType = jsonObj.getInteger("messageType");
		Integer messageChildType = jsonObj.getInteger("messageChildType");
		AbstractMsgModel content;
		// 微信模板消息
		if (MessageType.WECHAT.v() == messageType.intValue()
				&& MessageChildType.TEMPLATE.v() == messageChildType.intValue()) {
			content = jsonObj.getObject("content", WechatTemplateMsgModel.class);
		} else if (MessageType.PUSH.v() == messageType.intValue()) {// 推送
			content = jsonObj.getObject("content", PushModel.class);
		} else {
			content = jsonObj.getObject("content", TextModel.class);
		}
		jsonObj.remove("content");
		MsgTemplateModel msgTemplateModel = JSON.parseObject(jsonObj.toJSONString(), MsgTemplateModel.class);
		msgTemplateModel.setContent(content);
		msgTemplateModelService.save(msgTemplateModel);
		return Results.success(true);
	}

	/**
	 * 获取单条模板
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public Result<MsgTemplateModel> query(@PathVariable("code") String code) {
		return msgTemplateModelService.get(code);
	}

	/**
	 * 修改模板
	 * 
	 * @param msgTemplateModel
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public Result<Boolean> modify(@RequestBody MsgTemplateModel msgTemplateModel) {
		msgTemplateModelService.save(msgTemplateModel);
		return Results.success(true);
	}

	/**
	 * 删除模板
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.DELETE)
	public Result<Boolean> remove(@PathVariable("code") String code) {
		msgTemplateModelService.remove(code);
		return Results.success(true);
	}

	/**
	 * 查询消息模板列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public Result<Page<MsgTemplateModel>> history(String code,
			@PageableDefault(page = 0, size = 10, sort = "createTime", direction = Direction.DESC) Pageable pageable) {
		return null;
	}

}
