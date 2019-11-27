package com.example.reach;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class CreateEvent extends AppCompatActivity {
    String userid;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        userid = getIntent().getStringExtra("userid");
        final EditText dateText = findViewById(R.id.editText10);
        Button createButton = findViewById(R.id.button4);
        dateText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    c = Calendar.getInstance();
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year = c.get(Calendar.YEAR);
                    DatePickerDialog dpd = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
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
            @Override
            public void onClick(View view) {
                String eventName = ((EditText) findViewById(R.id.editText8)).getText().toString();
                String location= ((EditText) findViewById(R.id.editText9)).getText().toString();
                String date = ((EditText) findViewById(R.id.editText10)).getText().toString();
                String description = ((EditText) findViewById(R.id.editText11)).getText().toString();
                EventDatabaseHelper dbHelper = new EventDatabaseHelper(CreateEvent.this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(EventDatabaseHelper.USER_ID, userid);
                contentValues.put(EventDatabaseHelper.event_name,eventName);
                contentValues.put(EventDatabaseHelper.location,location);
                contentValues.put(EventDatabaseHelper.start_date,date);
                contentValues.put(EventDatabaseHelper.desc,description);
                database.insert(EventDatabaseHelper.EVENT_TABLE_NAME,"NullPlaceHolder",contentValues);
                finish();
            }
        });
    }
}
