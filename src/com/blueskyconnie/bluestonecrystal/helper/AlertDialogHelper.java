package com.blueskyconnie.bluestonecrystal.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.blueskyconnie.bluestonecrystal.R;

public final class AlertDialogHelper {

	public static void showNoInternetDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.info_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(context.getString(R.string.no_internet_error));
		builder.setNeutralButton(context.getString(R.string.confirm_exit), 
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
}
