package com.blueskyconnie.bluestonecrystal;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;

import com.blueskyconnie.bluestonecrystal.adapter.TabspagerAdapter;

public class MainActivity extends FragmentActivity implements
	TabListener {
	

	/**
	 * 
	 */
	private static final int[] TAB_NAMES = { R.string.tab_product, R.string.tab_facebook
		, R.string.tab_map, R.string.tab_contact };

	private static final String[] TAB_TAGS = { "product_tag", "facebook_tag", 
			"map_tag", "contact_tag" };
	
	private static final int[] TAB_ICONS = { -1, R.drawable.facebook_ic
		, R.drawable.location_ic, R.drawable.contact_ic };

	private static final String CURRENT_TAG_KEY = "current_tag";

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
		viewPager.setId(R.id.pager);
		// initializs fragmentpageradapter
		lstFragment.add(new ProductFragment());
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
	protected void onPause() {
		super.onPause();
		// save last visit tab in shared preference
		int current_tab = actionBar.getSelectedNavigationIndex();
		Editor editor = this.getPreferences(MODE_PRIVATE).edit();
		editor.putInt(CURRENT_TAG_KEY, current_tab);
		editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// restore last visit tab from shared preference
		SharedPreferences preferences = this.getPreferences(MODE_PRIVATE);
		int current_tab = preferences.getInt(CURRENT_TAG_KEY, 0);
		actionBar.setSelectedNavigationItem(current_tab);
	}

	@Override
	public void onBackPressed() {
		// prompt confirmation dialog before exit
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_NEGATIVE:   // confirm to exit
						// close dialog and do nothing
						finish();
						break;
					case DialogInterface.BUTTON_POSITIVE:   // cancel
						dialog.dismiss();
						break;
				}
			}
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.title_confirm_exit);
		builder.setPositiveButton(R.string.cancel_exit, listener);
		builder.setNegativeButton(R.string.confirm_exit, listener);
		AlertDialog quitDialog = builder.create();
		quitDialog.show();
	}
}
