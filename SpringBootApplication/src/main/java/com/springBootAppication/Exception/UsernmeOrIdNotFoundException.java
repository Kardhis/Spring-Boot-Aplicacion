package com.springBootAppication.Exception;

public class UsernmeOrIdNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -751511522395795097L;

	public UsernmeOrIdNotFoundException() {
		super("Usuario o Id no encontrado.");
	}
	
	public UsernmeOrIdNotFoundException(String message) {
		super(message);
	}
}
