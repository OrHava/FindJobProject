package com.sce.findjobproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }



    //Tests check if email and password are valid.
    @Test
    public void emailValidator_EmptyString_ReturnsTrue() {
        assertTrue(SignUp.isEmailValid("or6562@gmail.com"));
    }



}