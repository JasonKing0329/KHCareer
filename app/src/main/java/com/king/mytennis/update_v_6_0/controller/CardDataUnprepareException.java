package com.king.mytennis.update_v_6_0.controller;

public class CardDataUnprepareException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CardDataUnprepareException() {
		
	}

	@Override
	public String getMessage() {

		String message = "You must call prepareXXCardData before get data!\n" + super.getMessage();
		return message;
	}
	
}
