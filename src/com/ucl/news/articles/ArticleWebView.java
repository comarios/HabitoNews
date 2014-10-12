package com.ucl.news.articles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.ucl.news.dao.ArticleMetaDataDAO;
import com.ucl.news.main.ArticleActivity;
import com.ucl.news.main.MainActivity;
import com.ucl.news.utils.AutoLogin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

public class ArticleWebView extends WebView {

	private long tempSeconds;
	private Date currentScrolling = new Date();
	private Date tempScrolling = null;
	private int initialPosition = 0;
	
	public ArticleWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ArticleWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ArticleWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public OnBottomReachedListener mOnBottomReachedListener = null;
	private int mMinDistance = 0;

	/**
	 * Set the listener which will be called when the WebView is scrolled to
	 * within some margin of the bottom.
	 * 
	 * @param bottomReachedListener
	 * @param allowedDifference
	 */
	public void setOnBottomReachedListener(
			OnBottomReachedListener bottomReachedListener, int allowedDifference) {
		mOnBottomReachedListener = bottomReachedListener;
		mMinDistance = allowedDifference;
	}

	/**
	 * Implement this interface if you want to be notified when the WebView has
	 * scrolled to the bottom.
	 */
	public interface OnBottomReachedListener {
		void onBottomReached(View v);
	}

	// private OnEndScrollListener mOnEndScrollListener;
	//
	// public interface OnEndScrollListener {
	// public void onEndScroll();
	// }
	//
	// public OnEndScrollListener getOnEndScrollListener() {
	// return mOnEndScrollListener;
	// }
	//
	// public void setOnEndScrollListener(OnEndScrollListener
	// mOnEndScrollListener) {
	// this.mOnEndScrollListener = mOnEndScrollListener;
	// }

	@Override
	protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {

		// if (Math.abs(top - oldTop) < 2 || top >= getMeasuredHeight() || top
		// == 0) {
		// System.out.println("HERE ENTER 1");
		// if (mOnEndScrollListener != null) {
		// System.out.println("HERE ENTER 2");
		// mOnEndScrollListener.onEndScroll();
		// }
		//
		// }

		if (mOnBottomReachedListener != null) {
			// Set the ScrollUsed to TRUE
			ArticleActivity.setIsScrollUsedCustom(true);

			// tempScrolling = new Date();
			// System.out.println("temp Scrolling Time: " +
			// tempScrolling.getTime() + ", current Scrolling Time: " +
			// currentScrolling.getTime());
			// tempSeconds +=
			// TimeUnit.MILLISECONDS.toSeconds(tempScrolling.getTime() -
			// currentScrolling.getTime());
			// System.out.println("Seconds Scrolling: " + tempSeconds);
			// currentScrolling = tempScrolling;

			// if(isStartScroll){
			// //First Time Scrolled
			// System.out.println("First Time Scrolling");
			// tempScrolling = new Date();
			// isStartScroll = !isStartScroll;
			// tempSeconds =
			// TimeUnit.MILLISECONDS.toMillis(tempScrolling.getTime() -
			// currentScrolling.getTime());
			// currentScrolling = tempScrolling;
			// }else{
			// System.out.println("Other Times Scrolling");
			// tempScrolling = new Date();
			// tempSeconds +=
			// TimeUnit.MILLISECONDS.toMillis(tempScrolling.getTime() -
			// currentScrolling.getTime());
			// currentScrolling = tempScrolling;
			// }
			//

			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String dateTimeStr = dateformat.format(new Date().getTime());
			ArticleMetaDataDAO aArticleMetaDataDAO = new ArticleMetaDataDAO();
			
			aArticleMetaDataDAO.setUserID(AutoLogin.getUserID(AutoLogin.getSettingsFile(getContext())));
			aArticleMetaDataDAO.setUserSession(AutoLogin.getUserSession(AutoLogin.getSettingsFile(getContext())));
			aArticleMetaDataDAO.setArticleID(ArticleActivity.articleID);
			aArticleMetaDataDAO.setScrollRange(computeVerticalScrollRange());
			aArticleMetaDataDAO.setScrollExtent(computeVerticalScrollExtent());
			aArticleMetaDataDAO.setScrollOffset(computeVerticalScrollOffset());
			aArticleMetaDataDAO.setDateTime(dateTimeStr);
			
			ArticleActivity.articleMetaData.add(aArticleMetaDataDAO);
//			System.out.println("here time: " + dateTimeStr);
//			System.out.println("computeVerticalScrollRange(): "
//					+ computeVerticalScrollRange());
//			System.out.println("computeVerticalScrollExtent(): "
//					+ computeVerticalScrollExtent());
//			System.out.println("computeVerticalScrollOffset(): "
//					+ computeVerticalScrollOffset());
//			System.out.println("getScrollY(): " + getScrollY());
//			System.out.println("getScrollBarSize(): " + getScrollBarSize());
//			System.out.println("Start Scrolling");
//			System.out.println("getContentHeight: " + getContentHeight());
//			System.out.println("top: " + top);
//			System.out.println("getHeight(): " + getHeight());
//			System.out.println("mMinDistance: " + mMinDistance);

			// if ( (getContentHeight() - (top + getHeight())) <= mMinDistance )
			if (((getContentHeight() - getHeight()) + getContentHeight()) == top)
				mOnBottomReachedListener.onBottomReached(this);
		}
		super.onScrollChanged(left, top, oldLeft, oldTop);
	}

}