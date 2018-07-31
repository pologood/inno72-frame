package com.inno72.core.aop;


import com.inno72.log.LogAllContext;
import com.inno72.log.LogContext;
import com.inno72.log.vo.LogType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimeConsumingAround {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeConsumingAround.class);

	@Around("execution(* com.inno72.controller.*.*(..))")
	public Object logTime(ProceedingJoinPoint pjp) throws Throwable {

		long begin = System.currentTimeMillis();
		String method = pjp.getSignature().getName();
		String className = pjp.getTarget().getClass().getName();
		Object ret = pjp.proceed();
		long end = System.currentTimeMillis();

		String logDetail = "Math : ClassName<"+className+">.method<"+method+">, "
				+ "start:【"+ begin +"】, end:【"+ end +"】 duration ==> "+ (end - begin) + "s";

		new LogAllContext( LogType.BIZ )
				.tag("统计接口入出时间")
				.activityId("")
				.detail(logDetail)
				.operatorId("")
				.userId("")
				.bulid();
		LOGGER.info("统计接口入出时间! === {} ", logDetail);

		return ret;

	}
}
