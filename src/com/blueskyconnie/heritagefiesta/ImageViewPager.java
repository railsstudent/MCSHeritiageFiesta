package com.blueskyconnie.heritagefiesta;

import java.util.ArrayList;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.blueskyconnie.heritagefiesta.adapter.ImagepagerAdapter;
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
		viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {

			private static final float MIN_SCALE = 0.85f;
		    private static final float MIN_ALPHA = 0.5f;
			
		    @Override
			public void transformPage(View view, float position) {
		    	int pageWidth = view.getWidth();
		        int pageHeight = view.getHeight();

		        if (position < -1) { // [-Infinity,-1)
		            // This page is way off-screen to the left.
		            view.setAlpha(0);

		        } else if (position <= 1) { // [-1,1]
		            // Modify the default slide transition to shrink the page as well
		            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
		            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
		            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
		            if (position < 0) {
		                view.setTranslationX(horzMargin - vertMargin / 2);
		            } else {
		                view.setTranslationX(-horzMargin + vertMargin / 2);
		            }

		            // Scale the page down (between MIN_SCALE and 1)
		            view.setScaleX(scaleFactor);
		            view.setScaleY(scaleFactor);

		            // Fade the page relative to its size.
		            view.setAlpha(MIN_ALPHA +
		                    (scaleFactor - MIN_SCALE) /
		                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

		        } else { // (1,+Infinity]
		            // This page is way off-screen to the right.
		            view.setAlpha(0);
		        }
			}
		});
		
		pageTitle = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
		
		// 004fff
		pageTitle.setTextColor(Color.rgb(0, 4*15 + 15, 16*15 + 15));
		if (savedInstanceState == null) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
