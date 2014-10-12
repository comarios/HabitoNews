package com.ucl.news.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import main.java.org.mcsoxford.rss.RSSItem;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.ResponseContext;
import com.ucl.news.dao.RunningAppsDAO;
import com.ucl.news.dao.RunningAppsMetaDAO;
import com.ucl.news.main.MainActivity;
import com.ucl.news.reader.RetrieveFeedTask.AsyncResponse;
import com.ucl.news.utils.AutoLogin;
import com.ucl.news.utils.GetRunningAppsCategory;
import com.ucl.news.utils.GetRunningAppsCategory.AsyncRunningAppsResponse;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

public class RunningAppsService extends Service implements
		AsyncRunningAppsResponse {

	private Handler mHandler = new Handler();
	private static final int ONE_MINUTE = 60000;
	private static final int FIVE_SECONDS = 2000;
	private String curPackageName = null;
	private boolean initialServiceStart = true;
	private ActivityManager am;
	private GetRunningAppsCategory asyncGetRunningApps;
	private AsyncRunningAppsResponse delegate = null;
	private static final String NEWS_MAGAZINES = "News & Magazines";
	private static final String AndroidCore = "android";
	private GPSTracker gps;
	// private ArrayList<RunningAppsDAO> raDAO;

	private Runnable periodicTask = new Runnable() {
		public void run() {
			Log.v("PeriodicTimerService", "Awake");
			handleCommand();
			mHandler.postDelayed(periodicTask, FIVE_SECONDS);
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		System.out.println("service created");
		super.onCreate();
		// raDAO = new ArrayList<RunningAppsDAO>();

		mHandler.postDelayed(periodicTask, FIVE_SECONDS);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		System.out.println("service destroyed from RunningApps");
		// mHandler.removeCallbacks(periodicTask);
		// stopSelf();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Toast.makeText(this, "OnstartCommand", Toast.LENGTH_LONG).show();
		System.out.println("service OnstartCommand");

		return START_NOT_STICKY;
	}

	private void handleCommand() {

		System.out.println("screen mode: " + getScreenMode());
		System.out.println("isRunning: "
				+ isMyServiceRunning(RunningAppsService.class));
		if (getScreenMode() && isMyServiceRunning(RunningAppsService.class)) {

			am = (ActivityManager) getApplicationContext().getSystemService(
					Activity.ACTIVITY_SERVICE);

			String packageName = am.getRunningTasks(1).get(0).topActivity
					.getPackageName();
			System.out.println("packageName: " + packageName);

			System.out.println("packageName look for this one");

			PackageManager pm = getApplicationContext().getPackageManager();
			ApplicationInfo ai;
			try {
				ai = pm.getApplicationInfo(packageName, 0);
			} catch (final NameNotFoundException e) {
				ai = null;
			}
			final String applicationName = (String) (ai != null ? pm
					.getApplicationLabel(ai) : "(unknown)");

			// new GetRunningAppCategory().execute(applicationName);
			SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd HH:MM:SS.SSS");
			String dateTimeStr = dateformat.format(new Date().getTime());

			asyncGetRunningApps = new GetRunningAppsCategory(
					getApplicationContext());
			asyncGetRunningApps.execute(applicationName, packageName,
					dateTimeStr);
			asyncGetRunningApps.delegate = RunningAppsService.this;

			// if (initialServiceStart) {
			// curPackageName = am.getRunningTasks(1).get(0).topActivity
			// .getPackageName();
			// initialServiceStart = !initialServiceStart;
			// }
		} else {
			System.out.println("service not killed but doesn't log");
			// stopSelf();
		}
	}

	private boolean getScreenMode() {
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		return powerManager.isScreenOn();
	}

	@Override
	public void onProcessFinish(RunningAppsMetaDAO ramDAO) {
		// TODO Auto-generated method stub

		/*
		 * Discard non News & Magazines categories, log only news apps
		 */
		if (ramDAO != null) {
			if (ramDAO.getCategoryName() != null
					&& ramDAO.getCategoryName().equals(NEWS_MAGAZINES)) {

				System.out.println("this is news app");
				RunningAppsDAO raDAORow = new RunningAppsDAO();

				raDAORow.setUserID(AutoLogin.getUserID(AutoLogin
						.getSettingsFile(getApplicationContext())));
				raDAORow.setUserSession(AutoLogin.getUserSession(AutoLogin
						.getSettingsFile(getApplicationContext())));
				raDAORow.setAppName(ramDAO.getAppName());
				raDAORow.setPackageName(ramDAO.getPackageName());
				raDAORow.setCategoryName(ramDAO.getCategoryName());
				raDAORow.setStartTime(ramDAO.getStartTime());

				// raDAO.add(raDAORow);

				// Get Location of accessed news app

				gps = new GPSTracker(getApplicationContext());

				// check if GPS enabled
				if (gps.canGetLocation()) {

					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();
					raDAORow.setLat(latitude);
					raDAORow.setLon(longitude);
					System.out.println("Your Location is - \nLat: " + latitude
							+ "\nLong: ");
					// \n is for new line
					// Toast.makeText(getApplicationContext(),
					// "Your Location is - \nLat: " + latitude + "\nLong: " +
					// longitude, Toast.LENGTH_LONG).show();
				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					// gps.showSettingsAlert();
				}

				if (AutoLogin.getIsLoggedIN(AutoLogin
						.getSettingsFile(getApplicationContext()))) {

					appendRunningAppsInLocalFile(raDAORow);

					System.out.println("This app should be logged");
					System.out.println("finish userID: "
							+ AutoLogin.getUserID(AutoLogin
									.getSettingsFile(getApplicationContext())));
					System.out.println("finish userSession: "
							+ AutoLogin.getUserSession(AutoLogin
									.getSettingsFile(getApplicationContext())));
					System.out
							.println("finish appName: " + ramDAO.getAppName());
					System.out.println("finish packageName: "
							+ ramDAO.getPackageName());
					System.out.println("finish categoryName: "
							+ ramDAO.getCategoryName());

					System.out.println("finish categoryName: "
							+ ramDAO.getCategoryName());
					System.out.println("finish startTime: "
							+ ramDAO.getStartTime());
				}

			}
		}

	}

	private void appendRunningAppsInLocalFile(RunningAppsDAO raDAO) {

		if (MainActivity.runningAppsFile.exists()) {
			try {
				BufferedWriter bW;

				bW = new BufferedWriter(new FileWriter(
						MainActivity.runningAppsFile, true));

				String delimeter = ";";
				String row = raDAO.getUserID() + delimeter
						+ raDAO.getUserSession() + delimeter
						+ raDAO.getAppName() + delimeter
						+ raDAO.getPackageName() + delimeter
						+ raDAO.getCategoryName() + delimeter + raDAO.getLat()
						+ delimeter + raDAO.getLon() + delimeter
						+ raDAO.getStartTime() + delimeter;

				bW.write(row);
				bW.newLine();
				bW.flush();

				bW.close();
			} catch (Exception e) {

			}
		}

	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	// class GetRunningAppCategory extends AsyncTask<String, Void, String> {
	//
	// private Exception exception;
	// private String appCategory;
	//
	// protected String doInBackground(String... urls) {
	// try {
	// String androidID = Secure.getString(getApplicationContext()
	// .getContentResolver(), Secure.ANDROID_ID);
	// MarketSession session = new MarketSession();
	// session.login("habitonewsucl@gmail.com", "weloveadaptive");
	// session.getContext().setAndroidId("dead000beef");
	//
	// AppsRequest appsRequest = AppsRequest.newBuilder()
	// .setQuery(urls[0]).setStartIndex(0).setEntriesCount(10)
	// .setWithExtendedInfo(true).build();
	//
	// session.append(appsRequest, new Callback<AppsResponse>() {
	// @Override
	// public void onResult(ResponseContext context,
	// AppsResponse response) {
	// // Your code here
	// // response.getApp(0).getCreator() ...
	// // see AppsResponse class definition for more infos
	// List<App> apps = response.getAppList();
	// for (App app : apps) {
	// appCategory = app.getExtendedInfo().getCategory();
	// System.out.println("app category: "
	// + app.getExtendedInfo().getCategory());
	// }
	// }
	// });
	//
	// session.flush();
	//
	// return appCategory;
	// } catch (Exception e) {
	// this.exception = e;
	// return null;
	// }
	// }
	//
	// protected void onPostExecute(String category) {
	// // TODO: check this.exception
	// // TODO: do something with the feed
	//
	// delegate.processFinish(category);
	// System.out.println("async executed here : " + category);
	// }
	//
	// }

	// public interface AsyncRunningAppsResponse {
	// void processFinish(String category);
	// }
}