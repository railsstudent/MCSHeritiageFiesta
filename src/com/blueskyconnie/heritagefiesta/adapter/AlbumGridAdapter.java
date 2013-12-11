package com.blueskyconnie.heritagefiesta.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blueskyconnie.heritagefiesta.R;

public class AlbumGridAdapter extends ArrayAdapter<String> {

	private List<String> categories;
	private int resource;
	private Context context;
	
	public AlbumGridAdapter(Context context, int resource, List<String> categories) {
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
	public String getItem(int position) {
		if (position >= 0 && position < getCount()) {
			return categories.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		String val = getItem(position);
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
			view.setTag(holder);
		} else {
			holder = (AlbumHolder) view.getTag();
		}
		
		String albumName = getItem(position);
		holder.tvName.setText(albumName);
		return view;
	}
	
	private static class AlbumHolder {
		TextView tvName;
	}
}
