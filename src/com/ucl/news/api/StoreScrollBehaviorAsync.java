package com.ucl.news.api;

import java.util.ArrayList;
import java.util.List;

import com.ucl.news.dao.ArticleMetaDataDAO;
import com.ucl.news.main.ArticleActivity;

import main.java.org.mcsoxford.rss.RSSFeed;
import main.java.org.mcsoxford.rss.RSSItem;
import main.java.org.mcsoxford.rss.RSSReader;
import main.java.org.mcsoxford.rss.RSSReaderException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class StoreScrollBehaviorAsync extends
		AsyncTask<ArrayList<ArticleMetaDataDAO>, Void, String> {

	public AsyncResponse delegate = null;
	private Context _context;
	private ArticleActivity _caller;

	public StoreScrollBehaviorAsync(Context context, ArticleActivity caller) {

		this._context = context;
		this._caller = caller;
	}

	protected String doInBackground(
			ArrayList<ArticleMetaDataDAO>... articleMetaData) {

		ArrayList<ArticleMetaDataDAO> articleScrolling = articleMetaData[0];

		for (int i = 0; i < articleScrolling.size(); i++) {
			LoggingReadingScroll logReadingScrollhpt = new LoggingReadingScroll(
					_context, _caller, articleScrolling.get(i));
		}

		return "test";
	}

	protected void onPreExecute() {
		Log.e("ASYNC", "PRE EXECUTE");
		// progress.show();
	}

	protected void onPostExecute(String res) {
		// TODO: check this.exception
		// TODO: do something with the feed
		Log.e("ASYNC", "POST EXECUTE");
		delegate.processFinish(res);
		// progress.dismiss();
	}

	protected void onProgressUpdate(Void... progress) {
		// setProgressPercent(progress[0]);
	}

	public interface AsyncResponse {
		void processFinish(String outputFeed);
	}
}
