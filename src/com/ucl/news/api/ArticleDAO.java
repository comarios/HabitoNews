package com.ucl.news.api;
import android.os.Parcel;
import android.os.Parcelable;

public class ArticleDAO implements Parcelable {

	private long userID;
	private String userSession;
	private long articleID;
	private String articleName;
	private String articleURL;
	private double readingDuration;
	private long startTimestamp;
	private long endTimestamp;
	private Boolean isScrollUsed;
	private Boolean isScrollReachedBottom;
	private long scrollDuration;
	private int numberOfWordsInArticle;

	public ArticleDAO() {
	}

	public ArticleDAO(long _userID, String _userSession, String _articleName,
			long _articleID, String _articleURL, long _readingDuration, long _startTimestamp,
			long _endTimestamp, Boolean _isScroolUsed,
			Boolean _isScrollReachedBottom, long _scrollDuration,
			int _numberOfWordsInArticle) {

		userID = _userID;
		userSession = _userSession;
		articleID = _articleID;
		articleName = _articleName;
		articleURL = _articleURL;
		readingDuration = _readingDuration;
		startTimestamp = _startTimestamp;
		endTimestamp = _endTimestamp;
		isScrollUsed = _isScroolUsed;
		isScrollReachedBottom = _isScrollReachedBottom;
		scrollDuration = _scrollDuration;
		numberOfWordsInArticle = _numberOfWordsInArticle;

	}

	public ArticleDAO(Parcel pc) {

		userID = pc.readLong();
		userSession = pc.readString();
		articleID = pc.readLong();
		articleName = pc.readString();
		articleURL = pc.readString();
		readingDuration = pc.readLong();
		startTimestamp = pc.readLong();
		endTimestamp = pc.readLong();
		isScrollUsed = (pc.readInt() == 0) ? false : true;
		isScrollReachedBottom = (pc.readInt() == 0) ? false : true;
		scrollDuration = pc.readLong();
		numberOfWordsInArticle = pc.readInt();

	}

	public static final Parcelable.Creator<ArticleDAO> CREATOR = new Parcelable.Creator<ArticleDAO>() {
		public ArticleDAO createFromParcel(Parcel in) {
			return new ArticleDAO(in);
		}

		public ArticleDAO[] newArray(int size) {
			return new ArticleDAO[size];
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
		pc.writeString(articleName);
		pc.writeString(articleURL);
		pc.writeDouble(readingDuration);
		pc.writeLong(startTimestamp);
		pc.writeLong(endTimestamp);
		pc.writeInt(isScrollUsed ? 1 : 0);
		pc.writeInt(isScrollReachedBottom ? 1 : 0);
		pc.writeLong(scrollDuration);
		pc.writeInt(numberOfWordsInArticle);
	}

	/**
	 * Getters and Setters
	 */

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
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

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getArticleURL() {
		return articleURL;
	}

	public void setArticleURL(String articleURL) {
		this.articleURL = articleURL;
	}

	public double getReadingDuration() {
		return readingDuration;
	}

	public void setReadingDuration(long readingDuration) {
		this.readingDuration = readingDuration;
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

	public Boolean getIsScrollUsed() {
		return isScrollUsed;
	}

	public void setIsScrollUsed(Boolean isScrollUsed) {
		this.isScrollUsed = isScrollUsed;
	}

	public Boolean getIsScrollReachedBottom() {
		return isScrollReachedBottom;
	}

	public void setIsScrollReachedBottom(Boolean isScrollReachedBottom) {
		this.isScrollReachedBottom = isScrollReachedBottom;
	}

	public long getScrollDuration() {
		return scrollDuration;
	}

	public void setScrollDuration(long scrollDuration) {
		this.scrollDuration = scrollDuration;
	}

	public int getNumberOfWordsInArticle() {
		return numberOfWordsInArticle;
	}

	public void setNumberOfWordsInArticle(int numberOfWordsInArticle) {
		this.numberOfWordsInArticle = numberOfWordsInArticle;
	}
}
