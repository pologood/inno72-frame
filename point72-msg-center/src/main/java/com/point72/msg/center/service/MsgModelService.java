package com.point72.msg.center.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.point72.common.Result;
import com.point72.core.dto.MsgDTO;
import com.point72.msg.center.model.MsgModel;

/**
 * 
 * @author sunlulu
 *
 */
public interface MsgModelService {

	/**
	 * 保存消息
	 * 
	 * @param msgModel
	 */
	void save(MsgModel msgModel);

	/**
	 * 发送微信文本消息
	 * 
	 * @param msgModel
	 */
	void sendWeChatContext(MsgModel msgModel, String token);

	/**
	 * 发送微信模板消息
	 * 
	 * @param msgModel
	 */
	void sendWeChatModel(MsgModel msgModel, String token);

	/**
	 * 接受MQ传来的微信消息，并发送微信文本或模板消息
	 * 
	 * @param mqModel
	 */
	void sendWeChat(MsgDTO mqModel);

	/**
	 * 发送钉钉文本消息
	 * 
	 * @param msgModel
	 */
	void sendDingDingContext(MsgModel msgModel);

	/**
	 * 发送钉钉链接消息
	 * 
	 * @param msgModel
	 */
	void sendDingdingLink(MsgModel msgModel);

	/**
	 * 接受MQ传来的钉钉消息
	 * 
	 * @param mqModel
	 */
	void sendDingDing(MsgDTO mqModel);

	/**
	 * 发送短信消息
	 * 
	 * @param msgModel
	 */
	void sendSms(MsgModel msgModel);

	/**
	 * 接受MQ传来的短信消息，并发送消息
	 * 
	 * @param mqModel
	 */
	void sendSms(MsgDTO mqModel);

	/**
	 * 发送推送消息
	 * 
	 * @param msgModel
	 */
	void sendPush(MsgModel msgModel);

	/**
	 * 接受MQ传来的推送消息，并发送推送消息
	 * 
	 * @param mqModel
	 */
	void sendPush(MsgDTO mqModel);

	/**
	 * 发送邮件消息
	 * 
	 * @param msgModel
	 */
	void sendMail(MsgModel msgModel);

	/**
	 * 接受MQ传来的邮件消息，并发送邮件消息
	 * 
	 * @param mqModel
	 */
	void sendMail(MsgDTO mqModel);

	/**
	 * 通过机器人发送消息
	 * 
	 * @param mqModel
	 * @author Houkm 2017年6月22日
	 */
	void sendDingDingRobot(MsgDTO mqModel);

	/**
	 * 通过微应用发送消息
	 * 
	 * @param mqModel
	 * @author Houkm 2017年6月22日
	 */
	void sendDingDingMiniApp(MsgDTO mqModel);

	/**
	 * 根据条件查询分页消息
	 * 
	 * @param msgModel
	 * @return
	 */
	Result<Page<MsgModel>> getList(MsgModel msgModel, Pageable pageable);

	/**
	 * 获取单个消息
	 * 
	 * @param id
	 * @return
	 */
	Result<MsgModel> get(String id);

	/**
	 * 根据已有消息重新发送
	 * 
	 * @param id
	 * @return
	 * @author Houkm 2017年6月20日
	 */
	Result<MsgModel> resend(String id);

}
