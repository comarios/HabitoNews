package com.ucl.news.utils;

import com.ucl.newsreader.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;

public class Dialogs {

	public void createDialogHELP(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Adaptive News Framework - (ANF)")
				.setMessage(
						"ANF is a news reader app aiming to investigate the effects of adaptation on mobile user interfaces."
								+ "This customised app captures user's navigation and reading behaviour in attempt to gain knowledge about their users. Having this data, the app adapts"
								+ " its interface based on user's interaction history.")
				.setCancelable(true)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void createDialogINTERNET(final Activity activity,
			final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("No Internet Connection")
				.setMessage("Enable Internet Connection?")
				.setCancelable(true)
				.setPositiveButton("Internet Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent networkSettings = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								// Flag is required due to calling of new
								// activity outside of the MainActivity's
								// context
								networkSettings
										.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(networkSettings);

								// context.startActivity(new
								// Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void createDialogLoginERROR(final Activity activity,
			final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Invalid login credentials")
				.setCancelable(true)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
