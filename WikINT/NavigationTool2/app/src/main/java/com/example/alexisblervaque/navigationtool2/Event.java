package com.example.alexisblervaque.navigationtool2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexisblervaque on 13/12/2017.
 */

public class Event implements Parcelable {



    private int id;
    private String name;
    private Date firstDate;
    private Date endDate;
    private String description;
    private String lieu;
    private ArrayList<String> images;
    private int id_association;


    public Event(int id, String name, Date firstDate, Date endDate, String description, String lieu, ArrayList<String> images, int id_association) {
        this.id = id;
        this.name = name;
        this.firstDate = firstDate;
        this.endDate = endDate;
        this.description = description;
        this.lieu = lieu;
        this.images = images;
        this.id_association = id_association;
    }

    protected Event(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        lieu = in.readString();
        images = in.createStringArrayList();
        id_association = in.readInt();
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstDate=" + firstDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                ", lieu='" + lieu + '\'' +
                ", images=" + images +
                ", id_association=" + id_association +
                '}';
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
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
        dest.writeStringList(images);
        dest.writeInt(id_association);
    }
}

