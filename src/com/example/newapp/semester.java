package com.example.newapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by xxx on 2015-08-01.
 */
public class semester extends Activity {

    DatePicker datePicker;
    EditText weeks;
    EditText filename;
    Button submit,unset,add_sub;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semester);


        datePicker= (DatePicker) findViewById(R.id.datePicker);
        weeks= (EditText) findViewById(R.id.editWeek);
        //filename= (EditText) findViewById(R.id.editFile);
        submit= (Button) findViewById(R.id.button);
        unset= (Button) findViewById(R.id.button2);
        unset.setEnabled(false);
        add_sub= (Button) findViewById(R.id.button8);
        add_sub.setEnabled(false);

        SharedPreferences preferences= getSharedPreferences("semesterFile",MODE_PRIVATE);
        if(preferences.getInt("weeks", 9999)!=9999){
            datePicker.setEnabled(false);
            weeks.setText(""+preferences.getInt("weeks",9999));
            weeks.setEnabled(false);
            submit.setEnabled(false);
            unset.setEnabled(true);
            add_sub.setEnabled(true);

        }

    }

    public void Submit(View view){
        datePicker.setEnabled(false);


        String strName = weeks.getText().toString();
        if(TextUtils.isEmpty(strName)) {
            weeks.setError("Please Enter value");
//            return;
        }


        else{
            //save week and start date to shared memory
            SharedPreferences preferences= getSharedPreferences("semesterFile",MODE_PRIVATE);
            SharedPreferences.Editor editor= preferences.edit();
            editor.putInt("weeks", Integer.parseInt(weeks.getText().toString()));
            editor.putInt("startYear", datePicker.getYear());
            editor.putInt("startMonth",datePicker.getMonth());
            editor.putInt("startDay",datePicker.getDayOfMonth());

            editor.commit();

            //initialize hash map and save it
            //initialize and saving ArrayList<Day>
            init();




            startActivity(new Intent(this, MyActivity.class));
            finish();
        }



    }

    public  void Unset(View view){
        AlertDialog.Builder warningDialog =new AlertDialog.Builder(semester.this); //keep remember

        warningDialog.setTitle("Warning");
        warningDialog.setMessage("This action delete all your data and cannot be undone. Press continue to delete");
        warningDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteall();
                Intent intent = new Intent(semester.this, MyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        warningDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        warningDialog.show();
        //initList();


    }

    public void addSub(View view){
        Intent intent = new Intent(getApplicationContext(),AddSubject.class);
        startActivity(intent);

    }
    public void deleteall() {
        deleteFile("pageNavigationMap");
        deleteFile("pageList");
        deleteFile("mycozList");
        SharedPreferences preferences= getSharedPreferences("semesterFile",MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.remove("weeks");
        editor.commit();
    }

    public void initList() {

        ArrayList<Object> al = new ArrayList<>();
        Day d= new Day("some","thing");
        al.add(d);
        saveFile("testList", al);
        Log.d("List", "day lsit saved");
    }


    public void init() {

        HashMap<String,Integer> dateAndIndex = new HashMap<>();
        ArrayList<Day> semesterDays = new ArrayList<>();
        ArrayList<Subject> courseList= new ArrayList<>();
        int numOfDays= 7*Integer.parseInt(weeks.getText().toString());

        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());



        for (int i=0;i<numOfDays;i++){
            String day= calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
            SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy ");
            String currentDate = sdf.format(calendar.getTime());

            dateAndIndex.put(currentDate, i + 1);

            Day d = new Day(currentDate,day);
            semesterDays.add(d);


            calendar.add(Calendar.DATE, 1);

       }

        //saving hashmap<String date,int Index>
        saveFile("pageNavigationMap", dateAndIndex);

        //saving ArrayList<Day>
        saveFile("pageList", semesterDays);


        //save courseList file
        saveFile("mycozList", courseList);



    }

    public void showMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    public void saveFile(String name,Object object){
        FileOutputStream fos;
        try {
            //Constants.FILENAME
            fos = openFileOutput(name, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            Log.d("hash", "file saved");
            Log.d("hash",name);
            Log.d("hash",Object.class.getCanonicalName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            Log.d("hash","list could not saved");
            e.printStackTrace();
        }
    }

    public void Delete(View view){
        deleteFile(filename.getText().toString());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences preferences= getSharedPreferences("semesterFile",MODE_PRIVATE);
        if(preferences.getInt("weeks",9999)!=9999){
            datePicker.setEnabled(false);
            weeks.setEnabled(false);
        }


    }


}