package com.example.newapp;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class NotificationBarAlarm extends BroadcastReceiver {

	NotificationManager notifyManager;

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d("NotificationAlarm", "onReceive");

		notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		// This Activity will be started when the user clicks the notification
		// in the notification bar
		Intent notificationIntent = new Intent(context, MyActivity.class);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		Notification notif = new Notification(R.drawable.ic_stat_rooster_col, "A new notification just popped in!", System.currentTimeMillis());

		// Play sound?
		// If you want you can play a sound when the notification shows up.
		// Place the MP3 file into the /raw folder.
		notif.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.jingle);

		notif.setLatestEventInfo(context, "MyAttendence Notification ", "Mark your attendence for "+intent.getStringExtra("course"), contentIntent);

		notifyManager.notify(1, notif);







			//start activity
		//if(isRunning(context)){
		Log.d("arlam","arlam start");
			Intent i = new Intent(context,MyActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			context.startActivity(i);
		Log.d("arlam","arlam set");
		//}

	}
	public boolean isRunning(Context ctx) {
		ActivityManager activityManager = (ActivityManager)
				ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks =
				activityManager.getRunningTasks(Integer.MAX_VALUE);

		for (ActivityManager.RunningTaskInfo task : tasks) {
			if (ctx.getPackageName().equalsIgnoreCase(
					task.baseActivity.getPackageName()))
				return true;
		}

		return false;
	}

}