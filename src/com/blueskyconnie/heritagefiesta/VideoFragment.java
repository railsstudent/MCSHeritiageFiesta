package com.blueskyconnie.heritagefiesta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class VideoFragment extends YouTubePlayerSupportFragment {

	@Override
	public void initialize(String developerKey, OnInitializedListener listener) {
		super.initialize(developerKey, listener);
	}

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	}

	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		return super.onCreateView(arg0, arg1, arg2);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
	}

	
	
}
