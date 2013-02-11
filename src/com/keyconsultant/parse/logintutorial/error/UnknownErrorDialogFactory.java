package com.keyconsultant.parse.logintutorial.error;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Dialog factory for unknown errors. (Lazy :-) ). 
 * 
 * @author Trey Robinson
 *
 */
public class UnknownErrorDialogFactory { 
	
	public static AlertDialog createUnknownErrorDialog(Context context){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		 
		// set title
		alertDialogBuilder.setTitle("Error");

		// set dialog message
		alertDialogBuilder
			.setMessage("An Unknown Error Occured.")
			.setCancelable(false)
			.setPositiveButton("OK :-( ",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					dialog.cancel();
				}
			  });

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			
			return alertDialog;
	}
}
