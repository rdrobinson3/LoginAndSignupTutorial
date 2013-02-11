package com.keyconsultant.parse.logintutorial.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.keyconsultant.parse.logintutorial.R;
import com.squareup.otto.BusProvider;

/**
 * Base activity class that handles the interaction of the Activity with the service bus. Also includes
 * an application wide implementation of the progress dialog.  
 * 
 * 
 * @author Trey Robinson
 *
 */
public class BaseActivity extends FragmentActivity {

	protected StatusView mStatusView;
	protected View mMainView;
	
	@Override
	protected void onResume() {
		super.onResume();
		//register the activity to the service bus
		BusProvider.getInstance().register(this);
		
		//required view components for the loading screen
        mMainView = findViewById(R.id.main_view);
        mStatusView = (StatusView) findViewById(R.id.statusView);
	}
	
	@Override
	protected void onPause() {
		/**activity must be removed from the service bus in onPause or an error will occur
		  when the bus attempts to dispatch an event to the paused activity. **/ 
		BusProvider.getInstance().unregister(this);
		super.onPause();
	}
	
	/**
	 * Post the event to the service bus
	 * @param event
	 * 		The event to dispatch on the service bus
	 */
	protected void postEvent(Object event) {
		BusProvider.getInstance().post(event);
	}
	
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show, String message) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mStatusView.setVisibility(View.VISIBLE);
            mStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mMainView.setVisibility(View.VISIBLE);
            mMainView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
	
}
