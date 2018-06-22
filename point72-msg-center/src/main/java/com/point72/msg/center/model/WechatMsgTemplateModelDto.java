package com.point72.msg.center.model;

import lombok.Data;

@Data
public class WechatMsgTemplateModelDto extends MsgTemplateModel {
	private WechatTemplateMsgModel data;
}
