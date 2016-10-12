package com.example.newapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Autostart extends BroadcastReceiver {

	Context context;
	/**
	 * Listens for Android's BOOT_COMPLETED broadcast and then executes
	 * the onReceive() method.
	 */
	@Override
	public void onReceive(Context context, Intent arg1) {

		this.context= context;

		Log.d("Autostart", "BOOT_COMPLETED broadcast received. Executing starter service.");
		//if (arg1.getAction().equals("android.intent.action.BOOT_COMPLETED") ) {
            // Set the alarm here.
            //Intent intent = new Intent(context, StarterService.class);
            //context.startService(intent);

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy ");
		String currentDate = sdf.format(calendar.getTime());

		FileHandling fh= new FileHandling(context);
		HashMap<String,Integer> dateAndIndex= (HashMap<String, Integer>) fh.loadFile("pageNavigationMap");

		if(dateAndIndex.get(currentDate)!=null)
			setNextNotification(dateAndIndex.get(currentDate)-1);
        //}

	}

	public void setNextNotification(int index) {

		boolean Notificationset= false;
		FileHandling fh = new FileHandling(context);
		ArrayList<Day> pageList = (ArrayList<Day>) fh.loadFile("pageList");
		int startIndex= index;


		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		while(index <pageList.size()){



			for (int i = 0; i < pageList.get(index).todayLec.size(); i++) {

				Log.d("reamin", pageList.get(index).todayLec.get(i) + "radio state " + pageList.get(index).todayLec.get(i).radioBtnState);
				//check for not responded lectures
				if (pageList.get(index).todayLec.get(i).radioBtnState == -1) {

					int currentTime = calendar.get(Calendar.HOUR_OF_DAY) * 100 + calendar.get(Calendar.MINUTE);
					//if such a lecture is found then check the time
					Log.d("reamin", currentTime + " and " + pageList.get(index).todayLec.get(i).format);


					if(startIndex<index){
						//next day
						Log.d("reamin","start:"+startIndex+" currnt index:"+index);

						calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+index-startIndex);
						calendar.set(Calendar.HOUR_OF_DAY, pageList.get(index).todayLec.get(i).hour);
						Log.d("reamin", "set hour:" + pageList.get(index).todayLec.get(i).hour);
						calendar.set(Calendar.MINUTE, pageList.get(index).todayLec.get(i).min);


						Intent intent = new Intent(context, NotificationBarAlarm.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

						// Repeat the notification every 15 seconds
						AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
						//am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*50, pi);
						am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
						Notificationset= true;
						break;
					}
					else{
						if (pageList.get(index).todayLec.get(i).format > currentTime) {
							//set arlam

							calendar.set(Calendar.HOUR_OF_DAY, pageList.get(index).todayLec.get(i).hour);
							calendar.set(Calendar.MINUTE, pageList.get(index).todayLec.get(i).min);


							Intent intent = new Intent(context, NotificationBarAlarm.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

							PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

							// Repeat the notification every 15 seconds
							AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
							//am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*50, pi);
							am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
							Notificationset= true;
							break;
						}
					}

				}
				Log.d("reamin", "didnt find any remainng lec");
			}
			if(Notificationset)
				break;
			else
				index++;
			Log.d("reamin", "index incremented"+index);
		}

		Log.d("reamin", "end of method" + index);
	}
}