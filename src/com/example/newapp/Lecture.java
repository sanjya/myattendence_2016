package com.example.newapp;


import java.util.Calendar;
import java.util.Locale;

/**
 * Created by xxx on 2015-08-29.
 */
public class Lecture implements java.lang.Comparable ,java.io.Serializable  {

    String name,day;
    int hour,min,format,radioBtnState;

    public Lecture(String name,int hour,int min,String day){
        this.name= name;
        this.hour= hour;
        this.min= min;
        this.day= day;
        this.format= hour*100+min;
        this.radioBtnState=-1;

    }

    public void setRadioBtnState(int radioBtnState) {
        this.radioBtnState = radioBtnState;
    }

    @Override
    public String toString(){
        if(min<10){
            if(this.hour==12)
                return this.name+" "+this.day+" "+(this.hour)+":0"+min+"pm";
            else if(this.hour>12)
                return this.name+" "+this.day+" "+(this.hour-12)+":0"+min+"pm";
            else
                return this.name+" "+this.day+" "+(this.hour)+":0"+min+"am";
        }
        else{
            if(this.hour==12)
                return this.name+" "+this.day+" "+(this.hour)+":"+min+"pm";
            else if(this.hour>12)
                return this.name+" "+this.day+" "+(this.hour-12)+":"+min+"pm";
            else
                return this.name+" "+this.day+" "+(this.hour)+":"+min+"am";
        }
    }

    @Override
    public int compareTo(Object another) {
        Lecture lec = (Lecture) another;
        return this.format-lec.format;
    }
}
