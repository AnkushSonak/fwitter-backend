package com.fwitter.exceptions;

public class UnableToResolvePhotoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnableToResolvePhotoException() {
		super("The photo you are looking for can't be found");
	}
}
