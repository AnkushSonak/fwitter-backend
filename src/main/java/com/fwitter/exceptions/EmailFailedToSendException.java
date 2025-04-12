package com.fwitter.exceptions;

public class EmailFailedToSendException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailFailedToSendException() {
		super("The Email failed to send");
	}
}
