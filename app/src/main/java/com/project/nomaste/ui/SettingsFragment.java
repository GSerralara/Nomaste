package com.project.nomaste.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.nomaste.Adapters.RobotsAdapter;
import com.project.nomaste.Login;
import com.project.nomaste.MainActivity;
import com.project.nomaste.Model.Entity.Robot;
import com.project.nomaste.Model.Entity.ScheduleItem;
import com.project.nomaste.Model.Model;
import com.project.nomaste.R;
import com.project.nomaste.utils.JSONDataModeler;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsFragment extends Fragment {
    LinkedList<Robot> items;
    Button addRobot, logOut;
    RobotsAdapter adapter;
    public SettingsFragment(){
        items= new LinkedList<Robot>();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        // Get a reference to the ImageView in the fragment Layout
        adapter = new RobotsAdapter(items,rootView.getContext());
        RecyclerView recyclerView = rootView.findViewById(R.id.robotsAdmin_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(adapter);
        addRobot = rootView.findViewById(R.id.AddRobotbutton);
        addRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(rootView.getContext());
            }
        });

        logOut = rootView.findViewById(R.id.LogOut_button);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(((MainActivity) getActivity()).getApplicationContext(), Login.class));
                ((MainActivity) getActivity()).finish();
            }
        });

        return rootView;
    }
    /**
     * Display dialog
     */
    private void showDialog(Context context) {
         // Initialize the layout file
        View dialogView = View.inflate(context, R.layout.dialog_add_robot, null);
        AlertDialog.Builder mMessageBuilder = new AlertDialog.Builder(context);
        final AlertDialog mDialog = mMessageBuilder.create();
        //Confirm button
        TextView confirm = dialogView.findViewById(R.id.dialog_add);
        //Cancel button
        TextView cancel = dialogView.findViewById(R.id.dialog_cancel);
        //Description
        final EditText input = dialogView.findViewById(R.id.dialog_input);

         // Determine and cancel the button listener event
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str= input.getText().toString();
                int num =Integer.parseInt(str);
                items.add(new Robot(3,num));
                adapter.setItems(items);
                mDialog.dismiss();
                ((MainActivity)getActivity()).pushRobot(new Robot(3,num));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });


        // Set the background color to be transparent,
        // solve the problem of setting a white right angle after rounding
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setView(dialogView);
        mDialog.setCanceledOnTouchOutside(false);//Clicking on the screen does not disappear
        mDialog.show();
         //Set the parameters must be after the show, otherwise there is no effect
        WindowManager.LayoutParams params =   mDialog.getWindow().getAttributes();
        mDialog.getWindow().setAttributes(params);
    }

    public void setItems(LinkedList<Robot> items){
        this.items=items;
    }
    public void updateData(Model m){
        setItems(m.getRobots());
    }

    public LinkedList<Robot> getItems() {
        return items;
    }
}
