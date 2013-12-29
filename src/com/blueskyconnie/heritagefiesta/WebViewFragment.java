package com.blueskyconnie.heritagefiesta;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blueskyconnie.heritagefiesta.helper.AlertDialogHelper;
import com.blueskyconnie.heritagefiesta.helper.ConnectionDetector;

public class WebViewFragment extends Fragment implements OnClickListener {

	private WebView webView;
	private Bundle webViewBundle;
	private ImageButton btnBack;
	private ImageButton btnForward;
	private ImageButton btnRefresh;
	private ProgressBar progressBar;
	private ProgressBar webViewProgressBar;
	private ImageButton btnOpenBrowser;
	private String strHomepage;
	private FrameLayout frameLayout;
	private ConnectionDetector detector;

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

		strHomepage = getString(R.string.homepage);
		if (!strHomepage.startsWith("http://") && !strHomepage.startsWith("https://")) {
			strHomepage = "http://" + strHomepage;
		}
		
		// put the web view in the frameLayout
		frameLayout = (FrameLayout) rootView.findViewById(R.id.webView);
		webViewProgressBar = (ProgressBar) rootView.findViewById(R.id.web_view_progress);
		webView = new WebView(getActivity());
		// hide web view until the page is fully loaded
		webView.setVisibility(WebView.GONE);
		webView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		initWebView();
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}

			@SuppressLint("DefaultLocale")
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (detector.isConnectingToInternet()) {
					String tmpUrl = url.toUpperCase();
					if (tmpUrl.endsWith("PDF")) {
						try {
	  						 Uri selectedUri = Uri.parse(url);
							 String mimeType = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
							 Intent intentUrl = new Intent(Intent.ACTION_VIEW);
							 intentUrl.setDataAndType(selectedUri, mimeType);
							 startActivity(Intent.createChooser(intentUrl, getString(R.string.choose_viewer)));
						} catch (ActivityNotFoundException e) {
						    Toast.makeText(WebViewFragment.this.getActivity(),
						    		getString(R.string.viewer_not_installed), Toast.LENGTH_SHORT).show();
						}
						return true;
					} else if (!tmpUrl.endsWith("HTM")) {
						view.loadUrl(url);
						return true;
					} else if (!tmpUrl.endsWith("PDF") && !tmpUrl.endsWith("HTM")) {
						return super.shouldOverrideUrlLoading(view, url);
					}
				}
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				final Animation fade = new AlphaAnimation(0.0f, 1.0f);
		        fade.setDuration(200);
		        view.startAnimation(fade);
		        view.setVisibility(View.VISIBLE);
				webViewProgressBar.setVisibility(View.GONE);
				progressBar.setVisibility(View.INVISIBLE);
				btnRefresh.setVisibility(View.VISIBLE);
				updateActionView();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				btnRefresh.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				webViewProgressBar.setVisibility(View.VISIBLE);
				view.setVisibility(View.GONE);
				updateActionView();
			}
		});
		
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (view != null) {
					if (newProgress < 100 && webViewProgressBar.getVisibility() == View.GONE) {
						webViewProgressBar.setVisibility(View.VISIBLE);
					}
					webViewProgressBar.setProgress(newProgress);
					if (newProgress >= 100) {
						if (webView.getVisibility() == View.GONE) {
							webView.setVisibility(View.VISIBLE);
						}
						webViewProgressBar.setVisibility(View.GONE);
					}
				}
			}
		});
		frameLayout.addView(webView);

		progressBar = (ProgressBar) rootView.findViewById(R.id.loading1);
		btnBack = (ImageButton) rootView.findViewById(R.id.web_view_btn_back);
		btnForward = (ImageButton) rootView.findViewById(R.id.web_view_btn_forward);
		btnRefresh = (ImageButton) rootView.findViewById(R.id.web_view_btn_refresh);
		btnOpenBrowser = (ImageButton) rootView.findViewById(R.id.web_view_btn_share);
		
		btnBack.setOnClickListener(this);
		btnForward.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
		btnOpenBrowser.setOnClickListener(this);

		detector = new ConnectionDetector(getActivity()); 
		if (detector.isConnectingToInternet()) {
			if (webViewBundle == null) {
				webView.loadUrl(strHomepage);	
			} else {
				webView.restoreState(webViewBundle);
			}
			webViewProgressBar.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.VISIBLE);
		} else {
			webViewProgressBar.setVisibility(View.GONE);
			progressBar.setVisibility(View.GONE);
			AlertDialogHelper.showNoInternetDialog(getActivity());
		}
		
		updateActionView();
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
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (webView != null) {
			webView.onResume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (webViewBundle == null) {
			webViewBundle = new Bundle();
		}
		if (webView != null) {
			webView.saveState(webViewBundle);
			webView.onPause();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		frameLayout.removeView(webView);
		webView.removeAllViews();
		webView.destroy();
	}

	private void updateActionView() {
       if (webView.canGoBack()) {
    	   btnBack.setEnabled(true);
       } else {
    	   btnBack.setEnabled(false);
       }

       if (webView.canGoForward()) {
    	   btnForward.setEnabled(true);
       } else {
    	   btnForward.setEnabled(false);
       }
   }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.web_view_btn_back:
				webView.goBack();
				break;
			case R.id.web_view_btn_forward:
				webView.goForward();
				break;
			case R.id.web_view_btn_refresh:
				webView.reload();
				break;
			case R.id.web_view_btn_share:
				Uri uri = Uri.parse(strHomepage);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				break;
		}
		updateActionView();
	}
}
