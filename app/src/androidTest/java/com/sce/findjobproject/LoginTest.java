package com.sce.findjobproject;
;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import org.junit.Test;

public class LoginTest {
    @Test
    public void testLogin(){
        Log.d("Test","Trying to log in with username and password");
        onView(withId(R.id.Email)).perform(typeText("username"));
        onView(withId(R.id.password1)).perform(typeText("password"));
        onView(withId(R.id.loginbtn)).perform(click());

        Log.d("Test","Checking if login was successful");

        onView(withId(R.id.loginbtn)).check(matches(withText("Login successful")));
    }
}
