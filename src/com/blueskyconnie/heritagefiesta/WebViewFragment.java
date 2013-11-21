package com.blueskyconnie.heritagefiesta;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.blueskyconnie.heritagefiesta.helper.AlertDialogHelper;
import com.blueskyconnie.heritagefiesta.helper.ConnectionDetector;

public class WebViewFragment extends Fragment {

	private WebView webView;
	private Bundle webViewBundle;

	// http://www.lucazanini.eu/2013/android/how-to-save-the-state-of-a-webview-inside-a-fragment-of-an-action-bar/?lang=en

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
		webView = (WebView) rootView.findViewById(R.id.webView);
		initWebView();
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}

			@SuppressLint("DefaultLocale")
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.toUpperCase().endsWith("PDF")) {
					try {
						 Intent intentUrl = new Intent(Intent.ACTION_VIEW);
						 intentUrl.setDataAndType(Uri.parse(url), "application/pdf");
						 startActivity(Intent.createChooser(intentUrl, getString(R.string.choose_pdf_viewer)));
					} catch (ActivityNotFoundException e) {
					    Toast.makeText(WebViewFragment.this.getActivity(),
					    		getString(R.string.pdf_viewer_not_installed), Toast.LENGTH_SHORT).show();
					}
				} else {
					view.loadUrl(url);
				}
				
				return true;
			}
			
			
		});
		
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				 getActivity().setProgress(newProgress * 100);
			}
		});
	
		ConnectionDetector detector = new ConnectionDetector(getActivity()); 
		if (detector.isConnectingToInternet()) {
			if (webViewBundle == null) {
				webView.loadUrl(getString(R.string.homepage));	
			} else {
				webView.restoreState(webViewBundle);
			}
		} else {
			AlertDialogHelper.showNoInternetDialog(getActivity());
		}
		Log.i("WebViewFragment", "In OnCreateView");
		return rootView;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.getSettings().setDisplayZoomControls(false);
		}
		webView.setOnKeyListener(new MyKeyListener());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (webViewBundle == null) {
			webViewBundle = new Bundle();
		}
		webView.saveState(webViewBundle);
	}

	private class MyKeyListener implements OnKeyListener {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
				webView.goBack();
				return true;
			}
			return false;
		}
	}
}
