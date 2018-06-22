package com.point72.msg.center.model;

import com.point72.ddtalk.chat.model.LinkMsgModel;

import lombok.Data;

@Data
public class LinkModel extends AbstractMsgModel {

	private String text;
	private String title;
	private String messageUrl;
	private String picUrl;
	private String chatid;

	public LinkMsgModel transfer() {
		LinkMsgModel model = new LinkMsgModel(this.getTitle(), this.getText(), this.getPicUrl(), this.getMessageUrl(),
				this.getChatid());
		return model;
	}

}
