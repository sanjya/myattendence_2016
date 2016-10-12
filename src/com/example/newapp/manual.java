package com.example.newapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by xxx on 2015-08-29.
 */
public class manual extends Activity {
    ImageButton imageButton;
    Button timeBtn;
    TextView tv;
    ListView lv;
    ArrayAdapter<Lecture> ad;
    ArrayList<Lecture> al;
    TimePicker tp;
    String courseName,courseCode="";

    RadioButton mday;
    RadioButton tday;
    RadioButton wday;
    RadioButton thday;
    RadioButton fday;
    RadioButton sday;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        courseCode+=getIntent().getExtras().getString("code");

        tv.setText(courseName);

        //set Monday as default value, some error
        mday.setChecked(true);

        tp.setCurrentHour(8);
        tp.setCurrentMinute(0);
        tp.setIs24HourView(false);

        al= new ArrayList<>();
        ArrayList<Subject> nl= (ArrayList<Subject>) new FileHandling(getApplicationContext()).loadFile("mycozList");
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


    public void setLecture(View view){



        Lecture lecture= new Lecture(courseName,tp.getCurrentHour(),tp.getCurrentMinute(),getDay());
        al.add(lecture);
        ad.notifyDataSetChanged();
        Toast.makeText(this,"Click on lecture if you want to remove it ",Toast.LENGTH_LONG).show();
        //tp.getCurrentHour() tp.getCurrentMinute()


    }

    public void submit(View view){


        AlertDialog.Builder warningDialog =new AlertDialog.Builder(manual.this); //keep remember
        warningDialog.setTitle("Confirmation");
        warningDialog.setMessage("Are you sure that  you enetered the correct schedule for this subject?");
        warningDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(al.size()!=0){
                FileHandling f = new FileHandling(getApplicationContext());
                ArrayList<Subject> subjectArrayList = (ArrayList<Subject>) f.loadFile("mycozList");
                // f.showMessage("size "+al.size());

                Subject subject = new Subject(courseName, courseCode);
                subject.setLecFreq(al);

                subjectArrayList.add(subject);
                f.saveFile("mycozList", subjectArrayList);


                //pageList update
                updatePageList();

                Intent intent = new Intent(manual.this, MyActivity.class);
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



    }
    public void updatePageList(){

        FileHandling f= new FileHandling(getApplicationContext());
        ArrayList<Day> pageList = (ArrayList<Day>) f.loadFile("pageList");

        for(int i=0;i<pageList.size();++i){
            String key= pageList.get(i).name;
            Log.d("add","i= "+i);
            for (int j=0;j<al.size();++j){

                Log.d("add",""+j+"th item(lec) "+al.get(j).toString());
                if(al.get(j).day.equals(key)){
//added this new lecture object since duplicate marking
                    Lecture temp= new Lecture(al.get(j).name,al.get(j).hour,al.get(j).min,al.get(j).day);
                    pageList.get(i).todayLec.add(temp);
                    Log.d("add",""+j+"th item(lec) added");
                }

                else
                    Log.d("add",""+j+"th item(lec) not added");

            }
            Collections.sort(pageList.get(i).todayLec);
        }


        f.saveFile("pageList",pageList);


    }

    public String getDay(){
        if(mday.isChecked())
            return "Mon";
        if(tday.isChecked())
            return "Tue";
        if(wday.isChecked())
            return "Wed";
        if(thday.isChecked())
            return "Thu";
        if(fday.isChecked())
            return "Fri";
        if(sday.isChecked())
            return "Sat";
        else
            return "Mon";
    }





}