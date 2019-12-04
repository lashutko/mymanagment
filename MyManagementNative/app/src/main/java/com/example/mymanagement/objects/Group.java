package com.example.mymanagement.objects;

import java.util.ArrayList;

public class Group {

    public String name;
    public ArrayList<Programmer> programmers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public ArrayList<Programmer> getProgrammers() {
        return programmers;
    }


    public Group(String name, ArrayList<Programmer> programmers) {
        this.name = name;
        this.programmers = programmers;
    }
}
