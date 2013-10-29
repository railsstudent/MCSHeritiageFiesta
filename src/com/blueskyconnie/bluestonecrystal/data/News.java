package com.blueskyconnie.bluestonecrystal.data;

import java.sql.Timestamp;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
	
	private int id;
	private String subject;
	private String description;
	private Timestamp updateAt;
	
	public News() {}
	
	public News (Parcel in){
		id = in.readInt();
		subject = in.readString();
		description = in.readString();
		updateAt = new Timestamp(in.readLong());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result
				+ ((updateAt == null) ? 0 : updateAt.hashCode());
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
		News other = (News) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (updateAt == null) {
			if (other.updateAt != null)
				return false;
		} else if (!updateAt.equals(other.updateAt))
			return false;
		return true;
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(subject);
		dest.writeString(description);
		dest.writeLong(updateAt.getTime());
	}
	

	public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        public News createFromParcel(Parcel in) {
            return new News(in); 
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
