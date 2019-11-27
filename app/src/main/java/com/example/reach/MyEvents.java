package com.example.reach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MyEvents extends AppCompatActivity implements EventAdapter.onClickListener{
    String userid;
    String eventid;
    String event_name;
    String location;
    ArrayList<Item> myEventList = new ArrayList<Item>();
    ArrayList<Item> attendingList = new ArrayList<Item>();
//    String userSql = "SELECT rowid EVENT_ID, EVENT_NAME, LOCATION FROM EVENTS WHERE USER_ID = '"+ userid +"';";
    private RecyclerView.Adapter Adapter;
    private RecyclerView.LayoutManager LayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        Button createButton = findViewById(R.id.createButton);

        userid = getIntent().getStringExtra("userid");
        final String userSql = "SELECT rowid EVENT_ID, EVENT_NAME, LOCATION FROM EVENTS WHERE USER_ID = '"+ userid +"';";
        final String userSql2 = "SELECT rowid EVENT_ID, EVENT_NAME, LOCATION FROM EVENTS WHERE EVENT_ID = (SELECT EVENT_ID FROM ATTENDING WHERE USER_ID = '"+userid+"');";
        EventDatabaseHelper dbhelper = new EventDatabaseHelper(this);
        SQLiteDatabase database = dbhelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(userSql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            eventid = cursor.getString(cursor.getColumnIndex("EVENT_ID"));
            event_name = cursor.getString(cursor.getColumnIndex("EVENT_NAME"));
            location = cursor.getString(cursor.getColumnIndex("LOCATION"));
            //Insert into Items Object then append to Items Array for RecyclerView
            myEventList.add(new Item(R.drawable.reach, eventid + "-" + event_name, location, eventid));
            cursor.moveToNext();
        }
         RecyclerView recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        Adapter = new EventAdapter(myEventList,this);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setAdapter(Adapter);
        cursor = database.rawQuery(userSql2,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            eventid = cursor.getString(cursor.getColumnIndex("EVENT_ID"));
            event_name = cursor.getString(cursor.getColumnIndex("EVENT_NAME"));
            location = cursor.getString(cursor.getColumnIndex("LOCATION"));
            //Insert into Items Object then append to Items Array for RecyclerView
            attendingList.add(new Item(R.drawable.reach, eventid + "-" + event_name, location, eventid));
            cursor.moveToNext();
        }
        RecyclerView recyclerView2 = findViewById(R.id.recyclerview2);
        recyclerView2.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        Adapter = new EventAdapter(attendingList, this);
        recyclerView2.setLayoutManager(LayoutManager);
        recyclerView2.setAdapter(Adapter);




        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyEvents.this, CreateEvent.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMyEventClick(Item item) {
        Intent intent = new Intent(MyEvents.this,ViewEvent.class);
        intent.putExtra("eventid", item.getText3());
        intent.putExtra("userid", userid);
        startActivity(intent);


    }

}
