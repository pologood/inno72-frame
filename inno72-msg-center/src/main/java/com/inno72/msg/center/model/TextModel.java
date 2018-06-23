package com.inno72.msg.center.model;

import lombok.Data;

@Data
public class TextModel extends AbstractMsgModel {

	private String content;

	@Override
	public String text() {
		return content;
	}

}
