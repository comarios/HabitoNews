package com.ucl.news.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;
import android.view.View;

public class AutoLogin {

	private final static String AUTOLOGIN_FILENAME = "autologin";
	private final static String LOGGED_IN = "YES";
	
	public static void saveSettingsFile(Context applicationContext,
			String credentials) {

		FileOutputStream fos = null;
		try {
			fos = applicationContext.openFileOutput(AUTOLOGIN_FILENAME,
					Context.MODE_PRIVATE);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			fos.write(credentials.getBytes());
			fos.close();
		} catch (IOException e) {
			Log.e("Controller",
					e.getMessage() + e.getLocalizedMessage() + e.getCause());
		}
	}

	public static String getSettingsFile(Context applicationContext) {

		FileInputStream fin = null;
		String userSession = "";

		try {
			fin = applicationContext.openFileInput(AUTOLOGIN_FILENAME);
			int c;
			try {
				while ((c = fin.read()) != -1) {
					userSession = userSession + Character.toString((char) c);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}
		return userSession;

	}

	public static boolean getIsLoggedIN(String session) {
		String[] userSession;
		userSession = session.split(";");
		if(userSession[0].equals(LOGGED_IN))
			return true;
		else
			return false;
	}

	public static Long getUserID(String session) {
		String[] userSession;
		userSession = session.split(";");
		return Long.parseLong(userSession[1]);
	}

	public static String getUserSession(String session) {
		String[] userSession;
		userSession = session.split(";");
		return userSession[2];
	}
}
