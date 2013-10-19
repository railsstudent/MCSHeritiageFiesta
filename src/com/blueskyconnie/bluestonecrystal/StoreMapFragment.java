package com.blueskyconnie.bluestonecrystal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class StoreMapFragment extends Fragment {

	private static final LatLng SHOP_LATLNG = new LatLng(22.299132,114.173793);
	private static final int RQS_GooglePlayServices = 1;
	
	private MapView mapView;
	private GoogleMap map;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
		    MapsInitializer.initialize(this.getActivity());
 			setHasOptionsMenu(true);
		 } catch (GooglePlayServicesNotAvailableException e) {
		     e.printStackTrace();
		 }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_map, container, false);
		mapView = (MapView) view.findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mapView != null) {
			mapView.onDestroy();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (mapView != null) {
			mapView.onLowMemory();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mapView != null) {
			mapView.onPause();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

		if (resultCode == ConnectionResult.SUCCESS) {
			if (mapView != null) {
				map = mapView.getMap();
				if (map != null) {
					// draw a pin to indicate shop location
					map.moveCamera(CameraUpdateFactory.newLatLng(SHOP_LATLNG));
					map.animateCamera(CameraUpdateFactory.zoomTo(17));
					map.addMarker(new MarkerOptions().position(SHOP_LATLNG)
							.title(getString(R.string.map_shop_title))
							.snippet(getString(R.string.map_shop_address))
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
					map.setMyLocationEnabled(true);
					map.setOnMarkerClickListener(new OnMarkerClickListener() {
						@Override
						public boolean onMarkerClick(Marker marker) {
							marker.showInfoWindow();
							return false;
						}
					});
					
					map.setInfoWindowAdapter(new MyInfoWindowAdapter());
					map.setOnMapClickListener(new OnMapClickListener() {
						public void onMapClick(LatLng latLng) {
							map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
						}
					});
				}
				mapView.onResume();
			}
		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), RQS_GooglePlayServices).show();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_shop, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.item_shop) {
			map.moveCamera(CameraUpdateFactory.newLatLng(SHOP_LATLNG));
		}
		return super.onOptionsItemSelected(item);
	}
	
	private final class MyInfoWindowAdapter implements InfoWindowAdapter {
		@Override
		public View getInfoContents(Marker marker) {

			String marker_title = marker.getTitle();
			String marker_snippet = marker.getSnippet();
			if (marker_title.equals(getString(R.string.map_shop_title))) {
				View view = getActivity().getLayoutInflater().inflate(R.layout.layout_main_popup, null);
				ImageView imgView = (ImageView) view.findViewById(R.id.imgShop);
				TextView tvBuilding = (TextView) view.findViewById(R.id.tvBuilding);
				TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
				TextView tvSnippet = (TextView) view.findViewById(R.id.tvSnippet);

				imgView.setImageResource(R.drawable.solo_building2);
				tvTitle.setText(marker_title);
				tvSnippet.setText(marker_snippet);
				tvBuilding.setText(StoreMapFragment.this.getActivity().getString(R.string.map_shop_building));
				return view;
			} 
			return null;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
	}
}
