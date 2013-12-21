package com.blueskyconnie.heritagefiesta.listener;

import android.app.Activity;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;

public class YoutubePlayerInitializedListener implements OnInitializedListener {

	private static final int RECOVERY_DIALOG_REQUEST = 1;
	
	private Activity activity;
	private String videoId;
	
	public YoutubePlayerInitializedListener(Activity activity, String videoId) {
		this.activity = activity;
		this.videoId = videoId;
	}
	
	public void onInitializationFailure(YouTubePlayer.Provider provider, 
			YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(activity, RECOVERY_DIALOG_REQUEST).show();
		} else {
			Toast.makeText(activity, "Cannot load video", Toast.LENGTH_SHORT).show();
		}
	}

	public void onInitializationSuccess(YouTubePlayer.Provider provider, 
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			player.cueVideo(videoId);
		}
	}
}
