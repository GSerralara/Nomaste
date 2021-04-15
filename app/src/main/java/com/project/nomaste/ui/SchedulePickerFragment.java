package com.project.nomaste.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.project.nomaste.Login;
import com.project.nomaste.MainActivity;
import com.project.nomaste.R;
import com.project.nomaste.Views.DayPickerView;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SchedulePickerFragment extends Fragment {
    //ToDo: save instance in firebase
    //ToDo: solve dayPicker
    TextView timer;
    int h,m;
    DayPickerView dayPicker;
    Button saveSchedule;
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

        //dayPicker = rootView.findViewById(R.id.daypicker);
        saveSchedule = rootView.findViewById(R.id.button_SaveSchedule);
        saveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToScreen("Schedule");
            }
        });
        return rootView;
    }
}
