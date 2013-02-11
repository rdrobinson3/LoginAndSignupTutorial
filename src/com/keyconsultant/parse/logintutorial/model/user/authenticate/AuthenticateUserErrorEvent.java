package com.keyconsultant.parse.logintutorial.model.user.authenticate;

import com.keyconsultant.parse.logintutorial.event.ErrorEvent;
import com.parse.ParseException;

public class AuthenticateUserErrorEvent extends ErrorEvent {

	public AuthenticateUserErrorEvent(ParseException exception) {
		super(exception);
	}

}
