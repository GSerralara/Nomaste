package com.project.nomaste.Model.Entity;

import android.os.CountDownTimer;

public class ScheduleItem {
    int taskedRobot;
    String time;
    String[] days;
    CountDownTimer mCountDownTimer;

    public ScheduleItem(String time, String[] days, int robotId) {
        this.time = time;
        this.days = days;
        this.taskedRobot = robotId;
    }
    public ScheduleItem(String time, String data, int robotId) {
        this.time = time;
        setDays(data);
        this.taskedRobot = robotId;
    }
    public ScheduleItem(String time, int robotId) {
        this.time = time;
        this.days = days;
        this.taskedRobot = robotId;
    }

    public int getTaskedRobot() {
        return taskedRobot;
    }

    public void setTaskedRobot(int taskedRobot) {
        this.taskedRobot = taskedRobot;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String[] getDays() {
        return days;
    }
    public String getDaysString() {
        String str = "";
        for(int i=0;i<days.length;i++){
            str += days[i];
            if((i+1)<days.length)str += ',';
        }
        return str;
    }

    public void setDays(String[] days) {
        this.days = days;
    }
    public void setDays(String data) {
        this.days =data.split(",");
    }

    public void setmCountDownTimer(CountDownTimer mCountDownTimer) {
        this.mCountDownTimer = mCountDownTimer;
    }
    public void cancelCountDown(){
        this.mCountDownTimer.cancel();
    }
}
