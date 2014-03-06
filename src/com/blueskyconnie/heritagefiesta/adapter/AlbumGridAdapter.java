package com.blueskyconnie.heritagefiesta.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueskyconnie.heritagefiesta.R;
import com.blueskyconnie.heritagefiesta.data.GalleryItem;

public class AlbumGridAdapter extends ArrayAdapter<GalleryItem> {

	private List<GalleryItem> categories;
	private int resource;
	private Context context;
	
//	public AlbumGridAdapter(Context context, int resource, List<String> categories) {
//		super(context, resource, categories);
//		this.context = context;
//		this.resource = resource;
//		this.categories = categories;
//	}

	public AlbumGridAdapter(Context context, int resource, List<GalleryItem> categories) {
		super(context, resource, categories);
		this.context = context;
		this.resource = resource;
		this.categories = categories;
	}

	@Override
	public int getCount() {
		return categories.size();
	}

	@Override
	public GalleryItem getItem(int position) {
		if (position >= 0 && position < getCount()) {
			return categories.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		GalleryItem val = getItem(position);
		return val == null ? -1 : val.hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		AlbumHolder holder = null;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(this.resource, null);
			holder = new AlbumHolder();
			holder.tvName = (TextView) view.findViewById(R.id.tvAlbumName);
			holder.imgView = (ImageView) view.findViewById(R.id.imgAlbum);
			view.setTag(holder);
		} else {
			holder = (AlbumHolder) view.getTag();
		}
		
		GalleryItem galleryItem = getItem(position);
		holder.tvName.setText(galleryItem.getCategory());
		holder.imgView.setImageResource(galleryItem.getResourceId());
		return view;
	}
	
	private static class AlbumHolder {
		TextView tvName;
		ImageView imgView;
	}
}
