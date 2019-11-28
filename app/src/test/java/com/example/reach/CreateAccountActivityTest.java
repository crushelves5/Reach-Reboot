package com.example.reach;

import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;

public class CreateAccountActivityTest {

    @Test
    public void all_valid_inputs(){
        //Verify all valid inputs passes validation
        CreateAccount createAccount = new CreateAccount();
        assertTrue(createAccount.validateAccountInput("user3","9012","Bobby","Caldwell","Male"));
    }
    @Test
    public void empty_user(){
        //Verify empty username fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("","9012","Bobby","Caldwell","Male"));
    }
    @Test
    public void empty_password(){
        //Verify empty password fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","","Bobby","Caldwell","Male"));
    }
    @Test
    public void empty_firstName(){
        //Verify empty first name fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","9012","","Caldwell","Other"));
    }
    @Test
    public void empty_lastName(){
        //Verify empty last name fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","9012","Bobby","","Male"));
    }
    @Test
    public void empty_user_password(){
        //Verify empty username and password fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("","","Bobby","Caldwell","FeMale"));
    }
    @Test
    public void empty_user_firstName(){
        //Verify empty user name and first name fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("","9012","","Caldwell","Male"));
    }
    @Test
    public void empty_user_lastName(){
        //Verify empty username and last name fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("","9012","Bobby","","FeMale"));
    }
    @Test
    public void empty_password_firstName(){
        //Verify empty password and first name fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","","","Caldwell","Other"));
    }
    @Test
    public void empty_password_lastName(){
        //Verify empty password and last name fail validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","","Bobby","","Male"));
    }
    @Test
    public void empty_firstName_lastName(){
        //Verify empty first name and last name fail validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","9012","","","FeMale"));
    }
    @Test
    public void all_input_empty(){
        //Verify all empty input fail validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("","","","","Male"));
    }
    @Test
    public void username_over_character_limit(){
        //Verify username over character limit 12 fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("abcdefghijklmno","","","","Male"));
    }
    @Test
    public void password_over_character_limit(){
        //Verify password over character limit 12 fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","1234567890123","","","Male"));
    }
    @Test
    public void username_nonalphanumeric(){
        //Verify username that is nonalphanumeric fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("u s*er+3","1234","Bob","Dylan","Male"));
    }


}
