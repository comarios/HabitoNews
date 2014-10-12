package com.ucl.news.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class RunningAppsDAO implements Parcelable {

	private long userID;
	private String userSession;
	private String appName;
	private String packageName;
	private String categoryName;
	private double lat;
	private double lon;
	private String startTime;

	public RunningAppsDAO() {
	}

	public RunningAppsDAO(long _userID, String _userSession, String _appName,
			String _packageName, String _categoryName, double _lat,
			double _lon, String _startTime) {

		userID = _userID;
		userSession = _userSession;
		appName = _appName;
		packageName = _packageName;
		categoryName = _categoryName;
		lat = _lat;
		lon = _lon;
		startTime = _startTime;
	}

	public RunningAppsDAO(Parcel pc) {

		userID = pc.readLong();
		userSession = pc.readString();
		appName = pc.readString();
		packageName = pc.readString();
		categoryName = pc.readString();
		lat = pc.readDouble();
		lon = pc.readDouble();
		startTime = pc.readString();
	}

	public static final Parcelable.Creator<RunningAppsDAO> CREATOR = new Parcelable.Creator<RunningAppsDAO>() {
		public RunningAppsDAO createFromParcel(Parcel in) {
			return new RunningAppsDAO(in);
		}

		public RunningAppsDAO[] newArray(int size) {
			return new RunningAppsDAO[size];
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
		pc.writeString(appName);
		pc.writeString(packageName);
		pc.writeString(categoryName);
		pc.writeDouble(lat);
		pc.writeDouble(lon);
		pc.writeString(startTime);
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

}
