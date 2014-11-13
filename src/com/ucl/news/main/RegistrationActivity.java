package com.ucl.news.main;

import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import android.content.ContentValues;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.ucl.news.api.LoginHttpPostTask;
import com.ucl.news.api.RegistrationDAO;
import com.ucl.news.api.RegistrationHttpPostTask;
import com.ucl.news.utils.AutoLogin;
import com.ucl.news.utils.Dialogs;
import com.ucl.newsreader.R;

public class RegistrationActivity extends Activity implements OnClickListener {

	private Button mSubmit;
	private Button mCancel;

	private EditText mFname;
	private EditText mLname;
	private EditText mUsername;
	private EditText mPassword;
	private EditText mEmail;
	private Spinner mGender;
	private String Gen;
	private DatePicker mDoB;
	
	private ProgressBar progressRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.register);

		if (customTitleSupported) {
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.custom_title);
		}

		progressRegister = (ProgressBar) findViewById(R.id.progressBarRegister);
		
		// Assignment of UI fields to the variables
		mSubmit = (Button) findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);

		mCancel = (Button) findViewById(R.id.cancel);
		mCancel.setOnClickListener(this);

		mFname = (EditText) findViewById(R.id.efname);
		mLname = (EditText) findViewById(R.id.elname);

		// mUsername = (EditText) findViewById(R.id.reuname);
		mPassword = (EditText) findViewById(R.id.repass);
		mEmail = (EditText) findViewById(R.id.eemail);

		mGender = (Spinner) findViewById(R.id.spinner1);

		mDoB = (DatePicker) findViewById(R.id.dateOfBirth);

	}

	public static Date getDateFromDatePicket(DatePicker datePicker) {
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		return calendar.getTime();
	}

	public String formatDate(String dateStr) {

		DateFormat readFormat = new SimpleDateFormat("dd MMM yyyy HH:MM:SS", Locale.US);
		DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS.SSS", Locale.US);
		Date date = null;
		
		System.out.println("dateStr: " + dateStr);
		Log.e("theo check format: ", "" + dateStr);
		
		try {
			date = readFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("date" +  date);
		String formattedDate = "";
		if (date != null) {
			formattedDate = writeFormat.format(date);
		}

		return formattedDate;

	}

	public void register(String result) {

		Log.e("RESULT REGISTER", result);
		progressRegister.setVisibility(View.INVISIBLE);
		
		String userID = null;
		
Log.e("RES1", result);
		
		try {
			JSONObject jObject = new JSONObject(result); 
			userID = jObject.getString("UserID");
			Log.e("RES2", userID);
		} catch (JSONException e) {
			Log.e("Parse result", e.getLocalizedMessage());

		}
		
		if (result.equals("Not valid request")
				|| result.equals("Json doesn't contain any values") 
				|| result.isEmpty()
				|| userID.equals(null)
				|| result.contains("Duplicate")) {
			new Dialogs().createDialogLoginERROR(RegistrationActivity.this,
					getApplicationContext());
		} else {
			String credentials = "YES" + ";" + userID + ";"
					+ UUID.randomUUID().toString() + ";";
			AutoLogin.saveSettingsFile(getApplicationContext(), credentials);

			Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
			//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//			i.putExtra("userID", userID);
//			i.putExtra("ref", "RegistrationActivity");
			i.putExtra("ref", "RegistrationCaller");
			startActivity(i);
			//this.finish();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.cancel:
			Intent i = new Intent(getBaseContext(), LoginActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			// finish();
			break;

		case R.id.submit:

			progressRegister.setVisibility(View.VISIBLE);
			
			String fname = mFname.getText().toString();
			String lname = mLname.getText().toString();
			String pass = mPassword.getText().toString();
			String email = mEmail.getText().toString();
			String gender = mGender.getSelectedItem().toString();
			//Date dob = getDateFromDatePicket(mDoB);
			//String dobStr = formatDate(dob.toLocaleString());
			
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

			Calendar date = Calendar.getInstance();
			
			date.set(mDoB.getYear(),mDoB.getMonth(), mDoB.getDayOfMonth());
			
			String dobStr = dateformat.format(date.getTime());
			Log.e("Formatted getYear", ""+mDoB.getYear());
			Log.e("Formatted getMonth", ""+mDoB.getMonth());
			Log.e("Formatted getDayOfMonth",  ""+mDoB.getDayOfMonth());
			
			Log.e("Formatted date", dobStr);

			//System.out.println("Formatted date" +  dobStr);
			boolean invalid = false;

			if (fname.equals("")) {
				invalid = true;
				mFname.setBackgroundResource(R.drawable.error);
				progressRegister.setVisibility(View.GONE);
				// Toast.makeText(getApplicationContext(),
				// "Enter your Firstname",
				// Toast.LENGTH_SHORT).show();
			} else if (lname.equals("")) {
				invalid = true;
				mLname.setBackgroundResource(R.drawable.error);
				progressRegister.setVisibility(View.GONE);
				// Toast.makeText(getApplicationContext(),
				// "Please enter your Lastname", Toast.LENGTH_SHORT)
				// .show();
			} else if (pass.equals("")) {
				invalid = true;
				mPassword.setBackgroundResource(R.drawable.error);
				progressRegister.setVisibility(View.GONE);
				// Toast.makeText(getApplicationContext(),
				// "Please enter your Password", Toast.LENGTH_SHORT)
				// .show();
			} else if (email.equals("")) {
				invalid = true;
				mEmail.setBackgroundResource(R.drawable.error);
				progressRegister.setVisibility(View.GONE);
				// Toast.makeText(getApplicationContext(),
				// "Please enter your Email ID", Toast.LENGTH_SHORT)
				// .show();
			} else if (invalid == false) {
				RegistrationDAO registrationDAO = new RegistrationDAO();
				registrationDAO.setfName(fname);
				registrationDAO.setlName(lname);
				registrationDAO.setEmail_address(email);
				registrationDAO.setPassword(pass);
				registrationDAO.setGender(gender);
				registrationDAO.setDob(dobStr);

				RegistrationHttpPostTask rhpt = new RegistrationHttpPostTask(
						getApplicationContext(), this, registrationDAO);

				// Toast.makeText(getApplicationContext(),
				// "Values to be sent to the API:" + fname + "," + lname + "," +
				// pass + "," + email + "," + gender, Toast.LENGTH_SHORT)
				// .show();

				// finish();
			}

			break;
		}
	}

}
