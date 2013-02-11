package com.keyconsultant.parse.logintutorial;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.keyconsultant.parse.logintutorial.error.UnknownErrorDialogFactory;
import com.keyconsultant.parse.logintutorial.fragment.BaseFragment;
import com.keyconsultant.parse.logintutorial.model.user.UserManager;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.AuthenticateUserErrorEvent;
import com.parse.ParseException;
import com.squareup.otto.Subscribe;

/**
 * Fragment for logging in. Includes button for loading the Create account view. 
 * 
 * @author Trey Robinson
 *
 */
public class LoginFragment extends BaseFragment {

	public static final String EXTRA_USERNAME = "com.keyconsultant.parse.logintutorial.activity.extra.USERNAME";
    public static final String EXTRA_PASSWORD = "com.keyconsultant.parse.logintutorial.activity.extra.PASSWORD";

    // UI references.
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    
    /**
     * Factory method for creating new fragments
     * @return
     */
    public static LoginFragment newInstance(){
    	return new LoginFragment();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		mUserNameEditText = (EditText) view.findViewById(R.id.username);

		mPasswordEditText = (EditText) view.findViewById(R.id.password);
		mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        view.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        
        view.findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View view) {
        		createAccount();
        	}
        });
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null){
			mUserNameEditText.setText(savedInstanceState.getString(EXTRA_USERNAME));
			mPasswordEditText.setText(savedInstanceState.getString(EXTRA_PASSWORD));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(EXTRA_USERNAME, mUserNameEditText.getText().toString());
		outState.putString(EXTRA_PASSWORD, mPasswordEditText.getText().toString());
	}
	
	/**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

    	clearErrors();

        // Store values at the time of the login attempt.
        String username = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
        	mPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = mPasswordEditText;
            cancel = true;
        } else if (password.length() < 4) {
        	mPasswordEditText.setError(getString(R.string.error_invalid_password));
            focusView =mPasswordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
        	mUserNameEditText.setError(getString(R.string.error_field_required));
            focusView = mUserNameEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            UserManager.getInstance().authenticate(username.toLowerCase(Locale.getDefault()), password);
        }
    }

    /**
     * Load the create account view. 
     */
    private void createAccount(){
    	FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    	fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    	fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), CreateAccountFragment.newInstance());
    	fragmentTransaction.addToBackStack(null);
    	fragmentTransaction.commit();
    }
    
	/**
	 * Remove all edit text errors
	 */
	private void clearErrors(){
		mUserNameEditText.setError(null);
		mPasswordEditText.setError(null);
	}
	
	@Subscribe
	public void onSignInError(AuthenticateUserErrorEvent event){
		clearErrors();
		switch (event.getErrorCode()) {
		case ParseException.OBJECT_NOT_FOUND:
			mPasswordEditText.setError(getString(R.string.error_incorrect_password));
			mPasswordEditText.requestFocus();
			break;
		default:
			UnknownErrorDialogFactory.createUnknownErrorDialog(this.getActivity()).show();
			break;
		}
	}
}
