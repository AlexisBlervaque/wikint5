package com.example.alexisblervaque.wikint;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alexisblervaque on 13/12/2017.
 */

public class Event implements Parcelable {




    @Override
    public String toString() {
        return name;
    }


    private int id;
    private String name;
    private Date firstDate;
    private Date endDate;
    private String description;
    private String lieu;
    private String images;
    private int id_association;


    public Event(){}


    public Event(int id, int id_association)
    {
        this.id = id;
        this.name = "";
        this.firstDate = Calendar.getInstance().getTime();
        this.endDate = Calendar.getInstance().getTime();
        this.description = "";
        this.lieu = "";
        this.id_association = id_association;
    }

    public Event(int id, String name, Date firstDate, Date endDate, String description, String lieu, String images, int id_association) {
        this.id = id;
        this.name = name;
        this.firstDate = firstDate;
        this.endDate = endDate;
        this.description = description;
        this.lieu = lieu;
        this.images = images;
        this.id_association = id_association;
    }

    public Event(DataSnapshot ds)
    {
        this.id = ds.child("id").getValue(Integer.class);
        this.name = ds.child("name").getValue(String.class);
        this.firstDate = ds.child("firstDate").getValue(Date.class);
        this.endDate = ds.child("endDate").getValue(Date.class);
        this.description = ds.child("description").getValue(String.class);
        this.lieu = ds.child("lieu").getValue(String.class);
        this.images = ds.child("images").getValue(String.class);
        this.id_association = ds.child("id_association").getValue(Integer.class);
    }



    protected Event(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        lieu = in.readString();
        images = in.readString();
        id_association = in.readInt();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public Date getEndDate() {return endDate;}

    public void setEndDate(Date endDate) { this.endDate = endDate;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getId_association() {
        return id_association;
    }

    public void setId_association(int id_association) {
        this.id_association = id_association;
    }



    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(lieu);
        dest.writeString(images);
        dest.writeInt(id_association);
    }
}

