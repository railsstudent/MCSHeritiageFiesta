package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.blueskyconnie.bluestonecrystal.adapter.ProductAdapter;
import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;
import com.blueskyconnie.bluestonecrystal.helper.HttpClientHelper;

public class ProductFragment extends ListFragment {

	private ProgressDialog dialog;
	private WeakReference<RetrieveProductTask> asyncTaskWeakRef;
	private boolean hasClickedItem = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		dialog = new ProgressDialog(ProductFragment.this.getActivity()); 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_product, container, false);
		
		ImageButton imgBtnRefresh = (ImageButton) rootView.findViewById(R.id.imgRefresh);
		imgBtnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ConnectionDetector detector = new ConnectionDetector(getActivity());
				if (detector.isConnectingToInternet()) {
					startNewAsyncTask();
				} else {
					showNoInternetDialog();
				}
			}
		});
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Toast.makeText(getActivity(), "ProductFragment OnResume", Toast.LENGTH_SHORT).show();
		ConnectionDetector detector = new ConnectionDetector(getActivity());
		if (detector.isConnectingToInternet()) {
			startNewAsyncTask();
		} else {
			showNoInternetDialog();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}

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
	
	private void showNoInternetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.info_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(getString(R.string.no_internet_error));
		builder.setNeutralButton(getString(R.string.confirm_exit), 
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
	private class RetrieveProductTask extends AsyncTask<String,Void, List<Product>> {

		private WeakReference<ProductFragment> fragmentWeakRef;

        private RetrieveProductTask (ProductFragment fragment) {
            this.fragmentWeakRef = new WeakReference<ProductFragment>(fragment);
        }

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dialog != null) {
				dialog.setMessage(ProductFragment.this.getString(R.string.dowloading));
				dialog.show();
			}
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
				ProductAdapter adapter = new ProductAdapter(fragmentWeakRef.get().getActivity(),
						R.layout.product_row_layout, lstProducts);
				fragmentWeakRef.get().setListAdapter(adapter);
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		}
	}
	
}
