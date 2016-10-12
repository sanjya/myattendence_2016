package com.example.newapp;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by xxx on 2015-08-01.
 */
public class Day implements java.io.Serializable {

    String date;
    String name;
   // ArrayList<String> todayLec;
   ArrayList<Lecture> todayLec;
   // HashMap<String,Integer> radioButtonStatesMap;
    HashMap<Integer,Integer> radioButtonStatesMap;


    public Day(String date,String day){

        this.date= date;
        this.name= day;
        todayLec= new ArrayList<>();
        //radioButtonStatesMap= new HashMap<>();
    }


}
