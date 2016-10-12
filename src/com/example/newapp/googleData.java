package com.example.newapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.RecurrenceRule;
import com.google.ical.compat.javautil.DateIterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xxx on 2015-09-17.
 */
public class googleData extends Activity {
    ListView lv;
    ArrayAdapter<String> ad;
    ArrayList<String> subjectTitle;
    String link;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_data);

        subjectTitle=  getIntent().getExtras().getStringArrayList("subjectcode");
        link= getIntent().getExtras().getString("link");

       // al= new ArrayList<>();
       // al.add("abc");al.add("def");al.add("kdkd");
        lv = (ListView) findViewById(R.id.listView4);
        ad= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,subjectTitle);
        lv.setAdapter(ad);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void done(View view) throws IOException {
        SparseBooleanArray selected = lv.getCheckedItemPositions();

        FileHandling f = new FileHandling(getApplicationContext());
        ArrayList<Subject> subjectArrayList = (ArrayList<Subject>) f.loadFile("mycozList");
        // f.showMessage("size "+al.size());

        //course code already in the list
        ArrayList<String> manuallyAddedCourseCode= new ArrayList<>();
        ArrayList<String> automaticallyAddedCourseCode= new ArrayList<>();

        for (Subject temp:subjectArrayList){
            manuallyAddedCourseCode.add(temp.code);
        }


        for (int i = 0;i<selected.size();i++){
            if(selected.valueAt(i)){
                if(!manuallyAddedCourseCode.contains(subjectTitle.get(selected.keyAt(i)))){
                    automaticallyAddedCourseCode.add(subjectTitle.get(selected.keyAt(i)));
                }
            }
        }
        Log.d("check", "slected sub" + automaticallyAddedCourseCode);


        for (int i = 0;i<automaticallyAddedCourseCode.size();i++) {



            Subject subject = new Subject(automaticallyAddedCourseCode.get(i), automaticallyAddedCourseCode.get(i));
            ArrayList<Lecture> lectureArrayList = getLectures(automaticallyAddedCourseCode.get(i),link);

            subject.setLecFreq(lectureArrayList);
            subjectArrayList.add(subject);



//                    for(Lecture tmp:lectureArrayList){
//                        System.out.println(tmp);
//                    }

                    updatePageList(lectureArrayList);

                    //j++;
                }






        f.saveFile("mycozList", subjectArrayList);

    }

    private ArrayList<Lecture> getLectures(String code,String link) throws FileNotFoundException, IOException {

        ArrayList<Lecture> al= new ArrayList<>();

       URL url = new URL("https://www.google.com/calendar/ical/ptnc8ts0822mkjmus8uqo6l45k%40group.calendar.google.com/public/basic.ics");

//        URL url = new URL(link);
        URLConnection conn = url.openConnection();

        //parse the first iCalendar object from the data stream
        ICalendar ical = Biweekly.parse(new InputStreamReader(conn.getInputStream())).first();

        for(int i=0;i<ical.getEvents().size();i++){

            VEvent event = ical.getEvents().get(i);
            String summary = event.getSummary().getValue();
            if(summary.equals(code)){

                long starttime= event.getDateStart().getValue().getTime();
                long endtime = event.getDateEnd().getValue().getTime();
                int duration= (int) (endtime-starttime);
                int lecPerDay= duration/3600000;
                Log.d("cal","start"+starttime);
                Log.d("cal","end"+endtime);
                Log.d("cal","duration"+duration);
                Log.d("cal","lecperday"+lecPerDay);


                //check recurrnce of this event
                RecurrenceRule rrule = event.getRecurrenceRule();
                DateIterator it = rrule.getDateIterator(event.getDateStart().getValue());
                Calendar cal = Calendar.getInstance();

                Set<String> weekday= new HashSet<>();
                while (it.hasNext()) {

                    Date next = it.next();

                    cal.setTime(next);
                    System.out.println("date"+next.toString());

                    weekday.add(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH));
                }

                for(String val:weekday){

                    for (int j=0;j<lecPerDay;j++){
                        Lecture lecture= new Lecture(code,cal.get(Calendar.HOUR)+j,cal.get(Calendar.MINUTE),val);
                        al.add(lecture);
                    }
                }
            }
        }


        return al;
    }

    public  String getDay(String input) {

        String regex = "\\d{4}-\\d{2}-\\d{2}";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        Calendar calendar = Calendar.getInstance();

        while (matcher.find()) {
            String[] firstStartdate= matcher.group().split("-");


            calendar.set(Integer.parseInt(firstStartdate[0]), Integer.parseInt(firstStartdate[1]), Integer.parseInt(firstStartdate[2]));
//            System.out.println("day:" + calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH));


        }
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
    }

    public  int[] getTime(String input) {
        int[] hour_and_min=null;
//        String input = "<summary type='html'>Recurring Event&lt;br&gt;First start: 2015-06-08 11:00:00 IST&lt;br&gt;Duration: 3600 ";
//        String input = "hfdjdfjkdfj12/07/2004dddsss12/10/2010ñrrñrñr10/01/2000ksdifjsdifffffdd04/04/1998";
        String regex = "\\d{2}:\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {

            // System.out.println(matcher.group());
            String[] startTime= matcher.group().split(":");

            hour_and_min= new int[]{Integer.parseInt(startTime[0]),Integer.parseInt(startTime[1])};

        }
        return hour_and_min;
    }

    public void updatePageList(ArrayList<Lecture> lectureArrayList){

        FileHandling f= new FileHandling(getApplicationContext());
        ArrayList<Day> pageList = (ArrayList<Day>) f.loadFile("pageList");

        for(int i=0;i<pageList.size();++i){
            String key= pageList.get(i).name;
            Log.d("add","i= "+i);
            for (int j=0;j<lectureArrayList.size();++j){

                Log.d("add", "" + j + "th item(lec) " + lectureArrayList.get(j).toString());
                if(lectureArrayList.get(j).day.equals(key)){
                    pageList.get(i).todayLec.add(lectureArrayList.get(j));
                    Log.d("add",""+j+"th item(lec) added");
                }

                else
                    Log.d("add",""+j+"th item(lec) not added");

            }
            Collections.sort(pageList.get(i).todayLec);
        }


        f.saveFile("pageList",pageList);


    }


}