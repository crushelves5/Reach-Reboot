package com.example.reach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.commons.lang3.StringUtils;

public class CreateAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String [] Gender = {"Male","Female","Other"};
    String selectedGender = "Male";

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

        Button create = findViewById(R.id.button3);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Doesnt yet check for existing username in database
                EventDatabaseHelper dbHelper = new EventDatabaseHelper(CreateAccount.this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                String username = ((EditText) findViewById(R.id.editText3)).getText().toString();
                String password = ((EditText) findViewById(R.id.editText4)).getText().toString();
                String firstName = ((EditText) findViewById(R.id.editText5)).getText().toString();
                String lastName = ((EditText) findViewById(R.id.editText6)).getText().toString();
                String gender = selectedGender;
                dbHelper.insertUser(database,username,password,firstName,lastName,gender);
                Intent intent = new Intent(CreateAccount.this,LoginActivity.class);
                startActivity(intent);
            }
        });
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
}
