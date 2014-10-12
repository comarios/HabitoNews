package com.ucl.news.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import android.os.Environment;
import android.util.Log;

public class FileLogWriter {

	private final File path;
	private BufferedWriter buf;
	private String filename = "AdaptiveNewsLogging.txt";
	private File logFile;

	public FileLogWriter() {
		this.buf = null;
		this.path = new File(Environment.getExternalStorageDirectory()
				.toString() + "/AdaptiveNews");

		if (!path.exists())
			createDirectory();

		logFile = new File(path.getAbsolutePath() + "/" + filename);

		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean flushToFile(String data) {

		try {
			buf = new BufferedWriter(new FileWriter(logFile, true));

		} catch (Exception e) {
			Log.e("Problem with the file", e.getMessage());
			return false;
		}

		try {
			buf.append(data);
			buf.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			buf.close();
		} catch (IOException e) {
			Log.e("Problem with the file", e.getMessage());
			return false;
		}

		return true;
	}

	// private String generateFileName() {
	//
	// Date date = new Date();
	// long timestamp = date.getTime();
	//
	// return "AdaptiveNewsLogging_" + String.valueOf(timestamp) + ".txt";
	// }

	private boolean createDirectory() {
		return this.path.mkdir();
	}
}
