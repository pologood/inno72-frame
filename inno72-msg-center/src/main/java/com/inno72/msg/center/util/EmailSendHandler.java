package com.inno72.msg.center.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 发送邮件工具类
 * 
 * @author gaohuan
 *
 *         2018年8月14日
 */
@Component
public class EmailSendHandler {

	Logger logger = LoggerFactory.getLogger(EmailSendHandler.class);
	@Autowired
	private JavaMailSender mailSender;

	@Value("${mail.fromMail.addr}")
	private String from;

	/**
	 * 发送邮件
	 * 
	 * @param mobile
	 * @param msg
	 * @return
	 * @author gaohuan 2018年8月14日
	 */
	public String sendSimpleMail(String to, String subject, String content) {
		String result;
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);

		try {
			mailSender.send(message);
			result = "success";
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = "error";
		}

		return result;
	}

}
