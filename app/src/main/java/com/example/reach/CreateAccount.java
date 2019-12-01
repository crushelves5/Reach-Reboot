package com.example.reach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.StringUtils;

public class CreateAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String [] Gender = {"Male","Female","Other"};
    String selectedGender = "Male";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Gender);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        Button create = findViewById(R.id.button3);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
            //Doesnt yet check for existing username in database
                EventDatabaseHelper dbHelper = new EventDatabaseHelper(CreateAccount.this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                String username = ((EditText) findViewById(R.id.editText3)).getText().toString();
                String password = ((EditText) findViewById(R.id.editText4)).getText().toString();
                String firstName = ((EditText) findViewById(R.id.editText5)).getText().toString();
                String lastName = ((EditText) findViewById(R.id.editText6)).getText().toString();
                String gender = selectedGender;
                if (validateAccountInput(username,password,firstName,lastName,gender) == false){
                    Toast toast = Toast.makeText(CreateAccount.this,"One or more fields is entered incorrectly",Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    AccessDB accessDB = new AccessDB();
                    accessDB.execute(username,password,firstName,lastName,gender);

                }

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
        int id = item.getItemId();
        if (id == R.id.help){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
            builder.setTitle("Help");
            builder.setMessage("Activity Author: Jikyung Kim\nActivity Version:2\n\n" +
                    "Use this Activity to create a new account, fill in all the fields and click create to create your event");
            builder.create().show();
        }
        else{
            onBackPressed();
        }

        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
         selectedGender = parent.getItemAtPosition(pos).toString();

    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    //When back arrow is clicked
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    boolean validateAccountInput(String username, String password, String firstName, String lastName, String gender){
        CharSequence cs = username;
        if( (!username.equals("") && username.length() <=12 && StringUtils.isAlphanumeric(cs)) && (!password.equals("") && password.length() <=12)&&(!firstName.equals("")) && (!lastName.equals(""))&&(gender.equals("Male") || gender.equals("Female") || gender.equals("Other"))){
            return true;
        }
        else{
            return false;
        }


    }
    protected class AccessDB extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            EventDatabaseHelper dbHelper = new EventDatabaseHelper(CreateAccount.this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            String username = strings[0];
            String password = strings[1];
            String firstName = strings[2];
            String lastName = strings[3];
            String gender = selectedGender;
            dbHelper.insertUser(database, username, password, firstName, lastName, gender);
            Intent intent = new Intent(CreateAccount.this, LoginActivity.class);
            startActivity(intent);
            return null;
        }
    }
}
