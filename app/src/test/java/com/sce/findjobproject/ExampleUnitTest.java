package com.sce.findjobproject;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.sce.findjobproject.Home.CheckWhichUserForRecView;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.sce.findjobproject.SignIn.WhichUser;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
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