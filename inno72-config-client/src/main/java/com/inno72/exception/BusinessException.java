package com.inno72.exception;

public class BusinessException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4105621697399024219L;
	/** 为能够全面的保留系统产生异常的所有信息，以便用户调试程序而引入的实例变量 */
	private Throwable throwable;
	private String msg;
	// 错误信息代码
	private String msgId;

	@SuppressWarnings("unused")
	private BusinessException() {
	}

	/**
	 * 返回异常对象
	 * 
	 * @return Throwable 异常对象
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * 设置异常对象
	 * 
	 * @param throwable
	 *            Throwable 异常对象
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	protected BusinessException(String msgId, String msg) {
		this.msgId = msgId;
		this.msg = msg;
	}

	@Override
	public String getMessage() {
		return msg;
	}

}
