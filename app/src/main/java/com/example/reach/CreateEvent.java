package com.example.reach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * A form for the user to fill in, in order to add an event
 * @Author Bethel Adaghe
 */
public class CreateEvent extends AppCompatActivity {
    String userid;
    Calendar c;
    ProgressBar progressBar;
    /**
     * Initialize variables and references to UI elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        userid = getIntent().getStringExtra("userid");
        final EditText dateText = findViewById(R.id.editText10);
        Button createButton = findViewById(R.id.button4);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dateText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    c = Calendar.getInstance();
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year = c.get(Calendar.YEAR);
                    DatePickerDialog dpd = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
                        /**
                         * Generate String result from DatePicker selection
                         * @param datePicker DatePicker object
                         * @param i year
                         * @param i1 month (0-11)
                         * @param i2 day (1-31)
                         */
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            dateText.setText(i+"-"+(i1+1)+"-"+i2);
                        }
                    },year,month,day);
                    dpd.show();
                }

                return true;
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Extracts and submits form data to AccessDB class
             * @param view
             */
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String eventName = ((EditText) findViewById(R.id.editText8)).getText().toString();
                String location= ((EditText) findViewById(R.id.editText9)).getText().toString();
                String date = ((EditText) findViewById(R.id.editText10)).getText().toString();
                String description = ((EditText) findViewById(R.id.editText11)).getText().toString();
                if (validateEvent(eventName,location,date) == false){
                    Toast toast = Toast.makeText(CreateEvent.this,"One or more fields is invalid", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                        AccessDB accessDB = new AccessDB();
                        accessDB.execute(eventName,location,date,description);
                        finish();
                }

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
        int id = item.getItemId();
        if (id == R.id.help){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateEvent.this);
            builder.setTitle("Help");
            builder.setMessage("Activity Author: Bethel Adaghe\nActivity Version:2\n\n" +
                    "This Activity is used to create and insert events into the database. You must fill out all the fields properly to be able to create an Event, If Google Maps cannot generate a single match to the location, it will be rejected");
            builder.create().show();
        }
        else{
            onBackPressed();
        }

        return true;
    }

    /**
     * Validates the date passed in
     * @param date Date string
     * @return true if valid date
     */
    boolean dateValidator(String date){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate;
        //Set current date to midnight for better comparison
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(c);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        c = calendar.getTime();
        try {
            inputDate = format.parse(date);
            if(c.compareTo(inputDate) <= 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Validate form inputs
     * @param name name of event
     * @param location location string of event
     * @param date Date string of event
     * @return true if all fields are valid
     */
    boolean validateEvent(String name, String location, String date){

        if(!name.trim().equals("") && (!date.equals("") && dateValidator(date)) && !location.trim().equals("") && validate_location(location)== true ){
            return true;
        }
    return false;
    }
    protected class AccessDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String eventName = strings[0];
            String location= strings[1];
            String date = strings[2];
            String description = strings[3];
            EventDatabaseHelper dbHelper = new EventDatabaseHelper(CreateEvent.this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(EventDatabaseHelper.USER_ID, userid);
            contentValues.put(EventDatabaseHelper.event_name, eventName);
            contentValues.put(EventDatabaseHelper.location, location);
            contentValues.put(EventDatabaseHelper.start_date, date);
            contentValues.put(EventDatabaseHelper.desc, description);
            database.insert(EventDatabaseHelper.EVENT_TABLE_NAME, "NullPlaceHolder", contentValues);


            return null;
        }
    }

    /**
     * Validates location string using Geocoder
     * @param address
     * @return
     */
    boolean validate_location(String address){
        Geocoder geocoder = new Geocoder(CreateEvent.this);
        //If address produces an error, then reject
        try {
           List<Address> addresses = geocoder.getFromLocationName(address,5);
           addresses.get(0);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        catch(IOException e){
            return false;
        }
    }

}
