package com.inno72.msg.center.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.inno72.msg.center.OsType;
import com.inno72.msg.center.config.GetuiIOSProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;

import lombok.Data;

@Data

@Component
// @EnableConfigurationProperties({ GetuiAndroidProperties.class,
// GetuiIOSProperties.class })
public class GpushSendHandler {

	Logger logger = LoggerFactory.getLogger(GpushSendHandler.class);

	private IGtPush androidPushMachine;

	private IGtPush androidPushCheck;
	
	private IGtPush androidPushTmMachine;

	private IGtPush iosPush;
	// private IGtPush proPush;

	// @Autowired
	// GetuiAndroidProperties android;
	 @Autowired
	GetuiIOSProperties ios;
	// @Autowired
	// GetuiProProperties pro;

	private String host;

	@PostConstruct
	private void init() {
		androidPushMachine = new IGtPush(host, "q2P7jwmp9R97B1Misnf5y6", "5rOs0t4RQW7giCJY7uSPb9");
		androidPushCheck = new IGtPush(host, "qPXgOKKzFkAxtUD5IhDLk2", "sqA0pWF3qU5rtlwWErbGg");
		androidPushTmMachine = new IGtPush(host, "Z8Yd2w8Vgg8wWVOQA9FuL", "zHNltW4k9D9wVU1OKjWwD9");
		iosPush = new IGtPush(host, "qPXgOKKzFkAxtUD5IhDLk2", "sqA0pWF3qU5rtlwWErbGg");
	}

	public Map<String, Object> single(String receiver, AbstractTemplate tpl, int osType,int appType) {
		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(tpl);
		// 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
		message.setPushNetWorkType(0);
		Target target = new Target();
		target.setAlias(receiver);
		IPushResult ret = null;
		IGtPush push = getPush(osType, tpl, target,appType, false);
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			e.printStackTrace();
			logger.error("发送失败，准备重试");
			ret = push.pushMessageToSingle(message, target, e.getRequestId());
		}

		Map<String, Object> result = ret.getResponse();
		logger.info("推送结果: {}", result);

		return result;
	}

	/**
	 * 按标签发送
	 * @param tpl
	 * @param osType
	 * @param appType
	 * @param tagList
	 * @return
	 */
	public Map<String, Object> tag(AbstractTemplate tpl, int osType,int appType, List tagList) {
		String appId = "vxa494yf3Z7cb22lmvIxq2";
		String appKey = "qPXgOKKzFkAxtUD5IhDLk2";
		String masterSecret = "sqA0pWF3qU5rtlwWErbGg";
		String host = "http://sdk.open.api.igexin.com/apiex.htm";

		IGtPush push = new IGtPush(host, appKey, masterSecret);
		TransmissionTemplate transmissionTemplate = new TransmissionTemplate();
		transmissionTemplate.setTransmissionType(2);
		transmissionTemplate.setAppId(appId);
		transmissionTemplate.setAppkey(appKey);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id","12345");
		jsonObject.put("title","报警消息");
		jsonObject.put("content","消息详情");
		transmissionTemplate.setTransmissionContent(jsonObject.toJSONString());
		AppMessage message = new AppMessage();
		message.setData(transmissionTemplate);
		message.setOffline(true); //离线有效时间，单位为毫秒，可选 message.setOfflineExpireTime(24 * 1000 * 3600); //推送给App的⽬目标⽤用户需要满⾜足的条件
		AppConditions cdt = new AppConditions();
		List<String> appIdList = new ArrayList<String>();
		appIdList.add(appId);
		message.setAppIdList(appIdList);
		//⼿手机类型
		tagList = new ArrayList<String>();
		tagList.add("18911820367");
		cdt.addCondition(AppConditions.TAG,tagList);
		message.setConditions(cdt);
		IPushResult ret = push.pushMessageToApp(message,"CheckAppMessage_toApp");
		System.out.println(ret.getResponse().toString());
		Map<String, Object> result = ret.getResponse();

		//		logger.info("osType {}, appType {}, tagList {}", osType, appType, tagList);
//		AppMessage message = new AppMessage();
//		message.setData(tpl);
//		message.setOffline(true); //离线有效时间，单位为毫秒，可选 message.setOfflineExpireTime(24 * 1000 * 3600); //推送给App的⽬目标⽤用户需要满⾜足的条件
//		AppConditions cdt = new AppConditions();
//		List<String> appIdList = new ArrayList<String>();
////		appIdList.add("vxa494yf3Z7cb22lmvIxq2");
//		appIdList.add("vxa494yf3Z7cb22lmvIxq2");
//		message.setAppIdList(appIdList);
//		cdt.addCondition(AppConditions.TAG,tagList);
//		message.setConditions(cdt);
//		String taskGroupName = "";
//		IGtPush push = getPush(osType, tpl, null,appType, true);
//
//		IPushResult ret = null;
//		try {
//			ret = push.pushMessageToApp(message,"checkapp");
//		} catch (RequestException e) {
//			e.printStackTrace();
//			logger.error("发送失败，准备重试");
//			ret = push.pushMessageToApp(message,taskGroupName);
//		}
//
//		Map<String, Object> result = ret.getResponse();
//		logger.info("推送结果: {}", result);
		return result;
	}

	private IGtPush getPush(int osType, AbstractTemplate tpl, Target target,int appType, boolean isTag) {
		logger.info("osType {}, appType {}", osType, appType);
		if (osType == OsType.IOS.v()) {
			logger.info("苹果普通消息");
			tpl.setAppId("vxa494yf3Z7cb22lmvIxq2");
			tpl.setAppkey("qPXgOKKzFkAxtUD5IhDLk2");
//			tpl.setAppId(ios.getAppid());
//			tpl.setAppkey(ios.getAppkey());
			return iosPush;
		} else if (osType == OsType.ANDRIOD.v()) {
			if(appType==2){
				logger.info("推送巡检消息");
				tpl.setAppId("vxa494yf3Z7cb22lmvIxq2");
				tpl.setAppkey("qPXgOKKzFkAxtUD5IhDLk2");
				if (!isTag) {
					target.setAppId("vxa494yf3Z7cb22lmvIxq2");
				}
				return androidPushCheck;
			}if(appType==3){
				logger.info("推送天猫消息");
				tpl.setAppId("tqSDQPAvXB7eNqPZyuuCo8");
				tpl.setAppkey("Z8Yd2w8Vgg8wWVOQA9FuL");
				if (!isTag) {
					target.setAppId("tqSDQPAvXB7eNqPZyuuCo8");
				}
				return androidPushTmMachine;
			}else{
				logger.info("推送机器消息");
				tpl.setAppId("VOcpBv3ote8PCHDwqjNgb2");
				tpl.setAppkey("q2P7jwmp9R97B1Misnf5y6");
				if (!isTag) {
					target.setAppId("VOcpBv3ote8PCHDwqjNgb2");
				}
				return androidPushMachine;
			}
		}
		return null;
	}

}
