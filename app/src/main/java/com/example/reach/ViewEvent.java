package com.example.reach;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class ViewEvent extends AppCompatActivity implements OnMapReadyCallback {
    String eventid;
    String userid;
    String addressString;
    EventDatabaseHelper dbhelper;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Button attendButton = findViewById(R.id.attendButton);


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
            }
        });

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
}
