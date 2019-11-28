package com.example.reach;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateEventActivityTest {
    @Test
    public void all_standard_input(){
        //Verify that non empty name, non empty location and non empty date > current passes validation
        CreateEvent createEvent = new CreateEvent();
        assertTrue(createEvent.validateEvent("My Event 4","75, University Avenue","2020-01-01"));
    }
    @Test
    public void current_date(){

        //Verify that current date passes validation
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CreateEvent createEvent = new CreateEvent();
        assertTrue(createEvent.validateEvent("My Event 4","75, University Avenue",format.format(c)));
    }
    @Test
    public void old_date(){
        //Verify that old input date fails validation
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("My Event 4","75, University Avenue","2019-11-25"));
    }
    @Test
    public void empty_Date() {
        //Verify that location with only spaces fails validation
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("My Event", "75, University Avenue", ""));
    }
    @Test

    public void empty_name(){
        //Verify that empty event fails validation
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("","75, University Avenue","2019-12-28"));
    }
    @Test
    public void space_name(){
        //Verify that event name with only spaces
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("  ","75, University Avenue","2019-12-28"));
    }
    @Test
    public void empty_location(){
        //Verify that empty location fails validation
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("My Event","","2019-12-28"));
    }
    @Test
    public void space_location(){
        //Verify that location with only spaces fails validation
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("My Event"," ","2019-12-28"));
    }


}
