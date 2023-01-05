package com.sce.findjobproject;

import static com.sce.findjobproject.Home.CheckWhichUserForRecView;
import static com.sce.findjobproject.SignIn.WhichUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {
    private ArrayList<Object> Jobs;


    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    //Tests check if email and password are valid.
    @Test
    public void emailValidator_EmptyString_ReturnsTrue() {
        assertTrue(SignUp.isEmailValid("or6562@gmail.com"));
    }

    @Test
    public void testIsPasswordValid() {
        // Test with a valid password
        String validPassword = "abcdef";
        assertTrue(SignUp.isPasswordValid(validPassword));

        // Test with an invalid password
        String invalidPassword = "abcde";
        assertFalse(SignUp.isPasswordValid(invalidPassword));
    }



    @Test
    public void testCheckWhichUserForRecView() {
        // Set the value of 'WhichUser' to 1
        WhichUser = 1;
        // Call the 'CheckWhichUserForRecView' function
        int result = CheckWhichUserForRecView();
        // Verify that the function returns 1
        assertEquals(1, result);

        // Set the value of 'WhichUser' to 2
        WhichUser = 2;
        // Call the 'CheckWhichUserForRecView' function
        result = CheckWhichUserForRecView();
        // Verify that the function returns 2
        assertEquals(2, result);

        // Set the value of 'WhichUser' to 3
        WhichUser = 3;
        // Call the 'CheckWhichUserForRecView' function
        result = CheckWhichUserForRecView();
        // Verify that the function returns 3
        assertEquals(3, result);

        // Set the value of 'WhichUser' to 4
        WhichUser = 4;
        // Call the 'CheckWhichUserForRecView' function
        result = CheckWhichUserForRecView();
        // Verify that the function returns 0
        assertEquals(0, result);
    }






}