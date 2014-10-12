package com.ucl.news.logging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.ucl.news.api.ArticleDAO;
import com.ucl.news.main.ArticleActivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class Logger extends Service {

	private FileLogWriter flw;
	private ServiceReceiver serviceReceiver;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Start Logging", Toast.LENGTH_LONG).show();
		// gDetector = new GestureDetector(this);
		// Bind the gestureDetector to GestureListener
		flw = new FileLogWriter();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("IN: ");
		Calendar c = Calendar.getInstance();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());

		flw.flushToFile("Logging Started at: " + formattedDate);
		flw.flushToFile("\n");

		IntentFilter movementFilter;
		movementFilter = new IntentFilter(ArticleActivity.UPDATE);
		serviceReceiver = new ServiceReceiver();
		registerReceiver(serviceReceiver, movementFilter);
		return startId;
	}

	public class ServiceReceiver extends BroadcastReceiver {
		// Receives broadcast message from the service.
		@Override
		public void onReceive(Context context, Intent intent) {
			// Take the new data from service.
			ArticleDAO aDAO = intent
					.getParcelableExtra(ArticleActivity.MSG_SEND);

			System.out.println("Receive");
			
			Long userID = aDAO.getUserID();
			String userSession = aDAO.getUserSession();
			String articleName =  aDAO.getArticleName();
			String articleURL =  aDAO.getArticleURL();
			double readingDuration =  aDAO.getReadingDuration();
			long startTimestamp =  aDAO.getStartTimestamp();
			long endTimestamp =  aDAO.getEndTimestamp();
			Boolean isScrollUsed =  aDAO.getIsScrollUsed();
			Boolean isScrollReachedBottom =  aDAO.getIsScrollReachedBottom();
			//long scrollDuration =  aDAO.getScrollDuration();
			int numberOfWordsInArticle =  aDAO.getNumberOfWordsInArticle();

			System.out.println("Message Received: " + aDAO);
			System.out.println("Received userID: " + userID);
			System.out.println("Received userSession: " + userSession);
			System.out.println("Received articleName: " + articleName);
			System.out.println("Received articleURL: " + articleURL);
			System.out.println("Received readingDuration: " + readingDuration);
			System.out.println("Received startTimestamp: " + startTimestamp);
			System.out.println("Received endTimestamp: " + endTimestamp);
			System.out.println("Received isScrollUsed: " + isScrollUsed);
			System.out.println("Received isScrollReachedBottom: " + isScrollReachedBottom);
			//System.out.println("scrollDuration: " + scrollDuration);
			System.out.println("Received numberOfWordsInArticle: " + numberOfWordsInArticle);
			
//			flw.flushToFile("ArticleName: " + articleName + "\n");
//			flw.flushToFile("Startime: " + start + "\n");
//			flw.flushToFile("EndTime: " + end + "\n");
//			// long diffInSec = TimeUnit.MILLISECONDS.toSeconds(end-start);
//			flw.flushToFile("Read Time: " + periodOfReading + " sec \n");
		}
	}
	// @Override
	// public boolean onDown(MotionEvent arg0) {
	// // TODO Auto-generated method stub
	// Toast.makeText(this, "onDown", Toast.LENGTH_LONG).show();
	// return true;
	// }
	//
	// @Override
	// public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	// float velocityY) {
	// // TODO Auto-generated method stub
	// Toast.makeText(this, "onFling", Toast.LENGTH_LONG).show();
	// return true;
	// }
	//
	// @Override
	// public void onLongPress(MotionEvent e) {
	// // TODO Auto-generated method stub
	// Toast.makeText(this, "onLongPress", Toast.LENGTH_LONG).show();
	//
	// }
	//
	// @Override
	// public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
	// float distanceY) {
	// // TODO Auto-generated method stub
	// Toast.makeText(this, "onScroll", Toast.LENGTH_LONG).show();
	// return true;
	// }
	//
	// @Override
	// public void onShowPress(MotionEvent e) {
	// // TODO Auto-generated method stub
	// Toast.makeText(this, "onShowPress", Toast.LENGTH_LONG).show();
	// }
	//
	// @Override
	// public boolean onSingleTapUp(MotionEvent e) {
	// // TODO Auto-generated method stub
	// Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_LONG).show();
	// return true;
	// }

}
