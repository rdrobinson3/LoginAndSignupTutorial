package com.keyconsultant.parse.logintutorial.model.user.authenticate;

import com.keyconsultant.parse.logintutorial.event.ErrorEvent;
import com.parse.ParseException;

/**
 * Event for forgot password errors. 
 * @author Trey Robinson
 *
 */
public class UserForgotPasswordErrorEvent extends ErrorEvent {

	public UserForgotPasswordErrorEvent(ParseException exception) {
		super(exception);
	}

}
