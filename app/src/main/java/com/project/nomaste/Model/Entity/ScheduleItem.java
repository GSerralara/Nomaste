package com.project.nomaste.Model.Entity;

public class ScheduleItem {
    String time;
    String[] days;

    public ScheduleItem(String time, String[] days) {
        this.time = time;
        this.days = days;
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
}
