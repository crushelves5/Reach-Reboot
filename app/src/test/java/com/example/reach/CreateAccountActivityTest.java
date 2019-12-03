package com.example.reach;

import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;

/**
 * CreateAccountActivity Unit tests
 */
public class CreateAccountActivityTest {
    /**
     * Verify all valid inputs passes validation
     */
    @Test
    public void all_valid_inputs(){
        //Verify all valid inputs passes validation
        CreateAccount createAccount = new CreateAccount();
        assertTrue(createAccount.validateAccountInput("user3","9012","Bobby","Caldwell","Male"));
    }
    /**
     *Verify empty username fails validation
     */
    @Test
    public void empty_user(){
        //Verify empty username fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("","9012","Bobby","Caldwell","Male"));
    }
    /**
     *Verify empty password fails validation
     */
    @Test
    public void empty_password(){
        //Verify empty password fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","","Bobby","Caldwell","Male"));
    }

    /**
     * Verify empty first name fails validation
     */
    @Test
    public void empty_firstName(){
        //Verify empty first name fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","9012","","Caldwell","Other"));
    }
    /**
     *Verify empty last name fails validation
     */
    @Test
    public void empty_lastName(){
        //Verify empty last name fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","9012","Bobby","","Male"));
    }
    /**
     *Verify empty username and password fails validation
     */
    @Test
    public void empty_user_password(){
        //Verify empty username and password fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("","","Bobby","Caldwell","FeMale"));
    }
    /**
     *Verify empty user name and first name fails validation
     */
    @Test
    public void empty_user_firstName(){
        //Verify empty user name and first name fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("","9012","","Caldwell","Male"));
    }
    /**
     *Verify empty username and last name fails validation
     */
    @Test
    public void empty_user_lastName(){
        //Verify empty username and last name fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("","9012","Bobby","","FeMale"));
    }
    /**
     *Verify empty password and first name fails validation
     */
    @Test
    public void empty_password_firstName(){
        //Verify empty password and first name fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","","","Caldwell","Other"));
    }
    /**
     *Verify empty password and last name fail validation
     */
    @Test
    public void empty_password_lastName(){
        //Verify empty password and last name fail validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","","Bobby","","Male"));
    }
    /**
     *Verify empty first name and last name fail validation
     */
    @Test
    public void empty_firstName_lastName(){
        //Verify empty first name and last name fail validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","9012","","","FeMale"));
    }
    /**
     *Verify all empty input fail validation
     */
    @Test
    public void all_input_empty(){
        //Verify all empty input fail validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("","","","","Male"));
    }
    /**
     *Verify username over character limit 12 fails validation
     */
    @Test
    public void username_over_character_limit(){
        //Verify username over character limit 12 fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("abcdefghijklmno","","","","Male"));
    }
    /**
     *Verify password over character limit 12 fails validation
     */
    @Test
    public void password_over_character_limit(){
        //Verify password over character limit 12 fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("user3","1234567890123","","","Male"));
    }
    /**
     *Verify username that is nonalphanumeric fails validation
     */
    @Test
    public void username_nonalphanumeric(){
        //Verify username that is nonalphanumeric fails validation
        CreateAccount createAccount = new CreateAccount();
        assertFalse(createAccount.validateAccountInput("u s*er+3","1234","Bob","Dylan","Male"));
    }


}
