package com.blueskyconnie.heritagefiesta.data;

public class GalleryItem {

	private String category;
	private int resourceId;
	
	public GalleryItem(String category, int resourceId) {
		this.category = category;
		this.resourceId = resourceId;
	}

	public String getCategory() {
		return category;
	}

	public int getResourceId() {
		return resourceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + resourceId;
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
		GalleryItem other = (GalleryItem) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (resourceId != other.resourceId)
			return false;
		return true;
	}
	
}
