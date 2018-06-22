package com.point72.msg.center.util;

import java.util.Map;

import javax.annotation.PostConstruct;

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
import com.point72.msg.center.OsType;
import com.point72.msg.center.config.GetuiAndroidProperties;
import com.point72.msg.center.config.GetuiIOSProperties;
import com.point72.msg.center.config.GetuiProProperties;

import lombok.Data;

@Data

@Component
// @EnableConfigurationProperties({ GetuiAndroidProperties.class,
// GetuiIOSProperties.class })
public class GpushSendHandler {

	Logger logger = LoggerFactory.getLogger(GpushSendHandler.class);

	private IGtPush androidPush;
	private IGtPush iosPush;
	private IGtPush proPush;

	@Autowired
	GetuiAndroidProperties android;
	@Autowired
	GetuiIOSProperties ios;
	@Autowired
	GetuiProProperties pro;

	private String host;

	@PostConstruct
	private void init() {
		androidPush = new IGtPush(host, android.getAppkey(), android.getAppsecret());
		iosPush = new IGtPush(host, ios.getAppkey(), ios.getAppsecret());
		proPush = new IGtPush(host, pro.getAppkey(), pro.getAppsecret());
	}

	public Map<String, Object> single(String receiver, AbstractTemplate tpl, int osType) {
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
		IGtPush push = getPush(osType, tpl, target);
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

	private IGtPush getPush(int osType, AbstractTemplate tpl, Target target) {
		if (osType == OsType.IOS.v()) {
			logger.info("苹果基础版消息");
			tpl.setAppId(ios.getAppid());
			tpl.setAppkey(ios.getAppkey());
			target.setAppId(ios.getAppid());
			return iosPush;
		} else if (osType == OsType.ANDRIOD.v()) {
			logger.info("安卓版消息");
			tpl.setAppId(android.getAppid());
			tpl.setAppkey(android.getAppkey());
			target.setAppId(android.getAppid());
			return androidPush;
		} else if (osType == OsType.PRO.v()) {
			logger.info("苹果专业版消息");
			tpl.setAppId(pro.getAppid());
			tpl.setAppkey(pro.getAppkey());
			target.setAppId(pro.getAppid());
			return proPush;
		} else {
			return null;
		}
	}

}
