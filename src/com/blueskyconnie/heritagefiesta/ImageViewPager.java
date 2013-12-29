package com.blueskyconnie.heritagefiesta;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.blueskyconnie.heritagefiesta.adapter.ImagepagerAdapter;
import com.blueskyconnie.heritagefiesta.transformer.ZoomOutPageTransformer;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageViewPager extends Activity {

	private static final String STATE_POSITION = "STATE_POSITION";
	private static final String STATE_IMAGEURL = "STATE_IMAGEURL";

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ViewPager viewPager;
	private ArrayList<String> imageUrls = null;
	private PagerTitleStrip pageTitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_imagepager);

		int position = 0;
		viewPager = (ViewPager) findViewById(R.id.imgPager);
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		
		pageTitle = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
		
		// 004fff
		pageTitle.setTextColor(Color.rgb(0, 4*15 + 15, 16*15 + 15));
		if (savedInstanceState == null) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			if (getIntent() != null)  {
				imageUrls = getIntent().getStringArrayListExtra("urls");
				Log.i("onCreate", "Initialize viewPager's adapter."); 
			}
		} else {
			position = savedInstanceState.getInt(STATE_POSITION);
			imageUrls = savedInstanceState.getStringArrayList(STATE_IMAGEURL);
			Log.i("onCreate", "Restore position from savedInstanceState " + position); 
			Log.i("onCreate", "Restore imageurls from savedInstanceState " + imageUrls.size()); 
		}
		ImagepagerAdapter adapter = new ImagepagerAdapter(this,
				R.layout.item_pager_image, imageUrls, imageLoader);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);
		Log.i("onCreate", "end of ImageViewPager Activity"); 
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_POSITION, viewPager.getCurrentItem());
		outState.putStringArrayList(STATE_IMAGEURL, imageUrls);
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
