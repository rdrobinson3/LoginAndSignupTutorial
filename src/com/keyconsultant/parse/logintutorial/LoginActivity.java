package com.keyconsultant.parse.logintutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.keyconsultant.parse.logintutorial.activity.BaseActivity;
import com.keyconsultant.parse.logintutorial.forgotpassword.ForgotPasswordDialogFragment;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.AuthenticateUserErrorEvent;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.AuthenticateUserStartEvent;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.AuthenticateUserSuccessEvent;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.UserForgotPasswordErrorEvent;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.UserForgotPasswordStartEvent;
import com.keyconsultant.parse.logintutorial.model.user.authenticate.UserForgotPasswordSuccessEvent;
import com.parse.Parse;
import com.squareup.otto.Subscribe;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well. Based loosley on the default Login template. 
 * 
 * @author Trey Robinson
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        

        Parse.initialize(this, "Your App Id", "Your Client ID");
        
        FragmentManager fragmentManager = getSupportFragmentManager();
    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    	fragmentTransaction.replace(R.id.main_view, LoginFragment.newInstance());
    	fragmentTransaction.commit();
        
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_forgot_password:
	        	forgotPassword();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Open the forgotPassword dialog 
	 */
	private void forgotPassword(){
		 FragmentManager fm = getSupportFragmentManager();
	     ForgotPasswordDialogFragment forgotPasswordDialog = new ForgotPasswordDialogFragment();
	     forgotPasswordDialog.show(fm, null);
	}
	
	
    @Subscribe
    public void onSignInStart(AuthenticateUserStartEvent event){
    	showProgress(true, getString(R.string.login_progress_signing_in));
    }
    
    @Subscribe
	public void onSignInSuccess(AuthenticateUserSuccessEvent event){
    	showProgress(false, getString(R.string.login_progress_signing_in));
		Intent loginSuccess = new Intent(this, MainActivity.class);
		startActivity(loginSuccess);
		finish();
	}
    
    @Subscribe
	public void onSignInError(AuthenticateUserErrorEvent event){
		showProgress(false, getString(R.string.login_progress_signing_in));
	}
    
    @Subscribe
    public void onForgotPasswordStart(UserForgotPasswordStartEvent event){
    	showProgress(true, getString(R.string.login_progress_signing_in));
    }
    
    @Subscribe
    public void onForgotPasswordSuccess(UserForgotPasswordSuccessEvent event){
    	showProgress(false,getString(R.string.login_progress_signing_in));
    	Toast toast =Toast.makeText(this, "A password reset email has been sent.", Toast.LENGTH_LONG);
    	toast.show();
    }
    
    @Subscribe
    public void onForgotPasswordError(UserForgotPasswordErrorEvent event){
    	showProgress(false, getString(R.string.login_progress_signing_in));
    	Toast toast =Toast.makeText(this, "An error has occured. Please try again.", Toast.LENGTH_LONG);
    	toast.show();
    }
}
