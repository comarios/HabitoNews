package com.ucl.news.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import main.java.org.mcsoxford.rss.RSSItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.android.market.api.Main;
import com.ucl.news.adapters.ViewPagerAdapter;
import com.ucl.news.api.ArticleDAO;
import com.ucl.news.api.LoggingReadingBehavior;
import com.ucl.news.api.LoggingReadingScroll;
import com.ucl.news.api.RegistrationHttpPostTask;
import com.ucl.news.api.StoreScrollBehaviorAsync;
import com.ucl.news.api.StoreScrollBehaviorAsync.AsyncResponse;
import com.ucl.news.articles.ArticleWebView;
import com.ucl.news.articles.ArticleWebView.OnBottomReachedListener;
import com.ucl.news.dao.ArticleMetaDataDAO;
import com.ucl.news.logging.LoggedData;
import com.ucl.news.reader.News;
import com.ucl.news.reader.RSSItems;
import com.ucl.news.reader.RetrieveFeedTask;
import com.ucl.news.utils.AutoLogin;
import com.ucl.newsreader.R;

public class ArticleActivity extends Activity implements
		OnBottomReachedListener, AsyncResponse {

	private ArticleWebView webView;
	private ArticleDAO aDAO;
	public static ArrayList<ArticleMetaDataDAO> articleMetaData;
	private StoreScrollBehaviorAsync asyncStoreScroll;
	private long startReading;
	private long endReading;
	public static long articleID;
	private static int numberOfWordsInArticle;
	private static Boolean isScrollUsed = false;
	private Boolean isScrollReachedBottom = false;
	public static final String UPDATE = "com.ucl.news.main.ArticleActivity.action.UPDATE";
	public static final String MSG_SEND = "com.ucl.news.main.ArticleActivity.MSG_SEND";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.articles);

		if (customTitleSupported) {
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.custom_title);
		}

		TextView headerTitle = (TextView) findViewById(R.id.headerArticleTitle);

		final RSSItems rss = getIntent().getParcelableExtra(
				ViewPagerAdapter.EXTRA_MESSAGE);

		headerTitle.setText(rss.getTitle());

		// Change the url to mobile version
		String rssLink = rss.getLink();
		String mLinkURL = rssLink.replaceAll("www", "m");

		// System.out.println("RSS LINK: " + rss.getLink());
		// System.out.println("M LINK: " + mLinkURL);
		new ParseHTML().execute(mLinkURL);

		aDAO = new ArticleDAO();
		articleMetaData = new ArrayList<ArticleMetaDataDAO>();

		aDAO.setUserID(AutoLogin.getUserID(AutoLogin
				.getSettingsFile(getApplicationContext())));
		aDAO.setUserSession(AutoLogin.getUserSession(AutoLogin
				.getSettingsFile(getApplicationContext())));
		aDAO.setArticleName(rss.getTitle());
		aDAO.setArticleURL(Long.toString(extractArticleID(rss.getLink())));

		articleID = extractArticleID(rss.getLink());
		startReading = new Date().getTime();
		aDAO.setStartTimestamp(startReading);
	}

	private long extractArticleID(String articleURL) {

		String[] URLTokens;
		/*
		 * Parse it as sport article.
		 */
		if (articleURL.contains("/sport/0/")) {

			URLTokens = articleURL.split("/");
		} else {
			String[] URLTokensTemp = articleURL.split("#");
			if (URLTokensTemp[0].contains("-"))
				URLTokens = URLTokensTemp[0].split("-");
			else
				URLTokens = URLTokensTemp[0].split("/");
		}

		// for(int i = 0; i < URLTokens.length; i++)
		// System.out.println("storyID: " + URLTokens[URLTokens.length - 1]);
		return Long.parseLong(URLTokens[URLTokens.length - 1]);
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		
		endReading = new Date().getTime();
		aDAO.setEndTimestamp(endReading);
		aDAO.setReadingDuration(TimeUnit.MILLISECONDS.toSeconds(endReading
				- startReading));
		aDAO.setNumberOfWordsInArticle(numberOfWordsInArticle);
		aDAO.setIsScrollReachedBottom(isScrollReachedBottom);
		aDAO.setIsScrollUsed(isScrollUsed);

		LoggingReadingBehavior logReadinghpt = new LoggingReadingBehavior(
				getApplicationContext(), this, aDAO);

		File scrollFile = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "HabitoNews_Study/scroll_position.txt");
		
		if (scrollFile.exists()) {
			try {
				BufferedWriter bW;

				bW = new BufferedWriter(new FileWriter(scrollFile, true));
				for (int i = 0; i < articleMetaData.size(); i++) {

					String delimeter = ";";
					String row = articleMetaData.get(i).getUserID() + delimeter
							+ articleMetaData.get(i).getUserSession()
							+ delimeter + articleMetaData.get(i).getArticleID()
							+ delimeter
							+ articleMetaData.get(i).getScrollRange()
							+ delimeter
							+ articleMetaData.get(i).getScrollExtent()
							+ delimeter
							+ articleMetaData.get(i).getScrollOffset()
							+ delimeter + articleMetaData.get(i).getDateTime()
							+ delimeter;

					bW.write(row);
					bW.newLine();
					bW.flush();
				}

				bW.close();
			} catch (Exception e) {

			}
		}else {
			// Do something else.
			System.out.println("scroll_position file not found");
		}
		MainActivity.CallingFromArticleActivity = true;
//		Intent i = new Intent(ArticleActivity.this, MainActivity.class);
//		i.putExtra("ref", "ArticleCaller");
//		startActivity(i);
	}

	@Override
	public void processFinish(String res) {
		// TODO Auto-generated method stub

		Log.e("HERE CALL", "test");
		// System.out.println("result async: " + res);

	}

	public void storeReadingScroll(String result) {

		// Log.e("RESULT READING Scroll", result);
	}

	public void storeReadingBehavior(String result) {

		// Log.e("RESULT READING", result);
	}

	public static void setIsScrollUsedCustom(Boolean _isScrollUsed) {
		isScrollUsed = _isScrollUsed;
	}

	private void broadcastMSG(ArticleDAO aDAO) {
		Intent intent = new Intent(UPDATE);
		// System.out.println("Dao: " + aDAO);

		intent.putExtra(MSG_SEND, aDAO);
		sendBroadcast(intent);
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}
	}

	public int countWords(String str) {
		if (str == null || str.isEmpty())
			return 0;

		int count = 0;
		for (int e = 0; e < str.length(); e++) {
			if (str.charAt(e) != ' ') {
				count++;
				while (str.charAt(e) != ' ' && e < str.length() - 1) {
					e++;
				}
			}
		}
		return count;
	}

	private class ParseHTML extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			Document doc;

			String htmlcode = "";

			 System.out.println("hereHTML: " + htmlcode);
			try {
				doc = Jsoup.connect(params[0]).get();

				doc.select("script").remove();
				doc.select("div#orb-footer").remove();
				doc.select("footer").remove();
				doc.select("div.share").remove();
				doc.select("div.bbccom_display_none").remove();
				doc.select("div.navigation--footer").remove();
				doc.select("img#livestats").remove();
				doc.select("div.push").remove();
				doc.select("div.story-more-push").remove();
				doc.select("div.wrapper").remove();
				doc.select("div.column--secondary").remove();
				doc.select("div.orb-nav-section").remove();
				doc.select("div.site-brand-inner").remove();
				doc.select("div.orb-header").remove();
				doc.select("div#js-navigation-sections-wrapper").remove();
				doc.select("div.story-more").remove();
				doc.select("script").remove();
				doc.select("div.more-index").remove();
				doc.select("h2.small-promo-group__title").remove();
				doc.select("div.small-promo-group__body").remove();
				doc.select("span.index-title").remove();
				doc.select("span.index-title__container").remove();
				doc.select("div.nav-top").remove();
				doc.select("div.share-this").remove();
				doc.select("div.nav-bottom").remove();
				doc.select("div.alert").remove();
				doc.select("div.pagination-bottom").remove();
				doc.select("div.mod.ll.see-alsos.bg").remove();
				doc.select("div.mod.ll.bg").remove();
				// System.out
				// .println("image size: " + doc.select("figure").size());
				// // doc.select("div#most-popular").remove();
				// doc.select("div.layout-block-b").remove();
				// doc.select("div.share-body-bottom").remove();
				// doc.select("div#page-bookmark-links-head").remove();
				// doc.select("div#id-status-nav").remove();
				// doc.select("div#blq-sign-in").remove();
				// doc.select("div#blq-acc-links").remove();
				// doc.select("div#blq-nav").remove();
				// doc.select("div#related-services").remove();
				// doc.select("div#news-related-sites").remove();
				// doc.select("div#blq-foot").remove();
				// doc.select("div#header-wrapper").remove();
				// doc.select("div#blq-masthead").remove();
				// //doc.select("div#blq-container-outer").remove();
				//
				//
				// // Remove new stuff added
				// doc.select("div#blq-global").remove();
				// doc.select("div.story-related").remove();
				htmlcode = doc.html();
				// System.out.println(doc);

				 System.out.println("hereDOC: " + doc);

				numberOfWordsInArticle = countWords(htmlcode);

				// System.out.println("countWords4: " + numberOfWordsInArticle);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return htmlcode;
		}

		@SuppressLint("SetJavaScriptEnabled")
		@Override
		protected void onPostExecute(String result) {
			webView = (ArticleWebView) findViewById(R.id.webViewArticleStory);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setWebViewClient(new MyWebViewClient());
			webView.loadData(result, "text/html", null);
			webView.setOnBottomReachedListener(ArticleActivity.this, 300);
		}
	}

	@Override
	public void onBottomReached(View v) {
		// TODO Auto-generated method stub
		System.out.println("Scroll down");
		isScrollReachedBottom = true;
		// Toast.makeText(this, "Bottom reached", Toast.LENGTH_LONG).show();
	}

}
