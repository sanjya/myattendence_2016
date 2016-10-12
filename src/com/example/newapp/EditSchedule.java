package com.example.newapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by myAT on 2015-09-20.
 */
public class EditSchedule extends manual {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual);


        imageButton = (ImageButton) findViewById(R.id.imageButton);
        tv= (TextView) findViewById(R.id.textView10);
        lv= (ListView) findViewById(R.id.listView3);
        tp = (TimePicker) findViewById(R.id.timePicker);

        mday = (RadioButton) findViewById(R.id.radioButton4);
        tday= (RadioButton) findViewById(R.id.radioButton5);
        wday= (RadioButton) findViewById(R.id.radioButton6);
        thday= (RadioButton) findViewById(R.id.radioButton7);
        fday= (RadioButton) findViewById(R.id.radioButton8);
        sday= (RadioButton) findViewById(R.id.radioButton9);


        //Set course name
        courseName = getIntent().getExtras().getString("name");
        courseCode =getIntent().getExtras().getString("code");
        int pos = getIntent().getExtras().getInt("pos");

        tv.setText(courseName);

        //set Monday as default value, some error
        mday.setChecked(true);

        tp.setCurrentHour(8);
        tp.setCurrentMinute(0);
        tp.setIs24HourView(false);

        al= new ArrayList<>();
        ArrayList<Subject> nl= (ArrayList<Subject>) new FileHandling(getApplicationContext()).loadFile("mycozList");
        al= nl.get(pos).lecFreq;
        ad= new ArrayAdapter<Lecture>(getBaseContext(),android.R.layout.simple_list_item_checked,al);
        lv.setAdapter(ad);

        //optional
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                al.remove(position);
                ad.notifyDataSetChanged();
            }
        });






    }

    @Override
    public void submit(View view){

        AlertDialog.Builder warningDialog =new AlertDialog.Builder(EditSchedule.this); //keep remember
//        warningDialog.setTitle("Confirmation");
        DatePicker picker = new DatePicker(this);

        SharedPreferences preferences = getSharedPreferences("semesterFile",MODE_PRIVATE);

        Calendar c = Calendar.getInstance();
        c.set(preferences.getInt("startYear", 2015), preferences.getInt("startMonth", 01), preferences.getInt("startDay", 01));
//        picker.setMinDate(c.getTimeInMillis());

        c.add(Calendar.DATE, preferences.getInt("weeks", 0) * 7);
//       picker.setMaxDate(c.getTimeInMillis());


        warningDialog.setTitle("Changes Affect From");
        warningDialog.setView(picker);
        //warningDialog.setMessage("Are you sure that  you enetered the correct schedule for this subject?");
        warningDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                //to get the date that changes affected from
//                AlertDialog.Builder builder = new AlertDialog.Builder(EditSchedule.this);
//                DatePicker picker = new DatePicker(EditSchedule.this);
//                //picker.setCalendarViewShown(false);
//
//                builder.setTitle("This Permanant Change affect from");
//                builder.setView(picker);
//                builder.setNegativeButton("Cancel", null);
//                builder.setPositiveButton("Set", null);
//
//                builder.show();

                Log.d("date","year"+picker.getYear());

                if (al.size() != 0) {
                    FileHandling f = new FileHandling(getApplicationContext());
                    ArrayList<Subject> subjectArrayList = (ArrayList<Subject>) f.loadFile("mycozList");
                    // f.showMessage("size "+al.size());

                    ArrayList<Subject> toRemove = new ArrayList<Subject>();
                    for (Subject tempSub : subjectArrayList) {
                        if (tempSub.code.equals(courseCode))
                            toRemove.add(tempSub);

                    }

                    Log.d("date", "to remov" + toRemove);
                    subjectArrayList.removeAll(toRemove);

                    Log.d("date", "after removing" + subjectArrayList.size());

                    Subject subject = new Subject(courseName, courseCode);
                    subject.setLecFreq(al);

                    subjectArrayList.add(subject);
                    Log.d("date", "after adding" + subjectArrayList.size());

                    f.saveFile("mycozList", subjectArrayList);



                    //setting changing starting date
                    c.set(picker.getYear(),picker.getMonth(),picker.getDayOfMonth());
                    SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy ");
                    String changesStartingDate = sdf.format(c.getTime());

                    //pageList update
                    updatePageList(changesStartingDate);

                    Intent intent = new Intent(EditSchedule.this, MyActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"No Lecture is added to the Schedule ",Toast.LENGTH_LONG).show();



            }
        });
        warningDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        warningDialog.show();
        Log.d("no","nothing");
    }


    public void updatePageList(String changesStartingDate){

        FileHandling fh= new FileHandling(getApplicationContext());
        HashMap<String,Integer> dateAndIndex= (HashMap<String, Integer>) fh.loadFile("pageNavigationMap");

        int startingIndex= dateAndIndex.get(changesStartingDate)-1;

        //delete lec from ceratain date

        FileHandling f= new FileHandling(getApplicationContext());
        ArrayList<Day> pageList = (ArrayList<Day>) f.loadFile("pageList");


        for(int i=startingIndex;i<pageList.size();++i){

            ArrayList<Lecture> toRemove = new ArrayList<>();
            for (Lecture lec : pageList.get(i).todayLec) {
                if (lec.name.equals(courseName)) {
                    toRemove.add(lec);
                }
            }
            pageList.get(i).todayLec.removeAll(toRemove);
        }


        //add lec from ceratain date

//        FileHandling f= new FileHandling(getApplicationContext());
//        ArrayList<Day> pageList = (ArrayList<Day>) f.loadFile("pageList");

        for(int i=startingIndex;i<pageList.size();++i){
            String key= pageList.get(i).name;
            Log.d("add","i= "+i);
            for (int j=0;j<al.size();++j){

                Log.d("add", "" + j + "th item(lec) " + al.get(j).toString());
                if(al.get(j).day.equals(key)){


//added this new lecture object since duplicate marking 2016/06/02
                    Lecture temp= new Lecture(al.get(j).name,al.get(j).hour,al.get(j).min,al.get(j).day);
                    pageList.get(i).todayLec.add(temp);
                    //pageList.get(i).todayLec.add(al.get(j));
                    Log.d("add",""+j+"th item(lec) added");
                }

                else
                    Log.d("add",""+j+"th item(lec) not added");

            }
            Collections.sort(pageList.get(i).todayLec);
        }


        f.saveFile("pageList",pageList);

    }




}