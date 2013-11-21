package com.blueskyconnie.heritagefiesta;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.blueskyconnie.heritagefiesta.helper.AlertDialogHelper;
import com.blueskyconnie.heritagefiesta.helper.ConnectionDetector;

public class ContactFragment extends Fragment {

	private Button btnSendMail;
	private Button btnPhoneSchool;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
		
		btnSendMail = (Button) rootView.findViewById(R.id.btnSendMail);
		btnPhoneSchool = (Button) rootView.findViewById(R.id.btnPhone);
		OnClickListener listener = new MyOnClickListener();
		btnSendMail.setOnClickListener(listener);
		btnPhoneSchool.setOnClickListener(listener);
		return rootView;
	}
	
	private final class MyOnClickListener implements
		OnClickListener {
		@Override
		public void onClick(View view) {
			
			ConnectionDetector detector = new ConnectionDetector(getActivity());
			if (!detector.isConnectingToInternet()) {
				AlertDialogHelper.showNoInternetDialog(getActivity());
			} else {
				switch (view.getId()) {
					case R.id.btnSendMail:
						Intent itEmail = new Intent();
						itEmail.setAction(Intent.ACTION_VIEW);
						itEmail.setData(Uri.parse("mailto:" + getString(R.string.contact_email)));
						itEmail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
						itEmail.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text));
						try {
							startActivity(Intent.createChooser(itEmail, getString(R.string.choose_email_app)));
						} catch (ActivityNotFoundException ex) {
							Toast.makeText(ContactFragment.this.getActivity(), 
									getString(R.string.email_app_not_installed), Toast.LENGTH_SHORT).show();
						}
						break;
					case R.id.btnPhone:
						Intent itPhone = new Intent();
						itPhone.setAction(Intent.ACTION_CALL);
						String phone = getString(R.string.contact_phone).replace(" ", "");
						Log.i("MyOnClickListener", "Call telephone number: " + phone);
						itPhone.setData(Uri.parse("tel:" + phone));
						startActivity(itPhone);
						break;
				}
			}
		}
	}
}
