package com.ucl.news.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class NavigationalMetaDataDAO implements Parcelable {

	private long userID;
	private String userSession;
	private String categoryName;
	private String swipeDirection;
	private int itemPosition;
	private String dateTime;

	public NavigationalMetaDataDAO() {
	}

	public NavigationalMetaDataDAO(long _userID, String _userSession,
			String _categoryName, String _swipeDirection, int _itemPosition,
			String _dateTime) {

		userID = _userID;
		userSession = _userSession;
		categoryName = _categoryName;
		swipeDirection = _swipeDirection;
		itemPosition = _itemPosition;
		dateTime = _dateTime;
	}

	public NavigationalMetaDataDAO(Parcel pc) {

		userID = pc.readLong();
		userSession = pc.readString();
		categoryName = pc.readString();
		swipeDirection = pc.readString();
		itemPosition = pc.readInt();
		dateTime = pc.readString();
	}

	public static final Parcelable.Creator<NavigationalMetaDataDAO> CREATOR = new Parcelable.Creator<NavigationalMetaDataDAO>() {
		public NavigationalMetaDataDAO createFromParcel(Parcel in) {
			return new NavigationalMetaDataDAO(in);
		}

		public NavigationalMetaDataDAO[] newArray(int size) {
			return new NavigationalMetaDataDAO[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {
		// TODO Auto-generated method stub

		pc.writeLong(userID);
		pc.writeString(userSession);
		pc.writeString(categoryName);
		pc.writeString(swipeDirection);
		pc.writeInt(itemPosition);
		pc.writeString(dateTime);
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public String getUserSession() {
		return userSession;
	}

	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSwipeDirection() {
		return swipeDirection;
	}

	public void setSwipeDirection(String swipeDirection) {
		this.swipeDirection = swipeDirection;
	}

	public int getItemPosition() {
		return itemPosition;
	}

	public void setItemPosition(int itemPosition) {
		this.itemPosition = itemPosition;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	
}
