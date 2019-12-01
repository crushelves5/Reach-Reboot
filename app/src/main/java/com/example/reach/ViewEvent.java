package com.example.reach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

public class ViewEvent extends AppCompatActivity implements OnMapReadyCallback {
    String eventid;
    String userid;
    String addressString;
    EventDatabaseHelper dbhelper;
    SQLiteDatabase database;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Button attendButton = findViewById(R.id.attendButton);
        Button deleteButton = findViewById(R.id.button5);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressBar = findViewById(R.id.progressBar6);
        deleteButton.setVisibility(View.INVISIBLE);
        eventid = getIntent().getStringExtra("eventid");
        userid = getIntent().getStringExtra("userid");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String sql = "SELECT EVENTS.USER_ID, EVENTS.EVENT_NAME, EVENTS.LOCATION, EVENTS.START_DATE, EVENTS.DESCRIPTION, USERS.FIRST_NAME, USERS.LAST_NAME FROM EVENTS INNER JOIN USERS ON EVENTS.USER_ID = USERS.USER_ID WHERE EVENTS.EVENT_ID = '"+eventid+"';";
        dbhelper = new EventDatabaseHelper(this);
        database = dbhelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        TextView title = findViewById(R.id.textView16);
        TextView hostName = findViewById(R.id.textView18);
        TextView desc = findViewById(R.id.textView20);
        final TextView date = findViewById(R.id.textView21);
        TextView location = findViewById(R.id.textView22);
        title.setText(cursor.getString(cursor.getColumnIndex(EventDatabaseHelper.event_name)));
        hostName.setText(cursor.getString(cursor.getColumnIndex("FIRST_NAME"))+" "+ cursor.getString(cursor.getColumnIndex("LAST_NAME")));
        desc.setText(cursor.getString(cursor.getColumnIndex(EventDatabaseHelper.desc)));
        date.setText(cursor.getString(cursor.getColumnIndex(EventDatabaseHelper.start_date)));
        addressString = cursor.getString(cursor.getColumnIndex(EventDatabaseHelper.location));
        location.setText(addressString);
        if (userid.equals(cursor.getString(cursor.getColumnIndex(EventDatabaseHelper.USER_ID)))){
            attendButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
        cursor = database.rawQuery("SELECT USER_ID FROM ATTENDING WHERE USER_ID = '"+userid+"' AND EVENT_ID= '"+eventid+"';",null);

        if(cursor.moveToFirst()){
            attendButton.setVisibility(View.INVISIBLE);
        }
        cursor.close();
        attendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbhelper.insertAttending(database,eventid,userid);
                Toast.makeText(ViewEvent.this,"You are now set to attend this event", Toast.LENGTH_LONG).show();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewEvent.this);
                builder.setMessage(("Do you want to delete this event?"))
                        .setTitle("Delete Event")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Delete using ID
                                AccessDB accessDB = new AccessDB();
                                accessDB.execute(eventid);
                                Toast.makeText(ViewEvent.this, "Deleting Event",Toast.LENGTH_LONG);


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //User cancelled the dialog
                            }
                        })
                        .show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ViewEvent.this);
            builder.setTitle("Help");
            builder.setMessage("Activity Author: Onkar Deol\nActivity Version:5\n\n" +
                    "This Activity provides a more detailed viewed of a previously selected event. Here you can choose to Attend an event you are not hosting and also delete an event that you created. You can only Delete an event if you created it and only attend events you didnt create and are already not set to attend. Navigate on the map of the location to get sense of the area around the location");
            builder.create().show();
        }
        else{
            onBackPressed();
        }

        return true;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> address;
        Address location = null;
        try {
            address = geocoder.getFromLocationName(addressString,5);
            location = address.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GoogleMap mMap = googleMap;

        LatLng event = new LatLng(location.getLatitude(),location.getLongitude());
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style));
        mMap.addMarker(new MarkerOptions().position(event).title("Event Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(event,16));
    }
    protected class AccessDB extends AsyncTask<String, Integer, String>{
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {
            String event = strings[0];
            database.delete(dbhelper.EVENT_TABLE_NAME,dbhelper.event_id + "="+event,null);
            publishProgress(50);
            try{
                database.delete(dbhelper.ATTENDING_TABLE_NAME, dbhelper.event_id+"="+event,null);
            } catch (Exception e) {
                //Means Noone was attending the event
            }

            publishProgress(100);
            finish();
            return null;
        }
    }
}
