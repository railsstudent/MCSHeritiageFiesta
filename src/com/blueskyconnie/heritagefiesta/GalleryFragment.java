package com.blueskyconnie.heritagefiesta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.blueskyconnie.heritagefiesta.data.Product;
import com.blueskyconnie.heritagefiesta.helper.AlertDialogHelper;
import com.blueskyconnie.heritagefiesta.helper.ConnectionDetector;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

public class GalleryFragment extends ListFragment {
	
	private ProgressDialog dialog;
	private boolean hasClickedItem = false;
	private TextView tvUpdateTime;
	private ImageButton imgBtnRefresh;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		dialog = new ProgressDialog(getActivity()); 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_product, container, false);
		tvUpdateTime = (TextView) rootView.findViewById(R.id.tvLastUpdate);
		imgBtnRefresh = (ImageButton) rootView.findViewById(R.id.imgRefresh);
		imgBtnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ConnectionDetector detector = new ConnectionDetector(getActivity());
				if (detector.isConnectingToInternet()) {
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
		
//		// restore products 
//		MainActivity activity = (MainActivity) getActivity();
//		ProductAdapter adapter = new ProductAdapter(getActivity(), R.layout.product_row_layout, 
//				activity.getLstProducts());
//		setListAdapter(adapter);
//		tvUpdateTime.setText(getString(R.string.last_update_time) + 
//				MainActivity.sdf.format(new Date(activity.getLastProductUpdateTime())));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ListView listview = getListView();
		if (listview != null) {
			listview.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, true));
		}
//		Log.i("Product Fragment", "onResume");
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
			// go to detail activity
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

}
