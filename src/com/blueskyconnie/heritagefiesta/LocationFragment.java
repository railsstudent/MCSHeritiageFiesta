package com.blueskyconnie.heritagefiesta;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blueskyconnie.heritagefiesta.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class LocationFragment extends Fragment {

	private static final LatLng PRIMARY_SECTION_LATLNG = new LatLng(22.327365,114.179094);
	private static final LatLng MTR_LATLNG = new LatLng(22.325068,114.168403);
	
	private static final int RQS_GooglePlayServices = 1;
	
	private MapView mapView;
	private GoogleMap map;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
		    setRetainInstance(true);
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
	public void onResume() {
		super.onResume();
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if (resultCode == ConnectionResult.SUCCESS) {
			if (mapView != null) {
				map = mapView.getMap();
				if (map != null) {
					// draw a pin to indicate school location
					map.moveCamera(CameraUpdateFactory.newLatLng(PRIMARY_SECTION_LATLNG));
					map.animateCamera(CameraUpdateFactory.zoomTo(14));
					
					map.addMarker(new MarkerOptions().position(PRIMARY_SECTION_LATLNG)
							.title(getString(R.string.map_school_title))
							.snippet(getString(R.string.map_school_address))
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

					map.addMarker(new MarkerOptions().position(MTR_LATLNG)
							.title(getString(R.string.map_mtr_title))
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
					
					// add line
					PolylineOptions lineOptions = new PolylineOptions();
					lineOptions.add(MTR_LATLNG, PRIMARY_SECTION_LATLNG);
					lineOptions.color(Color.RED);
					map.addPolyline(lineOptions);
				}
				mapView.onResume();
			}
		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), RQS_GooglePlayServices).show();
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_location, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_legalnotices) {
			 String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(
				        getActivity());
		     AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(getActivity());
		     LicenseDialog.setTitle(getString(R.string.menu_legalnotices));
		     LicenseDialog.setMessage(LicenseInfo);
		     LicenseDialog.show();
		     return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private final class MyInfoWindowAdapter implements InfoWindowAdapter {
		@Override
		public View getInfoContents(Marker marker) {

			String marker_title = marker.getTitle();
			String marker_snippet = marker.getSnippet();
			if (marker_title.equals(getString(R.string.map_school_title))) {
				View view = getActivity().getLayoutInflater().inflate(R.layout.layout_main_popup, null);
				TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
				TextView tvSnippet = (TextView) view.findViewById(R.id.tvSnippet);
				tvTitle.setText(marker_title);
				tvSnippet.setText(marker_snippet);
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
