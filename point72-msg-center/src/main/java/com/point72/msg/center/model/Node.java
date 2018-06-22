package com.point72.msg.center.model;

import lombok.Data;

@Data
public class Node {
	private String value = "";
	private String color = "#173177";

	protected com.point72.wechat.msg.model.AbstractTemplateModel.Node transfer(
			com.point72.wechat.msg.model.AbstractTemplateModel.Node node) {
		node.setColor(getColor());
		node.setValue(getValue());
		return node;
	}

}