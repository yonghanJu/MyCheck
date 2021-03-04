package com.yonghan.studyhelp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.wearable.activity.WearableActivity;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    private Chronometer mChronometer;
    private Switch runnableSwitch;
    private MotionEvent event;
    private static int currentNum = 1;
    private static int tag = 1;
    private EditText et;
    private HashMap<Integer, TotalTime> each;
    private LinearLayout myRoot;
    private int weekint;
    //add
    private LocalDateTime dateTime;
    private ProgressBar TProgressbar;
    public ArrayList<ProgressBar> pList;
    private HashMap<Integer, ArrayList<ProgressBar>> spList;
    private TextView showT;
    //setting page
    private FrameLayout frameLayout;
    private Button doneButton;
    private EditText sName;
    private EditText targetHour;
    private EditText targetMin;
    private TextView eachT;
    private ScrollView scrollView;
    private int now = 0;


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        this.event = event;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setiing page
        frameLayout = findViewById(R.id.frameLayout);
        frameLayout.setVisibility(View.INVISIBLE);
        doneButton = findViewById(R.id.doneButton);
        sName = findViewById(R.id.sName);
        targetHour= findViewById(R.id.targetHour);
        targetMin = findViewById(R.id.targetMin);
        eachT = findViewById(R.id.eachT);
        scrollView = findViewById(R.id.scroll);

        //add elements
         final LinearLayout myRoot = (LinearLayout) findViewById(R.id.subject_List);
         myRoot.findViewById(R.id.subject_1).setTag(1);

         //first timer
        each = new HashMap<>();
        each.put(1, new TotalTime(1));

        Button add_button = (Button) findViewById(R.id.add_button);

        //timer
        mChronometer = (Chronometer) findViewById(R.id.choronometer);
        runnableSwitch = findViewById(R.id.switch1);
        runnableSwitch.setTag(-1);

        //test
        et = myRoot.findViewWithTag(1).findViewById(R.id.txt);
        et.setTag(999);
        //test fin

        //retrieve
        SharedPreferences sp = getSharedPreferences("appData", 0);
        String timeObj = sp.getString("TIMEOBJ","");
        each = new Gson().fromJson(timeObj, new TypeToken<HashMap<Integer, TotalTime>>(){}.getType());
        if(each == null) {
            each = new HashMap<>();
            each.put(1, new TotalTime(1));
        }
        if(TotalTime.getTTTGoal()<=0){
            each.clear();
            each.put(1, new TotalTime(1));
        }
        mChronometer.setBase(SystemClock.elapsedRealtime()-TotalTime.getTTT());

        //reset day
        dateTime = LocalDateTime.now();
        findViewById(R.id.week0).setTag(0);
        findViewById(R.id.week1).setTag(1);
        findViewById(R.id.week2).setTag(2);
        findViewById(R.id.week3).setTag(3);
        findViewById(R.id.week4).setTag(4);
        findViewById(R.id.week5).setTag(5);
        findViewById(R.id.week6).setTag(6);
        final ProgressBar TProgressbar = findViewById(R.id.progressBar);
        final TextView showT = findViewById(R.id.showT);
        final ArrayList<ProgressBar> pList = new ArrayList<>(7);
        pList.add((ProgressBar) findViewById(R.id.progressBar0));
        pList.add((ProgressBar) findViewById(R.id.progressBar1));
        pList.add((ProgressBar) findViewById(R.id.progressBar2));
        pList.add((ProgressBar) findViewById(R.id.progressBar3));
        pList.add((ProgressBar) findViewById(R.id.progressBar4));
        pList.add((ProgressBar) findViewById(R.id.progressBar5));
        pList.add((ProgressBar) findViewById(R.id.progressBar6));
        switch (dateTime.getDayOfWeek()){
            case MONDAY: if(dateTime.getHour()>3){
                weekint = 1;
            }else {
                weekint = 0;
            }
                break;
            case TUESDAY: if(dateTime.getHour()>3){
                weekint = 2;
            }else {
                weekint = 1;
            }
                break;
            case WEDNESDAY: if(dateTime.getHour()>3){
                weekint = 3;
            }else {
                weekint = 2;
            }
                break;
            case THURSDAY: if(dateTime.getHour()>3){
                weekint = 4;
            }else {
                weekint = 3;
            }
                break;
            case FRIDAY: if(dateTime.getHour()>3){
                weekint = 5;
            }else {
                weekint = 4;
            }
                break;
            case SATURDAY: if(dateTime.getHour()>3){
                weekint = 6;
            }else {
                weekint = 5;
            }
                break;
            case SUNDAY: if(dateTime.getHour()>3){
                weekint = 0;
            }else {
                weekint = 6;
            }
                break;
        }//set int of week
        for(int i = 0; i < 7; i++){
            TextView t = findViewById(R.id.dayCon).findViewWithTag(i);
            int j = (weekint+1+i) % 7;
            switch (j){
                case 0:
                    t.setText("Sun");
                    break;
                case 1:
                    t.setText("Mon");
                    break;
                case 2:
                    t.setText("Tus");
                    break;
                case 3:
                    t.setText("Wed");
                    break;
                case 4:
                    t.setText("Thu");
                    break;
                case 5:
                    t.setText("Fri");
                    break;
                case 6:
                    t.setText("Sat");
                    break;
            }
        }
        if(dateTime.getDayOfYear()!= TotalTime.getDayOfYear()) TotalTime.setIsNewDay(true);
        if ( Math.abs(dateTime.getDayOfYear() - TotalTime.getDayOfYear()) > 1){
            reset(pList);
            TotalTime.setIsNewDay(false);
        }else if (TotalTime.isNewDay() == true && dateTime.getHour()>3){
            reset(pList);
            TotalTime.setIsNewDay(false);
        }
        //set day after done resetting
        TotalTime.setDayOfYear(dateTime.getDayOfYear());
        //not month, pre weekint
        TotalTime.setMonth(weekint);
        TotalTime.setHour(dateTime.getHour());
        //test
        //et.setText(String.valueOf(dateTime.getHour()));

        //ui re
        if((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal())) < 1 && (int)TotalTime.getTTT()>1000){
            showT.setText("1%");
            TProgressbar.setProgress(1);
        }else{
            if((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal()))>100){
                showT.setText("100%");
            }else {
                showT.setText((int)(((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal()))+"%");
            }
            TProgressbar.setProgress((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal())));
        }
        for(int i =0; i < 6; i++){
            if (((int) TotalTime.getWeek(i)) * 100 / ((int) TotalTime.getTTTGoal())<1&& (int)TotalTime.getWeek(i)>1000){
                pList.get(i).setProgress(1);
            }else pList.get(i).setProgress(((int) TotalTime.getWeek(i)) * 100 / ((int) TotalTime.getTTTGoal()));
        }
        if (((int) TotalTime.getTTT()) * 100 / ((int) TotalTime.getTTTGoal())<1&& (int)TotalTime.getTTT()>1000){
            pList.get(6).setProgress(1);
        }else pList.get(6).setProgress(((int) TotalTime.getTTT()) * 100 / ((int) TotalTime.getTTTGoal()));
        long tc = SystemClock.elapsedRealtime() - mChronometer.getBase();
        mChronometer.setText(DateFormat.format("kk:mm:ss", tc));

        //implements Switch's function
        class runnableSwitchListener implements CompoundButton.OnCheckedChangeListener{

            boolean reset = true;

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TotalTime a = each.get(-(Integer) buttonView.getTag());
                    a.setTemp(System.currentTimeMillis());
                    a.setCheked(true);
                    //off other switch
                    for (int i = 1; (i < tag+1); i++){

                        if(-i == (Integer) buttonView.getTag()) continue;

                        try {
                            Switch s = myRoot.findViewWithTag(i).findViewWithTag(-i);
                            if(s.isChecked()) {
                                s.toggle();
                                TotalTime t = each.get(-(Integer) buttonView.getTag());
                                t.setCheked(false);
                                t.setEachTime(System.currentTimeMillis()-t.getTemp());
                                t.setTime(t.getTime() + t.getEachTime());
                                TotalTime.addTTT(t.getEachTime());
                                TotalTime.setWeek(6,TotalTime.getTTT());
                                //progress

                                if((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal())) <1 && (int)TotalTime.getTTT()>1000){
                                    pList.get(6).setProgress(1);
                                    TProgressbar.setProgress(1);
                                    showT.setText("1%");
                                }else{
                                    pList.get(6).setProgress((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal())));
                                    TProgressbar.setProgress((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal())));
                                    if((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal()))>100){
                                        showT.setText("100%");
                                    }else showT.setText((int)(((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal()))+"%");
                                }
                                break;
                            }
                        }catch (Exception e){continue;}
                    }

                    //switch's operation
                    if(reset == true) {
                        reset = false;
                        mChronometer.setBase(SystemClock.elapsedRealtime()-TotalTime.getTTT());
                    }
                    else mChronometer.setBase(SystemClock.elapsedRealtime()+mChronometer.getBase()-TotalTime.getSleepTime());
                    mChronometer.start();
                } else {
                    TotalTime.setSleepTime(SystemClock.elapsedRealtime());
                    mChronometer.stop();
                    TotalTime t = each.get(-(Integer) buttonView.getTag());
                    t.setCheked(false);

                    t.setEachTime(System.currentTimeMillis()-t.getTemp());
                    t.setTime(t.getTime() + t.getEachTime());
                    TotalTime.addTTT(t.getEachTime());
                    TotalTime.setWeek(6,TotalTime.getTTT());
                    //progressbar
                    if ((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal())) < 1 && (int)TotalTime.getTTT()>1000){
                        TProgressbar.setProgress(1);
                        showT.setText("1%");
                        pList.get(6).setProgress(1);
                    }else {
                        if((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal()))>100){
                            showT.setText("100%");
                        }else{
                            showT.setText((int)(((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal()))+"%");
                        }
                        TProgressbar.setProgress((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal())));
                        pList.get(6).setProgress((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal())));
                    }
                }
            }
        }

        //chronometer format custom
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer cArg) {
                long t = SystemClock.elapsedRealtime() - cArg.getBase();
                cArg.setText(DateFormat.format("kk:mm:ss", t));
            }
        });

        final runnableSwitchListener rs = new runnableSwitchListener();
        runnableSwitch.setOnCheckedChangeListener(rs);

        //progressbar
        ProgressBar p = findViewById(R.id.progressBar);

        //implement class of remove button
        class RemoveButton implements View.OnClickListener{
            //remove button tag
            int rmbTag;

            @Override
            public void onClick(View v) {
                rmbTag = (Integer)v.getTag();
                int layTag = rmbTag-1000;
                currentNum--;

                //stop timer
                Switch s = myRoot.findViewWithTag(layTag).findViewWithTag(-layTag);
                if(s.isChecked())s.toggle();

                //remove layout
                myRoot.removeView(myRoot.findViewWithTag(layTag));

                //remove goal
                each.get(layTag).setGoal(0);
                //remove TTObj
                each.remove(layTag);
            }
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //now = TotalTime.getNow();
                TextView t = myRoot.findViewWithTag(now).findViewById(R.id.txt);
                t.setText(sName.getText());
                long h, m;
                String hh= String.valueOf(targetHour.getText());
                String mm= String.valueOf(targetMin.getText());
                if(hh.isEmpty()){
                    h =0;
                }else h =Integer.parseInt(String.valueOf(targetHour.getText()));
                if(mm.isEmpty()){
                    m =0;
                }else m =Integer.parseInt(String.valueOf(targetMin.getText()));
                if(h==0&&m==0){
                    Toast.makeText(getApplicationContext(),"Set target time",Toast.LENGTH_SHORT).show();
                }else{
                    each.get(now).setTargetHour(h);
                    each.get(now).setTargetMin(m);
                    each.get(now).setGoal(h*3600*1000 + m*60*1000);
                    each.get(now).setSubjectName(String.valueOf(sName.getText()));

                    frameLayout.setVisibility(View.INVISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                }
                }
        });

        //class-function of eachInfo
        class EachInfoButton implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                scrollView.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);

                int i = ((Integer) v.getTag() )/ 999;
                //give tag
                now =i;
                //TotalTime.setNow(i);

                //implement setting page
                TotalTime t =each.get(i);
                if(t.getSubjectName()==null){
                    sName.setText("");
                }else sName.setText(each.get(i).getSubjectName());
                long n =t.getTime();
                eachT.setText(DateFormat.format("kk:mm:ss",n));
                if(t.getTargetHour()==0){
                    targetHour.setText("");
                }else targetHour.setText(String.valueOf(t.getTargetHour()));
                if(t.getTargetMin()==0){
                    targetMin.setText("");
                }else targetMin.setText(String.valueOf(t.getTargetMin()));
            }
        }

        et.setOnClickListener(new EachInfoButton());

        //UI retrieve, when i=1, it's already working
        for(int i = 2; i < tag +1; i++){
            if(each.containsKey(i)){
                //subject name
                View n = getLayoutInflater().inflate(R.layout.subject, null);
                myRoot.addView(n);
                n.setTag(i);
                EditText e = (EditText)n.findViewById(R.id.txt);
                e.setText(each.get(i).getSubjectName());

                //switch
                Switch s = (Switch)n.findViewById(R.id.switch2);
                s.setTag(-i);
                s.setOnCheckedChangeListener(rs);

                //remove
                RemoveButton rb = new RemoveButton();
                Button rmvb = n.findViewById(R.id.removeButton);
                rmvb.setTag(i+1000);
                rmvb.setOnClickListener(rb);

                //subject name
                TextView t = n.findViewById(R.id.txt);
                t.setOnClickListener(new EachInfoButton());
                t.setTag(i*999);
            }
        }

        //function of add button
        add_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add num
                currentNum++;
                tag++;

                //add view
                View n = getLayoutInflater().inflate(R.layout.subject, null);
                myRoot.addView(n);

                n.setTag(tag);

                Switch runnableSwitch2 = n.findViewById(R.id.switch2);
                runnableSwitch2.setTag(-tag);
                runnableSwitch2.setOnCheckedChangeListener(rs);

                //remove button
                RemoveButton rb = new RemoveButton();
                Button rmvb = n.findViewById(R.id.removeButton);
                rmvb.setOnClickListener(rb);
                rmvb.setTag(tag+1000);

                //add eachinfo
                EditText e = n.findViewById(R.id.txt);
                e.setOnClickListener(new EachInfoButton());
                e.setTag(tag* 999);

                //add timer instance
                each.put(tag, new TotalTime(tag));
                TotalTime.addTTTGoal(3600*1000);
            }
        });
        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    protected void onDestroy() {
        //pop up message "are you sure?"
        super.onDestroy();

        for (int i =0; i < tag+1; i++){
            if(each.containsKey(i)){
                Switch s = findViewById(R.id.subject_List).findViewWithTag(i).findViewWithTag(-i);
                s.toggle();
            }
        }
        saveName();
        SharedPreferences sp = getSharedPreferences("appData", 0);
        SharedPreferences.Editor editor= sp.edit();
        String timeObj = new Gson().toJson(each);
        editor.putString("TIMEOBJ", timeObj);
        editor.commit();
    }

    void saveName(){
        for(int i = 2; i < tag+1; i++){
            if(each.containsKey(i)){
                LinearLayout myRoot = (LinearLayout) findViewById(R.id.subject_List);
                View v = myRoot.findViewWithTag(i);
                EditText e = v.findViewById(R.id.txt);
                each.get(v.getTag()).setSubjectName(String.valueOf(e.getText()));
            }
        }
   }

   void reset(ArrayList<ProgressBar> progress){
        try{
        }catch (Exception e){ e.getStackTrace();}
       for(int i = 1; i < tag+1; i ++) {
           if (each.containsKey(i)) {
               myRoot = findViewById(R.id.subject_List);
               Switch s = myRoot.findViewWithTag(i).findViewWithTag(-i);
               if (s.isChecked()) s.toggle();
               TotalTime t = each.get(i);
               //set each time zero
               t.setTime(0);
           }
       }
       TotalTime.setWeek(6, TotalTime.getTTT());

       //change week info
       //new year
       if(TotalTime.getDayOfYear() > dateTime.getDayOfYear()){
           if(365-TotalTime.getDayOfYear()+dateTime.getDayOfYear()>6){
               for(int j=0;j<7;j++){
                   TotalTime.setWeek(j, 0);
               }
           }else if(weekint>TotalTime.getMonth()){
               //move z times
               for (int z=0; z < weekint-TotalTime.getMonth();z++){
                   for (int j=1;j<7; j++){
                       TotalTime.setWeek(j-1,TotalTime.getWeek(j));
                       TotalTime.setWeek(j, 0);
                   }
               }
           }else if(weekint < TotalTime.getMonth()){
               //move z times
               for (int z=0; z < 7+weekint-TotalTime.getMonth();z++){
                   for (int j=1;j<7; j++){
                       TotalTime.setWeek(j-1,TotalTime.getWeek(j));
                       TotalTime.setWeek(j, 0);
                   }
               }
           }else{
               for(int j=0;j<7;j++){
                   TotalTime.setWeek(j, 0);
               }
           }
       }else{
           //not new year
           if (TotalTime.getHour() > 3){
               for (int z=0; z < dateTime.getDayOfYear()-TotalTime.getDayOfYear();z++) {
                   for (int j = 1; j < 7; j++) {
                       TotalTime.setWeek(j - 1, TotalTime.getWeek(j));
                       TotalTime.setWeek(j, 0);
                   }
               }
           }
               else{
               for (int z=0; z < Math.abs(dateTime.getDayOfYear()-TotalTime.getDayOfYear() + 1);z++){
                   for (int j=1;j<7; j++){
                       TotalTime.setWeek(j-1,TotalTime.getWeek(j));
                       TotalTime.setWeek(j,0);
                   }
               }
           }
       }
       //set TTT zero
       TotalTime.setTTT(0);

       //show progress
       ProgressBar TProgressbar = findViewById(R.id.progressBar);
       if ((((int) TotalTime.getTTT()) * 100 / ((int) TotalTime.getTTTGoal()))<1&& (int)TotalTime.getTTT()>1000){
           TProgressbar.setProgress(1);
       }else TProgressbar.setProgress((int) (((int) TotalTime.getTTT()) * 100 / ((int) TotalTime.getTTTGoal())));
       TextView showT = findViewById(R.id.showT);
       if((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal()))<1&& (int)TotalTime.getTTT()>1000){
           showT.setText("1%");
       }else if((((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal()))>100){
           showT.setText("100%");
       }else showT.setText((int)(((int)TotalTime.getTTT())*100/((int) TotalTime.getTTTGoal()))+"%");
       for(int i =0; i < 7; i++){
           if (((int) TotalTime.getWeek(i)) * 100 / ((int) TotalTime.getTTTGoal())<1 && (int)TotalTime.getTTT()>1000){
               progress.get(i).setProgress(1);
           }
           progress.get(i).setProgress((((int) TotalTime.getWeek(i)) * 100 / ((int) TotalTime.getTTTGoal())));
       }
       mChronometer.setBase(SystemClock.elapsedRealtime());
   }
}

class TotalTime {
    private String subjectName;
    private static int hour;
    private static int minute;
    private static int second;
    private static int dayOfYear =0;
    private static int month=0;

    private static boolean isNewDay = false;

    private long time = 0;
    private long targetHour = 1;
    private long targetMin = 0;
    private long timeGaol=3600*1000;
    private static long SleepTime = 0;
    private long persTotalTime = 0;
    private long eachTime = 0;
    private long temp = 0;
    private boolean isCheked = false;
    private static long TTT =0;
    //null exception
    private static long TTTGoal=3600* 1000 +1000;
    private int tag = 0;
    private static long[] week = new long[7];
    private static int now;

    TotalTime(int tag){
        this.tag = tag;
    }

    public static void addTTTGoal(long l){TTTGoal +=l;}

    public void setGoal(long timeGoal){
        TTTGoal = TTTGoal-this.timeGaol;
        this.timeGaol = timeGoal;
        TTTGoal = TTTGoal+timeGoal;
    }
    public static void setNow(int n){now = n;}

    public static int getNow(){return now;}

    public void setTargetHour(long h){this.targetHour = h;}

    public long getTargetHour(){return targetHour;}

    public void setTargetMin(long m){this.targetMin = m;}

    public long getTargetMin(){return targetMin;}

    public long getTimeGaol(){return this.timeGaol;}

    public static long getTTTGoal(){return TTTGoal;}

    public static long getWeek(int day){return week[day];}

    public static void setWeek(int day, long time){week[day] = time;}

    public String getSubjectName(){return this.subjectName;}

    public void setSubjectName(String subjectName){this.subjectName = subjectName;}

    public int getTag(){return this.tag;}

    public void setTag(int tag){this.tag= tag;}

    public static void setTTT(long ttt){TTT = ttt;}

    public static long getTTT(){return TTT;}

    public static void addTTT(long time){ TTT = TTT + time;}

    public void setEachTime(long eachTime){this.eachTime = eachTime;}

    public long getEachTime(){return this.eachTime;}

    public void setCheked(boolean isCheked){this.isCheked = isCheked;}

    public boolean isCheked(){return isCheked;}

    public void setTemp(long temp){this.temp = temp;}

    public long getTemp(){return this.temp;}

    static public long getSleepTime(){
        return SleepTime;
    }

    static void setSleepTime(long lb){
        SleepTime = lb;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time){this.time = time; }

    public static  void setHour(int h){hour =h;}

    public static void setDayOfYear(int day){dayOfYear = day;}


    public static int getDayOfYear(){return dayOfYear;}

    public static void setMonth(int m){month = m;}

    public static int getMonth(){return month;}

    public static int getHour() {
        return hour;
    }

    public static void setMinute(int m){minute = m;}

    public static int getMinute() {
        return minute;
    }

    public static void setSecond(int s){second= s;}

    public static int getSecond() {
        return second;
    }

    public static boolean isNewDay(){return isNewDay;}

    public static void setIsNewDay(boolean i){isNewDay = i;}

    public static void getString() {
        System.out.print(hour+":"+minute+":"+second);
    }
}



