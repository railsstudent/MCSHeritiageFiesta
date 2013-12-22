package com.blueskyconnie.heritagefiesta;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
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
	private static final LatLng MTR_KOWLOON_TONG_LATLNG = new LatLng(22.337265,114.175928);
	private static final LatLng MTR_BUS1_LATLNG = new LatLng(22.327244,114.182686);
	private static final LatLng MTR_BUS2_LATLNG = new LatLng(22.32716,114.180594);
	
	private static final LatLng[] KOWLOON_TONG_ROUTE = {
		MTR_KOWLOON_TONG_LATLNG
		, new LatLng(22.337295,114.176442)
		, new LatLng(22.33793,114.176592)
		, new LatLng(22.33783,114.178931)
		, new LatLng(22.338088,114.179038)
		, new LatLng(22.338029,114.179274)
		, new LatLng(22.330209,114.178738)
		, PRIMARY_SECTION_LATLNG
	};
	
	private static final int RQS_GooglePlayServices = 1;

	private static final String PRINCE_EDWARD_MTR = "0";
	private static final String KOWLOON_TONG_MTR = "1";
	
	private MapView mapView;
	private GoogleMap map;	
	private Map<String, String[]> hmSnippet = new HashMap<String, String[]>();		

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
		    setRetainInstance(true);
			MapsInitializer.initialize(this.getActivity());
			setHasOptionsMenu(true);
			
			String[] mtrArray =  { 
					getString(R.string.map_mtr_snippet_first),  
					getString(R.string.map_mtr_snippet_second)	
			};
			hmSnippet.put(PRINCE_EDWARD_MTR, mtrArray);
			
			mtrArray =  new String[2];
			mtrArray[0] = getString(R.string.map_mtr_kt_snippet_first);
			mtrArray[1] = getString(R.string.map_mtr_kt_snippet_second);
			hmSnippet.put(KOWLOON_TONG_MTR, mtrArray);
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
							.snippet(PRINCE_EDWARD_MTR)
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

					map.addMarker(new MarkerOptions().position(MTR_BUS1_LATLNG)
							.title(getString(R.string.map_bus1_title))
							.snippet(getString(R.string.map_bus1_snippet))
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

					map.addMarker(new MarkerOptions().position(MTR_BUS2_LATLNG)
							.title(getString(R.string.map_bus2_title))
							.snippet(getString(R.string.map_bus2_snippet))
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

					map.addMarker(new MarkerOptions().position(MTR_KOWLOON_TONG_LATLNG)
							.title(getString(R.string.map_mtr_kowloontong_title))
							.snippet(KOWLOON_TONG_MTR)
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

					PolylineOptions kowloonTongLineOptions = new PolylineOptions();
					kowloonTongLineOptions.color(Color.RED);
					for (LatLng value : KOWLOON_TONG_ROUTE) {
						kowloonTongLineOptions.add(value);
					}
					map.addPolyline(kowloonTongLineOptions);
				}
				mapView.onResume();
			}
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
		switch (item.getItemId()) {
			case R.id.menu_legalnotices:
				 String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(getActivity());
			     AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(getActivity());
			     LicenseDialog.setTitle(getString(R.string.menu_legalnotices));
			     LicenseDialog.setMessage(LicenseInfo);
			     LicenseDialog.show();
			     return true;
			case R.id.menu_backtoschool:
				map.moveCamera(CameraUpdateFactory.newLatLng(PRIMARY_SECTION_LATLNG));
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
			} else if (hmSnippet.containsKey(marker_snippet)) {
				View view = getActivity().getLayoutInflater().inflate(R.layout.layout_multi_transport_method_popup, null);
				TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
				TextView tvSnippet1 = (TextView) view.findViewById(R.id.tvSnippet1);
				TextView tvSnippet2 = (TextView) view.findViewById(R.id.tvSnippet2);
				tvTitle.setText(marker_title);
				String[] strArray = hmSnippet.get(marker_snippet);
 				tvSnippet1.setText(strArray[0]);
				tvSnippet2.setText(strArray[1]);
				return view;
			} else {
				View view = getActivity().getLayoutInflater().inflate(R.layout.layout_simple_transport_popup, null);
				TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
				TextView tvSnippet = (TextView) view.findViewById(R.id.tvSnippet);
				tvTitle.setText(marker_title);
				tvSnippet.setText(marker_snippet);
				return view;
			}
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
	}
}
