package com.fwitter.exceptions;

public class PostDoesNotExistException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PostDoesNotExistException() {
		super("The post requested deos not exist.");
	}

}
