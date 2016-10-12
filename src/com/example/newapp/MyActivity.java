package com.example.newapp;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.EGLExt;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MyActivity extends FragmentActivity{
    /** Called when the activity is first created. */


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        SharedPreferences preferences= getSharedPreferences("semesterFile",MODE_PRIVATE);
        if(preferences.getInt("weeks", 9999)!=9999){

            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(new SampleAdapter(this, getSupportFragmentManager()));
            pager.setCurrentItem(setStartPagenumber());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy ");
            String currentDate = sdf.format(calendar.getTime());

            FileHandling fh= new FileHandling(this);
            HashMap<String,Integer> dateAndIndex= (HashMap<String, Integer>) fh.loadFile("pageNavigationMap");

            if(dateAndIndex.get(currentDate)!=null)
                setNextNotification(dateAndIndex.get(currentDate)-1);
        }
        else{
            Intent intent =new Intent(this,semester.class);
            startActivity(intent);
            showMessage(" Please set Semester Data");
            finish();
        }










    }

    public void setNextNotification(int index) {

        boolean Notificationset= false;
        FileHandling fh = new FileHandling(this);
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


                    Intent intent = new Intent(this, NotificationBarAlarm.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("course",pageList.get(index).todayLec.get(i).name);

                    PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // Repeat the notification every 15 seconds
                    AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
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


                        Intent intent = new Intent(this, NotificationBarAlarm.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("course",pageList.get(index).todayLec.get(i).name);

                        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Repeat the notification every 15 seconds
                        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
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

        Log.d("reamin", "end of method"+index);
    }

    public int setStartPagenumber() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy ");
        String currentDate = sdf.format(calendar.getTime());

        FileHandling fh= new FileHandling(getApplicationContext());
        HashMap<String,Integer> dateAndIndex= (HashMap<String, Integer>) fh.loadFile("pageNavigationMap");

        if(dateAndIndex.get(currentDate)==null)
            return 0;
        else
            return dateAndIndex.get(currentDate)-1;
    }

    public boolean checkSemInfo() {

        SharedPreferences preferences= getSharedPreferences("semesterFile", MODE_PRIVATE);
        //SharedPreferences.Editor editor= preferences.edit();
        //showMessage("" + preferences.getInt("weeks", 9999));

        HashMap<String,Integer> h= (HashMap<String, Integer>) loadFile("pageNavigationMap");
        ArrayList<Day> a= (ArrayList<Day>) loadFile("pageList");
        //ArrayList<Day> t= (ArrayList<Day>) loadFile("testList");

        //if(h!=null && a!= null)
        //showMessage("size " + h.size()+a.get(0).date);

        if(a!=null){
            String mess;


                    mess="\n"+a.get(5).name+" todaylec size"+a.get(5).todayLec.size();


            showMessage(mess);
        }

       return preferences.getInt("weeks", 9999)!=9999;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.mymenu, menu)

        menu.add(0,1,0,"Home");
        menu.add(0, 2, 0, "Edit Sem");
        //menu.add(0,3,0,"Add Subject");
        //menu.add(0, 4, 0, "Add SubjectS").setIcon(R.mipmap.manual);
       // menu.add(0, 5, 0,"google").setIcon(R.mipmap.google);
        //menu.add(0, 5, 0,"Bunk");
        menu.add(0, 6, 0,"My Attendence");
        //menu.getItem(0).setIcon(R.mipmap.ic_google);
        return super.onCreateOptionsMenu(menu);
        //return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case 1:
                Nav(new Intent(this,MyActivity.class));
                return true;
            case 2:
                Nav(new Intent(this,semester.class));
                return true;

            case 4:
                Nav(new Intent(this,AddSubject.class));
                return true;
            case 5:
                Nav(new Intent(this,Bunk.class));
                return true;
            case 6:
                long startdate=1;




                Calendar cal= Calendar.getInstance();
                SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy ");
                String currentDate = sdf.format(cal.getTime());



                FileHandling fh= new FileHandling(this);
                HashMap<String,Integer> dateAndIndex= (HashMap<String, Integer>) fh.loadFile("pageNavigationMap");




                if(dateAndIndex.get(currentDate)==null){
                  //dialog
                    AlertDialog.Builder warning= new AlertDialog.Builder(MyActivity.this);
                    warning.setMessage("Semester has not started yet");
                    warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intent i= new Intent(getApplicationContext(),MyActivity.class);
                            //startActivity(i);
                            //finish();
                        }
                    });

                    warning.show();

                }
                else {
                    Nav(new Intent(this,MyCourse.class));
                    return true;
                }



        }

        return super.onOptionsItemSelected(item);
    }

    public void Nav(Intent i){
        startActivity(i);
    }

    public void showMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    public Object loadFile(String name){
        Object o= null;
        try{
            FileInputStream fis = openFileInput(name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (FileNotFoundException e) {
            //  e.printStackTrace();
            showMessage("FileNotFoundException");

        }catch(IOException e){
            //e.printStackTrace();
            showMessage("IOException");
        }catch(ClassNotFoundException e){
            // e.printStackTrace();
            showMessage("ClassNotFoundException");
        }
        return o;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences preferences= getSharedPreferences("semesterFile",MODE_PRIVATE);
        if(preferences.getInt("weeks", 9999)!=9999){

            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(new SampleAdapter(this, getSupportFragmentManager()));
            pager.setCurrentItem(setStartPagenumber());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy ");
            String currentDate = sdf.format(calendar.getTime());

            FileHandling fh= new FileHandling(this);
            HashMap<String,Integer> dateAndIndex= (HashMap<String, Integer>) fh.loadFile("pageNavigationMap");

            if(dateAndIndex.get(currentDate)!=null)
                setNextNotification(dateAndIndex.get(currentDate)-1);
        }
        else{
            showMessage(" Semester Data is not set");
            //dailog  ???
            Intent intent =new Intent(this,semester.class);
            startActivity(intent);
            finish();

        }

        // Clear the Notification Bar
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    public void additionalLec(View view){
        Intent intent = new Intent(getApplicationContext(),AdditionalLec.class);
        startActivity(intent);
    }
}
