package com.example.newapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;

public class SampleAdapter extends FragmentStatePagerAdapter {
    Context ctxt = null;
    HashMap<String,Integer> dateAndIndex;

    public SampleAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt = ctxt;
        FileHandling fh= new FileHandling(ctxt);
        dateAndIndex= (HashMap<String, Integer>) fh.loadFile("pageNavigationMap");

    }

    @Override
    public int getCount() {
        SharedPreferences preferences= ctxt.getSharedPreferences("semesterFile",ctxt.MODE_PRIVATE);
        return preferences.getInt("weeks", 1)*7;
    }

    @Override
    public Fragment getItem(int position) {

        SharedPreferences preferences= ctxt.getSharedPreferences("semesterFile",ctxt.MODE_PRIVATE);

        if(position<preferences.getInt("weeks", 1)*7 && position>-1)
        return new SimpleFragment(position);

        return null;
    }
    @Override
    public String getPageTitle(int position) {
        int week= position/7+1;
        return "Week "+week;
    }
}