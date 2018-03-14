package com.example.maksymalekseyev.msis_5133;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectCourses extends AppCompatActivity {


    //Declarations
    private ArrayList<String> classList;
    public ArrayList<String> selectedClass;
    public ArrayList<Item> classListObj;
    private ArrayAdapter<String> classAdapter;
    public Button next;
    public ListView classListView;
    public String sql;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_c_selection);

        // Capturing selected program from previous activity
        final String selProg = getIntent().getStringExtra(SelectProgram.Extra_String);

        //Initializing array lists
        classList = new ArrayList<String>();
        selectedClass = new ArrayList<String>();
        classListObj = new ArrayList<Item>(); //this one receives list of objects from ExecuteSQL

        //Initializing array adapter
        classAdapter = new ArrayAdapter<String>(this, R.layout.program_row, R.id.programRow, classList);

        //Initializing list view and assigning array adapter to it
        classListView = (ListView) findViewById(R.id.classList);
        classListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        classListView.setAdapter(classAdapter);

        //Initializing "Next button"
        next = (Button) findViewById(R.id.btnCourseNext);


        //POPULATING LIST VIEW WITH PROGRAM RELATED CLASSES

        //Query to get all classes from the database that relate to selected program
        sql = " SELECT course.courseid, subject.alias, course.number, course.name, course.prerequisite";
        sql +=" FROM subject, course, programrequirement, program ";
        sql +=" WHERE subject.subjectid = course.subjectid ";
        sql +=" AND course.courseid = programrequirement.courseid ";
        sql +=" AND programrequirement.programid = program.programid ";
        sql +=" AND program.prog_name = '" + selProg + "'";


        //Execute query
        ExecuteSQL getCourses = new ExecuteSQL(sql, 5); //integer argument specifies number of columns expected back as a result of query
        getCourses.execute();

        //Capture database response (list of classes)
        classListObj.addAll(getCourses.getDbResponse());

        //Populate classList
        for (Item i:classListObj){
            classList.add(i.makeCourseLine());
        }

        //Notify adapter of changes
        classAdapter.notifyDataSetChanged();


        //CAPTURE SELECTED COURSES

        //Onclick listener for each individual line in courseList
        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //Saving clicked item into a variable
                String selectedItem = ((TextView)view).getText().toString();

                //Checking if selected item is in selected list

                //If yes removing from the list(equivalent to unchecking)
                if(selectedClass.contains(selectedItem)){
                    selectedClass.remove(selectedItem);

                    //If no adding to the list(equivalent to checking)
                } else {
                    selectedClass.add(selectedItem);
                }
            }
        });

        //Next button functionality
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    //Creating intent
                    Intent intent = new Intent(getApplicationContext(), CoursesToTake.class);

                    //Attaching selected classes and selected program to be passed on to next activity
                    intent.putExtra("Selected_Class", selectedClass);
                    intent.putExtra("Selected_Prog", selProg);

                    //Redirecting to the next activity
                    startActivity(intent);

            }

        });


    }

}
