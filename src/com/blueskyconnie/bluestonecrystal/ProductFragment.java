package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
import android.widget.TextView;

import com.blueskyconnie.bluestonecrystal.adapter.ProductAdapter;
import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.exception.BusinessException;
import com.blueskyconnie.bluestonecrystal.helper.AlertDialogHelper;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;
import com.blueskyconnie.bluestonecrystal.helper.HttpClientHelper;

public class ProductFragment extends ListFragment {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss aa", Locale.US);
	
	private ProgressDialog dialog;
	private WeakReference<RetrieveProductTask> asyncTaskWeakRef;
	private boolean hasClickedItem = false;
	private String cmsUrl;
	private TextView tvUpdateTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_product, container, false);
		
		tvUpdateTime = (TextView) rootView.findViewById(R.id.tvLastUpdate);
		ImageButton imgBtnRefresh = (ImageButton) rootView.findViewById(R.id.imgRefresh);
		imgBtnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ConnectionDetector detector = new ConnectionDetector(getActivity());
				if (detector.isConnectingToInternet()) {
					startNewAsyncTask(tvUpdateTime);
				} else {
					AlertDialogHelper.showNoInternetDialog(getActivity());
				}
			}
		});
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dialog = new ProgressDialog(getActivity()); 
		cmsUrl = getString(R.string.cms_url);
		ConnectionDetector detector = new ConnectionDetector(getActivity());
		if (detector.isConnectingToInternet()) {
			startNewAsyncTask(tvUpdateTime);
		} else {
			AlertDialogHelper.showNoInternetDialog(getActivity());
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
		    startActivityForResult(intent, 1);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		hasClickedItem = false;
	}

	private void startNewAsyncTask(TextView tvUpdateTime) {
		RetrieveProductTask asyncTask = new RetrieveProductTask(this, tvUpdateTime);
	    this.asyncTaskWeakRef = new WeakReference<RetrieveProductTask >(asyncTask);
	    asyncTaskWeakRef.get().execute(cmsUrl + "products_android.php?id=1");
	}
	
	private class RetrieveProductTask extends AsyncTask<String,Void, List<Product>> {

		private WeakReference<ProductFragment> fragmentWeakRef;
		private TextView tvLastUpdate;

        private RetrieveProductTask (ProductFragment fragment, TextView lblUpdate) {
            this.fragmentWeakRef = new WeakReference<ProductFragment>(fragment);
            this.tvLastUpdate = lblUpdate;
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
			try {
				List<Product> lstProduct = HttpClientHelper.retrieveProducts(params[0], cmsUrl);
				return lstProduct;
			} catch (BusinessException ex) {
				ex.printStackTrace();
			}
			return new ArrayList<Product>();
		}
		
		@Override
		protected void onPostExecute(List<Product> lstProducts) {
			super.onPostExecute(lstProducts);
			if (this.fragmentWeakRef.get() != null) {
				ProductAdapter adapter = new ProductAdapter(fragmentWeakRef.get().getActivity(),
						R.layout.product_row_layout, lstProducts);
				// update list adapter
				fragmentWeakRef.get().setListAdapter(adapter);
				// display current time
				if (tvLastUpdate != null) {
					tvLastUpdate.setText(sdf.format(Calendar.getInstance().getTime()));
				}
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		}
	}
}
