package com.ucl.news.reader;

import java.util.Date;
import java.util.List;

import main.java.org.mcsoxford.rss.MediaEnclosure;
import main.java.org.mcsoxford.rss.MediaThumbnail;
import android.os.Parcel;
import android.os.Parcelable;

public class RSSItems implements Parcelable {

	private String Content;
	private String Description;
	private String Title;
	private List<String> Categories;
	private String Link;
	private List<MediaThumbnail> Thumbnails;
	private MediaEnclosure Enclosure;
	private Date PubDate;

	public RSSItems(String Content, String Description, String Title,
			List<String> Categories, String Link,
			List<MediaThumbnail> Thumbnails, MediaEnclosure Enclosure,
			Date PubDate) {

		this.Content = Content;
		this.Description = Description;
		this.Title = Title;
		this.Categories = Categories;
		this.Link = Link;
		this.Thumbnails = Thumbnails;
		this.Enclosure = Enclosure;
	}

	public RSSItems(String Title, String Link) {
		this.Title = Title;
		this.Link = Link;
	}

	public RSSItems(Parcel in) {
		String[] data = new String[2];

		in.readStringArray(data);

		this.Title = data[0];
		this.Link = data[1];
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeStringArray(new String[] { this.Title, this.Link });
	}

	public static final Parcelable.Creator<RSSItems> CREATOR = new Parcelable.Creator<RSSItems>() {
		public RSSItems createFromParcel(Parcel in) {
			return new RSSItems(in);
		}

		public RSSItems[] newArray(int size) {
			return new RSSItems[size];
		}
	};

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public List<String> getCategories() {
		return Categories;
	}

	public void setCategories(List<String> categories) {
		Categories = categories;
	}

	public String getLink() {
		return Link;
	}

	public void setLink(String link) {
		Link = link;
	}

	public List<MediaThumbnail> getThumbnails() {
		return Thumbnails;
	}

	public void setThumbnails(List<MediaThumbnail> thumbnails) {
		this.Thumbnails = thumbnails;
	}

	public MediaEnclosure getEnclosure() {
		return Enclosure;
	}

	public void setEnclosure(MediaEnclosure enclosure) {
		Enclosure = enclosure;
	}

	public Date getPubDate() {
		return PubDate;
	}

	public void setPubDate(Date pubDate) {
		PubDate = pubDate;
	}

}
