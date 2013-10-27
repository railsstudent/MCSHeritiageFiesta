package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.blueskyconnie.bluestonecrystal.adapter.ProductAdapter;
import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.exception.BusinessException;
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
		dialog = new ProgressDialog(ProductFragment.this.getActivity()); 
		cmsUrl = getString(R.string.cms_url);
		Toast.makeText(getActivity(), cmsUrl, Toast.LENGTH_SHORT).show();
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
					showNoInternetDialog();
				}
			}
		});
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Toast.makeText(getActivity(), "Product: onResume", Toast.LENGTH_SHORT).show();
		ConnectionDetector detector = new ConnectionDetector(getActivity());
		if (detector.isConnectingToInternet()) {
			startNewAsyncTask(tvUpdateTime);
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
		    startActivityForResult(intent, 1);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		hasClickedItem = false;
	}

	private void startNewAsyncTask(TextView tvUpdateTime) {
		RetrieveProductTask asyncTask = new RetrieveProductTask(getActivity(), this, tvUpdateTime);
	    this.asyncTaskWeakRef = new WeakReference<RetrieveProductTask >(asyncTask);
	    Toast.makeText(getActivity(), cmsUrl + "products_android.php?id=1", Toast.LENGTH_SHORT).show();
	    asyncTaskWeakRef.get().execute(cmsUrl + "products_android.php?id=1");
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
		private TextView tvLastUpdate;
		private Context context;

        private RetrieveProductTask (Context context, ProductFragment fragment, TextView lblUpdate) {
            this.fragmentWeakRef = new WeakReference<ProductFragment>(fragment);
            this.tvLastUpdate = lblUpdate;
            this.context = context;
        }

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dialog != null) {
				dialog.setMessage(ProductFragment.this.getString(R.string.dowloading));
				dialog.show();
			}
			Toast.makeText(ProductFragment.this.getActivity(), "onPreExecute", Toast.LENGTH_SHORT).show();
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
				if (dialog != null) {
					dialog.dismiss();
				}
				// display current time
				if (tvLastUpdate != null) {
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH) + 1;
					int day = cal.get(Calendar.DATE);
					int hh = cal.get(Calendar.HOUR);
					int min = cal.get(Calendar.MINUTE);
					int sec = cal.get(Calendar.SECOND);
					int am_pm = cal.get(Calendar.AM_PM);
//					Toast.makeText(context, year + "-" + month + "-" + day + " " + hh
//							+ ":" + min + ":" + sec + " " + (am_pm == 1 ? "PM" : "AM")
// 							, Toast.LENGTH_LONG).show();
					//Toast.makeText(context, "month = " + month, Toast.LENGTH_LONG).show();
					Toast.makeText(context, sdf.format(Calendar.getInstance().getTime()), Toast.LENGTH_LONG).show();
					tvLastUpdate.setText(sdf.format(Calendar.getInstance().getTime()));
				}
			}
		}
	}
}
