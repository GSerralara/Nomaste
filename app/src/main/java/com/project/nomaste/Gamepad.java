package com.project.nomaste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.project.nomaste.Model.Entity.Robot;
import com.project.nomaste.Network.FirebaseConnector;
import com.project.nomaste.Network.HyperTextRequester;
import com.project.nomaste.utils.JSONDataModeler;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Gamepad extends Fragment {
    AutoCompleteTextView autoCTVRobots;
    public String pressedButton;
    int id;
    public Gamepad (){

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the fragment layout
        final View rootView = inflater.inflate(R.layout.gamepad, container, false);
        autoCTVRobots = rootView.findViewById(R.id.robotsGamepadList);
        SharedPreferences prefe = rootView.getContext().getApplicationContext().getSharedPreferences("datos", Context.MODE_PRIVATE);
        LinkedList<Robot> robots = JSONDataModeler.getRobots(prefe.getString("robot",""));
        if(robots == null||robots.isEmpty()) robots = JSONDataModeler.translateRobots(prefe.getString("robot",""));

        ArrayAdapter arrayAdapterRobots = new ArrayAdapter(rootView.getContext(),R.layout.list_robots,getRobotIds(robots));
        // to make default value
        autoCTVRobots.setText(arrayAdapterRobots.getItem(0).toString(),false);

        autoCTVRobots.setAdapter(arrayAdapterRobots);
        autoCTVRobots.setKeyListener(null);

        // to make default value

        final Button down = rootView.findViewById(R.id.down_button);
        down.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pressedButton="Down";
                id = Integer.parseInt(autoCTVRobots.getText().toString().replaceAll("[^0-9]", ""));
                URL patchUrl = HyperTextRequester.buildUrl("Manual/"+id+".json");
                new sendManualFirebaseTask().execute(patchUrl);
            }
        });
        final Button up = rootView.findViewById(R.id.up_button);
        up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pressedButton="Up";
                id = Integer.parseInt(autoCTVRobots.getText().toString().replaceAll("[^0-9]", ""));
                URL patchUrl = HyperTextRequester.buildUrl("Manual/"+id+".json");
                new sendManualFirebaseTask().execute(patchUrl);
            }
        });
        final Button right = rootView.findViewById(R.id.right_button);
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pressedButton="Right";
                id = Integer.parseInt(autoCTVRobots.getText().toString().replaceAll("[^0-9]", ""));
                URL patchUrl = HyperTextRequester.buildUrl("Manual/"+id+".json");
                new sendManualFirebaseTask().execute(patchUrl);
            }
        });
        final Button left = rootView.findViewById(R.id.left_button);
        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pressedButton="Left";
                id = Integer.parseInt(autoCTVRobots.getText().toString().replaceAll("[^0-9]", ""));
                URL patchUrl = HyperTextRequester.buildUrl("Manual/"+id+".json");
                new sendManualFirebaseTask().execute(patchUrl);
            }
        });
        final Button stop = rootView.findViewById(R.id.stop_button);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedButton="Stop";
                id = Integer.parseInt(autoCTVRobots.getText().toString().replaceAll("[^0-9]", ""));
                URL patchUrl = HyperTextRequester.buildUrl("Manual/"+id+".json");
                new sendManualFirebaseTask().execute(patchUrl);
            }
        });
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
    public class sendManualFirebaseTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(URL... params) {
            URL patchUrl = params[0];

            String body = JSONDataModeler.createBodyManualRequest(id,pressedButton);
            try {
                HyperTextRequester.patchDataFromHttpURL(patchUrl, body);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return body;
        }
    }
}
