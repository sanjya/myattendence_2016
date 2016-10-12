package com.example.newapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xxx on 2015-09-17.
 */
public class google extends Activity {
    EditText link;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google);

        link= (EditText) findViewById(R.id.editText3);
    }

    public void getData(View view) throws IOException {
//link.getText().toString()
        //doDownload("https://tiny.cc/comics","basic.ics");

        ArrayList<String> al=  readICS(link.getText().toString());


        if(al.size()>0){
            Intent intent= new Intent(this,googleData.class);
            Bundle mBundle = new Bundle();
            mBundle.putStringArrayList("subjectcode",al);
            mBundle.putString("link",link.getText().toString());
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }

    private ArrayList<String> readXML() {
        ArrayList<String> al= new ArrayList<>();
        Set<String> set= new HashSet<>();

        try {
//test staff
            String root_sd = Environment.getExternalStorageDirectory().toString();
            File fXmlFile =  new File( root_sd + "/mycal.xml" ) ;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            //Log.d("xml", doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("entry");

            System.out.println("----------------------------");
            //System.out.println("lenght :"+nList.getLength());


            /*ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();*/

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

//                System.out.println("\nCurrent Element :" + nNode.getNodeName());
//                Log.d("xml",nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    //System.out.println("Staff id : " + eElement.getAttribute("entry"));
//                    Log.d("xml",eElement.getAttribute("entry"));

//                    System.out.println("id : " + eElement.getElementsByTagName("title").item(0).getTextContent());
                   // Log.d("xml", eElement.getElementsByTagName("title").item(0).getTextContent());
//                    al.add(eElement.getElementsByTagName("title").item(0).getTextContent());
                    set.add(eElement.getElementsByTagName("title").item(0).getTextContent());

                }
            }

            al.addAll(set);
            Collections.sort(al);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return al;
    }

    private void doDownload(String urlLink, String fileName) {
        Thread dx = new Thread() {

            public void run() {
                File root = android.os.Environment.getExternalStorageDirectory();
                File dir = new File (root.getAbsolutePath());
                if(dir.exists()==false) {
                    dir.mkdirs();
                }
                //Save the path as a string value

                try {
                    URL url = new URL(urlLink);
                    Log.i("FILE_NAME", "File name is "+fileName);
                    Log.i("FILE_URLLINK", "File URL is "+url);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // this will be useful so that you can show a typical 0-100% progress bar
                    int fileLength = connection.getContentLength();

                    // download the file
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(dir+"/"+fileName);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                    Log.i(" DOWN FILES", "Done");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(" DOWN FILES", "ERROR IS" +e);
                }
            }
        };
        dx.start();
    }

    public ArrayList<String>  readICS(String link) throws IOException {
        ArrayList<String> al= new ArrayList<>();
        Set<String> set= new HashSet<>();

//        URL url = new URL("https://www.google.com/calendar/ical/ptnc8ts0822mkjmus8uqo6l45k%40group.calendar.google.com/public/basic.ics");

        URL url = new URL(link);
        URLConnection conn = url.openConnection();

        //parse the first iCalendar object from the data stream
        ICalendar ical = Biweekly.parse(new InputStreamReader(conn.getInputStream())).first();

        for(int i=0;i<ical.getEvents().size();i++){
            VEvent event = ical.getEvents().get(i);

            set.add(event.getSummary().getValue());
        }
        al.addAll(set);
        Collections.sort(al);
        return al;
    }
}