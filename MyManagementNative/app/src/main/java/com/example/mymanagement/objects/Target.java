package com.example.mymanagement.objects;

import java.util.ArrayList;

public class Target {
    public int activity_id;
    public String title;
    public String activityDate;
    public String hour;
    public  int duration;
    public int checked;
    public ArrayList<Programmer> programmersPresence = new ArrayList<>();
    public ArrayList<Programmer> programmersSimpleActivity = new ArrayList<>();

    public Target(int activity_id, String title, String activityDate, String hour, int duration, int checked) {
        this.activity_id = activity_id;
        this.title = title;
        this.activityDate = activityDate;
        this.hour = hour;
        this.duration = duration;
        this.checked = checked;
        this.programmersSimpleActivity = null;
    }

    public void setProgrammersPresence(ArrayList<Programmer> programmersPresence) {
        this.programmersPresence = programmersPresence;
    }
    public void setProgrammersSimpleActivity(ArrayList<Programmer> programmersSimpleActivity) {
        this.programmersSimpleActivity = programmersSimpleActivity;
    }

}
