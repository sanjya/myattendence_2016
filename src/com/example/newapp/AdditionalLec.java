package com.example.newapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xxx on 2015-09-20.
 */
public class AdditionalLec extends Activity {
    Spinner spinner;
    DatePicker dp;
    TimePicker tp;
    ArrayList<String> subjectCodes= new ArrayList<>();
    ArrayAdapter<String> ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtional);

        spinner= (Spinner) findViewById(R.id.spinner);
        dp= (DatePicker) findViewById(R.id.datePicker2);
        tp= (TimePicker) findViewById(R.id.timePicker2);



        tp.setCurrentHour(8);
        tp.setCurrentMinute(0);
        tp.setIs24HourView(false);


        FileHandling f = new FileHandling(getApplicationContext());
        ArrayList<Subject> subjectArrayList = (ArrayList<Subject>) f.loadFile("mycozList");

        for(Subject temp:subjectArrayList){
            subjectCodes.add(temp.name);
        }

        if(subjectArrayList.size()==0){
            Toast.makeText(this,"You have to add subjects before setting additonal lectures",Toast.LENGTH_LONG).show();
            Button bt= (Button) findViewById(R.id.button7);
            bt.setEnabled(false);
        }

        ad= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,subjectCodes);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);


    }

    public void setAdditionalLec(View view){

        Calendar calendar = Calendar.getInstance();
        calendar.set(dp.getYear(),dp.getMonth(),dp.getDayOfMonth());
        SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy ");
        String selectedDate = sdf.format(calendar.getTime());

        FileHandling fh= new FileHandling(this);
        HashMap<String,Integer> dateAndIndex= (HashMap<String, Integer>) fh.loadFile("pageNavigationMap");

        if(dateAndIndex.get(selectedDate)!=null){
            int index= dateAndIndex.get(selectedDate)-1;
            Lecture lecture= new Lecture((String)spinner.getSelectedItem(),tp.getCurrentHour(),tp.getCurrentMinute(),calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH));

            //Adding extra lecture to timetable
            FileHandling f= new FileHandling(getApplicationContext());
            ArrayList<Day> pageList = (ArrayList<Day>) f.loadFile("pageList");

            pageList.get(index).todayLec.add(lecture);

            Collections.sort(pageList.get(index).todayLec);
            f.saveFile("pageList", pageList);

            Intent intent= new Intent(this,MyActivity.class);
            startActivity(intent);
            finish();
        }

        else
            Toast.makeText(this,"Selected date is not available",Toast.LENGTH_LONG);



    }
}