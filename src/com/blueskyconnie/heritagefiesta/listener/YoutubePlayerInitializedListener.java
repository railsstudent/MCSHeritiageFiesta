package com.blueskyconnie.heritagefiesta.listener;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.blueskyconnie.heritagefiesta.helper.ConnectionDetector;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;

public class YoutubePlayerInitializedListener implements OnInitializedListener ,
		YouTubePlayer.OnFullscreenListener {

	private static final int RECOVERY_DIALOG_REQUEST = 1;
	
	private Activity activity;
	private String videoId;
	private ConnectionDetector detector;
	private  boolean showToast;
	
	public YoutubePlayerInitializedListener(Activity activity, String videoId, boolean showToast) {
		this.activity = activity;
		this.videoId = videoId;
		detector = new ConnectionDetector(activity);
		this.showToast = showToast;
	}
	
	public void onInitializationFailure(YouTubePlayer.Provider provider, 
			YouTubeInitializationResult errorReason) {
		if (errorReason != null) {
			if (errorReason.isUserRecoverableError()) {
				errorReason.getErrorDialog(activity, RECOVERY_DIALOG_REQUEST).show();
			} else {
				Toast.makeText(activity, "Cannot load video", Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.i("YoutubePlayerInitializedListener", "errorReason is null");
		}
	}

	public void onInitializationSuccess(YouTubePlayer.Provider provider, 
			YouTubePlayer player, boolean wasRestored) {
		
		//Tell the player you want to control the fullscreen change
		player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		player.setOnFullscreenListener(this);
		
		if (!wasRestored) {
			if (detector.isConnectingToInternet()) {
				if (player != null) {
					player.cueVideo(videoId);
				}
			}
		}
	}
	
	@Override
	public void onFullscreen(boolean fullSize) {
		// does not do anything when device in landscape mode
		if (showToast) {
			Toast.makeText(activity, "fullSize = " + fullSize, Toast.LENGTH_SHORT).show();
		}
	} 
}
