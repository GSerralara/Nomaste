package com.project.nomaste;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.project.nomaste.Model.Entity.Robot;
import com.project.nomaste.Model.Entity.ScheduleItem;
import com.project.nomaste.Model.Entity.User;
import com.project.nomaste.Model.Model;
import com.project.nomaste.Network.HyperTextRequester;
import com.project.nomaste.ui.HomeFragment;
import com.project.nomaste.ui.ScheduleFragment;
import com.project.nomaste.ui.SchedulePickerFragment;
import com.project.nomaste.ui.SettingsFragment;
import com.project.nomaste.ui.SmartCleaningFragment;
import com.project.nomaste.utils.JSONDataModeler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    Model model = new Model();
    //Nested views in app
    SmartCleaningFragment smartCleaning = new SmartCleaningFragment();
    SettingsFragment settings = new SettingsFragment();
    HomeFragment home = new HomeFragment();
    ScheduleFragment schedule = new ScheduleFragment();
    SchedulePickerFragment schedulePicker = new SchedulePickerFragment();
    Gamepad gamepad = new Gamepad();

    int idSelected;
    String manualOrder =new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.content_main);
        // Create MenuNav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        getRobotsFromFirebase();
        getSchedulesFromFirebase();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.setting:
                loadFragment(settings);
                return true;
            case R.id.schedule:
                loadFragment(schedule);
                return true;
            case R.id.smart_cleaning:
                loadFragment(smartCleaning);
                return true;
            case R.id.home:
                loadFragment(home);
                return true;
            case R.id.gamepad:
                loadFragment(gamepad);
                return true;
        }
        return false;
    }
    public void goToScreen(String screen){
        switch (screen){
            case "Schedule":
                loadFragment(schedule);
                break;
            case "ScheduleCreator":
                loadFragment(schedulePicker);
                break;
        }
    }
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }
    private void refreshFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(fragment);
        transaction.attach(fragment);
        transaction.commit();
    }
    private void getRobotsFromFirebase() {
        String uid = FirebaseAuth.getInstance().getUid();
        model.setUserLogged(new User(uid));
        URL searchUrl = HyperTextRequester.createURL("users",uid,"Robots.json");
        SharedPreferences prefe =getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        editor.putString("robot", "");
        editor.commit();
        //finish();
        new GetRobotFirebaseTask().execute(searchUrl);
    }
    private void getSchedulesFromFirebase(){
        String uid = FirebaseAuth.getInstance().getUid();
        model.setUserLogged(new User(uid));
        URL searchUrl = HyperTextRequester.createURL("users",uid,"Schedule.json");
        new GetScheduleFirebaseTask().execute(searchUrl);
    }

    private void storeJsonDataRobots(String data) {
        putDataOnSP(data);
        model.setRobots(JSONDataModeler.getRobots(data));
        if(model.getRobots() == null || model.getRobots().isEmpty()){
            model.setRobots(JSONDataModeler.translateRobots(data));
        }
        updateFragments();
    }
    public void addSchedule(String time, String robot, String days){

        int id = Integer.parseInt(robot.replaceAll("[^0-9]", ""));
        String uid = FirebaseAuth.getInstance().getUid();

        model.addSchedule(new ScheduleItem(time,days,id));

        URL patchUrl = HyperTextRequester.createURL("users",uid,"Schedule.json");
        new PatchScheduleFirebaseTask().execute(patchUrl);
    }
    private void storeJsonDataSchedule(String data) {
        System.out.println(data);
        model.setScheduleItems(JSONDataModeler.getSchedules(data));
        if(model.getScheduleItems() == null || model.getScheduleItems().isEmpty()){
            model.setScheduleItems(JSONDataModeler.translateSchedules(data));
        }
        schedule.getBDData(model.getScheduleItems());
        schedule.createTimer();
    }

    public void updateFragments(){

        this.home.updateData(getApplicationContext(),model);
        //refreshFragment(home);
        this.settings.updateData(model);
    }

    public void updateRobots(){
        LinkedList<Robot> robots = settings.getItems();
        for(Robot r:robots){
            System.out.println("R: "+r.id);
        }
    }

    @NonNull
    public Model getModel(){
        return model;
    }

    public void pushRobot(Robot newRobot){
        model.addRobot(newRobot);
        putDataOnSP(JSONDataModeler.createBodyRobotsSP(model.getRobots()));
        URL searchUrl = HyperTextRequester.createURL("users",model.getUID(),"Robots.json");
        new PatchRobotsFirebaseTask().execute(searchUrl);
        updateFragments();
    }

    private void putDataOnSP(String data){
        SharedPreferences prefe =getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        editor.putString("robot", data);
        editor.commit();
    }

    public void scleanOrder(String order,int id){
        URL patchUrl = HyperTextRequester.buildUrl("S-clean/"+id+".json");
        new PatchSCLEANFirebaseTask().execute(patchUrl);
        cleanOrder(id,"SClean");
    }

    public void cleanOrder(int id, String order){
        manualOrder=order;
        idSelected=id;
        if(order !="Clean" && order !="Stop") home.setCheck(true);
        URL patchUrl = HyperTextRequester.buildUrl("Manual/"+id+".json");
        new sendManualFirebaseTask().execute(patchUrl);
    }

    public class GetRobotFirebaseTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String streamOutput = null;
            try {
                streamOutput = HyperTextRequester.getResponseFromHttpUrl(searchUrl);
                Log.i("Stream-Data",""+streamOutput);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return streamOutput;
        }
        @Override
        protected void onPostExecute(String streamOutput) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (streamOutput != null && !streamOutput.equals("")) {
                // Call showJsonDataView if we have valid, non-null results
                storeJsonDataRobots(streamOutput);
                //mSearchResultsTextView.setText(githubSearchResults);
            } else {
                // Call showErrorMessage if the result is null in onPostExecute
                Log.e("ERROR-Data","Not Robots gotten");
            }
        }


    }

    public class GetScheduleFirebaseTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String streamOutput = null;
            try {
                streamOutput = HyperTextRequester.getResponseFromHttpUrl(searchUrl);
                Log.i("Stream-Data",""+streamOutput);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return streamOutput;
        }
        @Override
        protected void onPostExecute(String streamOutput) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (streamOutput != null && !streamOutput.equals("")) {
                storeJsonDataSchedule(streamOutput);
            } else {
                // Call showErrorMessage if the result is null in onPostExecute
                Log.e("ERROR-Data","Not Schedules gotten");
            }
        }


    }
    public class PatchRobotsFirebaseTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(URL... params) {
            URL patchUrl = params[0];
            String body = JSONDataModeler.createBodyRobotsPatchRequest(model.getRobots());
            try {
                HyperTextRequester.patchDataFromHttpURL(patchUrl, body);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return body;
        }
    }
    public class PatchScheduleFirebaseTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(URL... params) {
            URL patchUrl = params[0];
            String body = JSONDataModeler.createBodySchedule(model.getScheduleItems());
            try {
                HyperTextRequester.patchDataFromHttpURL(patchUrl, body);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return body;
        }
    }
    public class PatchSCLEANFirebaseTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(URL... params) {
            URL patchUrl = params[0];
            String body = smartCleaning.getDim();
            try {
                HyperTextRequester.patchDataFromHttpURL(patchUrl, body);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return body;
        }
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
            //ToDo: get selected id
            String body = JSONDataModeler.createBodyManualRequest(idSelected,manualOrder);
            try {
                HyperTextRequester.patchDataFromHttpURL(patchUrl, body);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return body;
        }
    }
}
