package com.example.alexisblervaque.wikint;

import java.util.ArrayList;

/**
 * Created by alexisblervaque on 16/12/2017.
 */

public class Data{
    private ArrayList<Association> associations;
    private ArrayList<Event> events;

    public Data(){
        associations = new ArrayList<>();
        events = new ArrayList<>();
    }

    public ArrayList<Association> getAssociations() {
        return associations;
    }

    public void setAssociations(ArrayList<Association> associations) {
        this.associations = associations;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
