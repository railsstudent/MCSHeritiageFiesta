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

@SuppressLint("SetJavaScriptEnabled")
public class FacebookFragment extends Fragment implements OnKeyListener {

	private WebView webView;
	
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
		
		webView.loadUrl(this.getString(R.string.facebook_url));
		return rootView;
	}
	
//	public boolean customOnKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (webView.canGoBack()) {
//				webView.goBack();
//				return true;
//			} else {
//				// From stackoverflow, move the activity down the activity stack()
//				getActivity().finish();
//				return true;
//			}
//		}
//		return getActivity().onKeyDown(keyCode, event);
//	}

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
}
