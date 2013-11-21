package com.blueskyconnie.heritagefiesta;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

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
				Intent intent = new Intent(activity.get(), MainActivity.class);
				activity.get().startActivity(intent);
				activity.get().finish();
			}
		}
	}
	
	private SplashHandler mHandlerPreload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		mHandlerPreload = new SplashHandler(this); 	
		
		AnimationDrawable anim = (AnimationDrawable) 
				getResources().getDrawable(R.drawable.anim_activity_main_preload);

		ImageView mImgView = (ImageView) findViewById(R.id.imgSplash);
		mImgView. setImageDrawable(anim);
		anim.start();

		new Thread(new Runnable() {
			
			private WeakReference<SplashActivity> activity;
			
			public void run() {
				
				activity  = new WeakReference<SplashActivity>(SplashActivity.this);
				
				String cms_url = activity.get().getString(R.string.cms_url);
				String categoryUrl = cms_url + "getCategoryImages.php";
				
				Calendar begin = Calendar.getInstance();
			   	int iDiffSec;
			   	
			   	RequestQueue reqQueue = Volley.newRequestQueue(activity.get());
			   	
			   	// make requests to retrieve categories
			   	// loop each category to retrieve the corresponding image url
			   	JsonArrayRequest jsonRequest = new JsonArrayRequest(categoryUrl, 
			   			new Response.Listener<JSONArray>() {
			   			 
				   	    // expect result
		   		        /* {
						    "mapping": [
						        {
						            "category": "outdoor",
						            "urls": [
						                "pic1",
						                "pic2",
						                "pic3"
						            ]
						        },
						        {
						            "category": "artroom",
						            "urls": [
						                "pic1",
						                "pic2",
						                "pic3"
						            ]
						        }
						    ]
						 } */
							@Override
							public void onResponse(JSONArray response) {
								for (int i = 0; i < response.length(); i++) {
									try {
										JSONObject jsObj = response.getJSONObject(i);
										// convert json object to a bean 
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
								Message msg = mHandlerPreload.obtainMessage();
								Bundle data = new Bundle();
								// TODO set data to message
								msg.setData(data);
								mHandlerPreload.sendMessage(msg);
							}
						}, 
			   			new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								mHandlerPreload.sendMessage(mHandlerPreload.obtainMessage());
							}
						}
			   	);	
			   	reqQueue.add(jsonRequest);
			}
		}).start();
	}
}
