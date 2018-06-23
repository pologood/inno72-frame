package com.inno72.msg.center.model;

import com.inno72.wechat.msg.model.DefaultTemplateModel;

import lombok.Data;

/**
 * 微信模板消息模板
 * 
 * @author Houkm
 *
 *         2017年6月15日
 */
@Data
public class WechatTemplateMsgModel extends AbstractMsgModel {

	private String touser;
	private String template_id;
	private String url;
	private DataNode data;

	public DefaultTemplateModel transfer() {
		DefaultTemplateModel tpl = new DefaultTemplateModel();
		tpl.setTouser(getTouser());
		tpl.setTemplate_id(getTemplate_id());
		tpl.setUrl(getUrl());
		tpl.setData(data.transfer(tpl));
		return tpl;
	}

}
