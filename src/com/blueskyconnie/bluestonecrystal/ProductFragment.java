package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.blueskyconnie.bluestonecrystal.adapter.ProductAdapter;
import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.helper.HttpClientHelper;

public class ProductFragment extends ListFragment {

	private WeakReference<RetrieveProductTask> asyncTaskWeakRef;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	    startNewAsyncTask();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_product, container, false);
		
		ImageButton imgBtnRefresh = (ImageButton) rootView.findViewById(R.id.imgRefresh);
		imgBtnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startNewAsyncTask();
			}
		});
		return rootView;
	}
	
	private boolean hasClickedItem = false;
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		if (!hasClickedItem) {
			// go to detail fragment
			Product product = (Product) l.getItemAtPosition(position);
		    Intent intent = new Intent(this.getActivity(), DetailActivity.class);
		    intent.putExtra("currentProduct", product);
		    hasClickedItem = true;
		    this.startActivityForResult(intent, 1);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		hasClickedItem = false;
	}

	private void startNewAsyncTask() {
		RetrieveProductTask asyncTask = new RetrieveProductTask(this);
	    this.asyncTaskWeakRef = new WeakReference<RetrieveProductTask >(asyncTask);
	    asyncTaskWeakRef.get().execute("");
	}
	
	private class RetrieveProductTask extends AsyncTask<String,Void, List<Product>> {

		private final ProgressDialog dialog = new ProgressDialog(ProductFragment.this.getActivity()); 
		private WeakReference<ProductFragment> fragmentWeakRef;

        private RetrieveProductTask (ProductFragment fragment) {
            this.fragmentWeakRef = new WeakReference<ProductFragment>(fragment);
        }

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage(ProductFragment.this.getString(R.string.dowloading));
			dialog.show();
		}

		@Override
		protected List<Product> doInBackground(String... params) {
			List<Product> lstProduct = HttpClientHelper.retrieveProducts(params[0]);
			return lstProduct;
		}
		
		@Override
		protected void onPostExecute(List<Product> lstProducts) {
			super.onPostExecute(lstProducts);
			if (this.fragmentWeakRef.get() != null) {
//				ProductAdapter adapter = new ProductAdapter(ProductFragment.this.getActivity(),
//						R.layout.product_row_layout, lstProducts);
				//ProductFragment.this.setListAdapter(adapter);
				//Toast.makeText(ProductFragment.this.getActivity(), "onPostExecute", Toast.LENGTH_SHORT).show();
				ProductAdapter adapter = new ProductAdapter(fragmentWeakRef.get().getActivity(),
						R.layout.product_row_layout, lstProducts);
				fragmentWeakRef.get().setListAdapter(adapter);
				dialog.dismiss();
			}
		}
	}
	
}
