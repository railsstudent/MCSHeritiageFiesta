package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueskyconnie.bluestonecrystal.data.Product;

public class DetailFragment extends Fragment {

	private WeakReference<DownloadImageTask> asyncTaskWeakRef;
	private TextView tvName;
	private TextView tvDescription;
	private TextView tvPrice;
	private ImageView imgProduct;
	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setRetainInstance(true);
//		
//		Bundle bundle = this.getArguments();
//		if (bundle != null) { 
//			Product product = (Product) bundle.getSerializable("currentProduct");
//			// retrieve image
//			startNewAsyncTask(product);
//		} 
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
		
		Bundle bundle = this.getArguments();
		if (bundle != null) { 
			Product product = (Product) bundle.getSerializable("currentProduct");
			// retrieve image
	//		startNewAsyncTask(product);
			tvName = (TextView) rootView.findViewById(R.id.tvItemName);
			tvDescription = (TextView) rootView.findViewById(R.id.tvItemDescription);
			tvPrice = (TextView) rootView.findViewById(R.id.tvItemPrice);
			imgProduct = (ImageView) rootView.findViewById(R.id.imgItem);
			tvName.setText(product.getName());
			tvDescription.setText(product.getDescription());
			tvPrice.setText(product.getPrice().toPlainString());
		} 
		
		return rootView;
	}
	
	private void startNewAsyncTask(Product product) {
		DownloadImageTask asyncTask = new DownloadImageTask(this);
	    this.asyncTaskWeakRef = new WeakReference<DownloadImageTask >(asyncTask);
	    // image url
	    asyncTaskWeakRef.get().execute(product.getImageUrl(), product.getName(), 
	    		product.getDescription(), product.getPrice().toPlainString());
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Product> {

		private final ProgressDialog dialog = new ProgressDialog(DetailFragment.this.getActivity()); 
		private WeakReference<DetailFragment> fragmentWeakRef;
		
		private DownloadImageTask (DetailFragment fragment) {
            this.fragmentWeakRef = new WeakReference<DetailFragment>(fragment);
        }
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage(DetailFragment.this.getString(R.string.dowloading));
			dialog.show();
		}

		@Override
		protected Product doInBackground(String... params) {
			Product product = new Product();
			product.setName(params[1]);
			product.setDescription(params[2]);
			product.setPrice(new BigDecimal(params[3]));
			product.setImageUrl(params[0]);
			// @TODO download image here
			return product;
		}

		@Override
		protected void onPostExecute(Product result) {
			super.onPostExecute(result);
			if (this.fragmentWeakRef.get() != null) {
//				View  tempView = this.fragmentWeakRef.get().getView();
//				TextView tvTemp = (TextView) tempView.findViewById(R.id.tvItemName);
//				tvTemp.setText(result.getName());
//				
//				tvTemp = (TextView) tempView.findViewById(R.id.tvItemDescription);
//				tvTemp.setText(result.getDescription());
//				
//				tvTemp = (TextView) tempView.findViewById(R.id.tvItemPrice);
//				tvTemp.setText(result.getPrice().toPlainString());
//				
//				ImageView imgView = (ImageView) tempView.findViewById(R.id.imgItem);
//				imgView.setImageBitmap(result.getImage());
				dialog.dismiss();
			}
		}
	}
}
