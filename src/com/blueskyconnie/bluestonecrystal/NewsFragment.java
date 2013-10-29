package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blueskyconnie.bluestonecrystal.adapter.NewsAdapter;
import com.blueskyconnie.bluestonecrystal.adapter.ProductAdapter;
import com.blueskyconnie.bluestonecrystal.data.News;
import com.blueskyconnie.bluestonecrystal.helper.AlertDialogHelper;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;

public class NewsFragment extends ListFragment {

	private ProgressDialog dialog;
	private WeakReference<RetrieveNewsTask> asyncTaskWeakRef;
	private String cmsUrl;
	private TextView tvUpdateTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);
		tvUpdateTime = (TextView) rootView.findViewById(R.id.tvNewsLastUpdate);
		
		ImageButton imgBtnRefresh = (ImageButton) rootView.findViewById(R.id.imgRefreshNews);
		imgBtnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ConnectionDetector detector = new ConnectionDetector(getActivity());
				if (detector.isConnectingToInternet()) {
					startNewAsyncTask(tvUpdateTime);
				} else {
					AlertDialogHelper.showNoInternetDialog(getActivity());
				}
			}
		});
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dialog = new ProgressDialog(getActivity()); 
		cmsUrl = getString(R.string.cms_url);
		
		MainActivity activity = (MainActivity) this.getActivity();
		List<News> lstNews = activity.getLstNews();
		NewsAdapter adapter = new NewsAdapter(getActivity(),
				R.layout.product_row_layout, lstNews);
		getListView().setAdapter(adapter);
		tvUpdateTime.setText(MainActivity.sdf.format(new Date(activity.getLastNewsUpdateTime())));
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
		RetrieveNewsTask asyncTask = new RetrieveNewsTask(this, tvUpdateTime);
	    asyncTaskWeakRef = new WeakReference<RetrieveNewsTask >(asyncTask);
	    asyncTaskWeakRef.get().execute(cmsUrl + "news_android.php?id=" + MainActivity.SHOP_ID);
	}

	private class RetrieveNewsTask extends AsyncTask<String, Void, List<News>> {

		private WeakReference<NewsFragment> fragmentWeakRef;
		private WeakReference<TextView> tvLastUpdate;
		
		private RetrieveNewsTask(NewsFragment fragment, TextView lblUpdate) {
			fragmentWeakRef = new WeakReference<NewsFragment>(fragment);
            tvLastUpdate = new WeakReference<TextView>(lblUpdate);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dialog != null) {
				dialog.setMessage(fragmentWeakRef.get().getString(R.string.dowloading));
				dialog.show();
			}
		}
		
		@Override
		protected List<News> doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(List<News> lstNews) {
			super.onPostExecute(lstNews);
			if (fragmentWeakRef.get() != null) {
				MainActivity activity = (MainActivity) fragmentWeakRef.get().getActivity();
				NewsAdapter adapter = new NewsAdapter(activity, R.layout.product_row_layout, lstNews);
				// update list adapter
				fragmentWeakRef.get().setListAdapter(adapter);
				// display current time
				if (tvLastUpdate.get() != null) {
					long curTime = Calendar.getInstance().getTimeInMillis();
					tvLastUpdate.get().setText(MainActivity.sdf.format(new Date(curTime)));
					activity.setLastNewsUpdateTime(curTime);
				}
				activity.setLstNews(lstNews);
			}
			
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}
}
