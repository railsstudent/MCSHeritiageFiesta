package com.blueskyconnie.heritagefiesta;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseFragmentActivity extends FragmentActivity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
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
			default:
				return false;
		}
	}
}
