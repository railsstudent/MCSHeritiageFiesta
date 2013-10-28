package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blueskyconnie.bluestonecrystal.helper.AlertDialogHelper;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;

public class NewsFragment extends Fragment {

	private ProgressDialog dialog;
	private WeakReference<RetrieveNewsTask> asyncTaskWeakRef;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dialog = new ProgressDialog(getActivity()); 
		ConnectionDetector detector = new ConnectionDetector(getActivity());
		if (detector.isConnectingToInternet()) {
			//startNewAsyncTask(tvUpdateTime);
		} else {
			AlertDialogHelper.showNoInternetDialog(getActivity());
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}
	
	
	private void startNewAsyncTask(TextView tvUpdateTime) {
		RetrieveNewsTask asyncTask = new RetrieveNewsTask(this);
	    this.asyncTaskWeakRef = new WeakReference<RetrieveNewsTask >(asyncTask);
	    //asyncTaskWeakRef.get().execute(cmsUrl + "products_android.php?id=1");
	}

	private class RetrieveNewsTask extends AsyncTask<String, Void, Void> {

		private WeakReference<NewsFragment> fragmentWeakRef;
		
		private RetrieveNewsTask(NewsFragment fragment) {
			fragmentWeakRef = new WeakReference<NewsFragment>(fragment);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dialog != null) {
				dialog.show();
			}
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (fragmentWeakRef.get() != null) {
				
			}
			
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}
}
