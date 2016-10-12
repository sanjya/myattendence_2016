package com.example.newapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xxx on 2015-08-02.
 */
public class AddSubject extends Activity {

    EditText name;
    EditText code;
    EditText mon;
    EditText tue;
    EditText wed;
    EditText thu;
    EditText fri;
    ImageButton imageButton2;

    Bundle outstate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subject);




        //use number picker api 11
        name= (EditText) findViewById(R.id.editText);
        code= (EditText) findViewById(R.id.editText2);
        imageButton2= (ImageButton) findViewById(R.id.imageButton2);


    }

    public void Add(View view){


        String strName = name.getText().toString();


        String strCode = code.getText().toString();

        if(TextUtils.isEmpty(strName)) {
            name.setError("Please Enter value");
//            return;
        }
        if(TextUtils.isEmpty(strCode)) {
            code.setError("Please Enter value");
//            return;
        }

        else{
            //Confirmation
            AlertDialog.Builder builder =new AlertDialog.Builder(AddSubject.this); //keep remember
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    FileHandling f = new FileHandling(getApplicationContext());
                    ArrayList<Subject> al = (ArrayList<Subject>) f.loadFile("mycozList");
                    // f.showMessage("size "+al.size());
                    boolean isContain = false;

                    for (Subject subject : al) {
                        Log.d("check",subject.name+isContain+name.getText().toString());
                        if (subject.name.equals(name.getText().toString()) ||subject.code.equals(code.getText().toString())) {
                            isContain = true;
                            Log.d("check",subject.name+isContain+name.getText().toString());
                            break;
                        }

                    }

                    if (isContain) {
                        //new dialog
                        Toast.makeText(getApplicationContext(),"You cant add",Toast.LENGTH_LONG).show();
                        AlertDialog.Builder warningDialog =new AlertDialog.Builder(AddSubject.this); //keep remember
                        warningDialog.setTitle("Warning");
                        warningDialog.setMessage("This subject is already in your course list. You can't it add it again");
                        warningDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        warningDialog.show();


                    } else {
                        //next activity
                        //Intent i = new Intent(AddSubject.this, MyCourse.class);
                        Intent i = new Intent(AddSubject.this, manual.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("name", name.getText().toString());
                        mBundle.putString("code", code.getText().toString());
                        i.putExtras(mBundle);
                        startActivity(i);
                    }


                }


            });


            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }






    }

    /*public void updateList() {
        FileHandling f= new FileHandling(getApplicationContext());
        ArrayList<Day> pageList = (ArrayList<Day>) f.loadFile("pageList");
        HashMap<String,Integer> h= getFrequency();

        for (int i=0;i<pageList.size();++i){
            String key= pageList.get(i).name;
            if(h.containsKey(key)){
                for (int j=0;j<h.get(key);++j){
                    pageList.get(i).todayLec.add(name.getText().toString());

                    int row= pageList.get(i).radioButtonStatesMap.size()+1;
                    Log.d("row","size "+row);
                    pageList.get(i).radioButtonStatesMap.put(row,-1);
                }




            }
        }

        f.saveFile("pageList",pageList);
    }
*/
    public HashMap<String,Integer> getFrequency(){
        HashMap<String,Integer> h= new HashMap<>();

        if(Integer.parseInt(mon.getText().toString())!=0)
        h.put("Mon",Integer.parseInt(mon.getText().toString()));

        if(Integer.parseInt(tue.getText().toString())!=0)
        h.put("Tue",Integer.parseInt(tue.getText().toString()));

        if(Integer.parseInt(wed.getText().toString())!=0)
        h.put("Wed",Integer.parseInt(wed.getText().toString()));

        if(Integer.parseInt(thu.getText().toString())!=0)
            h.put("Thu",Integer.parseInt(thu.getText().toString()));

        if(Integer.parseInt(fri.getText().toString())!=0)
            h.put("Fri",Integer.parseInt(fri.getText().toString()));

        return h;
    }
}