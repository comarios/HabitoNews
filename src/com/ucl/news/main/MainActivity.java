package com.ucl.news.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import main.java.org.mcsoxford.rss.RSSItem;
import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ucl.news.adapters.RowsAdapter;
import com.ucl.news.api.LoggingNavigationBehavior;
import com.ucl.news.api.LoggingReadingBehavior;
import com.ucl.news.api.NavigationDAO;
import com.ucl.news.api.Session;
import com.ucl.news.logging.Logger;
import com.ucl.news.reader.News;
import com.ucl.news.reader.RetrieveFeedTask;
import com.ucl.news.reader.RetrieveFeedTask.AsyncResponse;
import com.ucl.news.services.NewsAppsService;
import com.ucl.news.services.RunningAppsService;
import com.ucl.news.utils.AutoLogin;
import com.ucl.news.utils.Dialogs;
import com.ucl.news.utils.GPSLocation;
import com.ucl.news.utils.NetworkConnection;
import com.ucl.newsreader.R;

public class MainActivity extends Activity implements AsyncResponse {

	private ArrayList<News> news;
	private ProgressBar progress;
	private RowsAdapter rowsAdapter;
	private ListView entriesListView;
	private RetrieveFeedTask asyncTask;
	private String[] categories = { "Top Stories", "World", "UK", "Business",
			"Sports", "Politics", "Health", "Education & Family",
			"Science & Environment", "Technology", "Entertainment & Arts" };
	private NetworkConnection network = new NetworkConnection(MainActivity.this);
	private Intent logger;
	public static Session userSession = new Session();
	public static ArrayList<NavigationDAO> navigationDAO = new ArrayList<NavigationDAO>();
	public static boolean activitySwitchFlag = false;
	public static File scrollPositionFile;
	public static File runningAppsFile;
	public static File navigationalDataFile;
	private Intent runningAppsService;
	private Intent newsAppsService;
	public static boolean CallingFromArticleActivity = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		System.out.println("ON create called now2");
		setContentView(R.layout.activity_main);

		if (network.haveNetworkConnection()) {

			progress = (ProgressBar) findViewById(R.id.progressBar);

			int resourceID = R.layout.viewpager_main;
			entriesListView = (ListView) findViewById(R.id.mainVerticalList);
			news = new ArrayList<News>();
			rowsAdapter = new RowsAdapter(this, resourceID, news, this);
			entriesListView.setAdapter(rowsAdapter);

			// if(!AutoLogin.getIsLoggedIN(AutoLogin.getSettingsFile(getApplicationContext())))
			fetchRSS();
			CallingFromArticleActivity = false;

			/*
			 * Get GPS Location
			 */
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			LocationListener locationListener = new GPSLocation(
					getApplicationContext());
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 5000, 2, locationListener);

			this.newsAppsService = new Intent(NewsAppsService.class.getName());
			this.startService(newsAppsService);
			
			/*
			 * Create dir for storing scroll position
			 */
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// handle case of no SDCARD present
			} else {
				String dir = Environment.getExternalStorageDirectory()
						+ File.separator + "HabitoNews_Study";
				// create folder
				File folder = new File(dir); // folder name
				if (!folder.exists()) {
					folder.mkdir();
				}
				// create ScrollPosition file
				scrollPositionFile = new File(dir, "scroll_position.txt");
				try {
					scrollPositionFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// create RunningAppsFile file
				runningAppsFile = new File(dir, "news_runningApps.txt");
				try {
					runningAppsFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// create Navigational Data file
				navigationalDataFile = new File(dir, "navigational_data.txt");
				try {
					navigationalDataFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			
			//this.runningAppsService = new Intent(this.getApplicationContext(),
			//		RunningAppsService.class);

			//this.getApplicationContext().startService(this.runningAppsService);

			// }

			// navigationDAO = new ArrayList<NavigationDAO>();

			// // Get the UserID either from Login or Registration Activity
			// Intent i = getIntent();
			// // System.out.println("Intent:" + i);
			//
			// if (!i.hasExtra("ref")) {
			// System.out.println("App killed");
			// } else if (i != null
			// && (i.getStringExtra("ref").equals("LoginActivity")
			// || i.getStringExtra("ref").equals("WelcomeScreen") || i
			// .getStringExtra("ref").equals(
			// "RegistrationActivity"))) {
			//
			// // Initialize the NavigationDAO
			// navigationDAO = new ArrayList<NavigationDAO>();
			//
			//
			// } else if (i != null
			// && (i.getStringExtra("ref").equals("ArticleActivity"))) {
			// // Do nothing
			// System.out.println("From articles");
			// } else {
			// System.out.println("From outside");
			// }

		} else {
			new Dialogs().createDialogINTERNET(MainActivity.this,
					getApplicationContext());
		}

	}

	public void fetchRSS() {

		System.out.println("fetch HERE");
		progress.setVisibility(View.VISIBLE);
		asyncTask = new RetrieveFeedTask(getApplicationContext());
		asyncTask.execute("http://feeds.bbci.co.uk/news/rss.xml",
				"http://feeds.bbci.co.uk/news/world/rss.xml",
				"http://feeds.bbci.co.uk/news/uk/rss.xml",
				"http://feeds.bbci.co.uk/news/business/rss.xml",
				"http://feeds.bbci.co.uk/sport/0/rss.xml",
				"http://feeds.bbci.co.uk/news/politics/rss.xml",
				"http://feeds.bbci.co.uk/news/health/rss.xml",
				"http://feeds.bbci.co.uk/news/education/rss.xml",
				"http://feeds.bbci.co.uk/news/science_and_environment/rss.xml",
				"http://feeds.bbci.co.uk/news/technology/rss.xml",
				"http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml");
		asyncTask.delegate = MainActivity.this;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		// return true;

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_sync:
			rowsAdapter.clear();
			fetchRSS();
			return true;
		case R.id.action_logout:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void processFinish(ArrayList<List<RSSItem>> outputFeed) {
		// TODO Auto-generated method stub

		Log.e("HERE CALL", "test");

		// Treat the initial stage
		if (news.size() != categories.length) {
			for (int i = 0; i < outputFeed.size(); i++) {
				news.add(new News(categories[i], outputFeed.get(i)));
				// for (RSSItem item : outputFeed.get(i)) {
				// Log.e("RSS in list :", "" + i + ", " + item.getTitle());
				// }
				rowsAdapter.notifyDataSetChanged();
			}
		} else {

			for (int j = 0; j < news.size(); j++) {

				news.get(j).setContent(outputFeed.get(j));

			}
		}

		progress.setVisibility(View.INVISIBLE);

	}

	// public void startLogging() {
	//
	// /** Start InteractionService */
	// // IntentFilter movementFilter;
	// // movementFilter = new
	// // IntentFilter(InteractionService.MOVEMENT_UPDATE);
	// // serviceReceiver = new ServiceReceiver();
	// // registerReceiver(serviceReceiver, movementFilter);
	// System.out.println("MAIN");
	//
	// this.logger = new Intent(this.getApplicationContext(), Logger.class);
	//
	// this.getApplicationContext().startService(this.logger);
	// // startService(new Intent(this, InteractionService.class));
	// }
	//
	// // Stop the service
	// public void stopLogging() {
	// this.getApplicationContext().stopService(this.logger);
	// // stopService(new Intent(this, InteractionService.class));
	// }

	public void logout() {
		// Utils.saveToFileLoggedIN(this, getFilesDir(), Utils.IS_LOGGED_IN_NO);
		// userSession.setUserID(null);
		// userSession.setSession(null);
		// super.finish();
		// this.finish();
		String updateCredentials;
		updateCredentials = "NO"
				+ ";"
				+ AutoLogin.getUserID(AutoLogin
						.getSettingsFile(getApplicationContext()))
				+ ";"
				+ AutoLogin.getUserSession(AutoLogin
						.getSettingsFile(getApplicationContext())) + ";";
		AutoLogin.saveSettingsFile(getApplicationContext(), updateCredentials);

		System.out.println("logout: " + AutoLogin.getSettingsFile(getApplicationContext()));
		Intent i = new Intent(MainActivity.this, LoginActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
		startActivity(i);
		super.finish();
		this.finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("destroyed called");
		//this.stopService(newsAppsService);
		// super.finish();
		//this.getApplicationContext().stopService(this.runningAppsService);
	}

	@Override
	public void onPause() {
		super.onPause();

		CallingFromArticleActivity = false;

		// Intent callee = getIntent();
		//
		// System.out.println("navSize: " + navigationDAO.size());
		// System.out.println("callee ref: " + callee.getStringExtra("ref"));
		//
		// if (callee != null && callee.hasExtra("ref")) {
		// System.out.println("grapseta: " + callee.getStringExtra("ref"));
		//
		// if (!callee.getStringExtra("ref").equals("ArticleCaller")) {
		// if (navigationDAO.size() != 0) {
		//
		// System.out.println("is not empty");
		// for (int i = 0; i < navigationDAO.size(); i++) {
		//
		// System.out.println("orderID: "
		// + navigationDAO.get(i).getOrderID());
		// LoggingNavigationBehavior logNavigationhpt = new
		// LoggingNavigationBehavior(
		// getApplicationContext(), this, navigationDAO.get(i));
		// }
		// } else {
		// System.out.println("No navigation");
		// }
		// }
		// }

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		  Log.d("CDA", "onBackPressed Called");
		  Intent setIntent = new Intent(Intent.ACTION_MAIN);
		  setIntent.addCategory(Intent.CATEGORY_HOME);
		  setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		  startActivity(setIntent);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!CallingFromArticleActivity) {
			String updateCredentials;
			updateCredentials = "YES"
					+ ";"
					+ AutoLogin.getUserID(AutoLogin
							.getSettingsFile(getApplicationContext())) + ";"
					+ UUID.randomUUID().toString() + ";";
			AutoLogin.saveSettingsFile(getApplicationContext(),
					updateCredentials);
			System.out.println("resume from outside, update session");
		} else {
			System.out
					.println("resume from articleactivity, do not update session");
		}
	}

}
