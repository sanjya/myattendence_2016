package com.example.newapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.*;

/**
 * Created by xxx on 2015-08-02.
 */
public class FileHandling {
    Context ctx;
    public FileHandling(Context ctx){
        this.ctx=ctx;
    }

    public void saveFile(String name,Object object){
        FileOutputStream fos;
        try {
            //Constants.FILENAME
            fos = ctx.openFileOutput(name, Context.MODE_PRIVATE);
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

    public Object loadFile(String name){
        Object o= null;
        try{
            FileInputStream fis = ctx.openFileInput(name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (FileNotFoundException e) {
            //  e.printStackTrace();
            showMessage("FileNotFoundException");

        }catch(IOException e){
            //e.printStackTrace();
            showMessage("IOException");
        }catch(ClassNotFoundException e){
            // e.printStackTrace();
            showMessage("ClassNotFoundException");
        }
        return o;
    }

    public void showMessage(String message){
        Toast.makeText(ctx.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
