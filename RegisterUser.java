package com.example.maksymalekseyev.msis_5133;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterUser extends AppCompatActivity {

    //Declarations
    Boolean doesUserExist = false;
    ArrayList<Item> userList;

    String selectSql;
    String insertSql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    public void onSignUpClick(View v){

        userList = new ArrayList<Item>();

        //Querry to get user table from the database
        selectSql = "Select username, password from student";

        //Initialize object that talks to the database and execute querry
        ExecuteSQL getUsers = new ExecuteSQL(selectSql, 2); //integer argument specifies how many columns are expected(refer to ExecuteSQL class)
        getUsers.execute();

        //Push all users into the users array;
        userList.addAll(getUsers.getDbResponse());

        //Capturing user input
        EditText fName = (EditText) findViewById(R.id.fName);
        EditText lName = (EditText) findViewById(R.id.lName);
        EditText uName = (EditText) findViewById(R.id.uName);
        EditText uPass = (EditText) findViewById(R.id.uPass);
        EditText uPassRepeat = (EditText) findViewById(R.id.uPassRepeat);

        String firstName = fName.getText().toString();
        String lastName = lName.getText().toString();
        String userName = uName.getText().toString().toLowerCase();
        String password = uPass.getText().toString();
        String passwordRepeat = uPassRepeat.getText().toString();

        //Check if a person with the same username exists
        for(Item i:userList){
            if(i.userName.equals(userName)){
                doesUserExist = true;
            }
        }

        //Preparing sql statement that will insert new user into database
        insertSql = "Insert into student(firstname, lastname, username, password) ";
        insertSql +=" values('" + firstName + "', '" + lastName + "', '" + userName +"', '" + password + "')";


        //INPUT VALIDATION

        //Check that user provided values to all fields
        if(!firstName.equals("") && !lastName.equals("") && !userName.equals("") && !password.equals("") && !passwordRepeat.equals("")){

            //Check that password matches repeat password
            if(password.equals(passwordRepeat)){

                //Check if there is no user with the same username
                if(!doesUserExist){
                    ExecuteSQL insertUser = new ExecuteSQL(insertSql);
                    insertUser.insert();
                    Toast.makeText(getApplicationContext(), "You successfuly signed up", Toast.LENGTH_SHORT).show();
                    Intent goToLoginPage = new Intent(getApplicationContext(), Login.class);
                    startActivity(goToLoginPage);

                } else {
                    doesUserExist = false;
                    Toast.makeText(getApplicationContext(), "Account with provided username already exists", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "You must fill out all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
