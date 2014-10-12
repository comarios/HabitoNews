package com.ucl.news.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleMetaDataDAO implements Parcelable {

	private long userID;
	private String userSession;
	private long articleID;
	private int scrollRange;
	private int scrollExtent;
	private int scrollOffset;
	private String dateTime;

	public ArticleMetaDataDAO() {
	}

	public ArticleMetaDataDAO(long _userID, String _userSession,
			long _articleID, int _scrollRange, int _scrollExtent,
			int _scrollOffset, String _dateTime) {

		userID = _userID;
		userSession = _userSession;
		articleID = _articleID;
		scrollRange = _scrollRange;
		scrollExtent = _scrollExtent;
		scrollOffset = _scrollOffset;
		dateTime = _dateTime;
	}

	public ArticleMetaDataDAO(Parcel pc) {

		userID = pc.readLong();
		userSession = pc.readString();
		articleID = pc.readLong();
		scrollRange = pc.readInt();
		scrollExtent = pc.readInt();
		scrollOffset = pc.readInt();
		dateTime = pc.readString();
	}

	public static final Parcelable.Creator<ArticleMetaDataDAO> CREATOR = new Parcelable.Creator<ArticleMetaDataDAO>() {
		public ArticleMetaDataDAO createFromParcel(Parcel in) {
			return new ArticleMetaDataDAO(in);
		}

		public ArticleMetaDataDAO[] newArray(int size) {
			return new ArticleMetaDataDAO[size];
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
		pc.writeLong(articleID);
		pc.writeInt(scrollRange);
		pc.writeInt(scrollExtent);
		pc.writeInt(scrollOffset);
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

	public long getArticleID() {
		return articleID;
	}

	public void setArticleID(long articleID) {
		this.articleID = articleID;
	}

	public int getScrollRange() {
		return scrollRange;
	}

	public void setScrollRange(int scrollRange) {
		this.scrollRange = scrollRange;
	}

	public int getScrollExtent() {
		return scrollExtent;
	}

	public void setScrollExtent(int scrollExtent) {
		this.scrollExtent = scrollExtent;
	}

	public int getScrollOffset() {
		return scrollOffset;
	}

	public void setScrollOffset(int scrollOffset) {
		this.scrollOffset = scrollOffset;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

}
