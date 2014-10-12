package com.ucl.news.logging;

import com.ucl.news.reader.RSSItems;

import android.os.Parcel;
import android.os.Parcelable;

public class LoggedData implements Parcelable {

	private String articleName;
	private long startTimestamp;
	private long endTimestamp;
	private long periodOfReading;

	public LoggedData(String articleName, long startTimestamp, long endTimestamp) {
		this.articleName = articleName;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.periodOfReading = periodOfReading;
	}

	public LoggedData(Parcel pc) {

		this.articleName = pc.readString();
		this.startTimestamp = pc.readLong();
		this.endTimestamp = pc.readLong();
		this.periodOfReading = pc.readLong();
	}

	public LoggedData(){}
	
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel pc, int flags) {

		pc.writeString(articleName);
		pc.writeLong(startTimestamp);
		pc.writeLong(endTimestamp);
		pc.writeLong(periodOfReading);
	}

	public static final Parcelable.Creator<LoggedData> CREATOR = new Parcelable.Creator<LoggedData>() {
		public LoggedData createFromParcel(Parcel in) {
			return new LoggedData(in);
		}

		public LoggedData[] newArray(int size) {
			return new LoggedData[size];
		}
	};

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public long getPeriodOfReading() {
		return periodOfReading;
	}

	public void setPeriodOfReading(long periodOfReading) {
		this.periodOfReading = periodOfReading;
	}
}
