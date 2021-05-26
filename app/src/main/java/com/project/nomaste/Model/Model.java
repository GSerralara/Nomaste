package com.project.nomaste.Model;

import com.project.nomaste.Model.Entity.Robot;
import com.project.nomaste.Model.Entity.ScheduleItem;
import com.project.nomaste.Model.Entity.User;

import java.util.LinkedList;

public class Model {
    LinkedList<Robot> robots;
    User userLogged;
    LinkedList<ScheduleItem> scheduleItems;
    public Model() {
        this.scheduleItems = new LinkedList<ScheduleItem>();
        this.robots = new LinkedList<Robot>();
    }
    public Model(User userLogged) {
        this.userLogged = userLogged;
        this.scheduleItems = new LinkedList<ScheduleItem>();
        this.robots = new LinkedList<Robot>();
    }

    public Model(LinkedList<Robot> robots, User userLogged, LinkedList<ScheduleItem> scheduleItems) {
        this.robots = robots;
        this.userLogged = userLogged;
        this.scheduleItems = scheduleItems;
    }

    public LinkedList<Robot> getRobots() {
        return robots;
    }

    public void setRobots(LinkedList<Robot> robots) {
        this.robots = robots;
    }

    public User getUserLogged() {
        return userLogged;
    }

    public void setUserLogged(User userLogged) {
        this.userLogged = userLogged;
    }

    public LinkedList<ScheduleItem> getScheduleItems() {
        return scheduleItems;
    }

    public void setScheduleItems(LinkedList<ScheduleItem> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }
    public String getUID(){
        return this.userLogged.getUid();
    }
    public void addRobot(Robot r){
        boolean find=false;
        for (Robot ro:robots){
            if(ro.getId()==r.getId()){
                find = true;
            }
        }
        if(find){
            return;
        }else {
            int id = r.getId()+1;
            this.robots.set(id,r);
        }
    }
    public void addSchedule(ScheduleItem item){
        this.scheduleItems.add(item);
    }
}
