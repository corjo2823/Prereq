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
import android.widget.Toast;

import java.util.ArrayList;


public class SelectProgram extends AppCompatActivity {

    //Intent variable to be passed to next activity
    public final static String Extra_String = "com.example.maksymlekseyev.todo.Extra";


    //Declarations
    public ListView programListView;

    private ArrayList<String> programList;
    private ArrayList<String> selectedProg;
    private ArrayList<Item> programListObj; //this one receives list of objects from ExecuteSQL
    private ArrayAdapter<String> programAdapter;

    public Button next;

    public String sql;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prog_select);

        //Initializing Arraylists
        programList = new ArrayList<String>();
        programListObj = new ArrayList<Item>();
        selectedProg = new ArrayList<String>();

        //Initializing ArrayAdapter
        programAdapter = new ArrayAdapter<String>(this, R.layout.program_row, R.id.programRow, programList);

        //Initializing ListView and assigning ArrayAdapter to it
        programListView = (ListView) findViewById(R.id.programList);
        programListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        programListView.setAdapter(programAdapter);
        registerForContextMenu(programListView);

        //Initializing Next button
        next = (Button) findViewById(R.id.btnProgNext);



        //POPULATING PROGRAM SELECTION LIST

            //Query statement
        sql = "SELECT prog_name from program";

            //Executing query
        ExecuteSQL getCourses = new ExecuteSQL(sql, 1);//integer argument specifies number of columns expected
        getCourses.execute();

            //Saving query result into ArrayList
        programListObj.addAll(getCourses.getDbResponse());

            //Populating program list to be passed on to array adapter
        for (Item i:programListObj){
            programList.add(i.makeProgramLine());
        }

            //Notifying array adapter to reflect changes in the program list
        programAdapter.notifyDataSetChanged();



        //CAPTURING SELECTED PROGRAM (Business rule - only one program can be selected)

        //Creating listener for each item in the list view
        programListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                //Saving user selection into variable
                String selectedItem = ((TextView)view).getText().toString();

                //Checking if selected item is in selected list
                //If yes removing from the list(equivalent to unchecking)
                if(selectedProg.contains(selectedItem)){
                    selectedProg.remove(selectedItem);

                //If no adding to the list(equivalent to checking)
                } else {
                    selectedProg.add(selectedItem);
                }
            }
        });

        //Functionality for the "Next" button
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Ensuring that user selected only one program
                if(selectedProg.size() != 1){
                    Toast.makeText(getApplicationContext(), "You must select one program to proceed!", Toast.LENGTH_LONG).show();

                //If only one program selected
                } else {

                    //Creating variable to pass on to next activity with program selected
                    String toPass = selectedProg.get(0).toString();

                    //Redirecting to the next activity
                    Intent intent = new Intent(getApplicationContext(), SelectCourses.class);
                    intent.putExtra(Extra_String, toPass);
                    startActivity(intent);
                }

            }

        });

    }

}
