package com.ucl.news.api;

import android.os.Parcel;
import android.os.Parcelable;

public class NavigationDAO implements Parcelable {

	private long userID;
	private String userSession;
	private String categoryName;
	private int position;
	private int orderID;

	public NavigationDAO() {
	}

	public NavigationDAO(long _userID, String _userSession,
			String _categoryName, int _position, int _orderID) {

		userID = _userID;
		userSession = _userSession;
		categoryName = _categoryName;
		position = _position;
		orderID = _orderID;
	}

	public NavigationDAO(Parcel pc) {

		userID = pc.readLong();
		userSession = pc.readString();
		categoryName = pc.readString();
		position = pc.readInt();
		orderID = pc.readInt();
	}

	public static final Parcelable.Creator<NavigationDAO> CREATOR = new Parcelable.Creator<NavigationDAO>() {
		public NavigationDAO createFromParcel(Parcel in) {
			return new NavigationDAO(in);
		}

		public NavigationDAO[] newArray(int size) {
			return new NavigationDAO[size];
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
		pc.writeInt(position);
		pc.writeInt(orderID);
	}

	/**
	 * Getters and Setters
	 */
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

}
