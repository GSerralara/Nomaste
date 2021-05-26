package com.project.nomaste.Model.Entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Robot {
    public int speed;
    public int id;

    public Robot() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Robot(int speed, int id) {
       this.id = id;
       this.speed = speed;
    }
    public String getName(){
        return "Robot id "+id;
    }

    public int getSpeed() {
        return speed;
    }

    public int getId() {
        return id;
    }
}
