package com.point72.msg.center.model;

import lombok.Data;

@Data
public class PushTemplateModelDto extends MsgTemplateModel {
	private PushModel data;
}
