package com.ucl.news.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.ucl.news.dao.ArticleMetaDataDAO;
import com.ucl.news.main.ArticleActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoggingReadingScroll {

	Context context;
	String result2 = "as";
	ArrayList<ArticleMetaDataDAO> articleMetaData;
	ArticleActivity start;
	HttpAsyncTask task;

	public LoggingReadingScroll(Context con, ArticleActivity start,
			ArrayList<ArticleMetaDataDAO> _articleMetaData) {
		context = con;
		this.start = start;
		this.articleMetaData = _articleMetaData;
		task = new HttpAsyncTask();
		task.setMainActivity(start);
		task.setArticleMetaDataDAO(_articleMetaData);
		task.execute("http://habito.cs.ucl.ac.uk:9000/users/storeReadingScroll");
	}

	public boolean taskfinished() {
		if (task.getStatus() == AsyncTask.Status.FINISHED) {
			// My AsyncTask is done and onPostExecute was called
			return true;
		}
		return false;
	}

	public static String POST(String url, String article) {
		InputStream inputStream = null;
		String result = "";
		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			String json = "";
			json = article.toString();

			StringEntity se = new StringEntity(json);
			httpPost.setEntity(se);
			httpPost.setHeader("Content-type", "application/json");
			HttpResponse httpResponse = httpclient.execute(httpPost);
			inputStream = httpResponse.getEntity().getContent();

			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		private ArticleActivity articleActivity;
		private ArrayList<ArticleMetaDataDAO> articleMetaDataDAO;

		public void setMainActivity(ArticleActivity mainActivity) {
			this.articleActivity = mainActivity;

		}

		public void setArticleMetaDataDAO(
				ArrayList<ArticleMetaDataDAO> _articleMetaDataDAO) {
			this.articleMetaDataDAO = _articleMetaDataDAO;
		}

		@Override
		protected String doInBackground(String... urls) {
			String jsonString;

			Gson gsonObj = new Gson();
			jsonString = gsonObj.toJson(articleMetaData);

			return POST(urls[0], jsonString);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			// Toast.makeText(context, "Received "+result+" from couch",
			// Toast.LENGTH_LONG).show();
			result2 = result;
			try {
				articleActivity.storeReadingScroll(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public String myMethod() {
		// handle value
		return result2;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}
}
