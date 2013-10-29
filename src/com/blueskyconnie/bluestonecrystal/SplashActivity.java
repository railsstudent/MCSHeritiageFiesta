package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.blueskyconnie.bluestonecrystal.data.BatchData;
import com.blueskyconnie.bluestonecrystal.data.News;
import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.exception.BusinessException;
import com.blueskyconnie.bluestonecrystal.helper.AlertDialogHelper;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;
import com.blueskyconnie.bluestonecrystal.helper.HttpClientHelper;

public class SplashActivity extends Activity {

//	private static final int SPLASH_TIMEOUT = 1000;
	
	private WeakReference<LoadDataTask> asyncTaskWeakRef;
	private String cmsUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_splash);

		// create an async task to load data
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		}, SPLASH_TIMEOUT);

		cmsUrl = getString(R.string.cms_url);
		ConnectionDetector detector = new ConnectionDetector(this);
		if (detector.isConnectingToInternet()) {
  		    startNewAsyncTask();
		} else {
			AlertDialogHelper.showNoInternetDialog(this);
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	private void startNewAsyncTask() {
		LoadDataTask asyncTask = new LoadDataTask(this);
	    this.asyncTaskWeakRef = new WeakReference<LoadDataTask >(asyncTask);
	    String[] urls = { cmsUrl + "products_android.php?id=" + MainActivity.SHOP_ID, 
	    		cmsUrl + "news_android.php?id=" + MainActivity.SHOP_ID,
	    		cmsUrl + "shopInfo_android.php?id=" + MainActivity.SHOP_ID };
	    asyncTaskWeakRef.get().execute(urls);
	}

	private class LoadDataTask extends AsyncTask<String, Void, BatchData> {

		private WeakReference<SplashActivity> activityWeakRef;
		private LoadDataTask(SplashActivity activity) {
			this.activityWeakRef = new WeakReference<SplashActivity>(activity);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			View splash_view = View.inflate(SplashActivity.this, R.layout.activity_splash, null);
			setContentView(splash_view);
		}

		@Override
		protected BatchData doInBackground(String... params) {
			 /* This is just a code that delays the thread execution 4 times, 
             * during 850 milliseconds and updates the current progress. This 
             * is where the code that is going to be executed on a background 
             * thread must be placed. 
             */  
			String productUrl = params[0];
			String newsUrl = params[1];
		//	String shopUrl = params[2];

			BatchData data = new BatchData();
			Calendar cal = Calendar.getInstance();
			try {
				List<Product> lstProduct = HttpClientHelper.retrieveProducts(productUrl, cmsUrl);
				data.setLstProduct(lstProduct);
				data.setLastProductUpdateTime(cal.getTimeInMillis());
				
				List<News> lstNews = HttpClientHelper.retrieveNews(newsUrl);
				data.setLstNews(lstNews);
				data.setLastNewsUpdateTime(cal.getTimeInMillis());

				//	List<Product> lstProduct = HttpClientHelper.retrieveProducts(productUrl, cmsUrl);

				return data;
			} catch (BusinessException ex) {
				ex.printStackTrace();
			}
			
			data.setLstNews(new ArrayList<News>());
			data.setLstProduct(new ArrayList<Product>());
            return data;  
		}

		@Override
		protected void onPostExecute(BatchData result) {
			super.onPostExecute(result);
			Intent intent = new Intent(activityWeakRef.get(), MainActivity.class);
			intent.putExtra(MainActivity.BATCH_DATA, result);
			SplashActivity.this.startActivity(intent);
			finish();
		}
	}
	
}
