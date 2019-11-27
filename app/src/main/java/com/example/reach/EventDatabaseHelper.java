package com.example.reach;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDatabaseHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "CP317.db";
    static int version = 8;
    final static String USER_ID = "USER_ID";
    final static String USER_TABLE_NAME = "USERS";
    final static String USER_NAME = "USER_NAME";
    final static String PASSWORD = "PASSWORD";

    static final String EVENT_TABLE_NAME = "EVENTS";
    public static final String event_id = "EVENT_ID";
    public static final String event_name = "EVENT_NAME";
    public static final String location = "LOCATION";
    public static final String start_date = "START_DATE";
    public static final String desc = "DESCRIPTION";

    static final String ATTENDING_TABLE_NAME = "ATTENDING";
    static final String row = "id";

    final static String CREATE_USER_TABLE = "create table "+ USER_TABLE_NAME + " ("+
            USER_ID+ " integer primary key autoincrement, "+USER_NAME + " text not null, "+
            PASSWORD + " text not null, FIRST_NAME text, LAST_NAME text, GENDER text);";

    public static final String SQL_CREATE_ENTRIES_EVENTS =
            "CREATE TABLE " + EVENT_TABLE_NAME + " (" + event_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    USER_ID + " INTEGER," +
                    event_name + " TEXT," +
                    location + " TEXT," +
                    start_date + " DATE," +
                    desc + " TEXT)";

    static final String CREATE_ATTENDING_TABLE = "CREATE TABLE "+ATTENDING_TABLE_NAME+" (" + row +" integer primary key autoincrement, "+ event_id+" INTEGER, "+USER_ID+" INTEGER);";
    EventDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_ENTRIES_EVENTS);
        db.execSQL(CREATE_ATTENDING_TABLE);
        ContentValues contentValues = new ContentValues();
        //Insert Users
        contentValues.put(USER_NAME, "user1");
        contentValues.put(PASSWORD,"1234");
        contentValues.put("FIRST_NAME","Alpha");
        contentValues.put("LAST_NAME","Beta");
        contentValues.put("GENDER","Male");

        db.insert(USER_TABLE_NAME, "NullPlaceHolder", contentValues);
        contentValues.put(USER_NAME, "user2");
        contentValues.put(PASSWORD, "5678");
        contentValues.put("FIRST_NAME","Turner");
        contentValues.put("LAST_NAME","Tables");
        contentValues.put("GENDER","Female");

        db.insert(USER_TABLE_NAME,"NullPlaceHolder",contentValues);
        //Insert Events
        contentValues.clear();
        contentValues.put(USER_ID,1);
        contentValues.put(event_name,"Project Demo");
        contentValues.put(location,"75 University Ave W, Waterloo");
        contentValues.put(start_date,"2019-11-13");
        contentValues.put(desc,"Come and throw Down at our Project Demo");
        db.insert(EVENT_TABLE_NAME, "NullPlaceHolder",contentValues);
        contentValues.put(USER_ID,2);
        contentValues.put(event_name,"Project Demo 2");
        contentValues.put(location,"75 University Ave W, Waterloo");
        contentValues.put(start_date,"2019-11-14");
        contentValues.put(desc,"Come and throw Down at our Project Demo 2");
        db.insert(EVENT_TABLE_NAME, "NullPlaceHolder",contentValues);
        //Insert event to attending
        contentValues.clear();
        contentValues.put(event_id, 2);
        contentValues.put(USER_ID, 1);
        db.insert(ATTENDING_TABLE_NAME,"NullPlaceHolder",contentValues);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        //Updates to database in here
        if(newVer > oldVer){
            db.execSQL("DROP TABLE IF EXISTS USERS;");
            db.execSQL("DROP TABLE IF EXISTS EVENTS;");
            db.execSQL("DROP TABLE IF EXISTS ATTENDING;");
            onCreate(db);
        }
    }

    public void insertUser(SQLiteDatabase db, String username, String password, String firstName, String lastName, String gender){
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, username);
        contentValues.put(PASSWORD, password);
        contentValues.put("FIRST_NAME",firstName);
        contentValues.put("LAST_NAME", lastName);
        contentValues.put("GENDER",gender);
        db.insert(USER_TABLE_NAME, "NullPlaceHolder",contentValues);
    }
    public void insertAttending(SQLiteDatabase db,String event, String user){
        ContentValues contentValues = new ContentValues();
        contentValues.put(event_id,event);
        contentValues.put(USER_ID, user);
        db.insert(ATTENDING_TABLE_NAME, "NullPlaceHolder",contentValues);
    }
}
