package com.blueskyconnie.heritagefiesta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueskyconnie.heritagefiesta.data.Product;
import com.blueskyconnie.heritagefiesta.helper.ConnectionDetector;
import com.blueskyconnie.heritagefiesta.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class DetailActivity extends Activity {

	private ProgressDialog dialog; 
//	private WeakReference<DownloadImageTask> asyncTaskWeakRef;
	private Product currentProduct = null;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageView imgView;
	private TextView tvName;
	private TextView tvDesc; 
	private TextView tvPrice; 
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_detail);
		
		dialog = new ProgressDialog(this);
		imgView = (ImageView) findViewById(R.id.imgItem);
		tvName = (TextView) findViewById(R.id.tvItemName);
		tvDesc = (TextView) findViewById(R.id.tvItemDescription);
		tvPrice = (TextView) findViewById(R.id.tvItemPrice);

		if (getIntent() != null)  {
			currentProduct = (Product) getIntent().getParcelableExtra("currentProduct");
			if (currentProduct != null) { 
				ConnectionDetector detector = new ConnectionDetector(this);
				if (detector.isConnectingToInternet()) {
					// retrieve image
					imageLoader.displayImage(currentProduct.getImageUrl(), imgView, 
							new SimpleImageLoadingListener(){
								@Override
								public void onLoadingStarted(String imageUri, View view) {
									if (dialog != null) {
										dialog.setTitle(getString(R.string.app_name));
										dialog.setMessage(DetailActivity.this.getString(R.string.downloading));
										dialog.show();
									}
								}

								@Override
								public void onLoadingFailed(String imageUri,
										View view, FailReason failReason) {
									super.onLoadingFailed(imageUri, view, failReason);
									
									String message = null;
									switch (failReason.getType()) {
										case IO_ERROR:
											message = "Input/Output error";
											break;
										case DECODING_ERROR:
											message = "Image can't be decoded";
											break;
										case NETWORK_DENIED:
											message = "Downloads are denied";
											break;
										case OUT_OF_MEMORY:
											message = "Out Of Memory error";
											break;
										case UNKNOWN:
											message = "Unknown error";
											break;
									}
									if (dialog != null) {
										dialog.dismiss();
									}
									Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									super.onLoadingComplete(imageUri, view, loadedImage);
									if (dialog != null) {
										dialog.dismiss();
									}
								}
							});
					tvName.setText(currentProduct.getName());
					tvDesc.setText(currentProduct.getDescription());
					tvPrice.setText(currentProduct.getPrice().toPlainString());
				}
			}
		}
		Log.i("Detail Activity" ,"onCreate"); 
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// http://stackoverflow.com/questions/3378102/error-view-not-attached-to-window-manager/11448457#11448457
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// http://stackoverflow.com/questions/3378102/error-view-not-attached-to-window-manager/11448457#11448457
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
