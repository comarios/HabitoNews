package com.ucl.news.reader;

import java.util.List;

import main.java.org.mcsoxford.rss.RSSItem;

public class News {

	private String title;
	
	private List<RSSItem> content;

	public News(String title, List<RSSItem> content){
		
		this.title = title;
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<RSSItem> getContent() {
		return content;
	}

	public void setContent(List<RSSItem> content) {
		this.content = content;
	}
	
	
}
