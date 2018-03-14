package com.example.maksymalekseyev.msis_5133;

import android.os.Parcel;
import android.os.Parcelable;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by maksymalekseyev on 4/15/17.
 */


//This class represents a data structure
// and makes a process of manipulating
// results from the database more flexible

//ideally we should have created three separate classes for User, Program and Course
// but for the time being we generalized their attributes and methods under one class for simplicity sake

public class Item {

    public int id;
    public String alias;
    public int number;
    public String name;
    public int prereqId;
    public String prereqAlias;
    public int prereqNumber;
    public Boolean isTaken = false;
    public Boolean hasPrereq = false;
    public Boolean isPrereqTaken = false;
    public String programName;
    public String userName;
    public String password;


    //Constructor to run with program selection
    public Item(String name){
        this.programName = name;
    }


    //Constructor to run with authentication
    public Item(String user, String pass){
        userName  = user;
        password = pass;
    }

    //Constructor to run with courses
    public Item(int id1, String alias1, int number1, String name1, int prereqId1){
        id = id1;
        alias = alias1;
        number = number1;
        name = name1;
        prereqId = prereqId1;
    }

    //Make course string for displaying in a list view
    public String makeCourseLine(){
        return alias + " " + String.valueOf(number) + " - " + name;
    }


    //Make prerequisite course string for displaying in the list view
    public String makePrereqLine(){
        return alias + " " + String.valueOf(number) + " - " + name + "; \nPREREQ: " + prereqAlias + " " + String.valueOf(prereqNumber);
    }

    //Make program string to display in a list view
    public String makeProgramLine(){
        return programName;
    }




}
