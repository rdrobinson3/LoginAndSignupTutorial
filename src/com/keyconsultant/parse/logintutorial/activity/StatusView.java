package com.keyconsultant.parse.logintutorial.activity;

import com.keyconsultant.parse.logintutorial.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Loading view with nondeterminate spinner
 * 
 * @author Trey Robinson
 *
 */
public class StatusView extends LinearLayout {

	private TextView mStatusMessage;

	public StatusView(Context context) {
		super(context);
		initViews(context);
	}
	
	public StatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}
	
	public StatusView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}
	
	private void initViews(Context context){
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_status, this, true);
		mStatusMessage = (TextView)findViewById(R.id.status_message);
	}
	
	/**
	 * Set the message to display on the loading screen. 
	 * @param statusMessage
	 * 		Message to display on the loading screen
	 */
	public void setStatusMessage(String statusMessage){
		mStatusMessage.setText(statusMessage);
	}

}
