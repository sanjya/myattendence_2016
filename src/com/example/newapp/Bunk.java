package com.example.newapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by xxx on 2015-11-28.
 */
public class Bunk extends Activity  {

    TextView tv;
    ArrayList<String> missedLectures= new ArrayList<>();
    ArrayAdapter<String> ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bunk);


        tv= (TextView) findViewById(R.id.textView11);


        ad = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,missedLectures);


        TextView seekBarValue = (TextView)findViewById(R.id.textView11);
        seekBarValue.setText("");





    }


}