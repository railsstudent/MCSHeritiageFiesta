package com.blueskyconnie.heritagefiesta.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.blueskyconnie.heritagefiesta.MainActivity;
import com.blueskyconnie.heritagefiesta.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public final class AlertDialogHelper {

	public static void showNoInternetDialog(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.info_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(context.getString(R.string.no_internet_error));
		builder.setNeutralButton(context.getString(R.string.confirm_exit), 
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// mod by Connie Leung, 2014-03-08. Close current app
					if (context instanceof Activity) {
						((Activity) context).finish();
					}
				}
			});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
	public static void showConfirmExitDialog(final Context context, final ImageLoader imageLoader) {
		
		// prompt confirmation dialog before exit
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_NEGATIVE:   // confirm to exit
						// close dialog and do nothing
						if (context != null && context instanceof MainActivity) {
							dialog.dismiss();
							imageLoader.stop();
							((MainActivity) context).finish();
						} else {
							Toast.makeText(context, R.string.exit_error, Toast.LENGTH_LONG).show();	
						}
						break;
					case DialogInterface.BUTTON_POSITIVE:   // cancel
						dialog.dismiss();
						break;
				}
			}
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage(R.string.title_confirm_exit);
		builder.setPositiveButton(R.string.cancel_exit, listener);
		builder.setNegativeButton(R.string.confirm_exit, listener);
		AlertDialog quitDialog = builder.create();
		quitDialog.show();
	}
}