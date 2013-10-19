package com.blueskyconnie.bluestonecrystal;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Window;

import com.blueskyconnie.bluestonecrystal.adapter.TabspagerAdapter;

public class MainActivity extends FragmentActivity implements
	TabListener {
	
	/**
	 * 
	 */
	private static final int[] TAB_NAMES = { R.string.tab_product, R.string.tab_news, 
		R.string.tab_facebook, R.string.tab_map, R.string.tab_contact };

	private static final int[] TAB_ICONS = { -1, -1, R.drawable.facebook_ic, R.drawable.location_ic, 
				R.drawable.contact_ic };

	private ActionBar actionBar;
	private ViewPager viewPager;
	private TabspagerAdapter pageAdapter;
	private List<Fragment> lstFragment = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedBundle) {
		super.onCreate(savedBundle);
		
		requestWindowFeature(Window.FEATURE_PROGRESS);
		
		setContentView(R.layout.activity_main);
		actionBar = this.getActionBar();
		
		// initialize viewpager
		viewPager = (ViewPager) findViewById(R.id.pager);
		// initializs fragmentpageradapter
		lstFragment.add(new ProductFragment());
		lstFragment.add(new NewsFragment());
		lstFragment.add(new FacebookFragment());
		lstFragment.add(new StoreMapFragment());
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
			}
			
		});
		
		// add tabs
		for (int i = 0; i < TAB_NAMES.length; i++) {
			int tab_id = TAB_NAMES[i];
			int icon_id = TAB_ICONS[i];
			Tab tab = actionBar.newTab().setText(this.getString(tab_id));
			if (icon_id >= 0) {
				tab.setIcon(TAB_ICONS[i]);
			}
			tab.setTabListener(this);
			actionBar.addTab(tab);
		}
		
		actionBar.getTabAt(2).setIcon(R.drawable.facebook_ic);
		actionBar.getTabAt(3).setIcon(R.drawable.location_ic);
		actionBar.getTabAt(4).setIcon(R.drawable.location_ic);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
        // show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Fragment currFragment = lstFragment.get(viewPager.getCurrentItem());
			if (currFragment instanceof FacebookFragment) {
				return ((FacebookFragment) currFragment).customOnKeyDown(keyCode, event);
			}
	    }
		return super.onKeyDown(keyCode, event);
	}
}
