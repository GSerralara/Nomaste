package com.project.nomaste.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.nomaste.Gamepad;
import com.project.nomaste.MainActivity;
import com.project.nomaste.Model.Entity.Robot;
import com.project.nomaste.Model.Model;
import com.project.nomaste.R;
import com.project.nomaste.utils.JSONDataModeler;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    ImageView cleanCircle;
    AutoCompleteTextView autoCleanRobots;
    ToggleButton cleanButton;
    Activity parent;
    String selectedRobot = new String();
    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Get a reference to the ImageView in the fragment Layout
        autoCleanRobots = rootView.findViewById(R.id.robotsHomeList);
        parent = getActivity();
        updateData(rootView.getContext(), null);


        autoCleanRobots.setKeyListener(null);
        autoCleanRobots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRobot = autoCleanRobots.getText().toString().replaceAll("[^0-9]", "");
            }
        });
        cleanButton = rootView.findViewById(R.id.auto_clean_button);
        cleanCircle = rootView.findViewById(R.id.cleanCircle);


        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (cleanButton.isChecked()){
                   cleanCircle.setColorFilter(0xff00ff00, PorterDuff.Mode.MULTIPLY);
                   String str = autoCleanRobots.getText().toString().replaceAll("[^0-9]", "");
                   ((MainActivity) getActivity()).cleanOrder(Integer.parseInt(str),"Clean");
               }else{
                   String str = autoCleanRobots.getText().toString().replaceAll("[^0-9]", "");
                   cleanCircle.setColorFilter(getResources().getColor(R.color.colorGrey), PorterDuff.Mode.MULTIPLY);
                   ((MainActivity) getActivity()).cleanOrder(Integer.parseInt(str),"Stop");
               }
            }
        });
        return rootView;
    }
    public String getSelectedRobot(){
        return selectedRobot;
    }
    public void setCheck(boolean state){
        cleanButton.setChecked(state);
        if(state){
            cleanCircle.setColorFilter(0xff00ff00, PorterDuff.Mode.MULTIPLY);
        }else {
            cleanCircle.setColorFilter(getResources().getColor(R.color.colorGrey), PorterDuff.Mode.MULTIPLY);
        }
    }

    public void updateData(Context context, Model model){
        LinkedList<Robot> robots;
        try {
            if(model == null){
                SharedPreferences prefe = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
                robots = JSONDataModeler.getRobots(prefe.getString("robot",""));
                if(robots== null || robots.isEmpty()){
                    robots = JSONDataModeler.translateRobots(prefe.getString("robot",""));
                }
            }else{
                robots = model.getRobots();
            }

            ArrayAdapter arrayAdapterRobots = new ArrayAdapter(context, R.layout.list_robots, getRobotIds(robots));
            autoCleanRobots.setAdapter(arrayAdapterRobots);
            // to make default value
            autoCleanRobots.setText(arrayAdapterRobots.getItem(0).toString(),false);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


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
