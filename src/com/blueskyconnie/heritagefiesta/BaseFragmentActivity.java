package com.blueskyconnie.heritagefiesta;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseFragmentActivity extends FragmentActivity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.item_clear_memory_cache:
				imageLoader.clearMemoryCache();
				Log.i("BaseActivity", "Clear memory cache of image loader.");
				return true;
			case R.id.item_clear_disc_cache:
				imageLoader.clearDiscCache();
				Log.i("BaseActivity", "Clear disc cache of image loader.");
				return true;
			case R.id.item_rate_myapp:
				// Show rate me in google play store
				Intent intent = new Intent(Intent.ACTION_VIEW, 
						Uri.parse("market://details?id=" + getPackageName()));
				startActivity(intent);
				Log.i("BaseActivity", "Show Rate Me section of Google Play Store.");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
