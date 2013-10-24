package com.blueskyconnie.bluestonecrystal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;

@SuppressLint("SetJavaScriptEnabled")
public class FacebookFragment extends Fragment implements OnKeyListener {

	private WebView webView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_facebook, container, false);
		webView = (WebView) rootView.findViewById(R.id.wvFacbook);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.invokeZoomPicker();
		// set initial scale to 80%
		webView.setInitialScale(80);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(FacebookFragment.this.getActivity(), 
						"Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
		});
		
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				getActivity().setProgress(newProgress * 100);
			}
		});
		webView.setOnKeyListener(this);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Toast.makeText(getActivity(), "FacebookFragment OnResume", Toast.LENGTH_SHORT).show();
		ConnectionDetector detector = new ConnectionDetector(getActivity());
		if (detector.isConnectingToInternet()) {
			if (webView != null) {
				webView.loadUrl(getString(R.string.facebook_url));
			}		
		} //else {
//			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//			builder.setTitle(getString(R.string.info_title));
//			builder.setIcon(android.R.drawable.ic_dialog_alert);
//			builder.setMessage(getString(R.string.no_internect_error));
//			builder.setNeutralButton(getString(R.string.confirm_exit), new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//				}
//			});
//			AlertDialog alertDialog = builder.create();
//			alertDialog.show();
//		}
	}
	
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			WebView webView = (WebView) view;
			if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
				webView.goBack();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (webView != null) {
			Toast.makeText(getActivity(), "FaceFragment onpause", Toast.LENGTH_SHORT).show();
			webView.onPause();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		Toast.makeText(getActivity(), "FaceFragment OnStop", Toast.LENGTH_SHORT).show();
	}
	
	
	
}
