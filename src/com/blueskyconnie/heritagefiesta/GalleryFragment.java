package com.blueskyconnie.heritagefiesta;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.blueskyconnie.heritagefiesta.adapter.AlbumGridAdapter;
import com.blueskyconnie.heritagefiesta.data.Album;
import com.blueskyconnie.heritagefiesta.data.GalleryItem;
import com.blueskyconnie.heritagefiesta.helper.AlertDialogHelper;
import com.blueskyconnie.heritagefiesta.helper.ConnectionDetector;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class GalleryFragment extends Fragment {
	
	public static final int GALLERY_REQUEST_CODE = 1;
	
	private GridView gridView ;
	private boolean hasClickedItem = false;
	private SparseArray<String> sparseCategories = null;
	private List<String> categories = new ArrayList<String>();
	private List<Integer> lstCatId = new ArrayList<Integer>();
	private List<Album> lstAlbum = new ArrayList<Album>();
	private SparseArray<List<String>> categoryUrlMap = new SparseArray<List<String>>();
	private AdView adView;
	private TypedArray albumIcons;
	private List<GalleryItem> lstGalleryItem;
	
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			
			ConnectionDetector detector = new ConnectionDetector(GalleryFragment.this.getActivity());
			if (!detector.isConnectingToInternet()) {
				AlertDialogHelper.showNoInternetDialog(getActivity());
			} else {
				if (!hasClickedItem) {
					if (categories.size() > position && position < lstCatId.size()) {
						int categoryId = lstCatId.get(position).intValue();
						List<String> imageUrls = categoryUrlMap.get(categoryId);
						ArrayList<String> alImageUrls = new ArrayList<String> (imageUrls);
						hasClickedItem = true;
							
						Intent intent = new Intent(GalleryFragment.this.getActivity(), ImageViewPager.class);
						intent.putStringArrayListExtra("urls", alImageUrls);
						startActivityForResult(intent, GALLERY_REQUEST_CODE);
					} else {
						Toast.makeText(GalleryFragment.this.getActivity(), 
								getString(R.string.no_album_error), Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		sparseCategories = parseStringArray(R.array.category);
		categories.clear();
		for (int i = 0; i < sparseCategories.size(); i++) {
			int key = sparseCategories.keyAt(i);
			String category = sparseCategories.get(key, "");
			categories.add(category);
		}
		
		lstGalleryItem = new ArrayList<GalleryItem>();
		albumIcons = getResources().obtainTypedArray(R.array.gallery_icons);
		for (int i = 0; i < categories.size(); i++) {
			lstGalleryItem.add(new GalleryItem(categories.get(i), albumIcons.getResourceId(i, -1)));
		}
		albumIcons.recycle();
		
		View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
//		AlbumGridAdapter adapter = new AlbumGridAdapter(getActivity(), R.layout.album_row_grid, categories);
		AlbumGridAdapter adapter = new AlbumGridAdapter(getActivity(), R.layout.album_row_grid, lstGalleryItem);
		gridView = (GridView) rootView.findViewById(R.id.gridView);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(listener);

		// init configuration of adview
		adView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
								.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
								.build();
//								.addTestDevice("3BE2084011B4A10A")
		adView.loadAd(adRequest);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MainActivity activity = (MainActivity) getActivity();
		lstAlbum  = activity.getAlbums();
		for (Album album : lstAlbum) {
			categoryUrlMap.put(album.getCategoryId(), album.getImageUrl());
			lstCatId.add(album.getCategoryId());
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GALLERY_REQUEST_CODE) {
			hasClickedItem = false;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
//		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		adView.resume();
	}

	@Override
	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		adView.pause();
	}
	
	private SparseArray<String> parseStringArray(int stringArrayResourceId) {
	    String[] stringArray = getResources().getStringArray(stringArrayResourceId);
	    SparseArray<String> outputArray = new SparseArray<String>(stringArray.length);
	    for (String entry : stringArray) {
	        String[] splitResult = entry.split(":", 2);
	        outputArray.put(Integer.valueOf(splitResult[0]), splitResult[1]);
	    }
	    return outputArray;
	}
}
