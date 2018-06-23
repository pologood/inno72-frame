package com.inno72.msg.center.model;

import java.util.HashMap;
import java.util.Map;

import com.inno72.wechat.msg.model.AbstractTemplateModel;

import lombok.Data;

@Data
public class DataNode {
	Node first;
	Node remark;
	Map<String, Node> data;

	protected Map<String, com.inno72.wechat.msg.model.AbstractTemplateModel.Node> transfer(AbstractTemplateModel model) {

		Map<String, com.inno72.wechat.msg.model.AbstractTemplateModel.Node> map = new HashMap<>();
		map.put("first", first.transfer(model.new Node()));
		map.put("remark", remark.transfer(model.new Node()));
		data.forEach((k, v) -> {
			map.put(k, v.transfer(model.new Node()));
		});
		return map;
	}

}