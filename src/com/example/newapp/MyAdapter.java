package com.example.newapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xxx on 2015-08-04.
 */
public class MyAdapter extends ArrayAdapter<Lecture> {
    //ArrayList<String> objects;
    ArrayList<Lecture> objects;
    Context context;
    Day day;
    int index;


    public MyAdapter(Context context, int resource, int textViewResourceId, ArrayList<Lecture> objects,Day day,int index) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
        this.day=day;
        this.index=index;
    }



@Override
public View getView (int position, View convertView, ViewGroup parent){

    LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View root= inflater.inflate(R.layout.mylistitem,null,true);

    TextView tv= (TextView) root.findViewById(R.id.textViewItem);
    tv.setText(objects.get(position).toString());

    RadioButton rb= (RadioButton) root.findViewById(R.id.radioButton);
    RadioButton rb2= (RadioButton) root.findViewById(R.id.radioButton2);
    RadioButton rb3= (RadioButton) root.findViewById(R.id.radioButton3);


    //int option= day.radioButtonStatesMap.get(day.todayLec.get(position));
   // Log.d("row", day.name+" size " + day.radioButtonStatesMap.size()+" and item ");
    //int option= day.radioButtonStatesMap.get(position+1);
    int option = objects.get(position).radioBtnState;
    switch (option){
        //starts from 0
        case 0:
            rb.setChecked(true);
            break;
        case 1:
            rb2.setChecked(true);
            break;
        case 2:
            rb3.setChecked(true);
            break;
    }


    rb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showmessage("Great");
            //hashmap put value as 2
            updateRadioButtonState(position,0); //edit 01/06
//            setNextNotification(position);
        }


    });
    rb2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            showmessage("Enjoy");
            //hashmap put value as 1
            updateRadioButtonState(position,1); //edit 01/06
//            setNextNotification(position);
        }
    });
    rb3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showmessage("You missed another lecture, Not a good sign");
            //hashmap put value as 2
            updateRadioButtonState(position,2); //edit 01/06
//            setNextNotification(position);
        }
    });


    return root;

}



    public void showmessage(String mes){
        Toast.makeText(getContext(), mes, Toast.LENGTH_LONG).show();
    }

    public void updateRadioButtonState(int row,int buttonId){

        FileHandling fh= new FileHandling(getContext());
        ArrayList<Day> pageList = (ArrayList<Day>) fh.loadFile("pageList");

        //pageList.get(index).radioButtonStatesMap.put(row,buttonId);
        Log.d("update", "index" + index);
        //Log.d("update", "index" + (index + 7));
       // Log.d("update", "b4 this monday" + pageList.get(index ).todayLec.get(row).radioBtnState);
       // Log.d("update", "b4 next monday" + pageList.get(index + 7).todayLec.get(row).radioBtnState);


        pageList.get(index).todayLec.get(row).setRadioBtnState(buttonId);
        //pageList.get(index+7).todayLec.get(row).setRadioBtnState(2);
       // Log.d("update", "list size this monday" + pageList.get(index).todayLec.size());
///        Log.d("update","list size next monday"+pageList.get(index+6).todayLec.size());

       // Log.d("update","after this monday"+pageList.get(index).todayLec.get(row).radioBtnState);
        //Log.d("update", "after next monday" + pageList.get(index+7).todayLec.get(row).radioBtnState);
        fh.saveFile("pageList", pageList);

        ArrayList<Day> pageList1 = (ArrayList<Day>) fh.loadFile("pageList");
        //Log.d("update","this monday"+pageList1.get(index).todayLec.get(row).radioBtnState);
       // Log.d("update","this monday"+pageList1.get(index+7).todayLec.get(row).radioBtnState);
    }

    public void setNextNotification(int row){

        FileHandling fh= new FileHandling(getContext());
        ArrayList<Day> pageList = (ArrayList<Day>) fh.loadFile("pageList");



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());


        for(int i=0;i<objects.size();i++){

            Log.d("reamin",objects.get(i)+"radio state "+pageList.get(index).todayLec.get(i).radioBtnState);
            //check for not responded lectures
            if(pageList.get(index).todayLec.get(i).radioBtnState==-1){

                int currentTime = calendar.get(Calendar.HOUR)*100+calendar.get(Calendar.MINUTE);
                //if such a lecture is found then check the time
                Log.d("reamin",currentTime+" and "+pageList.get(index).todayLec.get(i).format);
                if(pageList.get(index).todayLec.get(i).format>currentTime){
                    //set arlam

                    calendar.set(Calendar.HOUR_OF_DAY, pageList.get(index).todayLec.get(i).hour);
                    calendar.set(Calendar.MINUTE, pageList.get(index).todayLec.get(i).min);


                    Intent intent = new Intent(context,NotificationBarAlarm.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // Repeat the notification every 15 seconds
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    //am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*50, pi);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                    break;
                }
            }
            Log.d("reamin","didnt find any remainng lec");
        }



    }

}
