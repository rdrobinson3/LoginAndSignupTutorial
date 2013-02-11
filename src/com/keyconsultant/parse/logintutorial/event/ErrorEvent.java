package com.keyconsultant.parse.logintutorial.event;

import com.parse.ParseException;

/**
 * Event that provides access to internal exception details
 * 
 * @author Trey Robinson
 *
 */
public class ErrorEvent {

	private ParseException exception;
	
	public ErrorEvent(ParseException exception){
		this.exception = exception;
	}
	
	/**
	 * @return
	 * 		Code for the error. 
	 */
	public int getErrorCode(){
		return exception.getCode();
	}
	
}
