package com.blueskyconnie.heritagefiesta;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blueskyconnie.heritagefiesta.helper.AlertDialogHelper;
import com.blueskyconnie.heritagefiesta.helper.ConnectionDetector;

public class ContactFragment extends Fragment {

	private ConnectionDetector detector;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		detector = new ConnectionDetector(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView =  inflater.inflate(R.layout.fragment_contact, container, false);
		TextView tvPhone = (TextView) rootView.findViewById(R.id.tvPhone);
		Linkify.addLinks(tvPhone, Linkify.PHONE_NUMBERS);
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_contact, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_email:
				//ConnectionDetector detector = new ConnectionDetector(getActivity());
				if (!detector.isConnectingToInternet()) {
					AlertDialogHelper.showNoInternetDialog(getActivity());
					return false;
				}
				createEmailIntent();
				return true;
			case R.id.menu_phone:
				createPhoneIntent();
				return true;
			case R.id.menu_share:
				if (!detector.isConnectingToInternet()) {
					AlertDialogHelper.showNoInternetDialog(getActivity());
					return false;
				}
				createShareIntent();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createEmailIntent() {
		
		Intent itEmail = new Intent();
		itEmail.setAction(Intent.ACTION_VIEW);
		itEmail.setData(Uri.parse("mailto:" + getString(R.string.contact_email)));
		itEmail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
		itEmail.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text));
		try {
			startActivity(Intent.createChooser(itEmail, getString(R.string.choose_app)));
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(getActivity(), 
					getString(R.string.app_not_found), Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	private void createPhoneIntent() {
		Intent itPhone = new Intent();
		itPhone.setAction(Intent.ACTION_CALL);
		String phone = getString(R.string.contact_phone).replace(" ", "");
		Log.i("MyOnClickListener", "Call telephone number: " + phone);
		itPhone.setData(Uri.parse("tel:" + phone));
		startActivity(itPhone);
	}
	
	private void createShareIntent() {
		Intent itemSendShare = new Intent();
		itemSendShare.setAction(Intent.ACTION_SEND);
		itemSendShare.setType("text/plain");
		itemSendShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
		itemSendShare.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
		try {
			startActivity(Intent.createChooser(itemSendShare, getString(R.string.choose_app)));
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(getActivity(), 
					getString(R.string.app_not_found), Toast.LENGTH_SHORT)
					.show();
		}
	}
}
