package com.project.nomaste.ui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.project.nomaste.Login;
import com.project.nomaste.MainActivity;
import com.project.nomaste.Model.Entity.Robot;
import com.project.nomaste.R;
import com.project.nomaste.Views.DayPickerView;
import com.project.nomaste.utils.JSONDataModeler;

import java.util.Calendar;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SchedulePickerFragment extends Fragment {
    //ToDo: save instance in firebase
    TextView timer;
    int h,m;
    Button saveSchedule;
    Spinner robot;
    ToggleButton tD,tL,tM,tMi,tJ,tV,tS;
    public SchedulePickerFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        final View rootView = inflater.inflate(R.layout.fragment_schedule_picker, container, false);
        // Get a reference to the ImageView in the fragment Layout
        timer = rootView.findViewById(R.id.timer);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        rootView.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                h=hour;
                                m = min;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,h,m);
                                timer.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(h,m);
                timePickerDialog.show();
            }
        });
        tD = rootView.findViewById(R.id.tD);
        tL = rootView.findViewById(R.id.tL);
        tM = rootView.findViewById(R.id.tM);
        tMi = rootView.findViewById(R.id.tMi);
        tJ = rootView.findViewById(R.id.tJ);
        tV = rootView.findViewById(R.id.tV);
        tS = rootView.findViewById(R.id.tS);

        saveSchedule = rootView.findViewById(R.id.button_SaveSchedule);
        saveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time ="\""+ timer.getText().toString()+"\"";
                String r =robot.getSelectedItem().toString();
                String markedButtons= "";
                if(tD.isChecked()){
                    markedButtons +="D";
                }
                if(tL.isChecked()){
                    if(!markedButtons.isEmpty()) markedButtons+=",";
                    markedButtons +="L";
                }
                if(tM.isChecked()){
                    if(!markedButtons.isEmpty()) markedButtons+=",";
                    markedButtons +="M";
                }
                if(tMi.isChecked()){
                    if(!markedButtons.isEmpty()) markedButtons+=",";
                    markedButtons +="Mi";
                }
                if(tJ.isChecked()){
                    if(!markedButtons.isEmpty()) markedButtons+=",";
                    markedButtons +="J";
                }
                if(tV.isChecked()){
                    if(!markedButtons.isEmpty()) markedButtons+=",";
                    markedButtons +="V";
                }
                if(tS.isChecked()){
                    if(!markedButtons.isEmpty()) markedButtons+=",";
                    markedButtons +="S";
                }
                if(markedButtons.isEmpty()){
                    Toast.makeText(rootView.getContext(),"Error: "+"Must select a day",Toast.LENGTH_SHORT).show();
                }else{
                    markedButtons = "\""+markedButtons+"\"";
                    ((MainActivity) getActivity()).addSchedule(time,r,markedButtons);
                    ((MainActivity) getActivity()).goToScreen("Schedule");
                }

            }
        });

        robot = rootView.findViewById(R.id.spinner_robotSchedule);
        SharedPreferences prefe = this.getActivity().getApplicationContext()
                .getSharedPreferences("datos", Context.MODE_PRIVATE);
        LinkedList<Robot> robots = JSONDataModeler.getRobots(prefe.getString("robot",""));
        if(robots == null||robots.isEmpty()) robots = JSONDataModeler.translateRobots(prefe.getString("robot",""));
        ArrayAdapter arrayAdapterRobots = new ArrayAdapter(getActivity(), R.layout.list_robots, getRobotIds(robots));
        robot.setAdapter(arrayAdapterRobots);

        return rootView;
    }
    private String [] getRobotIds(LinkedList<Robot> robots){
        if(robots.size()>0){
            String []robotsIds = new String[robots.size()];
            for (int i=0; i< robots.size();i++) {
                robotsIds[i] = "Robot id "+robots.get(i).id;
            }
            return robotsIds;
        }
        return new String[]{"No Robot"};
    }
}
