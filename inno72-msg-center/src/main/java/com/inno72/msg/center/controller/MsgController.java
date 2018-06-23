package com.inno72.msg.center.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.core.dto.MsgDTO;
import com.inno72.msg.center.model.MsgModel;
import com.inno72.msg.center.service.MsgFacedeService;
import com.inno72.msg.center.service.MsgModelService;

@RestController
@RequestMapping(value = "/msg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin
public class MsgController {

	@Autowired
	MsgModelService msgModelService;

	@Autowired
	MsgFacedeService msgFacedeService;

	/**
	 * 查询列表
	 * 
	 * @param msgModel
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Result<Page<MsgModel>> query(MsgModel msgModel,
			@PageableDefault(page = 0, size = 10, sort = "sentTime", direction = Direction.DESC) Pageable pageable) {
		return msgModelService.getList(msgModel, pageable);
	}

	/**
	 * 保存并发送消息
	 * 
	 * @param msgModel
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Result<Boolean> save(@RequestBody MsgDTO msgDTO) {
		msgFacedeService.transmit(msgDTO);
		return Results.success(true);
	}

	/**
	 * 获取单条消息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Result<MsgModel> query(@PathVariable("id") String id) {
		return msgModelService.get(id);
	}

	@PostMapping("/resend")
	public Result<MsgModel> resend(@RequestBody String id) {
		return msgModelService.resend(id);
	}

}
