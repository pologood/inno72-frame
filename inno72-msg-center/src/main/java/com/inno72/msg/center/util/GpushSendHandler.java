package com.inno72.msg.center.util;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	// private IGtPush iosPush;
	// private IGtPush proPush;

	// @Autowired
	// GetuiAndroidProperties android;
	// @Autowired
	// GetuiIOSProperties ios;
	// @Autowired
	// GetuiProProperties pro;

	private String host;

	@PostConstruct
	private void init() {
		androidPushMachine = new IGtPush(host, "q2P7jwmp9R97B1Misnf5y6", "5rOs0t4RQW7giCJY7uSPb9");
		androidPushCheck= new IGtPush(host, "qPXgOKKzFkAxtUD5IhDLk2", "sqA0pWF3qU5rtlwWErbGg");
		androidPushTmMachine= new IGtPush(host, "Z8Yd2w8Vgg8wWVOQA9FuL", "zHNltW4k9D9wVU1OKjWwD9");

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
		IGtPush push = getPush(osType, tpl, target,appType);
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

	private IGtPush getPush(int osType, AbstractTemplate tpl, Target target,int appType) {
		if(appType==2){
			logger.info("推送巡检消息");
			tpl.setAppId("vxa494yf3Z7cb22lmvIxq2");
			tpl.setAppkey("qPXgOKKzFkAxtUD5IhDLk2");
			target.setAppId("vxa494yf3Z7cb22lmvIxq2");
			return androidPushCheck;
		}if(appType==3){
			logger.info("推送天猫消息");
			tpl.setAppId("tqSDQPAvXB7eNqPZyuuCo8");
			tpl.setAppkey("Z8Yd2w8Vgg8wWVOQA9FuL");
			target.setAppId("tqSDQPAvXB7eNqPZyuuCo8");
			return androidPushTmMachine;
		}else{
			logger.info("推送机器消息");
			tpl.setAppId("VOcpBv3ote8PCHDwqjNgb2");
			tpl.setAppkey("q2P7jwmp9R97B1Misnf5y6");
			target.setAppId("VOcpBv3ote8PCHDwqjNgb2");
			return androidPushMachine;
		}
		
	}

}
