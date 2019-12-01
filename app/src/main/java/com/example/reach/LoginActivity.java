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
            @Override
            public void onClick(View view) {
                //Handover validation to AsyncTask
            Login login = new Login();
            login.execute(username.getText().toString(), password.getText().toString());

            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,CreateAccount.class);
                startActivity(intent);
            }
        });

        Snackbar.make((View)findViewById(android.R.id.content),"Use the Help menu item to learn about this activity",Snackbar.LENGTH_LONG).setAction("Action",null).show();
    }
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.help, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Help");
        builder.setMessage("Activity Author: Ranusha De Silva\nVersion:3\n\n" +
                "Use this Activity to log into the App, if you dont know the built in username and password check the dropbox, or Create a New account");
        builder.create().show();
        return true;
    }

    protected class Login extends AsyncTask<String, Integer, String>{
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

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
    void disableButton(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                loginButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

            }
        });

    }
    void enableButton(){        runOnUiThread(new Runnable() {

        @Override
        public void run() {

            loginButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

        }
    });
    }
    void invalidInput(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast toast = Toast.makeText(LoginActivity.this,"Invalid Username or Password, check dropbox",Toast.LENGTH_LONG);
                toast.show();

            }
        });


    }
    boolean is_valid(String username, String password){
        if ((username.equals("user1") && password.equals("1234")) || (username.equals("user2") && password.equals("5678"))){
            return true;
        }
        return false;

    }
}
