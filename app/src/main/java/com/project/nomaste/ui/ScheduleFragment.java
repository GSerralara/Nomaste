package com.project.nomaste.ui;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.nomaste.Adapters.ScheduleAdapter;
import com.project.nomaste.Login;
import com.project.nomaste.MainActivity;
import com.project.nomaste.Model.Entity.ScheduleItem;
import com.project.nomaste.Network.FirebaseConnector;
import com.project.nomaste.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;


public class ScheduleFragment extends Fragment {

    LinkedList<ScheduleItem> items;
    Button createSchedule;
    public ScheduleFragment() {
        // Required empty public constructor
        items= new LinkedList<ScheduleItem>();
        //ToDo: sacar items de firebase
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        //getBDData();

        ScheduleAdapter adapter = new ScheduleAdapter(items,rootView.getContext());
        RecyclerView recyclerView = rootView.findViewById(R.id.schedule_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(adapter);
        createSchedule = rootView.findViewById(R.id.createScheduleMainButton);
        createSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToScreen("ScheduleCreator");
            }
        });

        return rootView;
    }
    public void getBDData(LinkedList<ScheduleItem> data){
        items = data;
    }
    public boolean isToday(String[]days){
        //L,M,Mi,J,V,S,D ->strings
        //2,3,4,5,6,7,1 ->int
        String []week={"D","L","M","Mi","J","V","S"};
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for(int i=0;i<days.length;i++){
            if(week[dayOfWeek-1].equals(days[i].replace("\"",""))){
                return true;
            }
        }
        return false;
    }
    public void createTimer(){
        for(ScheduleItem item: items){
            Date date = new Date();

            Calendar now = Calendar.getInstance();
            Calendar alarm = GregorianCalendar.getInstance(Locale.US);
            SimpleDateFormat df = new SimpleDateFormat("HH:mm aa");
            try {
                date = df.parse(item.getTime().replace("\"",""));
                alarm.setTime(date);
                if(item.getTime().contains("PM")){
                    alarm.add(Calendar.HOUR_OF_DAY,12);
                }
                alarm.set(Calendar.YEAR,now.get(Calendar.YEAR));
                alarm.set(Calendar.MONTH,now.get(Calendar.MONTH));
                alarm.set(Calendar.DAY_OF_MONTH,now.get(Calendar.DAY_OF_MONTH));

                if(isToday(item.getDays())){
                    //System.out.println(alarm.getTime());
                    long timeLeft = alarm.getTimeInMillis();
                    if(timeLeft>now.getTimeInMillis()){
                        setTimer(item,timeLeft-now.getTimeInMillis());
                    }

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private void setTimer(final ScheduleItem item, long timeLeft){
        CountDownTimer mCountDownTimer;
        mCountDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //timeLeft = millisUntilFinished;
                //updateCountDownText();
            }
            @Override
            public void onFinish() {
                callClean(item.getTaskedRobot());
            }
        }.start();
        item.setmCountDownTimer(mCountDownTimer);
    }
    public void callClean(int id){
        ((MainActivity)getActivity()).cleanOrder(id,"Clean");
    }
    public void cancelClean(){
        for(final ScheduleItem item: items){
            Date date = new Date();
            Calendar now = Calendar.getInstance();
            Calendar alarm = GregorianCalendar.getInstance(Locale.US);
            SimpleDateFormat df = new SimpleDateFormat("HH:mm aa");
            try {
                date = df.parse(item.getTime().replace("\"",""));
                alarm.setTime(date);
                if(item.getTime().contains("PM")){
                    alarm.add(Calendar.HOUR_OF_DAY,12);
                }
                alarm.set(Calendar.YEAR,now.get(Calendar.YEAR));
                alarm.set(Calendar.MONTH,now.get(Calendar.MONTH));
                alarm.set(Calendar.DAY_OF_MONTH,now.get(Calendar.DAY_OF_MONTH));
                if(isToday(item.getDays())){
                    item.cancelCountDown();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}