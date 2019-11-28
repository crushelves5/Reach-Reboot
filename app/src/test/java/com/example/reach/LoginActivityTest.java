package com.example.reach;

import org.junit.Test;
import static org.junit.Assert.*;
public class LoginActivityTest {
    @Test
    public void correct_user_password(){

        //Validate the correct password and correct username
        LoginActivity activity = new LoginActivity();
        assertTrue(activity.is_valid("user1","1234"));
    }
    @Test
    public void invalid_user_valid_password(){
        //Validate the correct password and incorrect username
        LoginActivity activity = new LoginActivity();
        assertFalse(activity.is_valid("email@gmail.com","1234"));
    }
    @Test
    public void valid_user_invalid_password(){

        //Validate the incorrect password and correct username
        LoginActivity activity = new LoginActivity();
        assertFalse(activity.is_valid("user1","0qert"));

    }
    @Test
    public void invalid_user_invalid_password(){

        //Verify the incorrect password and incorrect username
        LoginActivity activity = new LoginActivity();
        assertFalse(activity.is_valid("email@yahoo.com","5678"));

    }
    @Test
    public void empty_user(){
        //Verify that empty username fails
        LoginActivity activity = new LoginActivity();
        assertFalse(activity.is_valid("","1234"));
    }
    @Test
    public void empty_password(){
        //Verify that empty password fails
        LoginActivity activity = new LoginActivity();
        assertFalse(activity.is_valid("user1",""));
    }
    @Test
    public void empty_user_password(){
        //Verify that empty username and password
        LoginActivity activity = new LoginActivity();
        assertFalse(activity.is_valid("",""));
    }


}
