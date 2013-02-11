package com.keyconsultant.parse.logintutorial.model.user.authenticate;

import com.parse.ParseUser;


public class AuthenticateUserSuccessEvent {

	public ParseUser user;
	public AuthenticateUserSuccessEvent(ParseUser user) {
		super();
		
		this.user = user;
	}

}
