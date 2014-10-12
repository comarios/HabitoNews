package com.ucl.news.reader;

import java.util.ArrayList;
import java.util.List;

import main.java.org.mcsoxford.rss.RSSFeed;
import main.java.org.mcsoxford.rss.RSSItem;
import main.java.org.mcsoxford.rss.RSSReader;
import main.java.org.mcsoxford.rss.RSSReaderException;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RetrieveFeedTask extends
		AsyncTask<String, Integer, ArrayList<List<RSSItem>>> {
	private RSSReader reader;
	private ArrayList<RSSFeed> feeds;
	private ArrayList<List<RSSItem>> rssItems;
	public AsyncResponse delegate = null;
	private ArrayList<RSSItem> tempRSS = null;
	private final int NUM_OF_ITEMS = 9;

	public RetrieveFeedTask(Context context) {

		// progress = new ProgressDialog(context);
		// progress.setMessage("Loading...");

	}

	protected ArrayList<List<RSSItem>> doInBackground(String... url) {
		reader = new RSSReader();
		feeds = new ArrayList<RSSFeed>();
		rssItems = new ArrayList<List<RSSItem>>();

		for (int i = 0; i < url.length; i++) {

			try {
				feeds.add(reader.load(url[i]));
			} catch (RSSReaderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			tempRSS = new ArrayList<RSSItem>();

			for (int j = 0; j < NUM_OF_ITEMS; j++) {
				tempRSS.add(feeds.get(i).getItems().get(j));

			}
			rssItems.add(tempRSS);
		}

		return rssItems;
	}

	protected void onPreExecute() {
		Log.e("ASYNC", "PRE EXECUTE");
		// progress.show();
	}

	protected void onPostExecute(ArrayList<List<RSSItem>> feed) {
		// TODO: check this.exception
		// TODO: do something with the feed
		Log.e("ASYNC", "POST EXECUTE");
		delegate.processFinish(feed);
		// progress.dismiss();
	}

	protected void onProgressUpdate(Integer... progress) {
		// setProgressPercent(progress[0]);
	}

	public interface AsyncResponse {
		void processFinish(ArrayList<List<RSSItem>> outputFeed);
	}
}
