package com.example.maksymalekseyev.msis_5133;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CoursesToTake extends AppCompatActivity {

    //Declarations
    ArrayList<Item>classList;
    ArrayList<String>toTakeList;
    ArrayList<String>prereqList;

    ArrayAdapter<String> classAdapter;
    ArrayAdapter<String> prereqAdapter;

    String  sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_to_take);


        //Capture taken classes from previous activity
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> takenCourses = bundle.getStringArrayList("Selected_Class");

        //Capture program taken from previous activity
        String selProg = bundle.getString("Selected_Prog");


        //Initializing array lists
        classList = new ArrayList<Item>();
        toTakeList = new ArrayList<String>();
        prereqList = new ArrayList<String>();

        //Initializing array lists
        classAdapter = new ArrayAdapter<String>(this, R.layout.to_take_row, R.id.toTakeRow, toTakeList);
        prereqAdapter = new ArrayAdapter<String>(this, R.layout.to_take_row, R.id.toTakeRow, prereqList);

        //Initializing list views
        ListView classListView = (ListView) findViewById(R.id.toTakeList);
        ListView prereqListView = (ListView) findViewById(R.id.prereqList);

        //Assigning array adapters to list views
        classListView.setAdapter(classAdapter);
        prereqListView.setAdapter(prereqAdapter);

        //Sql statement to get all courses for selected program
        sql = " SELECT course.courseid, subject.alias, course.number, course.name, course.prerequisite";
        sql +=" FROM subject, course, programrequirement, program ";
        sql +=" WHERE subject.subjectid = course.subjectid ";
        sql +=" AND course.courseid = programrequirement.courseid ";
        sql +=" AND programrequirement.programid = program.programid ";
        sql +=" AND program.prog_name = '" + selProg + "'";

        //Executing sql statement
        ExecuteSQL getCourses = new ExecuteSQL(sql, 5);
        getCourses.execute();

        //Populating class list
        classList.addAll(getCourses.getDbResponse());


        //Populating prerequisite columns within the class list
        for(Item i:classList){
            if(i.prereqId > 0 ){
                i.hasPrereq = true;
                for(Item y:classList){
                    if (y.id == i.prereqId){
                        i.prereqAlias = y.alias;
                        i.prereqNumber = y.number;
                    }
                }
            }
        }


        //Marking taken courses withing the class list
        for(Item i:classList){
            for(String y:takenCourses){
                if(i.makeCourseLine().equals(y)){
                    i.isTaken = true;
                }
            }
        }




        //Marking taken prerequisites withing the cllass list
        for(Item i:classList){
            for(Item y:classList){
                if((i.hasPrereq) && (String.valueOf(i.prereqId).equals(String.valueOf(y.id)) && y.isTaken)){
                    i.isPrereqTaken = true;
                };
            }
        }



        //PREREQUISITE LOGIC
        for(Item course : classList){

            //Check if course was not taken
            if(!course.isTaken){

                //Check if course has prerequisite

                //If course has prerequisite that have not been taken
                if(course.hasPrereq && !course.isPrereqTaken){

                    //Add this course to a list of courses requiring prerequisite
                    prereqList.add(course.makePrereqLine());
                } else {

                    //Add this course to a list of available to take courses
                    toTakeList.add(course.makeCourseLine());
                }
            }
        }

        //Notifying array adapters of changes to be reflected in list views
        classAdapter.notifyDataSetChanged();
        prereqAdapter.notifyDataSetChanged();


    }
}
