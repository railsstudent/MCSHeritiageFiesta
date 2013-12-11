package com.blueskyconnie.heritagefiesta.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.blueskyconnie.heritagefiesta.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImagepagerAdapter extends PagerAdapter {

	private List<String> imageUrls;
	private ImageLoader imageLoader;
	private Context context; 
	private int resourceId;
	private DisplayImageOptions options;
	
	public ImagepagerAdapter(Context context ,int resourceId, List<String> imageUrls, ImageLoader imageLoader) {
		this.context = context;
		this.resourceId = resourceId;
		this.imageUrls = imageUrls;
		this.imageLoader = imageLoader;
	
		options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.showImageForEmptyUri(R.drawable.img_stub)
			.showStubImage(R.drawable.img_stub)
			.showImageOnFail(R.drawable.img_error)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.resetViewBeforeLoading(true)
			.build();
	}
	
	@Override
	public int getCount() {
		return imageUrls.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String title = context.getString(R.string.pagerTitleLeft) + (position + 1) + 
					context.getString(R.string.pagerTitleRight);
		return title;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view.equals(obj);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resourceId, null);
		ImageView imgView = (ImageView) view.findViewById(R.id.albumImage);
		final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.imgLoading);
		imageLoader.displayImage(imageUrls.get(position), imgView, options, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				spinner.setVisibility(Spinner.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
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
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				spinner.setVisibility(Spinner.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				spinner.setVisibility(Spinner.GONE);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				spinner.setVisibility(Spinner.GONE);
			}
		});
		container.addView(view);
		return view;
	}
	
}
