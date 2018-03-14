package com.example.maksymalekseyev.msis_5133;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by maksymalekseyev on 4/14/17.
 */

//WE ARE NOT USING ASYNC TASK FUNCTIONALITY.
//THE APP CAN NOT GO TO THE NEXT STEP UNLESS RESULT FROM THE QUERY IS RECEIVED

public class ExecuteSQL {

    // Variable declarations
    private int colNum; //specifies how many columns are expected back as result of the query

    private String message = "";
    private String dbRequest = "";


    private Boolean isSuccess = false;

    private Connection con;
    private ResultSet rs;

    //Query result will be saved to an array of custom object "Item" (please refer to class "Item" for details)
    private ArrayList<Item> dbResponse = new ArrayList<>();


    //Accesors and mutators
    public void setDbRequest(String sql){
        this.dbRequest = sql;
    }

    public String getDbRequest(){
        return this.dbRequest;
    }

    public ArrayList<Item> getDbResponse(){
      return this.dbResponse;
    };


    //Constructor for select queries(used in the following classes: "SelectProgram", "SelectCourses", "CoursesToTake")
    public ExecuteSQL(String query, int resColNum) {
        this.dbRequest = query;
        this.colNum = resColNum;
    }

    // Constructor for insert queries(used in the following classes: "RegisterUser")
    public ExecuteSQL(String query) {
        this.dbRequest = query;
    }


    //Method to run Select Query
    public void execute(){
        try
        {
            // Connect to database
            con = connectionclass();

            // Check if connection failed
            if (con == null)
            {
                message = "Check Your Internet Access!";
            }

            // Proceed with request if connected
            else
            {
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(dbRequest);

                message = "Query successful!";
                isSuccess=true;

                //based on the number of colums specified
                //the program will run various constructors for an "Item" object
                //colNum = 1 is used by "SelectProgram" class and only requires one column from DB - program_name
                //colNum = 5 is used by "SelectCourses" class and requires five columns back from DB
                //colNum = 2 is used by "Login" class and requires two columns back from DB - username and password

                while (rs.next()){
                    if(colNum == 1 ){
                        dbResponse.add(new Item(rs.getString(1)));
                    } else if(colNum == 5){
                        dbResponse.add(new Item(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5)));
                    } else if(colNum == 2){
                        dbResponse.add(new Item(rs.getString(1), rs.getString(2)));
                    }
                }


                con.close();

            }
        }

        // Catch any errors
        catch (Exception ex)
        {
            isSuccess = false;
            message = ex.getMessage();

            Log.d ("sql error", message);
        }

    }

    //Method to run Insert query
    public void insert(){
        try
        {
            // Connect to database
            con = connectionclass();

            // Check if connection failed
            if (con == null)
            {
                message = "Check Your Internet Access!";
            }

            // Proceed with request if connected
            else
            {
                Statement stmt = con.createStatement();
                stmt.executeQuery(dbRequest);

                message = "Query successful!";
                isSuccess=true;
                con.close();

            }
        }

        // Catch any errors
        catch (Exception ex)
        {
            isSuccess = false;
            message = ex.getMessage();

            Log.d ("sql error", message);
        }

    }





    @SuppressLint("NewApi")
    public Connection connectionclass()
    {
        // The first two lines always the same
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //your database connection string goes below
            ConnectionURL = "jdbc:jtds:sqlserver://malekse.database.windows.net:1433;DatabaseName=Course_prereq;user=malekse@malekse;password=Msis5133;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }

}
