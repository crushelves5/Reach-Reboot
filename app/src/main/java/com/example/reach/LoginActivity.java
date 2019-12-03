package com.example.reach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/**
 * A simple class interface for user to use to log into the application
 * @author Ranusha De Silva
 * @version 3
 */
public class LoginActivity extends AppCompatActivity {
    static SQLiteDatabase database;
    EditText username;
    EditText password;
    ProgressBar progressBar;
    Button loginButton;
    Button createButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //reference xml items
        loginButton = findViewById(R.id.button);
        createButton = findViewById(R.id.button2);
        username = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the login button is clicked to submit input
             * @param view view passed from the onClickListener of the button
             */
            @Override
            public void onClick(View view) {
                //Handover validation to AsyncTask
            Login login = new Login();
            login.execute(username.getText().toString(), password.getText().toString());

            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Starts the CreateAccount Activity
             * @param view view passed from the onClickListener of the button
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,CreateAccount.class);
                startActivity(intent);
            }
        });

        Snackbar.make((View)findViewById(android.R.id.content),"Use the Help menu item to learn about this activity",Snackbar.LENGTH_LONG).setAction("Action",null).show();
    }

    /**
     * Built in function to inflate menu layout items
     * @param m reference to Menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.help, m);
        return true;
    }

    /**
     *Creates Dialog box when menu item is selected
     * @param item the menu item selected
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Help");
        builder.setMessage("Activity Author: Ranusha De Silva\nVersion:3\n\n" +
                "Use this Activity to log into the App, if you dont know the built in username and password check the dropbox, or Create a New account");
        builder.create().show();
        return true;
    }

    /**
     *Async class to validate user with database
     */
    protected class Login extends AsyncTask<String, Integer, String>{
        /**
         * Update progressbar
         * @param values percentage of progress complete
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        /**
         * Validates user and starts MainActivity
         * @param strings contains username and password strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {

            disableButton();
            String name = strings[0];
            String pass = strings[1];
            publishProgress(25);
            //Query database
            String query = "SELECT USER_ID FROM USERS WHERE USER_NAME = '"+name+"' AND PASSWORD = '"+pass+"' ;";
            EventDatabaseHelper dbHelper = new EventDatabaseHelper(LoginActivity.this);
            database = dbHelper.getWritableDatabase();
            publishProgress(50);
            Cursor cursor = database.rawQuery(query,null);
            if (cursor.moveToFirst()){
                String userId = cursor.getString(cursor.getColumnIndex("USER_ID"));
                publishProgress(75);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userid",userId);
                publishProgress(100);
                enableButton();
                startActivity(intent);
            }
            else{
                    enableButton();
                    invalidInput();
            }

            return null;
        }
    }

    /**
     * Disables the login button
     */
    void disableButton(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                loginButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

            }
        });

    }

    /**
     * Re-enables the login button
     */
    void enableButton(){        runOnUiThread(new Runnable() {

        @Override
        public void run() {

            loginButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

        }
    });
    }

    /**
     * Called when credentials are deeemed invalid
     */
    void invalidInput(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast toast = Toast.makeText(LoginActivity.this,"Invalid Username or Password, check dropbox",Toast.LENGTH_LONG);
                toast.show();

            }
        });


    }

    /**
     * Validates the form of the input
     * @param username username of the user account
     * @param password password of the user account
     * @return
     */
    boolean is_valid(String username, String password){
        if ((username.equals("user1") && password.equals("1234")) || (username.equals("user2") && password.equals("5678"))){
            return true;
        }
        return false;

    }
}
