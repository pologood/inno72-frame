package com.inno72.msg.center.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.inno72.config.client.SMSProperties;
import com.inno72.msg.center.service.impl.MsgModelServiceImpl;
import com.inno72.plugin.http.HttpClient;

/**
 * 发送短信接口
 * 
 * @author Houkm
 *
 *         2017年6月16日
 */
@Component
public class SmsSendHandler {
	
	private Logger logger = LoggerFactory.getLogger(SmsSendHandler.class);


	@Autowired
	SMSProperties smsProperties;

	Map<String, String> param = new HashMap<>();

	/**
	 * 通过云片平台发送
	 * 
	 * @param mobile
	 * @param msg
	 * @return
	 * @author Houkm 2017年6月16日
	 */
	public String sendYunpian(String mobile, String msg) {
		Map<String, String> param = new HashMap<>();
		param.put("mobile", mobile);
		param.put("text", msg);
		param.put("apikey", smsProperties.getYunpian().getApikey());
		String result = HttpClient.form(smsProperties.getYunpian().getApi(), param, null);
		return result;
	}

	/**
	 * 通过筑望发送短信
	 * 
	 * @param mobile
	 * @param msg
	 * @return
	 * @author Houkm 2017年6月18日
	 */
	public String sendZhuwang(String mobile, String msg) {
		Map<String, String> params = new HashMap<>();
		params.put("mobile", mobile);
		params.put("content", msg);
		params.put("userid", smsProperties.getZhuwang().getUserid());
		params.put("account", smsProperties.getZhuwang().getAccount());
		params.put("password", smsProperties.getZhuwang().getPassword());
		params.put("sendTime", "");
		params.put("action", "send");
		params.put("extno", "");
		String result = HttpClient.form(smsProperties.getZhuwang().getApi(), params, null);
		return result;
	}
	
	
	/**
	 * 发送联江短信
	 * @param mobile
	 * @param msg
	 * @return
	 */
	public String sendLianJiang(String mobile, String msg) {
		long temp = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<>();
		map.put("content", msg);
		map.put("code", temp);
		map.put("mobile", mobile);
		map.put("account", smsProperties.getLianjiang().getAccount());
		map.put("timestamp", temp);
		String key = Md5Util.getMD5String(smsProperties.getLianjiang().getKey() + Md5Util.getMD5String(String.valueOf(temp)));
		map.put("key", key);
		logger.info("联江短信发送参数：{}"+JSON.toJSONString(map));
		String result = HttpClient.post(smsProperties.getLianjiang().getApi(), JSON.toJSONString(map));
		return result;
	}

}
