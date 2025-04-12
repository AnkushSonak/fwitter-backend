package com.fwitter.exceptions;

public class UserDoesNotExistsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserDoesNotExistsException() {
		super("The user you are looking for doesn't exists...");
	}

}
