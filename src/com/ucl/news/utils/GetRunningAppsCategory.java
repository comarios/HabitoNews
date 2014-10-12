package com.ucl.news.utils;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings.Secure;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.ResponseContext;
import com.ucl.news.dao.RunningAppsMetaDAO;

public class GetRunningAppsCategory extends AsyncTask<String, Void, RunningAppsMetaDAO> {

	private Exception exception;
	private String appCategory;
	public AsyncRunningAppsResponse delegate = null;
	private Context applicationContext;
	private static final String NEWS_MAGAZINES = "News & Magazines";
	
	public GetRunningAppsCategory(Context _context) {
		applicationContext = _context;
	}

	protected RunningAppsMetaDAO doInBackground(String... urls) {
		try {
			final RunningAppsMetaDAO ramDAO = new RunningAppsMetaDAO();
			
			ramDAO.setAppName(urls[0]);
			ramDAO.setPackageName(urls[1]);
			ramDAO.setStartTime(urls[2]);
			
			System.out.println("search term: " + urls[0]);
//			String androidID = Secure.getString(
//					applicationContext.getContentResolver(), Secure.ANDROID_ID);
			MarketSession session = new MarketSession();
			session.login("habitonewsucl@gmail.com", "weloveadaptive");
			session.getContext().setAndroidId("dead000beef");//dead000beef

			String searchTerm = "pname:<com.cnn.mobile.android.phone>";
			
			AppsRequest appsRequest = AppsRequest.newBuilder()
					.setQuery(urls[0]).setStartIndex(0).setEntriesCount(10)
					.setWithExtendedInfo(true).build();

			session.append(appsRequest, new Callback<AppsResponse>() {
				@Override
				public void onResult(ResponseContext context,
						AppsResponse response) {
					// Your code here
					// response.getApp(0).getCreator() ...
					// see AppsResponse class definition for more infos
					System.out.println("response from Market API");
					List<App> apps = response.getAppList();
					System.out.println("apps lenght: " + apps.size());
					for (App app : apps) {
						
						appCategory = app.getExtendedInfo().getCategory();
						System.out.println("appCategory from GetRunn: " + appCategory);
						if(appCategory.equals(NEWS_MAGAZINES)){
							ramDAO.setCategoryName(appCategory);
						}
//						System.out.println("app category: "
//								+ app.getExtendedInfo().getCategory());
					}
				}
			});

			session.flush();

			return ramDAO;
		} catch (Exception e) {
			this.exception = e;
			return null;
		}
	}

	protected void onPostExecute(RunningAppsMetaDAO ramDAO) {
		// TODO: check this.exception
		// TODO: do something with the feed

		delegate.onProcessFinish(ramDAO);
		//System.out.println("async executed here : " + category);
	}

	public interface AsyncRunningAppsResponse {
		void onProcessFinish(RunningAppsMetaDAO ramDAO);
	}
}
