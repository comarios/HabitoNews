package com.ucl.news.main;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ucl.news.api.LoginDAO;
import com.ucl.news.api.LoginHttpPostTask;
import com.ucl.news.utils.AutoLogin;
import com.ucl.news.utils.Dialogs;
import com.ucl.news.utils.NetworkConnection;
import com.ucl.newsreader.R;

public class LoginActivity extends Activity {

	private EditText mUsername;
	private EditText mPassword;
	private ProgressBar progressLogin;
	private final String NOT_AUTHORISED_USER = "-1";
	private NetworkConnection network = new NetworkConnection(
			LoginActivity.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (AutoLogin.getIsLoggedIN(AutoLogin
				.getSettingsFile(getApplicationContext()))) {
			Intent i = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(i);
		} else {
			final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.login);

			if (customTitleSupported) {
				getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
						R.layout.custom_title);
			}

			// getActionBar().setIcon(R.drawable.habitonews_logo);

			if (!network.haveNetworkConnection()) {
				new Dialogs().createDialogINTERNET(LoginActivity.this,
						getApplicationContext());
			}
		}

		progressLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
	}

	public void authenticate(String result) {

		progressLogin.setVisibility(View.INVISIBLE);

		String userID = null;
		// Display error message
		// Log.e("RES1", result);
		//
		// System.out.println("result: " + result);
		try {
			JSONObject jObject = new JSONObject(result);
			userID = jObject.getString("UserID");
			// Log.e("RES2", userID);
		} catch (JSONException e) {
			Log.e("Parse result", e.getLocalizedMessage());

		}

		if (result.equals("Not valid json")
				|| result.equals("Json doesn't contain any values")
				|| result.isEmpty() || userID.equals(null)
				|| userID.equals(NOT_AUTHORISED_USER)) {
			new Dialogs().createDialogLoginERROR(LoginActivity.this,
					getApplicationContext());
		} else {
			String credentials = "YES" + ";" + userID + ";"
					+ UUID.randomUUID().toString() + ";";
			AutoLogin.saveSettingsFile(getApplicationContext(), credentials);

			Intent i = new Intent(LoginActivity.this, MainActivity.class);
			i.putExtra("ref", "LoginCaller");
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(i);
		}
	}

	/** Called when the user clicks the signIn button */
	public void btnSignIn(View view) {

		mUsername = (EditText) findViewById(R.id.txtUsername);
		mPassword = (EditText) findViewById(R.id.txtPassword);

		Log.e("values", mUsername.getText().toString()
				+ mPassword.getText().toString());

		// Call API to authenticate USER
		LoginDAO users = new LoginDAO();
		users.setEmail_address(mUsername.getText().toString());
		users.setPassword(mPassword.getText().toString());

		progressLogin.setVisibility(View.VISIBLE);
		LoginHttpPostTask hpt = new LoginHttpPostTask(getApplicationContext(),
				this, users);
	}

	/** Called when the user clicks the signIn button */
	public void btnSignUp(View view) {

		Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
		startActivity(i);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
}
