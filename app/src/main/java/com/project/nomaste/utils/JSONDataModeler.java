package com.project.nomaste.utils;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.project.nomaste.Model.Entity.Robot;
import com.project.nomaste.Model.Entity.ScheduleItem;

import java.util.Iterator;
import java.util.LinkedList;

public class JSONDataModeler {
    private Gson g;
    public static LinkedList<Robot> getRobots(String content){
        Log.i("Modeler", "getRobots: "+content);
        LinkedList<Robot> robots = new LinkedList<>();
        Gson g = new GsonBuilder().create();
        try{
            JsonArray data = g.fromJson(content, JsonArray.class);
            for(int i = 0; i < data.size();i++){
                if (!data.get(i).isJsonNull()){
                    robots.add(new Robot(data.get(i).getAsJsonObject().get("Speed").getAsInt(),
                            data.get(i).getAsJsonObject().get("id").getAsInt()));
                }
            }
        }catch (JsonSyntaxException | JsonIOException | NullPointerException  e){
            e.printStackTrace();
        }
        return  robots;
    }
    public static LinkedList<Robot> translateRobots(String content){
        LinkedList<Robot> robots = new LinkedList<>();
        if(content.isEmpty()) return robots;

        Gson g = new GsonBuilder().create();
        try{
            JsonObject data = g.fromJson(content, JsonObject.class);
            Iterator x = data.keySet().iterator();
            JsonArray array = new JsonArray();
            while (x.hasNext()){
                String key = (String) x.next();
                array.add(data.get(key));
            }
            for(int i = 0; i < array.size();i++){
                if (!array.get(i).isJsonNull()){
                    robots.add(new Robot(array.get(i).getAsJsonObject().get("Speed").getAsInt(),
                            array.get(i).getAsJsonObject().get("id").getAsInt()));
                }
            }
        }catch (JsonSyntaxException | JsonIOException | NullPointerException  e){
            e.printStackTrace();
        }
        return  robots;
    }
    public static LinkedList<ScheduleItem> getSchedules(String content){
        Log.i("Modeler", "getRobots: "+content);
        LinkedList<ScheduleItem> schedules = new LinkedList<>();
        Gson g = new GsonBuilder().create();
        try{
            JsonArray data = g.fromJson(content, JsonArray.class);
            for(int i = 0; i < data.size();i++){
                if (!data.get(i).isJsonNull()){
                    schedules.add(new ScheduleItem(
                            data.get(i).getAsJsonObject().get("time").toString(),
                            data.get(i).getAsJsonObject().get("days").toString(),
                            data.get(i).getAsJsonObject().get("tasked").getAsInt()));
                }
            }
        }catch (JsonSyntaxException | JsonIOException | NullPointerException  e){
            e.printStackTrace();
        }
        return  schedules;
    }
    public static LinkedList<ScheduleItem> translateSchedules(String content){
        LinkedList<ScheduleItem> schedules = new LinkedList<>();
        if(content.isEmpty()) return schedules;

        Gson g = new GsonBuilder().create();
        try{
            JsonObject data = g.fromJson(content, JsonObject.class);
            Iterator x = data.keySet().iterator();
            JsonArray array = new JsonArray();
            while (x.hasNext()){
                String key = (String) x.next();
                array.add(data.get(key));
            }
            for(int i = 0; i < array.size();i++){
                if (!array.get(i).isJsonNull()){
                    schedules.add(new ScheduleItem(
                            array.get(i).getAsJsonObject().get("time").toString(),
                            array.get(i).getAsJsonObject().get("days").toString(),
                            array.get(i).getAsJsonObject().get("tasked").getAsInt()));
                }
            }
        }catch (JsonSyntaxException | JsonIOException | NullPointerException  e){
            e.printStackTrace();
        }
        return schedules;
    }
    public static String createBodyManualRequest(int id, String action){
        String data = "{\"Button\":\""+action+"\",\"Id\":"+id+"}";
        JsonObject obj = new JsonObject();
        obj.addProperty("Button", action);
        obj.addProperty("Id", id);
        return obj.toString();
    }

    public static String createBodyRobotsPatchRequest(LinkedList<Robot> robots){
        String data = "{";
        int pos =0;
        for(int i = 0; i < robots.size();i++){
            int id =robots.get(i).getId();
            data += "\""+id+"\":"+"{\"Speed\":"+robots.get(i).getSpeed()+",\"id\":"+id+"}";
            if((i+1)!=robots.size()) data+=",";
        }
        data +="}";
        Log.i("PATCH",data);
        return data;
    }
    public static String createBodyRobotsSP(LinkedList<Robot> robots){
        String data = "[";
        int pos =0;
        for(int i = 0; i < robots.size();i++){
            int id =robots.get(i).getId();
            data += "{\"Speed\":"+robots.get(i).getSpeed()+",\"id\":"+id+"}";
            if((i+1)!=robots.size()) data+=",";
        }
        data +="]";
        Log.i("SP",data);
        return data;
    }

    public static String buildContentSchedule(String time, String robot, String days){
        String content = "";
        int id = Integer.parseInt(robot.replaceAll("[^0-9]", ""));
        //{"days":"L,M,S,D","tasked":1,"time":"13:05 AM"}
        content+="{\"days\":"+days+",\"tasked\":"+id+",\"time\":"+time+"}";
        return content;
    }
    public static String createBodySchedule(LinkedList<ScheduleItem> items){
        String content="{";
        for (int i=0; i<items.size();i++){
            content +="\""+i+"\":";
            content += JSONDataModeler.buildContentSchedule(items.get(i).getTime(),
                    items.get(i).getTaskedRobot()+"",items.get(i).getDaysString());
            if((i+1)<items.size()) content+=",";
        }
        content+="}";
        return content;
    }
}
