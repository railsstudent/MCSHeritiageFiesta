package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.helper.ImageDecodeHelper;

public class DetailActivity extends Activity {

	private static final int REQ_WIDTH = 320;
	private static final int REQ_HEIGHT = 320;

	private WeakReference<DownloadImageTask> asyncTaskWeakRef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_detail);
		
		if (getIntent() != null)  {
//			Product product = (Product) getIntent().getSerializableExtra("currentProduct");
			Product product = (Product) getIntent().getParcelableExtra("currentProduct");
			if (product != null) { 
				// retrieve image
				startNewAsyncTask(product);
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private void startNewAsyncTask(Product product) {
		DownloadImageTask asyncTask = new DownloadImageTask(this);
	    this.asyncTaskWeakRef = new WeakReference<DownloadImageTask >(asyncTask);
	    // image url
	    asyncTaskWeakRef.get().execute(product.getImageUrl(), product.getName(), 
	    		product.getDescription(), product.getPrice().toPlainString());
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Product> {

		private final ProgressDialog dialog = new ProgressDialog(DetailActivity.this); 
		private WeakReference<DetailActivity> fragmentWeakRef;
		
		private DownloadImageTask (DetailActivity fragment) {
            this.fragmentWeakRef = new WeakReference<DetailActivity>(fragment);
        }
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage(DetailActivity.this.getString(R.string.dowloading));
			dialog.show();
		}

		@Override
		protected Product doInBackground(String... params) {

			Product product = new Product();
			product.setName(params[1]);
			product.setDescription(params[2]);
			product.setPrice(new BigDecimal(params[3]));
			product.setImageUrl(params[0]);

//			Bitmap bitmapProduct = ImageDecodeHelper.decodeSampledBitmapFromStream(params[0], REQ_WIDTH, REQ_HEIGHT);
			Bitmap bitmapProduct = ImageDecodeHelper.decodeSampledBitmapFromByteArray(params[0], REQ_WIDTH, REQ_HEIGHT);
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
				tvItemName.setText(result.getName());
				
				TextView tvItemDesc = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemDescription);
				tvItemDesc.setText(result.getDescription());
				
				TextView tvItemPrice = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemPrice);
				tvItemPrice.setText(result.getPrice().toPlainString());
				
				if (result.getImage() != null) {
					ImageView imgView = (ImageView) fragmentWeakRef.get().findViewById(R.id.imgItem);
					imgView.setImageBitmap(result.getImage());
				}
				dialog.dismiss();
			}
		}
	}
}
