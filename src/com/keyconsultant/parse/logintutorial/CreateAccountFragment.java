package com.keyconsultant.parse.logintutorial;

import java.util.Locale;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.keyconsultant.parse.logintutorial.error.UnknownErrorDialogFactory;
import com.keyconsultant.parse.logintutorial.fragment.BaseFragment;
import com.keyconsultant.parse.logintutorial.model.user.UserManager;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.AuthenticateUserErrorEvent;
import com.parse.ParseException;
import com.squareup.otto.Subscribe;

/**
 * Create an Account. Username is the primary method of login. Email is used for forgotten password recovery. 
 * 
 * @author Trey Robinson
 *
 */
public class CreateAccountFragment extends BaseFragment implements OnClickListener {

    protected static final String EXTRA_EMAIL = "com.keyconsultant.parse.logintutorial.fragment.extra.EMAIL";
    protected static final String EXTRA_USERNAME = "com.keyconsultant.parse.logintutorial.fragment.extra.USERNAME";
    protected static final String EXTRA_PASSWORD = "com.keyconsultant.parse.logintutorial.fragment.extra.PASSWORD";
    protected static final String EXTRA_CONFIRM = "com.keyconsultant.parse.logintutorial.fragment.extra.CONFIRMPASSWORD";
	
	private EditText mUserNameEditText;
	private EditText mEmailEditText; 
	private EditText mPasswordEditText;
	private EditText mConfirmPasswordEditText;
	private Button mCreateAccountButton;
	
	private String mEmail;
	private String mUsername;
	private String mPassword;
	private String mConfirmPassword;
	
	/**
	 * Factory method for creating fragment instances.
	 * @return
	 */
	public static CreateAccountFragment newInstance(){
		return new CreateAccountFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_create_account, container, false);
		
		mUserNameEditText = (EditText)view.findViewById(R.id.etUsername);
		mEmailEditText = (EditText)view.findViewById(R.id.etEmail);
		mPasswordEditText = (EditText)view.findViewById(R.id.etPassword);
		mConfirmPasswordEditText = (EditText)view.findViewById(R.id.etPasswordConfirm);
		
		mCreateAccountButton = (Button)view.findViewById(R.id.btnCreateAccount);
		mCreateAccountButton.setOnClickListener(this);
		return view;
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(savedInstanceState != null){
			mEmailEditText.setText(savedInstanceState.getString(EXTRA_EMAIL));
			mUserNameEditText.setText(savedInstanceState.getString(EXTRA_USERNAME));
			mPasswordEditText.setText(savedInstanceState.getString(EXTRA_PASSWORD));
			mConfirmPasswordEditText.setText(savedInstanceState.getString(EXTRA_CONFIRM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(EXTRA_EMAIL, mEmailEditText.getText().toString());
		outState.putString(EXTRA_USERNAME, mUserNameEditText.getText().toString());
		outState.putString(EXTRA_PASSWORD, mPasswordEditText.getText().toString());
		outState.putString(EXTRA_CONFIRM, mConfirmPasswordEditText.getText().toString());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCreateAccount:
			createAccount();
			break;

		default:
			break;
		}
	}
	
	/**
	 * Some front end validation is done that is not monitored by the service. 
	 * If the form is complete then the information is passed to the service. 
	 */
	private void createAccount(){
		clearErrors();
		
		boolean cancel = false;
        View focusView = null;

     // Store values at the time of the login attempt.
        mEmail = mEmailEditText.getText().toString();
        mUsername = mUserNameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        mConfirmPassword = mConfirmPasswordEditText.getText().toString();
        
        // Check for a valid confirm password.
        if (TextUtils.isEmpty(mConfirmPassword)) {
        	mConfirmPasswordEditText.setError(getString(R.string.error_field_required));
        	focusView = mConfirmPasswordEditText;
        	cancel = true;
        } else if (mPassword != null && !mConfirmPassword.equals(mPassword)) {
        	mPasswordEditText.setError(getString(R.string.error_invalid_confirm_password));
        	focusView = mPasswordEditText;
        	cancel = true;
        }
        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = mPasswordEditText;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordEditText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordEditText;
            cancel = true;
        }
        
        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailEditText.setError(getString(R.string.error_field_required));
            focusView = mEmailEditText;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailEditText.setError(getString(R.string.error_invalid_email));
            focusView = mEmailEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            UserManager.getInstance().signUp(mUsername.toLowerCase(Locale.getDefault()), mEmail, mPassword);
            
        }
        
	}
	
	/**
	 * Remove error messages from all fields. 
	 */
	private void clearErrors(){ mEmailEditText.setError(null);
		mUserNameEditText.setError(null);
		mPasswordEditText.setError(null);
		mConfirmPasswordEditText.setError(null);
	}
	
	@Subscribe
	public void onSignInError(AuthenticateUserErrorEvent event){
		clearErrors();
		switch (event.getErrorCode()) {
			case ParseException.INVALID_EMAIL_ADDRESS:
				mEmailEditText.setError(getString(R.string.error_invalid_email));
				mEmailEditText.requestFocus();
				break;
			case ParseException.EMAIL_TAKEN:
				mEmailEditText.setError(getString(R.string.error_duplicate_email));
				mEmailEditText.requestFocus();
				break;
			case ParseException.USERNAME_TAKEN:
				mUserNameEditText.setError(getString(R.string.error_duplicate_username));
				mUserNameEditText.requestFocus();
				break;
			default:
				UnknownErrorDialogFactory.createUnknownErrorDialog(this.getActivity()).show();
				break;
		}
	}
	
}
