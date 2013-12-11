package com.blueskyconnie.heritagefiesta;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
	private ImageButton btnOpenBrowser;
	private String strHomepage;

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
				String tmpUrl = url.toUpperCase();
				if (tmpUrl.endsWith("PDF")) {
					try {
						 Intent intentUrl = new Intent(Intent.ACTION_VIEW);
						 intentUrl.setDataAndType(Uri.parse(url), "application/pdf");
						 startActivity(Intent.createChooser(intentUrl, getString(R.string.choose_pdf_viewer)));
					} catch (ActivityNotFoundException e) {
					    Toast.makeText(WebViewFragment.this.getActivity(),
					    		getString(R.string.pdf_viewer_not_installed), Toast.LENGTH_SHORT).show();
					}
				} else if (!tmpUrl.endsWith("HTM")) {
					view.loadUrl(url);
				}
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				progressBar.setVisibility(View.INVISIBLE);
				btnRefresh.setVisibility(View.VISIBLE);
				updateActionView();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				btnRefresh.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				updateActionView();
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
				webView.loadUrl(strHomepage);	
			} else {
				webView.restoreState(webViewBundle);
			}
		} else {
			AlertDialogHelper.showNoInternetDialog(getActivity());
		}
		
		progressBar = (ProgressBar) rootView.findViewById(R.id.loading1);
		btnBack = (ImageButton) rootView.findViewById(R.id.web_view_btn_back);
		btnForward = (ImageButton) rootView.findViewById(R.id.web_view_btn_forward);
		btnRefresh = (ImageButton) rootView.findViewById(R.id.web_view_btn_refresh);
		btnOpenBrowser = (ImageButton) rootView.findViewById(R.id.web_view_btn_share);
		
		btnBack.setOnClickListener(this);
		btnForward.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
		btnOpenBrowser.setOnClickListener(this);
		
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
//		webView.setOnKeyListener(new MyKeyListener());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (webViewBundle == null) {
			webViewBundle = new Bundle();
		}
		webView.saveState(webViewBundle);
	}

	private void updateActionView() {
       if (webView.canGoBack())
    	   btnBack.setEnabled(true);
       else
    	   btnBack.setEnabled(false);

       if (webView.canGoForward())
    	   btnForward.setEnabled(true);
       else
    	   btnForward.setEnabled(false);
       
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
