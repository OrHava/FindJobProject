package com.sce.findjobproject;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Sign_Up_Test {

    @Rule
    public ActivityScenarioRule<Splash> mActivityScenarioRule =
            new ActivityScenarioRule<>(Splash.class);

    @Test
    public void sign_Up_Test() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.signin), withText("sign up"),
                        childAtPosition(
                                allOf(withId(R.id.view),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.edit_email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_email2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.edit_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_password2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.button_register), withText("REGISTER"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.button_register), withText("register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.button_register), withText("register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.edit_email), withText("123"),
                        withParent(withParent(withId(R.id.edit_email2))),
                        isDisplayed()));
        editText.check(matches(withText("123")));

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.edit_email), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_email2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(click());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.edit_email), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_email2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText4.perform(replaceText("123@"));

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.edit_email), withText("123@"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_email2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText5.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.edit_email), withText("123@"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_email2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText6.perform(click());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.edit_email), withText("123@"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_email2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText7.perform(replaceText("123@gmail.com"));

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.edit_email), withText("123@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_email2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText8.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText9 = onView(
                allOf(withId(R.id.edit_password), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_password2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText9.perform(replaceText("1234567"));

        ViewInteraction textInputEditText10 = onView(
                allOf(withId(R.id.edit_password), withText("1234567"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.edit_password2),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText10.perform(closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.button_register), withText("register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton4.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
