package com.ucl.news.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ucl.news.utils.AutoLogin;
import com.ucl.newsreader.R;

public class WelcomeScreen extends Activity {

	private static int SPLASH_TIME_OUT = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (AutoLogin.getIsLoggedIN(AutoLogin
				.getSettingsFile(getApplicationContext()))) {

			System.out.println("credentials: "
					+ AutoLogin.getSettingsFile(getApplicationContext()));
			Intent i = new Intent(this, MainActivity.class);
			i.putExtra("ref","WelcomeScreenCaller");
			
			startActivity(i);
		} else {
			System.out.println("MainActivity.user HERE2");
			setContentView(R.layout.welcome_screen);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent i = new Intent(WelcomeScreen.this,
							LoginActivity.class);
					startActivity(i);
					finish();
				}
			}, SPLASH_TIME_OUT);
		}
	}
}
