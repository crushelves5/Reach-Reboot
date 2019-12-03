package com.example.reach;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.reach.EventDatabaseHelper.EVENT_TABLE_NAME;
import static com.example.reach.EventDatabaseHelper.event_id;
import static com.example.reach.EventDatabaseHelper.event_name;
import static com.example.reach.EventDatabaseHelper.location;
import static com.example.reach.EventDatabaseHelper.start_date;

/**
 * Generates a list of all available events for user to select
 * @Author Jordan Harris
 */
public class MainActivity extends AppCompatActivity implements EventAdapter.onClickListener
{
    private androidx.recyclerview.widget.RecyclerView RecyclerView;
    private RecyclerView.Adapter Adapter;
    private RecyclerView.LayoutManager LayoutManager;
    static SQLiteDatabase database;
    static final String event_query = "";
    String userid;
    ArrayList<Item> exampleList;
    ProgressBar progressBar;
    String query;

    /**
     * Initialize variables and references to UI elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userid = getIntent().getStringExtra("userid");
        //Button
        Button myEButton = findViewById(R.id.button);

        //db stuff
        EventDatabaseHelper dbHelper = new EventDatabaseHelper(this);
        exampleList = new ArrayList<>();
        database = dbHelper.getWritableDatabase();

        //recycler view stuff
        RecyclerView = findViewById(R.id.EventRecyclerView);
        RecyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        Adapter = new EventAdapter(exampleList, this);
        RecyclerView.setLayoutManager(LayoutManager);
        RecyclerView.setAdapter(Adapter);
        progressBar  = findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.INVISIBLE);
        query = "SELECT rowid " + event_id + "," + location +  "," + event_name +
                " FROM " + EVENT_TABLE_NAME +";";

       //populate events
        AccessDB accessDB = new AccessDB();
        accessDB.execute(query);
//        queryEvents(exampleList,database,query);
        Adapter.notifyDataSetChanged();


        myEButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Starts the MyEvents Activity and passes userid
             * @param view View object
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyEvents.class);
                intent.putExtra("userid", userid);
                startActivityForResult(intent,10);
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Snackbar.make((View)findViewById(android.R.id.content),"Use the Help menu item to learn about this activity",Snackbar.LENGTH_LONG).setAction("Action",null).show();


    }
    /**
     * Built in function to inflate menu layout items
     * @param m reference to Menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menu_items, m);
        return true;
    }

    /**
     * Navigates to other activities based on menu item selection
     * @param item Menu item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.help:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Help");
                builder.setMessage("Activity Author: Jordan Harris\nActivity Version:5\n\n" +
                        "This Activity provides you with a List of all available events, Select an event to get more details on it, if you only want to see events related to you, visit My Events");
                builder.create().show();
                break;
            case R.id.create:
                Intent intent = new Intent(MainActivity.this, CreateEvent.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
                break;
            case R.id.myevents:
                intent = new Intent(MainActivity.this, MyEvents.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
                break;
            default:
                onBackPressed();
                break;
        }

        return true;
    }

    /**
     * Queries the database and loads events into ArrayList
     * @param exampleList The ArrayList to load event object into
     * @param db Reference to database
     * @param query Query String
     */
    public void queryEvents(ArrayList<Item> exampleList, SQLiteDatabase db, String query)
    {
        final Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {

                String name = cursor.getString(cursor.getColumnIndex(event_name));
                Log.i("Event name: " , name);
                String loc = cursor.getString(cursor.getColumnIndex(location));
                String eid = cursor.getString(cursor.getColumnIndex(event_id));
                //String eid = cursor.getString(cursor.getColumnIndex(event_id));
                exampleList.add(new Item(R.drawable.reach, eid + "-" + name, loc, eid));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    /**
     * When event is selected, Starts ViewEvent Activity and passes event and user ids
     * @param item Item selected
     */
    @Override
    public void onMyEventClick(Item item) {
        Intent intent = new Intent(MainActivity.this,ViewEvent.class);
        intent.putExtra("eventid", item.getText3());
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

    /**
     * Async Class to read database
     */
    protected class AccessDB extends AsyncTask<String, Integer, String>{
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
         * Runs in background to query database and load event objects into ArrayList
         * @param strings contains query string
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            final String query = strings[0];
            queryEvents(exampleList,database,query);
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
