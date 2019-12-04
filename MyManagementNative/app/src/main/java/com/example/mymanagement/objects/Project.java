package com.example.mymanagement.objects;

import java.util.ArrayList;

public class Project {
    public String name;
    public Group group;
    public ArrayList<Target> targets = new ArrayList<>();

    public Project(String name, Group group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

    public ArrayList<Target> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<Target> targets) {
        this.targets = targets;
    }

    public void addTarget(Target target){
        this.targets.add(target);
    }

}
