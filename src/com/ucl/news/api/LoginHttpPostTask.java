package com.ucl.news.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.ucl.news.api.LoginDAO;
import com.ucl.news.main.LoginActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoginHttpPostTask {

	Context context;
	String result2 = "as";
	LoginDAO users;
	LoginActivity start;
	HttpAsyncTask task;

	public LoginHttpPostTask(Context con, LoginActivity start, LoginDAO _users) {
		context = con;
		this.start = start;
		this.users = _users;
		task = new HttpAsyncTask();
		task.setMainActivity(start);
		task.setUsersDAO(_users);
		task.execute("http://habito.cs.ucl.ac.uk:9000/users/login");
	}

	public boolean taskfinished() {
		if (task.getStatus() == AsyncTask.Status.FINISHED) {
			// My AsyncTask is done and onPostExecute was called
			return true;
		}
		return false;
	}

	public static String POST(String url, JSONObject users) {
		InputStream inputStream = null;
		String result = "";
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			String json = "";

			// 3. build jsonObject

			// jsonObject.accumulate("twitter", person.getTwitter());

			// 4. convert JSONObject to JSON to String
			json = users.toString();

			// ** Alternative way to convert Person object to JSON string usin
			// Jackson Lib
			// ObjectMapper mapper = new ObjectMapper();
			// json = mapper.writeValueAsString(person);

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			// httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		// 11. return result
		return result;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		private LoginActivity loginActivity;
		private LoginDAO users;

		public void setMainActivity(LoginActivity mainActivity) {
			this.loginActivity = mainActivity;

		}

		public void setUsersDAO(LoginDAO _users) {
			this.users = _users;
		}

		@Override
		protected String doInBackground(String... urls) {

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject
						.accumulate("email_address", users.getEmail_address());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				jsonObject.accumulate("password", users.getPassword());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return POST(urls[0], jsonObject);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			// Toast.makeText(context, "Received "+result+" from couch",
			// Toast.LENGTH_LONG).show();
			result2 = result;
			try {
				loginActivity.authenticate(result);
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
