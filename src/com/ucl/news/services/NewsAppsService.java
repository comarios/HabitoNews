package com.ucl.news.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.ucl.news.dao.RunningAppsDAO;
import com.ucl.news.dao.RunningAppsMetaDAO;
import com.ucl.news.main.MainActivity;
import com.ucl.news.utils.AutoLogin;
import com.ucl.news.utils.GetRunningAppsCategory;
import com.ucl.news.utils.WellKnownNewsApps;
import com.ucl.news.utils.GetRunningAppsCategory.AsyncRunningAppsResponse;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

public class NewsAppsService extends Service implements
		AsyncRunningAppsResponse {

	private static final String TAG = NewsAppsService.class.getSimpleName();
	private static final int FIVE_SECONDS = 5000;
	private ActivityManager am;
	private GetRunningAppsCategory asyncGetRunningApps;
	private static final String NEWS_MAGAZINES = "News & Magazines";
	private GPSTracker gps;
	private Handler handler;
	private static Timer timer;
	private double lat;
	private double lon;
	private TimerTask tt;

	// private ActivityManager am = (ActivityManager)
	// this.getApplicationContext().getSystemService(ACTIVITY_SERVICE);

	// private TimerTask updateTask = new TimerTask() {
	// @Override
	// public void run() {
	// Log.i(TAG, "Running");
	// // String packageName =
	// // am.getRunningTasks(1).get(0).topActivity.getPackageName();
	// // System.out.println("packageName: " + packageName);
	// }
	// };

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service creating");
		handler = new Handler();
		
		if (timer != null) {
			timer.cancel();
		} else {
			timer = new Timer();
		}

		tt = new TimerTask() {
			@Override
			public void run() {
				Log.i(TAG, "running");
				handleCommand();
			}
		};

		timer.scheduleAtFixedRate(tt, 0, FIVE_SECONDS);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Service destroying");
		tt.cancel();
		timer.cancel();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_NOT_STICKY;
	}

	private void handleCommand() {

		System.out.println("screen mode: " + getScreenMode());
		System.out.println("isRunning: "
				+ isMyServiceRunning(NewsAppsService.class));
		if (getScreenMode() && isMyServiceRunning(NewsAppsService.class)) {

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

			SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			String dateTimeStr = dateformat.format(new Date().getTime());

			/**
			 * Check if the app is a news app from the WellKnow list of news
			 * apps
			 */
			System.out.println("check for packageName: " + packageName);
			if (WellKnownNewsApps.getInstance().isNewsAppExist(packageName)) {

				RunningAppsDAO raDAORow = new RunningAppsDAO();

				raDAORow.setUserID(AutoLogin.getUserID(AutoLogin
						.getSettingsFile(getApplicationContext())));
				raDAORow.setUserSession(AutoLogin.getUserSession(AutoLogin
						.getSettingsFile(getApplicationContext())));
				raDAORow.setAppName(applicationName);
				raDAORow.setPackageName(packageName);
				raDAORow.setCategoryName(NEWS_MAGAZINES);
				raDAORow.setStartTime(dateTimeStr);

				Runnable runnable = new Runnable() {
		            @Override
		            public void run() {
		                handler.post(new Runnable() { // This thread runs in the UI
		                    @Override
		                    public void run() {
		                    	// Get Location of accessed news app
		        				gps = new GPSTracker(getApplicationContext());

		        				// check if GPS enabled
		        				if (gps.canGetLocation()) {

		        					lat = gps.getLatitude();
		        					lon = gps.getLongitude();
		        					
		        					System.out.println("Your Location is - \nLat: " + lat
		        							+ "\nLong: " + lon);
		        					// \n is for new line
		        					// Toast.makeText(getApplicationContext(),
		        					// "Your Location is - \nLat: " + latitude + "\nLong: " +
		        					// longitude, Toast.LENGTH_LONG).show();
		        				} else {
		        					// can't get location
		        					// GPS or Network is not enabled
		        					// Ask user to enable GPS/network in settings
		        					// gps.showSettingsAlert();
		        					System.out.println("can't get location");
		        				}

		                    }
		                });
		            }
		        };
		        new Thread(runnable).start();
		        
		        raDAORow.setLat(lat);
				raDAORow.setLon(lon);
				
				// Handler handler = new Handler(Looper.getMainLooper());
				//
				// handler = new Runnable() {
				// @Override
				// public void run() {
				// // Run your task here
				// // Get Location of accessed news app
				// gps = new GPSTracker(getApplicationContext());
				//
				// // check if GPS enabled
				// if (gps.canGetLocation()) {
				//
				// double latitude = gps.getLatitude();
				// double longitude = gps.getLongitude();
				// raDAORow.setLat(latitude);
				// raDAORow.setLon(longitude);
				// System.out.println("Your Location is - \nLat: " + latitude
				// + "\nLong: " + longitude);
				// // \n is for new line
				// // Toast.makeText(getApplicationContext(),
				// // "Your Location is - \nLat: " + latitude + "\nLong: " +
				// // longitude, Toast.LENGTH_LONG).show();
				// } else {
				// // can't get location
				// // GPS or Network is not enabled
				// // Ask user to enable GPS/network in settings
				// // gps.showSettingsAlert();
				// System.out.println("can't get location");
				// }
				// }
				// },1000);
				//

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
					System.out.println("finish appName: "
							+ raDAORow.getAppName());
					System.out.println("finish packageName: "
							+ raDAORow.getPackageName());
					System.out.println("finish categoryName: "
							+ raDAORow.getCategoryName());

					System.out.println("finish categoryName: "
							+ raDAORow.getCategoryName());
					System.out.println("finish startTime: "
							+ raDAORow.getStartTime());
				}

			} else {
				System.out.println("app not found in the list");
			}

			// asyncGetRunningApps = new GetRunningAppsCategory(
			// getApplicationContext());
			// asyncGetRunningApps.execute(applicationName, packageName,
			// dateTimeStr);
			// asyncGetRunningApps.delegate = NewsAppsService.this;

		} else {
			System.out.println("service not killed but doesn't log");
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
					System.out.println("finish appName: "
							+ raDAORow.getAppName());
					System.out.println("finish packageName: "
							+ raDAORow.getPackageName());
					System.out.println("finish categoryName: "
							+ raDAORow.getCategoryName());

					System.out.println("finish categoryName: "
							+ raDAORow.getCategoryName());
					System.out.println("finish startTime: "
							+ raDAORow.getStartTime());
				}

			}
		}

	}

	private void appendRunningAppsInLocalFile(RunningAppsDAO raDAO) {

		File newsAppsFile = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "HabitoNews_Study/news_runningApps.txt");
		if (newsAppsFile.exists()) {
			System.out.println("file found");
			try {
				BufferedWriter bW;

				bW = new BufferedWriter(new FileWriter(newsAppsFile, true));

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
		} else {
			// Do something else.
			System.out.println("news_runningApps file not found");
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
}