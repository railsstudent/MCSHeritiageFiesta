package com.blueskyconnie.heritagefiesta;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.blueskyconnie.heritagefiesta.data.Album;
import com.blueskyconnie.heritagefiesta.helper.ConnectionDetector;

public class SplashActivity extends Activity {
	
	private static class SplashHandler extends Handler {

		private final WeakReference<SplashActivity> activity;
		
		public SplashHandler(SplashActivity activity) {
			this.activity = new WeakReference<SplashActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (activity.get() != null) {
				Bundle bundle = msg.getData();
				ArrayList<Album> albums = new ArrayList<Album>();
				if (bundle != null) {
					albums = bundle.getParcelableArrayList("albums");
				}
				Intent intent = new Intent(activity.get(), MainActivity.class);
				intent.putParcelableArrayListExtra("albums", albums);
				activity.get().startActivity(intent);
				activity.get().finish();
			}
		}
	}
	
	private SplashHandler mHandlerPreload;
	private String cms_url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			ConnectionDetector detector = new ConnectionDetector(this);
			if (!detector.isConnectingToInternet()) {
				setContentView(R.layout.activity_splash);
				showNoInternetDialogStartActivity(this);
			} else {
				setContentView(R.layout.activity_splash);
				cms_url = getString(R.string.cms_url);
				mHandlerPreload = new SplashHandler(this); 	
			
				AnimationDrawable anim = (AnimationDrawable) 
						getResources().getDrawable(R.drawable.anim_activity_main_preload);
	
				ImageView mImgView = (ImageView) findViewById(R.id.imgSplash);
				mImgView. setImageDrawable(anim);
				anim.start();
				
				new Thread(new MyRunnable(cms_url, mHandlerPreload, this)).start();
			}
		}
	}
	
	private static class MyRunnable implements Runnable {

		private WeakReference<SplashActivity> activity;
		private SplashHandler handler;
		private String cms_url;
		
		MyRunnable (String cms_url, SplashHandler handler, SplashActivity splashActivity) {
			this.cms_url = cms_url;
			this.handler = handler;
			this.activity = new WeakReference<SplashActivity>(splashActivity);
		}
		
		private Album convertAlbum(JSONObject jsObj) throws JSONException {

			int categoryId = jsObj.getInt("categoryId");
			String category = jsObj.getString("category");
			//String chiCategory = new String(category.getBytes("ISO-8859-1"));
			ArrayList<String> alUrl = new ArrayList<String>();
			JSONArray jsonUrls = jsObj.getJSONArray("urls");
			for (int j = 0; j < jsonUrls.length(); j++) {
				alUrl.add(cms_url + jsonUrls.getString(j));
			}
			Album album = new Album();
			album.setCategoryId(categoryId);
			album.setCategory(category);
			//album.setCategory(chiCategory);
			album.setImageUrl(alUrl);
			return album;
		}
		
		@Override
		public void run() {
		   	String categoryUrl = cms_url + "albums_android.php";
			
		   	// make requests to retrieve albums
			JsonObjectRequest jsonRequest = new JsonObjectRequest(Method.GET, categoryUrl, null,
		   			new Response.Listener<JSONObject>() {
		   			 
						@Override
						public void onResponse(JSONObject response) {
							ArrayList<Album> albums = new ArrayList<Album>();
							try {
								JSONArray myArray = response.getJSONArray("mapping");
								for (int i = 0; i < myArray.length(); i++) {
									// convert json object to an album bean 
									try {
										JSONObject jsObj = myArray.getJSONObject(i);
										albums.add(convertAlbum(jsObj));
									} catch (JSONException e1) {
										e1.printStackTrace();
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							Message msg = handler.obtainMessage();
							Bundle data = new Bundle();
							data.putParcelableArrayList(MainActivity.ALBUMS, albums);
							msg.setData(data);
							handler.sendMessage(msg);
						}
					}, 
		   			new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(activity.get(), activity.get().getString(R.string.http_request_error),
									Toast.LENGTH_SHORT).show();
							Message msg = handler.obtainMessage();
							Bundle data = new Bundle();
							data.putParcelableArrayList(MainActivity.ALBUMS, new ArrayList<Album>());
							msg.setData(data);
							handler.sendMessage(msg);
						}
					}
		   	);	
			RequestManager.getRequestQueue().add(jsonRequest);
		}
	}
	
	private void showNoInternetDialogStartActivity(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.info_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(context.getString(R.string.no_internet_error));
		builder.setNeutralButton(context.getString(R.string.confirm_exit), 
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					// close the app. mod by Connie Leung, 2014-03-08 begins
//					ArrayList<Album> albums = new ArrayList<Album>();
//					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//					intent.putParcelableArrayListExtra(MainActivity.ALBUMS, albums);
//					startActivity(intent);
					// end 2014-03-08
					finish();
				}
			});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
}
