package com.blueskyconnie.heritagefiesta.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable, Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9128138891015072545L;
	
	private int categoryId;
	private String category;
	private List<String> imageUrl = new ArrayList<String>();
	
	public Album() {}
	
	public Album(Parcel in) {
		categoryId = in.readInt();
		category = in.readString();
		in.readStringList(imageUrl);
	}
	
	public List<String> getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(List<String> imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + categoryId;
		result = prime * result
				+ ((imageUrl == null) ? 0 : imageUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (categoryId != other.categoryId)
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		return true;
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(categoryId);
		dest.writeString(category);
		dest.writeStringList(imageUrl);
	}

	public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {

		@Override
		public Album createFromParcel(Parcel source) {
			return new Album(source);
		}

		@Override
		public Album[] newArray(int size) {
			return new Album[size];
		}
	};
}
