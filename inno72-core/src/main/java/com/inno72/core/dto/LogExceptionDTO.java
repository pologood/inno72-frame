package com.inno72.core.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.inno72.annotation.ExceptionNotify;

import lombok.Data;

/**
 * 记录异常DTO
 * 
 * @author Houkm
 *
 *         2017年7月10日
 */

@Data
public class LogExceptionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1686471705099423281L;
	private Exception ex;
	private ExceptionNotify exceptionNotify;
	private LocalDateTime caughtTime;
	private String applicationName;
	private String instanceName;

}
