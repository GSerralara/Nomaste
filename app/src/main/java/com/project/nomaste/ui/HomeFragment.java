package com.project.nomaste.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.nomaste.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    ImageView cleanCircle;
    ToggleButton cleanButton;
    //ToDo: Make change in button color
    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Get a reference to the ImageView in the fragment Layout

        cleanButton = rootView.findViewById(R.id.auto_clean_button);
        cleanCircle = rootView.findViewById(R.id.cleanCircle);


        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (cleanButton.isChecked()){
                   cleanCircle.setColorFilter(0xff00ff00, PorterDuff.Mode.MULTIPLY);
               }else{
                   cleanCircle.setColorFilter(getResources().getColor(R.color.colorGrey), PorterDuff.Mode.MULTIPLY);
               }
            }
        });
        return rootView;
    }
}
