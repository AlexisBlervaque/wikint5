package com.example.alexisblervaque.wikint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Created by alexisblervaque on 16/12/2017.
 */

public class gsonDate{

    public gsonDate(){}
    public gsonDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "gsonDate{" +
                "date=" + date +
                '}';
    }

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date convertToDate(String dateString)
    {
        String jsonString = "{\"date\": \"" + dateString + "\"}";
        Gson gson = new GsonBuilder().create();
        gsonDate date = gson.fromJson(jsonString,gsonDate.class);
        return date.getDate();
    }
}
