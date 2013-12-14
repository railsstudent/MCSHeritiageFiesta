package com.blueskyconnie.heritagefiesta;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.blueskyconnie.heritagefiesta.helper.AlertDialogHelper;
import com.blueskyconnie.heritagefiesta.helper.ConnectionDetector;

public class GalleryFragment extends Fragment {
	
	public static final int GALLERY_REQUEST_CODE = 1;
	
	private GridView gridView ;
	private boolean hasClickedItem = false;
	private List<String> categories = new ArrayList<String>();
	private List<Integer> lstCatId = new ArrayList<Integer>();
	private List<Album> lstAlbum = new ArrayList<Album>();
	private SparseArray<List<String>> categoryUrlMap = new SparseArray<List<String>>();
	
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			
			ConnectionDetector detector = new ConnectionDetector(GalleryFragment.this.getActivity());
			if (!detector.isConnectingToInternet()) {
				AlertDialogHelper.showNoInternetDialog(getActivity());
			} else {
				if (!hasClickedItem) {
					if (categories.size() > position) {
						int category = lstCatId.get(position).intValue();
						List<String> imageUrls = categoryUrlMap.get(category);
						ArrayList<String> alImageUrls = new ArrayList<String> (imageUrls);
						
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
		MainActivity activity = (MainActivity) getActivity();
		lstAlbum  = activity.getAlbums();
		for (Album album : lstAlbum) {
			categoryUrlMap.put(album.getCategoryId(), album.getImageUrl());
			categories.add(album.getCategory());
			lstCatId.add(album.getCategoryId());
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
		AlbumGridAdapter adapter = new AlbumGridAdapter(getActivity(), R.layout.album_row_grid, categories);
		gridView = (GridView) rootView.findViewById(R.id.gridView);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(listener);
		
		return rootView;
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
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
