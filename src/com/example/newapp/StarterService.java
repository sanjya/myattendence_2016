package com.example.newapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class StarterService extends Service {
	private static final String TAG = "MyService";
    public static  AlarmManager am;

	/**
	 * The started service starts the AlarmManager.
	 */
	@Override
	public void onStart(Intent intent, int startid) {

		Toast.makeText(this, "called again", Toast.LENGTH_LONG).show();
		Log.d("MyService", "Service is onStart");

		Toast.makeText(this, "My Service started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");



	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
	}
}