package com.example.mymanagement.objects;

import java.util.ArrayList;

public class Curator {

    private int ID;
    private String name;
    private String tagId;
    private String mobileToken;
    public ArrayList<Project> projects;

    public Curator(int ID, String name, String tagId, String mobileToken, ArrayList<Project> projects) {
        this.ID = ID;
        this.name = name;
        this.tagId = tagId;
        this.mobileToken = mobileToken;
        this.projects = projects;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public String getTagId() {
        return tagId;
    }

    public String getMobileToken() {
        return mobileToken;
    }
    public void setMobileToken(String mobileToken) {
        this.mobileToken = mobileToken;
    }
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
