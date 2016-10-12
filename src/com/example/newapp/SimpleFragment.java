package com.example.newapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxx on 2015-08-01.
 */

@SuppressLint("ValidFragment")
public class SimpleFragment extends Fragment {

    String text;
    int num;
    //ArrayList<String> al ;
    ArrayList<Lecture> al;
    ArrayAdapter<String> ad;
    MyAdapter md;

    ArrayList<Day> pageList;

    public SimpleFragment(int num) {
        this.num=num;
    }

    public Day getDay(){

        FileHandling f= new FileHandling(getActivity().getApplicationContext());
        pageList= (ArrayList<Day>) f.loadFile("pageList");

        return pageList.get(num);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment, container, false);
        TextView textview = (TextView) root.findViewById(R.id.textView1);
        ListView lv= (ListView) root.findViewById(R.id.listView);

        al= getDay().todayLec;

//        ad= new ArrayAdapter<String>(root.getContext(),R.layout.mylistitem,R.id.textViewItem,al);
//        lv.setAdapter(ad);

        String[] str= new String[]{"Mon","Tue","Wed"};
        List<String> ls= new ArrayList<>() ;
        ls.add("Mon");
        ls.add("Some");

        md= new MyAdapter(root.getContext(),R.layout.mylistitem,R.id.textViewItem,al,getDay(),num);
        lv.setAdapter(md);

        textview.setText(getDay().date + " " + getDay().name);

        return root;
    }





}