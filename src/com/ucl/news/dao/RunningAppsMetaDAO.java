package com.ucl.news.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class RunningAppsMetaDAO implements Parcelable {

	private String appName;
	private String packageName;
	private String categoryName;
	private String startTime;

	public RunningAppsMetaDAO() {
	}

	public RunningAppsMetaDAO(String _appName, String _packageName,
			String _categoryName, String _startTime) {

		appName = _appName;
		packageName = _packageName;
		categoryName = _categoryName;
		startTime = _startTime;
	}

	public RunningAppsMetaDAO(Parcel pc) {

		appName = pc.readString();
		packageName = pc.readString();
		categoryName = pc.readString();
		startTime = pc.readString();
	}

	public static final Parcelable.Creator<RunningAppsMetaDAO> CREATOR = new Parcelable.Creator<RunningAppsMetaDAO>() {
		public RunningAppsMetaDAO createFromParcel(Parcel in) {
			return new RunningAppsMetaDAO(in);
		}

		public RunningAppsMetaDAO[] newArray(int size) {
			return new RunningAppsMetaDAO[size];
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

		pc.writeString(appName);
		pc.writeString(packageName);
		pc.writeString(categoryName);
		pc.writeString(startTime);
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
}
