package com.keyconsultant.parse.logintutorial;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.keyconsultant.parse.logintutorial.model.user.UserManager;

public class MainActivity extends Activity {

	private TextView currentUserTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		currentUserTextView = (TextView)findViewById(R.id.tvCurrentUsername);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		currentUserTextView.setText("Welcome, " + UserManager.getInstance().getUser().getUsername());
	}

}
