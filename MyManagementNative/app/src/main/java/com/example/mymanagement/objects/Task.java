package com.example.mymanagement.objects;

import java.util.ArrayList;

public class Task {
    public String name;
    public ArrayList<Project> projects = new ArrayList<>();

    public Task(String name,  ArrayList<Project> projects) {
        this.name = name;
        this.projects = projects;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}
