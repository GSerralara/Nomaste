package com.project.nomaste.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.project.nomaste.Adapters.ScheduleAdapter;
import com.project.nomaste.Login;
import com.project.nomaste.MainActivity;
import com.project.nomaste.Model.Entity.ScheduleItem;
import com.project.nomaste.R;

import java.util.LinkedList;


public class ScheduleFragment extends Fragment {

    LinkedList<ScheduleItem> items;
    Button createSchedule;
    public ScheduleFragment() {
        // Required empty public constructor
        items= new LinkedList<ScheduleItem>();
        items.add(new ScheduleItem("10:30 AM",new String[]{"L","M","Mi","J","V","S","D"}));
        items.add(new ScheduleItem("23:00 PM",new String[]{"L","Mi","S","D"}));
        items.add(new ScheduleItem("15:20 PM",new String[]{"S","D"}));
        items.add(new ScheduleItem("18:00 PM",new String[]{"L","Mi","D"}));
        items.add(new ScheduleItem("03:00 AM",new String[]{"L","V","S","D"}));
        items.add(new ScheduleItem("00:45 AM",new String[]{"L","Mi","D"}));
        //ToDo: sacar items de firebase
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        // Get a reference to the ImageView in the fragment Layout
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
}