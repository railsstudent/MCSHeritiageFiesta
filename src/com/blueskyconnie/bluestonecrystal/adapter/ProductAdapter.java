package com.blueskyconnie.bluestonecrystal.adapter;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueskyconnie.bluestonecrystal.R;
import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.helper.ImageDecodeHelper;

public class ProductAdapter extends ArrayAdapter<Product> {

	private static final int THUMBNAIL_WIDTH = 50;
	private static final int THUMBNAIL_HEIGHT = 50;

	private List<Product> products;
	private Context context;
	private int resourceId;
	
	public ProductAdapter(Context context, int resource, List<Product> objects) {
		super(context, resource, objects);
		this.context = context;
		this.products = objects;
		this.resourceId = resource;
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public Product getItem(int position) {
		if (position < getCount()) {
			return products.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		Product product = getItem(position);
		return product == null ? -1 : product.hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ProductHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = (View)inflater.inflate(resourceId, null);
			holder = new ProductHolder();
			
			// find ui controls from view and assign them to holder object
			holder.tvProductName  = (TextView) view.findViewById(R.id.tvProductName);
			holder.imgProductThumbnail = (ImageView) view.findViewById(R.id.imgProductThumbnail);
			view.setTag(holder);
		} else {
			holder = (ProductHolder) view.getTag();
		}
		
		Product product = getItem(position);
		holder.tvProductName.setText(product.getName());
		if (holder.imgProductThumbnail != null) {
			new ImageDownloaderTask(holder.imgProductThumbnail).execute(product.getImageUrl());
		}
		
	//	if (product.getImage() != null) {
	//		holder.imgProductThumbnail.setImageBitmap(product.getImage());
	//		holder.imgProductThumbnail.setScaleType(ScaleType.FIT_CENTER);
	//	}
		return view;
	}
	
	private static class ProductHolder {
		TextView tvProductName;
		ImageView imgProductThumbnail;
	}
	
	private class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

		private WeakReference<ImageView> imageViewReference;

        private ImageDownloaderTask (ImageView imageView) {
            this.imageViewReference = new WeakReference<ImageView>(imageView);
        }
        
		@Override
		protected Bitmap doInBackground(String... params) {
			return ImageDecodeHelper.decodeSampledBitmapFromByteArray(params[0], 
					THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			ImageView imgView = this.imageViewReference.get();
			if (imgView != null) {
				if (bitmap != null) {
					imgView.setImageBitmap(bitmap);
				} else {
					imgView.setImageResource(R.id.imgProductThumbnail);
				}
			}
		}
	}
}
