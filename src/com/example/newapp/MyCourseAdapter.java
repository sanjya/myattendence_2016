package com.example.newapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxx on 2015-08-24.
 */
public class MyCourseAdapter extends ArrayAdapter {
    Context context;
    ArrayList<Subject> objects;
    AlertDialog.Builder builder ;

    public MyCourseAdapter(Context context, int resource, ArrayList<Subject> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.builder = new AlertDialog.Builder(context);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root= inflater.inflate(R.layout.course_list_item,null,true);

        TextView courseName= (TextView) root.findViewById(R.id.textViewSubject);
        TextView courseAttendence= (TextView) root.findViewById(R.id.textViewAttedence);

        courseName.setText(objects.get(position).name+"("+objects.get(position).code+")");
        courseAttendence.setText("" + objects.get(position).precentage + "%");


        /*root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "item " + position, Toast.LENGTH_LONG).show();
                return false;
            }
        });*/






        root.setBackgroundColor(setColor(objects.get(position).precentage));



        return root;

    }

    private int setColor(double precentage) {
        //green FF23FF0B

        if(precentage<80.0){
            return  Color.parseColor("#ffff2810");
        }
        else{
            return Color.parseColor("#FF23FF0B");
        }





    }
}
