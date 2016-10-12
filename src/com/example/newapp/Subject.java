package com.example.newapp;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xxx on 2015-08-02.
 */
public class Subject implements java.io.Serializable {

    String name;
    String code;
    HashMap<String,Integer> freq;
    ArrayList<Lecture> lecFreq;

    int notRespond;
    int attended;
    int canceled;
    int missed;
    int remaining;
    double precentage;



    public Subject(String name,String code){
        this.name=name;
        this.code=code;
        //this.freq= freq;
        this.notRespond=0;
        this.attended=0;
        this.canceled=0;
        this.missed=0;
        this.remaining=0;
        this.precentage=0.0;
        this.lecFreq= new ArrayList<>();

    }

    public void setLecFreq(Lecture lec){
        this.lecFreq.add(lec);
    }

    public void setLecFreq(ArrayList<Lecture> al){
        this.lecFreq.addAll(al);
    }
    private int getAttendedLecutures(){
        return 0;
    }

    public int getMissedLectures(){
        return 0;
    }

    public void getPrecentage(FileHandling fh){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy ");
        String currentDate = sdf.format(calendar.getTime());


        //Context context=null;
        //FileHandling fh= new FileHandling(context.getApplicationContext());
        HashMap<String,Integer> dateAndIndex= (HashMap<String, Integer>) fh.loadFile("pageNavigationMap");
        ArrayList<Day> semesterDays = (ArrayList<Day>) fh.loadFile("pageList");
        //Log.d("attendence","not responds "+semesterDays.get(0).name);


        int todayIndex= dateAndIndex.get(currentDate);//new MyActivity().setStartPagenumber();
         int count = semesterDays.size();//dateAndIndex.get(currentDate);

        for (int i=0;i<count;++i){
           //HashMap<Integer,Integer> radioButton = semesterDays.get(i).radioButtonStatesMap;
            ArrayList<Lecture> todayLectureArrayList = semesterDays.get(i).todayLec;

            for(Lecture lecture:todayLectureArrayList){

                String subjectname= lecture.name;

                if(subjectname.equals(this.name)){
                    int value= lecture.radioBtnState;

                    switch (value){
                        case -1:
                            if(count<todayIndex)
                            notRespond++;
                            else
                            remaining++;
                            break;
                        case 0:
                            attended++;
                            break;
                        case 1:
                            canceled++;
                            break;
                        case 2:
                            missed++;
                            break;
                    }
                    /*Log.d("attendence","not responds "+notRespond);
                    Log.d("attendence","attended "+attended);
                    Log.d("attendence","canceld "+attended);
                    Log.d("attendence","missed "+missed);*/
                }

            }
        }

        Log.d("attendence","not responds "+notRespond);
        Log.d("attendence","attended "+attended);
        Log.d("attendence","canceld "+canceled);
        Log.d("attendence","missed "+missed);

        this.precentage= Math.round((100.0*attended/(attended+missed))*100.0)/100.0;


    }

    @Override
    public String toString(){
        //double precentage= 100.0*attended/(attended+missed);
        return this.name+"("+code+")"+" "+this.precentage+"%";
    }



}
