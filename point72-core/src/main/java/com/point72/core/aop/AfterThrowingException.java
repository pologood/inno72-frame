package com.point72.core.aop;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.point72.annotation.ExceptionNotify;
import com.point72.config.client.MqProperties;
import com.point72.core.dto.LogExceptionDTO;

@Aspect
@Component
public class AfterThrowingException {

	@Pointcut("within(com.point72..*) && !@annotation(com.point72.core.aop.WithOutAfterThrowingCut) && !execution(* com.point72.msg.center.*.*(..))")
	public void cut() {
	}

	@Autowired
	RabbitTemplate template;

	@Autowired
	private MqProperties props;

	private final String PACKAGE_PREFIXX = "com.point72";

	@Value("${spring.application.name}")
	private String applicationName;

	/**
	 * Exception异常记录
	 * 
	 * @param ex
	 * @author Houkm 2017年7月10日
	 */
	@AfterThrowing(pointcut = "cut()", throwing = "ex")
	public void processUnCaughtSystemException(Exception ex) {
		process(ex);
	}

	private void process(Exception ex) {

		// 报警中心记录队列
		LogExceptionDTO dto = new LogExceptionDTO();
		dto.setCaughtTime(LocalDateTime.now());
		dto.setEx(ex);
		dto.setApplicationName(applicationName);
		// 获取异常堆栈信息
		StackTraceElement[] stackTraceElementAry = ex.getStackTrace();
		List<StackTraceElement> beforeStackTrace = Arrays.asList(stackTraceElementAry);
		// 获取异常堆栈中com.point72包下的堆栈
		List<StackTraceElement> afterStackTrace = beforeStackTrace.stream()
				.filter(s -> s.getClassName().contains(PACKAGE_PREFIXX))
				.collect(Collectors.toCollection(ArrayList::new));
		afterStackTrace.forEach(stack -> {
			try {
				// 加载异常堆栈中的class
				Class<?> clazz = Class.forName(stack.getClassName());
				// 异常堆栈中的方法
				String method = stack.getMethodName();
				// 获取类中的所有方法
				Method[] methods = clazz.getDeclaredMethods();
				Arrays.asList(methods).forEach(methodItem -> {
					// 如果类中的方法与异常堆栈中的方法相同
					if (methodItem.getName().equals(method)) {
						// 查看是否有ExceptionNotify注解,有的话发送消息
						ExceptionNotify exceptionNotify = methodItem.getAnnotation(ExceptionNotify.class);
						if (exceptionNotify != null) {
							dto.setExceptionNotify(exceptionNotify);
						}
					}
				});
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
		// 发送到报警中心
		template.convertAndSend(props.getLogException().getExchange(), props.getLogException().getKey(), dto);

	}

}
