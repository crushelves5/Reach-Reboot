package com.example.reach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
}
