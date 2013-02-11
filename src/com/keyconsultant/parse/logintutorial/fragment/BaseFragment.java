package com.keyconsultant.parse.logintutorial.fragment;

import com.squareup.otto.BusProvider;

import android.support.v4.app.Fragment;

/**
 * Fragment class that resolves basic service bus requirement (Register, deregister, Event Posting)
 * 
 * @author Trey Robinson
 *
 */
public class BaseFragment extends Fragment {
	@Override
	public void onResume() {
		super.onResume();
		BusProvider.getInstance().register(this);
		
	}
	
	@Override
	public void onPause() {
		/**fragment must be removed from the service bus in onPause or an error will occur
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
}
