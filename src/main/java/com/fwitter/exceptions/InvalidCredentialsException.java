package com.fwitter.exceptions;

public class InvalidCredentialsException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCredentialsException() {
		super("Username or password does not exists.");
	}
}
