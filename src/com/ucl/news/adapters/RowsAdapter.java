package com.ucl.news.adapters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import main.java.org.mcsoxford.rss.RSSItem;

import android.content.Context;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucl.news.api.LoggingNavigationMetadata;
import com.ucl.news.api.LoginHttpPostTask;
import com.ucl.news.dao.NavigationalMetaDataDAO;
import com.ucl.news.dao.RunningAppsDAO;
import com.ucl.news.main.MainActivity;
import com.ucl.news.reader.News;
import com.ucl.news.utils.AutoLogin;
import com.ucl.newsreader.R;

public class RowsAdapter extends ArrayAdapter<News> {

	int resource;

	private Context applicationContext;
	// Declare Variables
	private SmartViewPager viewPager;
	private PagerAdapter adapter;
	private View mTouchTarget;
	private MainActivity activity;
	private int mCurrentTabPosition;
	private static final String LEFT_TO_RIGHT = "left to right";
	private static final String RIGHT_TO_LEFT = "right to left";
	private static final String NO_SWIPE = "no swipe";

	// private int MAX_ITEMS_DISPLAYED = 0;

	public RowsAdapter(Context _context, int _resource, List<News> _items,
			MainActivity _activity) {
		super(_context, _resource, _items);
		resource = _resource;
		applicationContext = _context;
		activity = _activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout rows;
		// get item returns to the adapter the item at position
		News item = getItem(position);

		final String title = item.getTitle();
		List<RSSItem> list = item.getContent();

		if (convertView == null) {
			rows = new RelativeLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(resource, rows, true);
		} else {
			rows = (RelativeLayout) convertView;
		}

		TextView categoryView = (TextView) rows.findViewById(R.id.category);
		categoryView.setText(title);

		// Locate the ViewPager in viewpager_main.xml
		viewPager = (SmartViewPager) rows.findViewById(R.id.pager);

		// Pass results to ViewPagerAdapter Class
		adapter = new ViewPagerAdapter(applicationContext, list, title);
		// viewPager.setOffscreenPageLimit(5);

		// Binds the Adapter to the ViewPager
		viewPager.setAdapter(adapter);

		System.out.println("currentITEM1 " + viewPager.getCurrentItem());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			// int oldPos = viewPager.getCurrentItem();

			@Override
			public void onPageSelected(int position) {
				System.out.println("pos selected: " + position);
				System.out.println("currentITEM2 " + viewPager.getCurrentItem());
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

				String swipeDirection = onTabChanged(viewPager.getAdapter(),
						mCurrentTabPosition, position);
				mCurrentTabPosition = position;

				if (!swipeDirection.equals(NO_SWIPE)) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSS");
					String dateTimeStr = dateformat.format(new Date().getTime());

					LoggingNavigationMetadata hptNavMetaData = new LoggingNavigationMetadata(
							getContext(), constructNavigationalMedaDataObject(
									title, swipeDirection, position,
									dateTimeStr));

					//Commented coz navigationalMetaData is stored in the server
//					appendNavigationalMetaDataLocalFile(constructNavigationalMedaDataObject(
//							title, swipeDirection, position, dateTimeStr));
				}
				// if(MAX_ITEMS_DISPLAYED < position){
				// MAX_ITEMS_DISPLAYED = position;
				// System.out.println("MAX_ITEMS_DISPLAYED1: " +
				// MAX_ITEMS_DISPLAYED);
				// }

				System.out.println("positionOnPageScrolled: " + position);
				// System.out.println("positionOffset: " + positionOffset);
				// System.out.println("positionOffsetPixels: "
				// + positionOffsetPixels);
				System.out.println("pos mCurrentTabPosition: "
						+ mCurrentTabPosition);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			protected NavigationalMetaDataDAO constructNavigationalMedaDataObject(
					String categoryName, String swipeDirection,
					int itemPosition, String timeStart) {

				NavigationalMetaDataDAO nmdDAO = new NavigationalMetaDataDAO();
				nmdDAO.setUserID(AutoLogin.getUserID(AutoLogin
						.getSettingsFile(applicationContext)));
				nmdDAO.setUserSession(AutoLogin.getUserSession(AutoLogin
						.getSettingsFile(applicationContext)));
				nmdDAO.setCategoryName(title);
				nmdDAO.setSwipeDirection(swipeDirection);
				nmdDAO.setItemPosition(itemPosition);
				nmdDAO.setDateTime(timeStart);

				return nmdDAO;
			}

//			private void appendNavigationalMetaDataLocalFile(
//					NavigationalMetaDataDAO nmdDAO) {
//
//				File navigationalFile = new File(Environment
//						.getExternalStorageDirectory()
//						+ File.separator
//						+ "HabitoNews_Study/navigational_data.txt");
//
//				if (navigationalFile.exists()) {
//					try {
//						BufferedWriter bW;
//
//						bW = new BufferedWriter(new FileWriter(
//								navigationalFile, true));
//
//						String delimeter = ";";
//						String row = nmdDAO.getUserID() + delimeter
//								+ nmdDAO.getUserSession() + delimeter
//								+ nmdDAO.getCategoryName() + delimeter
//								+ nmdDAO.getSwipeDirection() + delimeter
//								+ nmdDAO.getItemPosition() + delimeter
//								+ nmdDAO.getDateTime() + delimeter;
//
//						bW.write(row);
//						bW.newLine();
//						bW.flush();
//
//						bW.close();
//					} catch (Exception e) {
//
//					}
//				} else {
//					// Do something else.
//					System.out.println("navigational file not found");
//				}
//
//			}

			protected String onTabChanged(final PagerAdapter adapter,
					final int oldPosition, final int newPosition) {

				// Calc if swipe was left to right, or right to left
				System.out.println("oldPosition: " + oldPosition
						+ ", newPosition: " + newPosition);
				if (oldPosition > newPosition) {
					// left to right
					System.out.println("going left to right in category: "
							+ title);
					return LEFT_TO_RIGHT;
				} else if (oldPosition < newPosition) {
					// right to left
					System.out.println("going right to left in category: "
							+ title);
					return RIGHT_TO_LEFT;

				} else {
					System.out.println("do nothing");
					return NO_SWIPE;
				}

			}

		});
		return rows;
	}

}
