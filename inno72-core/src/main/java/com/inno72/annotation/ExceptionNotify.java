package com.inno72.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记方法发生异常时是否通知指定的人员
 * 
 * @author Houkm
 *
 *         2017年8月10日
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExceptionNotify {

	/**
	 * 问题责任人
	 * 
	 * @return
	 * @author Houkm 2017年7月7日
	 */
	public String owner();

	/**
	 * 问题标题
	 * 
	 * @return
	 * @author Houkm 2017年7月7日
	 */
	public String title();

	/**
	 * 报警通知接收人<br>
	 * 此接收人的值为钉钉userid
	 * 
	 * @return
	 * @author Houkm 2017年7月10日
	 */
	public String[] notifyUser() default "";

	/**
	 * 报警通知接收群
	 * 
	 * @return
	 * @author Houkm 2017年7月10日
	 */
	public String notifyGroup() default "";

	/**
	 * 项目名
	 * 
	 * @return
	 * @author Houkm 2017年7月17日
	 */
	public String project();

	/**
	 * 功能名
	 * 
	 * @return
	 * @author Houkm 2017年7月17日
	 */
	public String function();

}
