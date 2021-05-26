package com.project.nomaste.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.project.nomaste.MainActivity;
import com.project.nomaste.Model.Entity.Robot;
import com.project.nomaste.R;
import com.project.nomaste.Register;
import com.project.nomaste.utils.JSONDataModeler;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SmartCleaningFragment extends Fragment {
    Spinner robotSpinner, positionSpinner;
    Button cleanButton,stopButton;
    EditText h,w;
    ImageView surface;
    TextView height, width;

    public SmartCleaningFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        final View rootView = inflater.inflate(R.layout.fragment_smartcleaning, container, false);
        // Get a reference to the ImageView in the fragment Layout
        cleanButton = rootView.findViewById(R.id.button_clean);
        stopButton = rootView.findViewById(R.id.button_stop_sclean);
        robotSpinner = rootView.findViewById(R.id.spinner_robot);
        positionSpinner = rootView.findViewById(R.id.spinner_position);
        h = rootView.findViewById(R.id.tv_h);
        w = rootView.findViewById(R.id.tv_w);
        surface = rootView.findViewById(R.id.surface);
        height = rootView.findViewById(R.id.height);
        width = rootView.findViewById(R.id.width);

        String[] pos = {"Top-Left", "Top-Right", "Bottom-Left", "Bottom-Right"};
        ArrayAdapter arrayAdapterPos= new ArrayAdapter(getActivity(), R.layout.list_robots, pos);
        positionSpinner.setAdapter(arrayAdapterPos);

        SharedPreferences prefe = this.getActivity().getApplicationContext()
                .getSharedPreferences("datos", Context.MODE_PRIVATE);
        LinkedList<Robot> robots = JSONDataModeler.getRobots(prefe.getString("robot",""));
        if(robots == null||robots.isEmpty()) robots = JSONDataModeler.translateRobots(prefe.getString("robot",""));
        ArrayAdapter arrayAdapterRobots = new ArrayAdapter(getActivity(), R.layout.list_robots, getRobotIds(robots));
        robotSpinner.setAdapter(arrayAdapterRobots);
        positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String uri;
                int imageResource;
                Drawable res;
                switch (position) {
                    case 0:
                        uri = "@drawable/topleft";  // where myresource (without the extension) is the file
                        imageResource= getResources().getIdentifier(uri, null, getActivity().getPackageName());
                        res = getResources().getDrawable(imageResource);
                        surface.setImageDrawable(res);
                        break;

                    case 1:
                        uri = "@drawable/topright";  // where myresource (without the extension) is the file
                        imageResource= getResources().getIdentifier(uri, null, getActivity().getPackageName());
                        res = getResources().getDrawable(imageResource);
                        surface.setImageDrawable(res);
                        break;

                    case 2:
                        uri = "@drawable/bottomleft";  // where myresource (without the extension) is the file
                        imageResource= getResources().getIdentifier(uri, null, getActivity().getPackageName());
                        res = getResources().getDrawable(imageResource);
                        surface.setImageDrawable(res);
                        break;

                    case 3:
                        uri = "@drawable/bottomright";  // where myresource (without the extension) is the file
                        imageResource= getResources().getIdentifier(uri, null, getActivity().getPackageName());
                        res = getResources().getDrawable(imageResource);
                        surface.setImageDrawable(res);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        h.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                height.setText(h.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        w.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                width.setText(w.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String id_str = robotSpinner.getSelectedItem().toString().replaceAll("[^0-9]", "");
                    String str_position = positionSpinner.getSelectedItem().toString();
                    String dimH = height.getText().toString();
                    String dimW = width.getText().toString();
                    String dim = "\"Dim\":{\"H\":" + Integer.parseInt(dimH) + ",";
                    dim += "\"W\":" + Integer.parseInt(dimW) + "}";
                    String order = "{\"Corner\":\"" + str_position + "\"," + dim;
                    System.out.println(order);
                    ((MainActivity) getActivity()).scleanOrder(order,Integer.parseInt(id_str));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    Toast.makeText(rootView.getContext(),"Error: "+"Input 0.0 not valid",Toast.LENGTH_SHORT).show();
                }
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_str = robotSpinner.getSelectedItem().toString().replaceAll("[^0-9]", "");
                ((MainActivity) getActivity()).cleanOrder(Integer.parseInt(id_str),"Stop");
            }
        });
        return rootView;
    }

    public String getDim(){
        String str_position = positionSpinner.getSelectedItem().toString();
        String dimH = height.getText().toString();
        String dimW = width.getText().toString();
        String dim = "\"Dim\":{\"H\":" + Integer.parseInt(dimH) + ",";
        dim += "\"W\":" + Integer.parseInt(dimW) + "}";
        String order = "{\"Corner\":\"" + str_position + "\"," + dim+"}";
        return order;
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
