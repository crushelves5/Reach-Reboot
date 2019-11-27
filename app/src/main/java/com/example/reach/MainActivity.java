package com.example.reach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.reach.EventDatabaseHelper.EVENT_TABLE_NAME;
import static com.example.reach.EventDatabaseHelper.event_id;
import static com.example.reach.EventDatabaseHelper.event_name;
import static com.example.reach.EventDatabaseHelper.location;
import static com.example.reach.EventDatabaseHelper.start_date;

public class MainActivity extends AppCompatActivity implements EventAdapter.onClickListener
{




    //Counters
    int week_num;



    private androidx.recyclerview.widget.RecyclerView RecyclerView;
    private RecyclerView.Adapter Adapter;
    private RecyclerView.LayoutManager LayoutManager;
    static SQLiteDatabase database;
    static final String event_query = "";
    String userid;
    LocalDate today, monday, sunday;



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
        final ArrayList<Item> exampleList = new ArrayList<>();
        database = dbHelper.getWritableDatabase();

        //recycler view stuff
        RecyclerView = findViewById(R.id.EventRecyclerView);
        RecyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        Adapter = new EventAdapter(exampleList, this);
        RecyclerView.setLayoutManager(LayoutManager);
        RecyclerView.setAdapter(Adapter);
        //set week
        today = LocalDate.now();
        monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY)
        {
            monday = monday.minusDays(1);
        }

        // Go forward to get Sunday
        sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY)
        {
            sunday = sunday.plusDays(1);
        }



        String query = "SELECT rowid " + event_id + "," + location +  "," + event_name +
                " FROM " + EVENT_TABLE_NAME +";";



        //populate events
        queryEvents(exampleList, database, query);
        Adapter.notifyDataSetChanged();


        myEButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyEvents.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });





    }
    @Override
    public void onMyEventClick(Item item) {
        Intent intent = new Intent(MainActivity.this,ViewEvent.class);
        intent.putExtra("eventid", item.getText3());
        intent.putExtra("userid",userid);
        startActivity(intent);
    }

}
