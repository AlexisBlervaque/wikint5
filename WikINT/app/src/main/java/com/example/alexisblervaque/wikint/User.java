package com.example.alexisblervaque.wikint;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

/**
 * Created by alexisblervaque on 16/12/2017.
 */

public class User implements Parcelable{



    private String firstName;
    private String lastName;
    private ArrayList<Integer> associationsId;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setToken(String token) {
        this.email = email;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public User() {
    }

    public User(String firstName, String lastName, ArrayList<Integer> associationsId, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.associationsId = associationsId;
        this.email = email;
    }

    public User(DataSnapshot data)
    {
        this.firstName = data.child("firstName").getValue(String.class);
        this.lastName  = data.child("lastName").getValue(String.class);
        DataSnapshot AssociationsIdData = data.child("associationsId");
        ArrayList<Integer> AssociationsId = new ArrayList<>();
        for (int i = 0; i<data.child("associationsId").getChildrenCount();i++)
        {
            int idAssociation = AssociationsIdData.child(String.valueOf(i)).getValue(Integer.class);
            AssociationsId.add(idAssociation);
        }
        this.associationsId = AssociationsId;
        this.email = data.child("email").getValue(String.class);
    }


    protected User(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ArrayList<Integer> getAssociationsId() {
        return associationsId;
    }

    public void setAssociationsId(ArrayList<Integer> associationsId) {
        this.associationsId = associationsId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
    }
}
