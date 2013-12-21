package com.blueskyconnie.heritagefiesta;

import android.content.pm.ActivityInfo;
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
		Log.i("VideoFragment", String.format("onActivityCreated - API Key = %s, Video ID = %s", API_KEY, VIDEO_ID));
		if (showToast) {
			Toast.makeText(getActivity(), String.format("onActivityCreated - API Key = %s, Video ID = %s", API_KEY, VIDEO_ID), 
				Toast.LENGTH_SHORT).show();
		}
		initialize(API_KEY, new YoutubePlayerInitializedListener(getActivity(), VIDEO_ID));
	}

	@Override
	public void initialize(String developerKey, OnInitializedListener listener) {
		super.initialize(developerKey, listener);
		Log.i("VideoFragment", "initialize - API Key = " + API_KEY);
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.i("VideoFragment", "On Pause");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
