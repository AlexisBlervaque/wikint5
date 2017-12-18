package com.example.alexisblervaque.navigationtool2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by alexisblervaque on 13/12/2017.
 */

public class Association implements Parcelable {




    private int id;
    private String name;
    private String president;
    private String local;
    private String description;
    private ArrayList<String> pictures;
    private ArrayList<String> members;
    private ArrayList<Integer> id_Events;

    public Association(int id, String name, String president, String local, String description, ArrayList<String> pictures, ArrayList<String> members, ArrayList<Integer> id_Events) {
        this.id = id;
        this.name = name;
        this.president = president;
        this.local = local;
        this.description = description;
        this.pictures = pictures;
        this.members = members;
        this.id_Events = id_Events;
    }


    protected Association(Parcel in) {
        id = in.readInt();
        name = in.readString();
        president = in.readString();
        local = in.readString();
        description = in.readString();
        pictures = in.createStringArrayList();
        members = in.createStringArrayList();
    }



    @Override
    public String toString() {
        return "Association{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", president='" + president + '\'' +
                ", local='" + local + '\'' +
                ", description='" + description + '\'' +
                ", pictures=" + pictures +
                ", members=" + members +
                ", id_Events=" + id_Events +
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

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public ArrayList<Integer> getId_Events() {
        return id_Events;
    }

    public void setId_Events(ArrayList<Integer> id_Events) {
        this.id_Events = id_Events;
    }

    public static final Creator<Association> CREATOR = new Creator<Association>() {
        @Override
        public Association createFromParcel(Parcel in) {
            return new Association(in);
        }

        @Override
        public Association[] newArray(int size) {
            return new Association[size];
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
        dest.writeString(president);
        dest.writeString(local);
        dest.writeString(description);
        dest.writeStringList(pictures);
        dest.writeStringList(members);
    }
}
