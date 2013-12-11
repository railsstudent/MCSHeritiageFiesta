package com.blueskyconnie.heritagefiesta;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.blueskyconnie.heritagefiesta.adapter.TabspagerAdapter;
import com.blueskyconnie.heritagefiesta.data.Album;
import com.blueskyconnie.heritagefiesta.helper.AlertDialogHelper;

public class MainActivity extends BaseFragmentActivity implements
	TabListener {
	
	public static final String ALBUMS = "albums";
	public static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	/**
	 * 
	 */
	private static final int[] TAB_NAMES = { R.string.tab_introduction, R.string.tab_gallery, 
		 R.string.tab_website, R.string.tab_map, R.string.tab_contact };

	private static final String[] TAB_TAGS = { "intro_tag", "gallery_tag", "website_tag", 
			"map_tag", "contact_tag" };
	
	private static final int[] TAB_ICONS = { R.drawable.ic_brochure, R.drawable.ic_gallery  
		, R.drawable.ic_website, R.drawable.ic_location, R.drawable.ic_contact };

	private ActionBar actionBar;
	private ViewPager viewPager;
	private TabspagerAdapter pageAdapter;
	private List<Fragment> lstFragment = new ArrayList<Fragment>();
	private int currentPosition;
	
	private List<Album> albums;

	@Override
	protected void onCreate(Bundle savedBundle) {
		super.onCreate(savedBundle);
		
		//requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_main);
		actionBar = getActionBar();
		
		// initialize viewpager
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setId(R.id.pager);
		// initialize fragmentpageradapter
		lstFragment.add(new IntroductionFragment());
		lstFragment.add(new GalleryFragment());
		lstFragment.add(new WebViewFragment());
		lstFragment.add(new LocationFragment());
		lstFragment.add(new ContactFragment());
		pageAdapter = new TabspagerAdapter(getSupportFragmentManager(), lstFragment);
		viewPager.setAdapter(pageAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
				// http://stackoverflow.com/questions/10853611/viewpager-with-fragments-onpause-onresume
				FragmentManager fragment_manager = MainActivity.this.getSupportFragmentManager();
				Fragment new_fragment = MainActivity.this.getActiveFragment(fragment_manager, viewPager.getId(), position);
				Fragment old_fragment = MainActivity.this.getActiveFragment(fragment_manager, viewPager.getId(), currentPosition);
				if (old_fragment != null) {
					old_fragment.setUserVisibleHint(false);
					old_fragment.onPause();
				}
				if (new_fragment != null) {
					new_fragment.setUserVisibleHint(true);
					new_fragment.onResume();
				}
				currentPosition = position;
			}
		});
		
		// add tabs
		for (int i = 0; i < TAB_NAMES.length; i++) {
			int tab_id = TAB_NAMES[i];
			int icon_id = TAB_ICONS[i];
			String tab_tag = TAB_TAGS[i];
			Tab tab = actionBar.newTab()
					.setText(this.getString(tab_id))
					.setTag(tab_tag);
			if (icon_id >= 0) {
				tab.setIcon(TAB_ICONS[i]);
			}
			tab.setTabListener(this);
			actionBar.addTab(tab);
		}
		
		if (getIntent() != null) {
			albums = getIntent().getParcelableArrayListExtra(MainActivity.ALBUMS);
		} else {
			albums = new ArrayList<Album>();
		}
	}

	private Fragment getActiveFragment(FragmentManager fragmentManager, int viewPagerId, int position) {
		return fragmentManager.findFragmentByTag("android:switcher:" + viewPagerId + ":" + position);
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
        // show selected fragment view
		viewPager.setCurrentItem(tab.getPosition());
		currentPosition = tab.getPosition();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	} 

	@Override
	public void onBackPressed() {
		AlertDialogHelper.showConfirmExitDialog(this, imageLoader);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, actionBar.getSelectedNavigationIndex());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
				int position = savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM);
				actionBar.setSelectedNavigationItem(position);
			}
		}
	}

//	// http://qtcstation.com/2011/12/fragment-activities-with-multiple-fragments/
//	// force it to portrait
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		Log.i("MainActivity", "onConfigurationChanged begins");
//		if (pageAdapter != null) {
//			Fragment fragment = pageAdapter.getItem(viewPager.getCurrentItem());
//			if(fragment!=null && fragment.isResumed()){
//				//do nothing here if we're showing the fragment
//			}else{
//				setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT); // otherwise lock in portrait
//			}
//		}
//		super.onConfigurationChanged(newConfig);
//		Log.i("MainActivity", "onConfigurationChanged ends");
//	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
}
