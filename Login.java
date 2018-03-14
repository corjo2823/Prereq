package com.example.maksymalekseyev.msis_5133;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    //Declarations:
    ArrayList<Item> userList;
    String sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //Login button functionality
    public void btnLoginClick(View v) {

        //Capture user name and password input from the user
        EditText userName = (EditText) findViewById(R.id.userName);
        EditText password = (EditText) findViewById(R.id.password);

        String user = userName.getText().toString().toLowerCase();
        String pass = password.getText().toString();

        //Initialize array to store users
        userList = new ArrayList<Item>();

        //Querry to get user table from the database
        sql = "Select username, password from student";

        //Initialize object that talks to the database and execute querry
        ExecuteSQL getUsers = new ExecuteSQL(sql, 2); //integer argument specifies how many columns are expected(refer to ExecuteSQL class)
        getUsers.execute();

        //Push all users into the users array;
        userList.addAll(getUsers.getDbResponse());


        //Check if user provided input for username
        if(!user.equals("")){

            //User Authentication
            for(Item i:userList){

                //Check if there is a record in the database that corresponds to the value user provided
                if(user.equals(i.userName)){

                    //Check if identified user provided correct password
                    if(pass.equals(i.password)){

                        //Redirect to Program selection page
                        Intent goToProgPage = new Intent(getApplicationContext(), SelectProgram.class);
                        startActivity(goToProgPage);

                    //Notify user that password is wrong
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            //Notify user that username input is required
        } else {
            Toast.makeText(getApplicationContext(), "Enter username and password", Toast.LENGTH_SHORT).show();
        }

    }

    //Signup button functionality
    public void btnSignUpClick(View v){

        Intent goToSignUpPage = new Intent(getApplicationContext(), RegisterUser.class);
        startActivity(goToSignUpPage);

    }

}
