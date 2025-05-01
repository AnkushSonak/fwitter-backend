package com.fwitter.exceptions;

public class UnableToCreatePostException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnableToCreatePostException() {
		super("Unable to create Post at this time.");
	}
}
