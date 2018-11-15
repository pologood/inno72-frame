package com.inno72.msg.center.service.impl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.gexin.rp.sdk.template.NotificationTemplate;
import com.inno72.msg.center.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.config.client.ExceptionProperties;
import com.inno72.core.aop.WithOutAfterThrowingCut;
import com.inno72.core.dto.MsgDTO;
import com.inno72.ddtalk.chat.CorpChatHandler;
import com.inno72.ddtalk.chat.GroupChatHandler;
import com.inno72.exception.ExceptionBuilder;
import com.inno72.mongo.MongoUtil;
import com.inno72.msg.center.model.LinkModel;
import com.inno72.msg.center.model.MsgModel;
import com.inno72.msg.center.model.MsgTemplateModel;
import com.inno72.msg.center.model.PushModel;
import com.inno72.msg.center.model.QyWechatMsgModel;
import com.inno72.msg.center.model.TextModel;
import com.inno72.msg.center.model.WechatTemplateMsgModel;
import com.inno72.msg.center.service.MsgModelService;
import com.inno72.msg.center.service.MsgTemplateModelService;
import com.inno72.msg.center.util.EmailSendHandler;
import com.inno72.msg.center.util.GpushSendHandler;
import com.inno72.msg.center.util.SmsSendHandler;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.wechat.common.ResultHandler;
import com.inno72.wechat.msg.MsgSender;
import com.inno72.wechat.msg.custom.TextMsgModel;
import com.inno72.wechat.msg.custom.TextMsgModel.Text;

@Component
@WithOutAfterThrowingCut
public class MsgModelServiceImpl implements MsgModelService {

	private Logger logger = LoggerFactory.getLogger(MsgModelServiceImpl.class);

	@Autowired
	MsgTemplateModelService msgTemplateModelService;

	@Autowired
	MongoUtil mongoUtil;
	@Resource
	private IRedisUtil redisUtil;
	@Resource

	@Autowired
	ExceptionProperties exceptionProp;

	@Autowired
	private SmsSendHandler smsSendHandler;

	@Autowired
	private EmailSendHandler emailSendHandler;

	@Autowired
	private GpushSendHandler gpushSendHandler;

	private static final String EXTERNAL_TOKEN_KEY = "WECHAT_TOKEN";
	private static final String EXTERNAL_WECHAT_TEMPLATE_MSG_ID = "EXTERNAL_WECHAT_TEMPLATE_MSG_ID";

	private static final String QY_WECHAT_SENDURL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={0}";

	private static final String QY_WECHAT_ACCESSTOKEN = "public:qyCheckAgentAccToken";

	@Override
	public void save(MsgModel msgModel) {
		if (msgModel.getSentTime() == null) {
			msgModel.setSentTime(LocalDateTime.now());
		}
		if (msgModel.getModel() == null) {
			msgModel.setModel(msgTemplateModelService.get(msgModel.getCode()).getData()); // 设置model
		}
		msgModel.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		logger.info("保存消息：{}", msgModel);
		mongoUtil.insert(msgModel);
	}

	@Override
	public void sendWeChatContext(MsgModel msgModel, String token) {
		// 微信token
		// String token = getWechatToken();

		TextModel textModel = (TextModel) msgModel.getContent();

		// 微信消息对象
		TextMsgModel model = new TextMsgModel();
		Text text = model.new Text();
		text.setContent(textModel.getContent());
		model.setText(text);
		model.setTouser(msgModel.getReceiver());

		// 请求微信接口
		String result = MsgSender.sendCustom(token, model);
		// 处理请求结果
		processWechatResut(result, msgModel);
	}

	@Override
	public void sendWeChatModel(MsgModel msgModel, String token) {
		// 微信token
		// String token = getWechatToken();

		// 获取微信模板信息
		WechatTemplateMsgModel wechatTemplateMsgModel = (WechatTemplateMsgModel) msgModel.getContent();

		// 发送微信请求
		String result = MsgSender.sendTpl(token, wechatTemplateMsgModel.transfer());

		// 处理请求结果
		processWechatResut(result, msgModel);
	}

	@Override
	public void sendWeChat(MsgDTO mqModel) {
		MsgModel msgModel = this.init(mqModel); // 初始化消息
		// 设置content内容
		MsgTemplateModel msgTemplateModel = msgModel.getModel();

		// 微信token
		String token = mqModel.getParams().get(EXTERNAL_TOKEN_KEY);
		token = token == null || "".equals(token) ? getWechatToken() : token;

		if (msgTemplateModel.getMessageChildType() == MessageChildType.TEXT.v()) { // 微信文本
			logger.info("微信文本消息");
			// 替换文本内容
			TextModel textModel = (TextModel) msgTemplateModel.getContent();
			logger.info("模板替换参数map：{}", mqModel.getParams());
			mqModel.getParams().forEach((k, v) -> {
				if (v != null) {
					logger.info("参数{}设置值为{}", k, v);
					textModel.setContent(textModel.getContent().replaceAll("\\{\\{" + k + "\\}\\}", v));
				} else {
					logger.error("参数{}的值为null，忽略替换", k);
				}
			});
			msgModel.setContent(textModel);
			if (checkSendTime(msgModel)) {
				this.sendWeChatContext(msgModel, token);
			}
		} else { // 微信模板
			logger.info("微信模板消息");

			Map<String, String> paramMap = mqModel.getParams();

			// 外部公众号的模板消息id
			String external_wechat_template_msg_id = paramMap.get(EXTERNAL_WECHAT_TEMPLATE_MSG_ID);
			// paramMap.remove(EXTERNAL_WECHAT_TEMPLATE_MSG_ID);
			WechatTemplateMsgModel wechatTemplateMsgModel = (WechatTemplateMsgModel) msgTemplateModel.getContent();
			if (external_wechat_template_msg_id != null && !"".equals(external_wechat_template_msg_id)) {
				logger.info("外部公众号消息模板ID: {}", external_wechat_template_msg_id);
				wechatTemplateMsgModel.setTemplate_id(external_wechat_template_msg_id);
			}
			logger.info("模板消息ID: {}", wechatTemplateMsgModel.getTemplate_id());
			wechatTemplateMsgModel.setTouser(msgModel.getReceiver());
			String url = paramMap.get("url");
			if (url != null && !"".equals(url)) {
				logger.info("模板点击url: {}", url);
				wechatTemplateMsgModel.setUrl(paramMap.get("url"));
			}

			paramMap.remove("url");
			paramMap.remove("template_id");

			// 设置data体
			com.inno72.msg.center.model.DataNode dataNode = wechatTemplateMsgModel.getData();
			com.inno72.msg.center.model.Node first = dataNode.getFirst();
			first.setValue(paramMap.get("first"));
			logger.info("模板标题: {}", first.getValue());
			dataNode.setFirst(first);
			paramMap.remove("first");

			com.inno72.msg.center.model.Node remark = dataNode.getRemark();
			remark.setValue(paramMap.get("remark"));
			logger.info("模板备注: {}", remark.getValue());
			dataNode.setRemark(remark);
			paramMap.remove("remark");

			paramMap.forEach((k, v) -> {
				com.inno72.msg.center.model.Node node = dataNode.getData().get(k);
				if (node != null) {
					node.setValue(v);
				}
			});

			msgModel.setContent(wechatTemplateMsgModel);
			if (checkSendTime(msgModel)) {
				this.sendWeChatModel(msgModel, token);
			}
		}

	}

	private MsgModel init(MsgDTO mqModel) {
		MsgModel msgModel = new MsgModel();
		MsgTemplateModel msgTemplateModel = msgTemplateModelService.get(mqModel.getCode()).getData(); // 获取模板
		msgModel.setModel(msgTemplateModel); // 设置模板
		msgModel.setMessageType(msgTemplateModel.getMessageType()); // 设置消息类型
		msgModel.setTitle(mqModel.getTitle()); // 设置标题
		msgModel.setSentBy(mqModel.getSentBy()); // 设置创建人
		msgModel.setReceiver(mqModel.getReceiver()); // 设置接收人
		logger.info("初始化消息: {}", JSON.toJSONString(msgModel));
		return msgModel;
	}

	@Override
	public void sendQyWechatMsg(MsgDTO mqModel) {
		logger.info("企业微信消息");

		MsgModel msgModel = this.init(mqModel); // 初始化消息
		// 设置content内容
		MsgTemplateModel msgTemplateModel = msgModel.getModel();

		Map<String, String> paramMap = mqModel.getParams();

		QyWechatMsgModel qyMsgModel = (QyWechatMsgModel) msgTemplateModel.getContent();
		TextModel textModel = qyMsgModel.getText();

		logger.info("模板替换参数map：{}", mqModel.getParams());
		paramMap.forEach((k, v) -> {
			if (v != null) {
				logger.info("参数{}设置值为{}", k, v);
				textModel.setContent(textModel.getContent().replaceAll("\\{\\{" + k + "\\}\\}", v));
			} else {
				logger.error("参数{}的值为null，忽略替换", k);
			}
		});

		Map<String, String> addedParam = mqModel.getAddedParams();
		logger.info("模板替换参数map：{}", addedParam);
		String touser = addedParam.get("touser");
		String toparty = addedParam.get("toparty");
		String totag = addedParam.get("totag");
		String agentid = addedParam.get("agentid");
		if (StringUtil.notEmpty(touser))
			qyMsgModel.setTouser(touser);
		if (StringUtil.notEmpty(toparty))
			qyMsgModel.setTouser(toparty);
		if (StringUtil.notEmpty(totag))
			qyMsgModel.setTotag(totag);
		if (StringUtil.notEmpty(agentid))
			qyMsgModel.setAgentid(Integer.parseInt(agentid));

		qyMsgModel.setMsgtype("text");
		qyMsgModel.setText(textModel);

		// 请求微信接口
		String accessToken = redisUtil.get(QY_WECHAT_ACCESSTOKEN);
		String url = MessageFormat.format(QY_WECHAT_SENDURL, accessToken);
		String result = HttpClient.post(url, JSON.toJSONString(qyMsgModel));

		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getInteger("errcode") == 0) {
			logger.info("企业消息发送成功");
		} else {
			logger.info(resultJson.getString("errmsg"));
		}

	}

	@Override
	@WithOutAfterThrowingCut
	public void sendDingDingContext(MsgModel msgModel) {
		logger.info("钉钉文本消息");
		TextModel textModel = (TextModel) msgModel.getContent();
		String chatid = null;
		if (msgModel.getReceiver() != null && !msgModel.getReceiver().equals("")) {
			chatid = msgModel.getReceiver();
		} else {
			chatid = msgModel.getModel().getReceiver();
		}
		logger.info("chatid: {}", chatid);
		String content = textModel.getContent();
		logger.info("消息内容: {}", content);
		String result = null;
		com.inno72.ddtalk.chat.model.TextMsgModel model = new com.inno72.ddtalk.chat.model.TextMsgModel(content,
				chatid);
		if (msgModel.isRobot()) {
			String token = chatid;
			model.setChatid(null);
			result = GroupChatHandler.sendRabotMsg(token, model);
		} else if (msgModel.isMiniApp()) {
			result = CorpChatHandler
					.asyncsendText(msgModel.getModel().getReceiver(), this.getDDToken(), msgModel.getUserIds(),
							content);
		} else {
			String token = getDDToken();
			result = GroupChatHandler.send(token, model);
		}
		processDDTalkResut(result, msgModel);
	}

	@Override
	@WithOutAfterThrowingCut
	public void sendDingdingLink(MsgModel msgModel) {
		logger.info("钉钉链接消息");
		LinkModel linkModel = (LinkModel) msgModel.getContent();
		String result = null;
		com.inno72.ddtalk.chat.model.LinkMsgModel ddLinkModel = linkModel.transfer();
		if (msgModel.isRobot()) {
			String token = linkModel.getChatid();
			ddLinkModel.setChatid(null);
			result = GroupChatHandler.sendRabotMsg(token, ddLinkModel);
		} else if (msgModel.isMiniApp()) {
			linkModel.transfer().getLink();
			String anentId = msgModel.getAgentId();
			result = CorpChatHandler.asyncsendLink(anentId, this.getDDToken(), msgModel.getUserIds(), ddLinkModel);
		} else {
			String token = getDDToken();
			ddLinkModel.setChatid(msgModel.getModel().getReceiver());
			result = GroupChatHandler.send(token, ddLinkModel);
		}
		processDDTalkResut(result, msgModel);
	}

	@Override
	@WithOutAfterThrowingCut
	public void sendDingDing(MsgDTO mqModel) {
		MsgModel msgModel = this.init(mqModel); // 初始化消息
		// 设置content内容
		logger.info("模板替换参数map：{}", mqModel.getParams());
		generateDDMsgModel(msgModel, mqModel.getParams(), mqModel.getTitle());
	}

	@Override
	@WithOutAfterThrowingCut
	public void sendDingDingRobot(MsgDTO mqModel) {
		MsgModel msgModel = this.init(mqModel); // 初始化消息
		msgModel.setRobot(true);
		logger.info("模板替换参数map：{}", mqModel.getParams());
		generateDDMsgModel(msgModel, mqModel.getParams(), mqModel.getTitle());
	}

	@Override
	@WithOutAfterThrowingCut
	public void sendDingDingMiniApp(MsgDTO mqModel) {
		MsgModel msgModel = this.init(mqModel); // 初始化消息
		msgModel.setMiniApp(true);
		Map<String, String> params = mqModel.getParams();
		logger.info("模板替换参数map：{}", params);

		String userIdsStr = params.get("userIds");
		List<String> userIds = new ArrayList<>();
		Arrays.asList(userIdsStr.split(",")).forEach(id -> {
			userIds.add(id);
		});
		msgModel.setUserIds(userIds);
		params.remove("userIds");
		String agentId = params.get("agentId") == null ? msgModel.getModel().getReceiver() : params.get("agentId");
		msgModel.setAgentId(agentId);
		logger.info("微应用[{}]消息接收人[{}]", agentId, userIds);
		generateDDMsgModel(msgModel, params, mqModel.getTitle());
	}

	@Override
	public void sendSms(MsgModel msgModel) {
		// 获取短信信息
		TextModel textModel = (TextModel) msgModel.getContent();
		String message = textModel.getContent();

		String result = null;
		boolean status = false;
		// 筑望短信
		if (msgModel.getModel().getMessageChildType() == MessageChildType.ZHUWANG.v()) {
			logger.info("筑望短信: {}", message);
			result = smsSendHandler.sendZhuwang(msgModel.getReceiver(), message);
			logger.info("筑望短信结果: {}", result);
			try {
				Document document = DocumentHelper.parseText(result);
				Element root = document.getRootElement();
				status = root.elementText("returnstatus").trim().toLowerCase().equals("success");
				msgModel.setStatus(status ? StateType.SUCCESS.getV() : StateType.FAILURE.getV());
				msgModel.setStatusMessage(root.elementText("message").trim());
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		} else {// 云片短信
			logger.info("云片短信: {}", message);
			// 发送短信信息
			result = smsSendHandler.sendYunpian(msgModel.getReceiver(), message);
			logger.info("云片短信结果: {}", result);
			JSONObject jsonObj = JSON.parseObject(result);
			// 根据发送短信的结果设置消息状态和消息文本
			status = jsonObj.getInteger("code") == 0;
			msgModel.setStatus(status ? StateType.SUCCESS.getV() : StateType.FAILURE.getV());
			msgModel.setStatusMessage(jsonObj.getString("msg"));
		}
		msgModel.setResult(result);
		this.save(msgModel);
		if (!status) {
			throw ExceptionBuilder.build(exceptionProp)
					.format("send_msg_failed", msgModel.getId(), msgModel.getStatusMessage()).create();
		}
	}

	@Override
	public void sendSms(MsgDTO mqModel) {
		MsgModel msgModel = this.init(mqModel); // 初始化消息
		// 设置content内容
		MsgTemplateModel msgTemplateModel = msgModel.getModel();
		TextModel textModel = (TextModel) msgTemplateModel.getContent();
		logger.info("模板替换参数map：{}", mqModel.getParams());
		mqModel.getParams().forEach((k, v) -> {
			if (v != null) {
				logger.info("参数{}设置值为{}", k, v);
				textModel.setContent(textModel.getContent().replaceAll("\\{\\{" + k + "\\}\\}", v));
			} else {
				logger.error("参数{}值为null,忽略替换", k);
			}
		});
		msgModel.setContent(textModel);
		if (checkSendTime(msgModel)) {
			this.sendSms(msgModel);
		}
	}

	@Override
	public void sendPush(MsgModel msgModel) {
		// 获取推送文本信息
		PushModel pushModel = (PushModel) msgModel.getContent();
		String content = pushModel.getContent();
		logger.info("推送消息:pushModel->{}", JSON.toJSONString(pushModel));
		Map<String, Object> result = null;
		if (pushModel.getTemplateType() == TransmissionTemplateType.TRANSMISSION_MSG.v()) {
			logger.info("透传消息");
			TransmissionTemplate template = new TransmissionTemplate();
			template.setTransmissionType(pushModel.getTransmissionType());
			logger.info("收到消息是否打开应用:{}", pushModel.getTransmissionType() == 1 ? "启动" : "不启动");
			template.setTransmissionContent(content);
			logger.info("透传消息内容: {}", content);

			if (StringUtil.isNotEmpty(pushModel.getTags())) { // 判断参数里带tags
				result = gpushSendHandler.tag(template, pushModel.getOsType(), pushModel.getAppType(),
						Arrays.asList(pushModel.getTags()));
			} else {

				result = gpushSendHandler
						.single(msgModel.getReceiver(), template, pushModel.getOsType(), pushModel.getAppType());
			}

		} else if (pushModel.getTemplateType() == TransmissionTemplateType.NOTIFICATION_OPEN_APPLICATION.v()) {
			logger.info("通知打开应用消息");
			if (pushModel.getOsType() == OsType.IOS.v() || pushModel.getOsType() == OsType.PRO.v()) {
				TransmissionTemplate template = generateIOSNotifyTemplate(pushModel.getText(), content,
						pushModel.getTransmissionType(), pushModel.getSound());
				result = gpushSendHandler.single(msgModel.getReceiver(), template, pushModel.getOsType(), pushModel.getAppType());
			} else {
				NotificationTemplate template = new NotificationTemplate();
				template.setTransmissionType(pushModel.getTransmissionType());
				logger.info("收到消息是否打开应用:{}", pushModel.getTransmissionType() == 1 ? "启动" : "不启动");
				template.setTransmissionContent(content);

				logger.info("通知消息透传内容: {}", content);
				logger.info("通知消息标题: {}", pushModel.getTitle());
				logger.info("通知消息内容: {}", pushModel.getText());
				template.setTitle(pushModel.getTitle());
				template.setText(pushModel.getText());
				result = gpushSendHandler.single(msgModel.getReceiver(), template, pushModel.getOsType(), pushModel.getAppType());
			}
		}

		boolean status = result.get("result").toString().toLowerCase().equals("ok");
		// 根据发送的结果设置消息状态和消息文本
		msgModel.setStatus(status ? StateType.SUCCESS.getV() : StateType.FAILURE.getV());
		msgModel.setStatusMessage(result.get("result").toString());
		msgModel.setResult(result.toString());
		this.save(msgModel);
		if (!status) {
			throw ExceptionBuilder.build(exceptionProp)
					.format("send_msg_failed", msgModel.getId(), msgModel.getStatusMessage()).create();
		}

	}

	@Override
	public void sendPush(MsgDTO mqModel) {
		MsgModel msgModel = this.init(mqModel); // 初始化消息
		// 设置content内容
		MsgTemplateModel msgTemplateModel = msgModel.getModel();
		PushModel pushModel = (PushModel) msgTemplateModel.getContent();
		Map<String, String> map = mqModel.getParams();
		logger.info("模板替换参数map：{}", map);

		pushModel.setTitle(map.get("title"));
		pushModel.setText(map.get("text"));
		pushModel.setTags(map.get("tags"));

		map.remove("title");
		map.remove("text");
		map.forEach((k, v) -> {
			if (v != null) {
				logger.info("参数{}设置值为{}", k, v);
				pushModel.setContent(pushModel.getContent().replaceAll("\\{\\{" + k + "\\}\\}", v));
			} else {
				logger.info("参数{}值为null，忽略替换", k);
			}
		});
		msgModel.setContent(pushModel);
		if (checkSendTime(msgModel)) {
			this.sendPush(msgModel);
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void sendMail(MsgModel msgModel) {
		// 获取推送文本信息
		TextModel textModel = (TextModel) msgModel.getContent();
		String message = textModel.getContent();
		// 发送邮件
		logger.info("发送邮件: {}", message);
		String result = emailSendHandler.sendSimpleMail(msgModel.getReceiver(), msgModel.getTitle(), message);
		logger.info("邮件发送结果: {}", "success".equals(result) ? "成功" : "失败");
		// 根据发送邮件的结果设置状态
		msgModel.setStatus("success".equals(result) ? StateType.SUCCESS.getV() : StateType.FAILURE.getV());
		msgModel.setStatusMessage("success".equals(result) ? "成功" : "失败");
		msgModel.setResult(result);

		this.save(msgModel);
		if (!"success".equals(result)) {
			throw ExceptionBuilder.build(exceptionProp).format("send_email_failed", msgModel.getId(), "邮件发送失败")
					.create();
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void sendMail(MsgDTO mqModel) {
		MsgModel msgModel = this.init(mqModel); // 初始化消息
		// 设置content内容
		MsgTemplateModel msgTemplateModel = msgModel.getModel();
		TextModel textModel = (TextModel) msgTemplateModel.getContent();
		logger.info("模板替换参数map：{}", mqModel.getParams());
		mqModel.getParams().forEach((k, v) -> {
			if (v != null) {
				logger.info("参数{}设置值为{}", k, v);
				textModel.setContent(textModel.getContent().replaceAll("\\{\\{" + k + "\\}\\}", v));
			} else {
				logger.info("参数{}值为null，忽略替换", k);
			}
		});
		msgModel.setContent(textModel);
		if (checkSendTime(msgModel)) {
			this.sendMail(msgModel);
		}
	}

	@Override
	public Result<Page<MsgModel>> getList(MsgModel msgModel, Pageable pageable) {

		Query query = new Query();
		if (msgModel.getKey() != null && !"".equals(msgModel.getKey())) {
			Pattern pattern = Pattern.compile("^.*" + msgModel.getKey() + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(new Criteria()
					.orOperator(Criteria.where("receiver").regex(pattern), Criteria.where("sentBy").regex(pattern),
							Criteria.where("code").regex(pattern)));
		}
		if (msgModel.getStartTime() != null) {
			Criteria timeParam = Criteria.where("sentTime")
					.gte(LocalDateTimeUtil.toDate(msgModel.getStartTime(), ZoneOffset.ofHours(8)));
			if (msgModel.getEndTime() != null) {
				timeParam.lt(LocalDateTimeUtil.toDate(msgModel.getEndTime(), ZoneOffset.ofHours(8)));
			}
			query.addCriteria(timeParam);
		}
		if (msgModel.getStatus() != null && !msgModel.getStatus().equals("")) {
			query.addCriteria(Criteria.where("status").is(msgModel.getStatus()));
		}
		int rows = (int) mongoUtil.count(query, MsgModel.class);
		query.with(new Sort(Direction.DESC, "sentTime"));
		query.with(pageable);
		List<MsgModel> list = mongoUtil.find(query, MsgModel.class);
		Page<MsgModel> page = new PageImpl<>(list, pageable, rows);
		return Results.success(page);
	}

	@Override
	public Result<MsgModel> get(String id) {
		MsgModel msgModel = mongoUtil.findById(id, MsgModel.class);
		return Results.success(msgModel);
	}

	@Override
	public Result<MsgModel> resend(String id) {
		MsgModel msgModel = this.get(id).getData();
		if (msgModel != null) {
			if (checkSendTime(msgModel)) {
				msgModel.setSentTime(LocalDateTime.now());
				int messageType = msgModel.getMessageType();
				int messageChildType = msgModel.getModel().getMessageChildType();
				if (messageType == MessageType.WECHAT.v()) {
					if (messageChildType == MessageChildType.TEXT.v()) {
						sendWeChatContext(msgModel, getWechatToken());
					} else if (messageChildType == MessageChildType.TEMPLATE.v()) {
						sendWeChatModel(msgModel, getWechatToken());
					}
				} else if (messageType == MessageType.DINGDING.v() || messageType == MessageType.MINIAPP.v()
						|| messageType == MessageType.ROBOT.v()) {
					if (messageType == MessageType.MINIAPP.v()) {
						msgModel.setMiniApp(true);
					}

					if (messageType == MessageType.ROBOT.v()) {
						msgModel.setRobot(true);
					}

					if (messageChildType == MessageChildType.TEXT.v()) {
						sendDingDingContext(msgModel);
					} else if (messageChildType == MessageChildType.LINK.v()) {
						sendDingdingLink(msgModel);
					}
				} else if (messageType == MessageType.SMS.v()) {
					sendSms(msgModel);
				} else if (messageType == MessageType.PUSH.v()) {
					sendPush(msgModel);
				} else if (messageType == MessageType.MAIL.v()) {
					sendMail(msgModel);
				}
			}
		}
		return Results.success();
	}

	/**
	 * 获取微信token
	 *
	 * @return
	 * @author Houkm 2017年6月16日
	 */
	private String getWechatToken() {
		// // 微信token
		// Object pretoken = memcachedClient.get(memKeys.getWxtoken());
		// if (pretoken == null) {
		// throw
		// ExceptionBuilder.build(exceptionProp).format("systemError").create();
		// }
		// String token = pretoken.toString();
		// // String token =
		// //
		// "R258bliNwcHYUqlvj5217-kpKlmizAQAdtwQ9kpwYsCqvvd7oTQbf2QSPcHT52h7vVFoYdobALPVDpOGBX3nV2DoJhLgPnmA2oyBDzVjfwLSEt1_yc7I0HtJWtka59i3TJFhACACSO";
		// todo gxg 待修改
		return "";
	}

	/**
	 * 获取钉钉token
	 *
	 * @return
	 * @author Houkm 2017年6月16日
	 */
	private String getDDToken() {
		// // 微信token
		// Object pretoken = memcachedClient.get(memKeys.getDdtoken());
		//
		// String token = pretoken == null ? "0c862edd6cab3467b9a3ecc623594950"
		// : pretoken.toString();
		// todo gxg 待修改
		String url = "https://oapi.dingtalk.com/gettoken?corpid=dingd04d2d6ca18d0fd535c2f4657eb6378f&corpsecret=2ralypy62nV4kL8DOMjWWEoJyQkFnjNhlin3PzdkIMs1LQ7jj8huTsqibi7UdaKD";
		String result = HttpClient.get(url);
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getInteger("errcode") == 0) {
			return resultJson.getString("access_token");
		}
		return "";
	}

	/**
	 * 处理微信接口返回结果
	 *
	 * @param result
	 * @param msgModel
	 * @author Houkm 2017年6月16日
	 */
	private void processWechatResut(String result, MsgModel msgModel) {
		// 解析微信接口返回数据
		ResultHandler handler = ResultHandler.create(result);
		// 根据发送微信的结果设置消息状态和消息文本
		msgModel.setStatus(handler.isSuccess() ? StateType.SUCCESS.getV() : StateType.FAILURE.getV());
		msgModel.setStatusMessage(handler.getErrmsg() == null ? "成功" : handler.getErrmsg());
		msgModel.setResult(result);
		this.save(msgModel);
		if (!handler.isSuccess()) {
			throw ExceptionBuilder.build(exceptionProp).format("send_msg_failed", msgModel.getId(), result).create();
		}
	}

	/**
	 * 处理钉钉接口返回结果
	 *
	 * @param result
	 * @param msgModel
	 * @author Houkm 2017年6月16日
	 */
	private void processDDTalkResut(String result, MsgModel msgModel) {
		if (msgModel.isMiniApp()) {
			processDDTalkOpenRestResut(result, msgModel);
		} else {
			com.inno72.ddtalk.ResultHandler handler = new com.inno72.ddtalk.ResultHandler(result);
			// 根据发送钉钉的结果设置消息状态和消息文本
			msgModel.setStatus(handler.isSuccess() ? StateType.SUCCESS.getV() : StateType.FAILURE.getV());
			msgModel.setStatusMessage(handler.getErrmsg());
			msgModel.setResult(result);
			this.save(msgModel);
			if (!handler.isSuccess()) {
				throw ExceptionBuilder.build(exceptionProp).format("send_msg_failed", msgModel.getId(), result)
						.create();
			}
		}
	}

	/**
	 * 处理钉钉开放接口返回结果
	 *
	 * @param result
	 * @param msgModel
	 * @author Houkm 2017年6月16日
	 */
	private void processDDTalkOpenRestResut(String result, MsgModel msgModel) {

		com.inno72.ddtalk.RestResultHandler handler = new com.inno72.ddtalk.RestResultHandler(result,
				CorpChatHandler.getAsyncsendMethod());
		String taskId = handler.getString("task_id");

		msgModel.setStatus(handler.isSuccess() ? StateType.SUCCESS.getV() : StateType.FAILURE.getV());
		String realResult = CorpChatHandler.getSendResult(msgModel.getAgentId(), taskId, getDDToken());
		handler = new com.inno72.ddtalk.RestResultHandler(realResult, CorpChatHandler.getSendResultMethod());
		// 根据发送钉钉的结果设置消息状态和消息文本
		msgModel.setStatusMessage(CorpChatHandler.getReadableSendResult(realResult));
		msgModel.setResult(result);

		msgModel.setReceiver(msgModel.getAgentId() + "&" + msgModel.getUserIds());
		this.save(msgModel);
		if (!handler.isSuccess()) {
			throw ExceptionBuilder.build(exceptionProp).format("send_msg_failed", msgModel.getId(), result).create();
		}
	}

	private boolean checkSendTime(MsgModel msgModel) {
		boolean send = msgModel.getModel().getSendTime().check();
		// todo gxg 需要恢复
		// if (!send) {
		// logger.info("不在发送时间，消息不发送");
		// msgModel.setStatus("忽略");
		// msgModel.setStatusMessage("不在发送时间");
		// this.save(msgModel);
		// }
		return true;
	}

	private void generateDDMsgModel(MsgModel msgModel, Map<String, String> params, String title) {
		// 设置content内容
		MsgTemplateModel msgTemplateModel = msgModel.getModel();
		if (msgTemplateModel.getMessageChildType() == MessageChildType.TEXT.v()) { // 钉钉文本
			// 替换文本内容
			TextModel textModel = (TextModel) msgTemplateModel.getContent();
			params.forEach((k, v) -> {
				if (v != null) {
					logger.info("参数{}设置值为{}", k, v);
					textModel.setContent(textModel.getContent().replaceAll("\\{\\{" + k + "\\}\\}", v));
				} else {
					logger.info("参数{}值为null，忽略替换", k);
				}
			});
			msgModel.setContent(textModel);
			if (checkSendTime(msgModel)) {
				this.sendDingDingContext(msgModel);
			}
		} else { // 钉钉LINK
			TextModel textModel = (TextModel) msgTemplateModel.getContent();
			LinkModel linkModel = new LinkModel();
			linkModel.setTitle(title);
			linkModel.setMessageUrl(params.get("messageUrl"));
			linkModel.setPicUrl(params.get("picUrl"));
			if (linkModel.getPicUrl() == null || linkModel.getPicUrl().equals("")) {
				linkModel.setPicUrl("http://erp.pinwheelmedical.com/images/logo.png");
			}
			params.remove("messageUrl");
			params.remove("picUrl");
			params.forEach((k, v) -> {
				if (v != null) {
					logger.info("参数{}设置值为{}", k, v);
					textModel.setContent(textModel.getContent().replaceAll("\\{\\{" + k + "\\}\\}", v));
				} else {
					logger.info("参数{}值为null，忽略替换", k);
				}
			});
			linkModel.setText(textModel.getContent());
			linkModel.setChatid(
					msgModel.getReceiver() == null ? msgModel.getModel().getReceiver() : msgModel.getReceiver());
			// 设置DataNode节点
			msgModel.setContent(linkModel);
			if (checkSendTime(msgModel)) {
				this.sendDingdingLink(msgModel);
			}
		}
	}

	private TransmissionTemplate generateIOSNotifyTemplate(String text, String content, int transmissionType,
			String sound) {
		logger.info("IOS通知消息");
		TransmissionTemplate template = new TransmissionTemplate();
		template.setTransmissionType(transmissionType);
		logger.info("收到消息是否打开应用:{}", transmissionType == 1 ? "启动" : "不启动");
		template.setTransmissionContent(content);
		logger.info("透传消息内容: {}", content);

		logger.info("铃声文件: {}", sound);

		APNPayload payload = new APNPayload();
		payload.setAutoBadge("+1");
		payload.addCustomMsg("payload", content);
		payload.setContentAvailable(1);
		payload.setSound(sound == null || sound.equals("") ? "default" : sound);
		payload.setAlertMsg(new APNPayload.SimpleAlertMsg(text));
		template.setAPNInfo(payload);
		return template;
	}

	/**
	 * 百度熊掌号token
	 *
	 * @return
	 * @author Houkm 2018年4月2日
	 */
	private String getXiongZhangToken() {
		// // 熊掌token
		// Object pretoken = memcachedClient.get("$XZ_TOKEN");
		// if (pretoken == null) {
		// throw
		// ExceptionBuilder.build(exceptionProp).format("systemError").create();
		// }
		// String token = pretoken.toString();
		// // String token =
		// //
		// "24.a86907df946abd8d0b96d5238a542ec5.7200.1523422065.282335-11013504";
		// todo gxg 待修改
		return "";
	}

}
