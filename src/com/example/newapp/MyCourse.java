package com.example.newapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by xxx on 2015-08-02.
 */
public class MyCourse extends Activity {
    ListView lv;
    ArrayAdapter<Subject> ad;
    ArrayList<Subject> al;
    MyCourseAdapter mcd;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycourse);




        lv= (ListView) findViewById(R.id.listView2);


        FileHandling f= new FileHandling(getApplicationContext());
        ArrayList<Subject> al= (ArrayList<Subject>) f.loadFile("mycozList");

        //Log.d("atten", "sub " + al.get(0).getMissedLectures());



        for(int i=0;i<al.size();++i){
            al.get(i).getPrecentage(f);
        }




      // ad= new ArrayAdapter<Subject>(this,android.R.layout.simple_list_item_1,al);
      //lv.setAdapter(ad);

        mcd= new MyCourseAdapter(getApplicationContext(),R.layout.course_list_item,al);
        lv.setAdapter(mcd);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyCourse.this); //keep remember
                builder.setTitle("Summery for " + al.get(position).name);

                String[] sarray = {
                        "Attended:\t\t\t\t\t" + al.get(position).attended,
                        "Missed:\t\t\t\t\t\t" + al.get(position).missed,
                        "Canceled:\t\t\t\t\t" + al.get(position).canceled,
                        "Not Responded:\t" + al.get(position).notRespond,
                        "Reamaining:\t\t\t" + al.get(position).remaining,};
                builder.setItems(sarray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });

        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                AlertDialog.Builder operation = new AlertDialog.Builder(MyCourse.this);
                operation.setMessage("Do following operation on this subject");
                operation.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("long clicked", "pos: " + pos);
                        Intent i = new Intent(getApplicationContext(), EditSchedule.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("name", al.get(pos).name);
                        mBundle.putString("code", al.get(pos).code);
                        mBundle.putInt("pos",pos);
                        i.putExtras(mBundle);
                        startActivity(i);
                    }
                });


                operation.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("long clicked", "neg: ");
                        String subname= al.get(pos).name;
                        al.remove(pos);
                        f.saveFile("mycozList",al);
                        mcd.notifyDataSetChanged();

                        //update list
                        deleteLecOfSubject(subname);
                    }
                });

                operation.show();

                return true;
            }
        });








    }


    public void deleteLecOfSubject(String name){
        FileHandling f= new FileHandling(getApplicationContext());
        ArrayList<Day> pageList = (ArrayList<Day>) f.loadFile("pageList");

        for(int i=0;i<pageList.size();++i){


            ArrayList<Lecture> toRemove = new ArrayList<>();
            for (Lecture lec : pageList.get(i).todayLec) {
                if (lec.name.equals(name)) {
                    toRemove.add(lec);
                }
            }
            pageList.get(i).todayLec.removeAll(toRemove);
            Collections.sort(pageList.get(i).todayLec);
        }


        f.saveFile("pageList",pageList);
    }
}