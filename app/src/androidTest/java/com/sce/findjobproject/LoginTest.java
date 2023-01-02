package com.sce.findjobproject;
;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

public class LoginTest {
    @Test
    public void testLogin(){
        DatabaseReference database= FirebaseDatabase.getInstance().getReference();
        database.child("logs").push().setValue("Trying to log in with username and password");
        onView(withId(R.id.Email)).perform(typeText("username"));
        onView(withId(R.id.password1)).perform(typeText("password"));
        onView(withId(R.id.loginbtn)).perform(click());
        database.child("logs").push().setValue("Checking if login was successful");
        onView(withId(R.id.loginbtn)).check(matches(withText("Login successful")));
    }
}
