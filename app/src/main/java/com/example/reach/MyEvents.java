package com.example.reach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * Presents all events affiliated with user
 * @Author Bethel Adaghe
 */
public class MyEvents extends AppCompatActivity implements EventAdapter.onClickListener{
    String userid;
    String eventid;
    String event_name;
    String location;
    ArrayList<Item> myEventList = new ArrayList<Item>();
    ArrayList<Item> attendingList = new ArrayList<Item>();
    SQLiteDatabase database;
    ProgressBar progressBar;
//    String userSql = "SELECT rowid EVENT_ID, EVENT_NAME, LOCATION FROM EVENTS WHERE USER_ID = '"+ userid +"';";
    private RecyclerView.Adapter Adapter;
    private RecyclerView.LayoutManager LayoutManager;
    /**
     * Initialize variables and references to UI elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        Button createButton = findViewById(R.id.createButton);
        progressBar = findViewById(R.id.progressBar5);
        progressBar.setVisibility(View.INVISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        userid = getIntent().getStringExtra("userid");
        final String userSql = "SELECT rowid EVENT_ID, EVENT_NAME, LOCATION FROM EVENTS WHERE USER_ID = '"+ userid +"';";
        final String userSql2 = "SELECT rowid EVENT_ID, EVENT_NAME, LOCATION FROM EVENTS WHERE EVENT_ID = (SELECT EVENT_ID FROM ATTENDING WHERE USER_ID = '"+userid+"');";
        EventDatabaseHelper dbhelper = new EventDatabaseHelper(this);
        database = dbhelper.getWritableDatabase();
        EventAccessDB eventAccess = new EventAccessDB();
        eventAccess.execute(userSql,userSql2);
         RecyclerView recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        Adapter = new EventAdapter(myEventList,this);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setAdapter(Adapter);
        //
        RecyclerView recyclerView2 = findViewById(R.id.recyclerview2);
        recyclerView2.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        Adapter = new EventAdapter(attendingList, this);
        recyclerView2.setLayoutManager(LayoutManager);
        recyclerView2.setAdapter(Adapter);
        createButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Starts CreateEvent Activity and passes userid
             * @param view View Object
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyEvents.this, CreateEvent.class);
                intent.putExtra("userid",userid);
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
        int id = item.getItemId();
        if (id == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyEvents.this);
            builder.setTitle("Help");
            builder.setMessage("Activity Author: Bethel Adaghe\nActivity Version:7\n\n" +
                    "This activity splits events affiliated with you into Events in which you are the host and events in which you have chosen to attend, You can select an event to view it in detail or create more events Event");
            builder.create().show();
        } else {
            onBackPressed();
        }
        return true;
    }
    /**
     * When event is selected, Starts ViewEvent Activity and passes event and user ids
     * @param item Item selected
     */
    @Override
    public void onMyEventClick(Item item) {
        Intent intent = new Intent(MyEvents.this,ViewEvent.class);
        intent.putExtra("eventid", item.getText3());
        intent.putExtra("userid", userid);
        startActivity(intent);


    }

    /**
     * Async Class to read database
     */
    protected class EventAccessDB extends AsyncTask<String, Integer, String> {
        /**
         * Update progress bar
         * @param values percentage of progress
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }
        /**
         * Runs in background to query database and load event objects into 2 ArrayLists
         * @param strings contains query strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            String query1 = strings[0];
            String query2 = strings[1];
            Cursor cursor = database.rawQuery(query1,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                eventid = cursor.getString(cursor.getColumnIndex("EVENT_ID"));
                event_name = cursor.getString(cursor.getColumnIndex("EVENT_NAME"));
                location = cursor.getString(cursor.getColumnIndex("LOCATION"));
                //Insert into Items Object then append to Items Array for RecyclerView
                myEventList.add(new Item(R.drawable.reach, eventid + "-" + event_name, location, eventid));
                cursor.moveToNext();
            }
            publishProgress(50);
            cursor = database.rawQuery(query2,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                eventid = cursor.getString(cursor.getColumnIndex("EVENT_ID"));
                event_name = cursor.getString(cursor.getColumnIndex("EVENT_NAME"));
                location = cursor.getString(cursor.getColumnIndex("LOCATION"));
                //Insert into Items Object then append to Items Array for RecyclerView
                attendingList.add(new Item(R.drawable.reach, eventid + "-" + event_name, location, eventid));
                cursor.moveToNext();
            }
            publishProgress(100);
            return null;
        }
    }
    /**
     * When returning to this Activity, recreate activity
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

}
