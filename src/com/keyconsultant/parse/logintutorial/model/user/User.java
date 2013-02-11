package com.keyconsultant.parse.logintutorial.model.user;

import com.parse.ParseUser;

public class User extends ParseUser{
	
	public User(String username, String email, String password){
		this.setEmail(email);
		this.setUsername(username);
		this.setPassword(password);
	}
	
}
