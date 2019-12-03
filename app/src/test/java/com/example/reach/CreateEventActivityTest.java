package com.example.reach;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CreateEventActivity Unit Tests
 */
public class CreateEventActivityTest {
    /**
     * Verify that non empty name, non empty location and non empty date > current passes validation
     */
    @Test
    public void all_standard_input(){
        //Verify that non empty name, non empty location and non empty date > current passes validation
        CreateEvent createEvent = new CreateEvent();
        assertTrue(createEvent.validateEvent("My Event 4","75, University Avenue","2020-01-01"));
    }

    /**
     * Verify that current date passes validation
     */
    @Test
    public void current_date(){

        //Verify that current date passes validation
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CreateEvent createEvent = new CreateEvent();
        assertTrue(createEvent.validateEvent("My Event 4","75, University Avenue",format.format(c)));
    }
    /**
     *Verify that old input date fails validation
     */
    @Test
    public void old_date(){
        //Verify that old input date fails validation
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("My Event 4","75, University Avenue","2019-11-25"));
    }
    /**
     *Verify that location with only spaces fails validation
     */
    @Test
    public void empty_Date() {
        //Verify that location with only spaces fails validation
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("My Event", "75, University Avenue", ""));
    }
    /**
     *Verify that empty event fails validation
     */
    @Test

    public void empty_name(){
        //Verify that empty event fails validation
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("","75, University Avenue","2019-12-28"));
    }
    /**
     *Verify that event name with only spaces
     */
    @Test
    public void space_name(){
        //Verify that event name with only spaces
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("  ","75, University Avenue","2019-12-28"));
    }
    /**
     *Verify that empty location fails validation
     */
    @Test
    public void empty_location(){
        //Verify that empty location fails validation
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("My Event","","2019-12-28"));
    }
    /**
     *Verify that location with only spaces fails validation
     */
    @Test
    public void space_location(){
        //Verify that location with only spaces fails validation
        CreateEvent createEvent = new CreateEvent();
        assertFalse(createEvent.validateEvent("My Event"," ","2019-12-28"));
    }


}
