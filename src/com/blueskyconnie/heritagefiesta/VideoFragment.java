package com.blueskyconnie.heritagefiesta;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.blueskyconnie.heritagefiesta.listener.YoutubePlayerInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class VideoFragment extends YouTubePlayerSupportFragment   {
	
	private static final String API_KEY = "AIzaSyASS3OSaZyNQGs0ndEtIgutYKTac1GIi_M";
	private static final String VIDEO_ID = "f6mV9FK3RF0";
	private boolean showToast = false;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.setRetainInstance(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		try {
			Log.i("VideoFragment", String.format("onActivityCreated - API Key = %s, Video ID = %s", API_KEY, VIDEO_ID));
			if (showToast) {
				Toast.makeText(getActivity(), String.format("onActivityCreated - API Key = %s, Video ID = %s", API_KEY, VIDEO_ID), 
					Toast.LENGTH_SHORT).show();
			}
			Activity activity = getActivity();
			if (activity != null) {
				initialize(API_KEY, new YoutubePlayerInitializedListener(activity, VIDEO_ID, showToast));
			} else {
				Log.i("VideoFragment", "onActivityCreated - activity is null.");
			}
		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void initialize(String developerKey, OnInitializedListener listener) {
		super.initialize(developerKey, listener);
		Log.i("VideoFragment", "initialize - API Key = " + API_KEY);
	}
}
