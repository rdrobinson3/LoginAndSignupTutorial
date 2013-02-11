package com.keyconsultant.parse.logintutorial.model.user;

import com.keyconsultant.parse.logintutorial.model.manager.BaseManager;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.AuthenticateUserErrorEvent;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.AuthenticateUserStartEvent;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.AuthenticateUserSuccessEvent;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.UserForgotPasswordErrorEvent;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.UserForgotPasswordStartEvent;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.UserForgotPasswordSuccessEvent;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;
import com.squareup.otto.Produce;

/**
 * Manages user authentication and account updates
 * 
 * @author Trey Robinson
 * 
 */
public class UserManager extends BaseManager {

	private static UserManager mManager;

	public static UserManager getInstance() {
		if (mManager == null) {
			mManager = new UserManager();
		}

		return mManager;
	}

	/**
	 * @return Current user
	 */
	public ParseUser getUser() {
		return ParseUser.getCurrentUser();
	}

	/**
	 * Authenticate the user
	 * 
	 * @param username
	 *            Username for the account
	 * @param password
	 *            Password for the account
	 */
	public void authenticate(String username, String password) {
		postEvent(produceUserSignInStartEvent());
		ParseUser.logInInBackground(username, password, new UserLogInCallback());
	}

	/**
	 * Create new user. If the user email already exists an error will be
	 * dispatched via the service bus
	 * 
	 * @param email
	 *            The user email for the new account
	 * @param password
	 *            Password for the new account
	 */
	public void signUp(String username, String email, String password) {
		User newUser = new User(username, email, password);
		postEvent(produceUserSignInStartEvent());
		newUser.signUpInBackground(new UserSignUpCallback(newUser));
	}

	/**
	 * Dispatches a forgot password request. The request will send an email to the address if an account for that address exists. 
	 * @param email
	 * 		Email for the account that has forgotten their password
	 */
	public void forgotPassword(String email) {
		postEvent(new UserForgotPasswordStartEvent());
		ParseUser.requestPasswordResetInBackground(email, new UserForgotPasswordCallback());
	}

	/**
	 * Creates an event notifying that authentication has begun
	 * 
	 * @return
	 */
	@Produce
	public AuthenticateUserStartEvent produceUserSignInStartEvent() {
		return new AuthenticateUserStartEvent();
	}

	/**
	 * Creates an event containing the signed in user
	 * 
	 * @param user
	 *            User currently signed in
	 * @return
	 */
	@Produce
	public AuthenticateUserSuccessEvent produceUserSignInSuccessEvent(
			ParseUser user) {
		return new AuthenticateUserSuccessEvent(user);
	}

	/**
	 * Creates an even for sign in errors
	 * 
	 * @return
	 */
	@Produce
	public AuthenticateUserErrorEvent produceUserSignInErrorEvent(
			ParseException exception) {
		return new AuthenticateUserErrorEvent(exception);
	}
	
	/**
	 * Creates an event notifying that forgot password has begun
	 * 
	 * @return
	 */
	@Produce
	public UserForgotPasswordStartEvent produceUserForgotPasswordStartEvent() {
		return new UserForgotPasswordStartEvent();
	}
	
	/**
	 * Creates an event for successful Forgot Password request
	 * 
	 * @param user
	 *            User currently signed in
	 * @return
	 */
	@Produce
	public UserForgotPasswordSuccessEvent produceUserForgotPasswordSuccessEvent() {
		return new UserForgotPasswordSuccessEvent();
	}
	
	/**
	 * Creates an event for failed Forgot Password attempt
	 * 
	 * @return
	 */
	@Produce
	public UserForgotPasswordErrorEvent produceUserForgotPasswordErrorEvent(ParseException exception) {
		return new UserForgotPasswordErrorEvent(exception);
	}

	private class UserSignUpCallback extends SignUpCallback {

		User user;

		public UserSignUpCallback(User user) {
			this.user = user;
		}

		@Override
		public void done(ParseException e) {
			if (e == null) {
				postEvent(produceUserSignInSuccessEvent(user));
			} else {
				postEvent(produceUserSignInErrorEvent(e));
			}

		}
	}

	private class UserLogInCallback extends LogInCallback {

		public UserLogInCallback() {
			super();
		}

		@Override
		public void done(ParseUser user, ParseException e) {
			if (e == null) {
				postEvent(produceUserSignInSuccessEvent(user));
			} else {
				postEvent(produceUserSignInErrorEvent(e));
			}
		}
	}
	
	private class UserForgotPasswordCallback extends RequestPasswordResetCallback{
		public UserForgotPasswordCallback(){
			super();
		}
		
		@Override
		public void done(ParseException e) {
			if (e == null) {
				postEvent(produceUserForgotPasswordSuccessEvent());
			} else {
				postEvent(produceUserForgotPasswordErrorEvent(e));
			}
		}		
	}
}
