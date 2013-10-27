package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;
import com.blueskyconnie.bluestonecrystal.helper.ImageDecodeHelper;

public class DetailActivity extends Activity {

	private static final int REQ_WIDTH = 320;
	private static final int REQ_HEIGHT = 320;

	private ProgressDialog dialog; 
	private WeakReference<DownloadImageTask> asyncTaskWeakRef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_detail);
		dialog = new ProgressDialog(this);
		if (getIntent() != null)  {
			Product product = (Product) getIntent().getParcelableExtra("currentProduct");
			if (product != null) { 
				ConnectionDetector detector = new ConnectionDetector(this);
				if (detector.isConnectingToInternet()) {
					// retrieve image
					startNewAsyncTask(product);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle(getString(R.string.info_title));
					builder.setIcon(android.R.drawable.ic_dialog_alert);
					builder.setMessage(getString(R.string.no_internet_error));
					builder.setNeutralButton(getString(R.string.confirm_exit), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					});
					AlertDialog alertDialog = builder.create();
					alertDialog.show();
				}
			}
		}
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
		super.onBackPressed();
		finish();
	}

	private void startNewAsyncTask(Product product) {
		DownloadImageTask asyncTask = new DownloadImageTask(this, product);
	    this.asyncTaskWeakRef = new WeakReference<DownloadImageTask >(asyncTask);
	    // image url
	    asyncTaskWeakRef.get().execute();
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Product> {

		private WeakReference<DetailActivity> fragmentWeakRef;
		private Product product;
		
		private DownloadImageTask (DetailActivity fragment, Product product) {
            this.fragmentWeakRef = new WeakReference<DetailActivity>(fragment);
            this.product = product;
        }
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (fragmentWeakRef.get() != null) {
				ImageView imgView = (ImageView) fragmentWeakRef.get().findViewById(R.id.imgItem);
				TextView tv1 = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemName);
				TextView tv2 = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemDescription);
				TextView tv3 = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemPrice);
				TextView lbl1 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblItemName);
				TextView lbl2 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblDescription);
				TextView lbl3 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblPrice);

				if (imgView != null) {
					imgView.setVisibility(ImageView.GONE);
				}
				
				if (tv1 != null) {
					tv1.setVisibility(TextView.GONE);
				}
				
				if (tv2 != null) {
					tv2.setVisibility(TextView.GONE);
				}
				
				if (tv3 != null) {
					tv3.setVisibility(TextView.GONE);
				}
				
				if (lbl1 != null) {
					lbl1.setVisibility(TextView.GONE);
				}
				
				if (lbl2 != null) {
					lbl2.setVisibility(TextView.GONE);
				}
				
				if (lbl3 != null) {
					lbl3.setVisibility(TextView.GONE);
				}
			}
			
			if (dialog != null) {
				dialog.setMessage(DetailActivity.this.getString(R.string.dowloading));
				dialog.show();
			}
		}

		@Override
		protected Product doInBackground(String... params) {

			Product product = new Product();
			product.setName(this.product.getName());
			product.setDescription(this.product.getDescription());
			product.setPrice(this.product.getPrice());
			product.setImageUrl(this.product.getImageUrl());

			Bitmap bitmapProduct = ImageDecodeHelper.decodeSampledBitmapFromByteArray(
					product.getImageUrl(), REQ_WIDTH, REQ_HEIGHT);
			if (bitmapProduct != null) {
				product.setImage(bitmapProduct);
			} else {
				product.setImage(null);
			}
			return product;
		}

		@Override
		protected void onPostExecute(Product result) {
			super.onPostExecute(result);
			if (this.fragmentWeakRef.get() != null) {
				TextView tvItemName = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemName);
				if (tvItemName != null) {
					tvItemName.setText(result.getName());
					tvItemName.setVisibility(TextView.VISIBLE);
				}
				
				TextView tvItemDesc = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemDescription);
				if (tvItemDesc != null) {
					tvItemDesc.setText(result.getDescription());
					tvItemDesc.setVisibility(TextView.VISIBLE);
				}
				
				TextView tvItemPrice = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemPrice);
				if (tvItemPrice != null) {
					tvItemPrice.setText(result.getPrice().toPlainString());
					tvItemPrice.setVisibility(TextView.VISIBLE);
				}
				
				ImageView imgView = (ImageView) fragmentWeakRef.get().findViewById(R.id.imgItem);
				if (result.getImage() != null) {
					imgView.setImageBitmap(result.getImage());
				} 
				imgView.setVisibility(ImageView.VISIBLE);
				if (dialog != null) {
					dialog.dismiss();
				}
				
				TextView lbl1 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblItemName);
				TextView lbl2 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblDescription);
				TextView lbl3 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblPrice);
				if (lbl1 != null) {
					lbl1.setVisibility(TextView.VISIBLE);
				}
				if (lbl2 != null) {
					lbl2.setVisibility(TextView.VISIBLE);
				}
				if (lbl3 != null) {
					lbl3.setVisibility(TextView.VISIBLE);
				}
			}
		}
	}
}
