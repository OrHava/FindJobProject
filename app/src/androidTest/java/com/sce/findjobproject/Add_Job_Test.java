package com.sce.findjobproject;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
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
public class Add_Job_Test {

    @Rule
    public ActivityScenarioRule<Splash> mActivityScenarioRule =
            new ActivityScenarioRule<>(Splash.class);

    @Test
    public void add_Job_Test() {
        ViewInteraction materialRadioButton = onView(
                allOf(withId(R.id.radio_LFE), withText("???? ??????"),
                        childAtPosition(
                                allOf(withId(R.id.RadioGroup),
                                        childAtPosition(
                                                withId(R.id.view),
                                                6)),
                                1),
                        isDisplayed()));
        materialRadioButton.perform(click());

        ViewInteraction materialRadioButton2 = onView(
                allOf(withId(R.id.radio_LFE), withText("???? ??????"),
                        childAtPosition(
                                allOf(withId(R.id.RadioGroup),
                                        childAtPosition(
                                                withId(R.id.view),
                                                6)),
                                1),
                        isDisplayed()));
        materialRadioButton2.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.sign_in_button),
                        childAtPosition(
                                allOf(withId(R.id.socialicons),
                                        childAtPosition(
                                                withId(R.id.view),
                                                8)),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.btnProfile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navButtons),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edtCompany), withText("123"),
                        childAtPosition(
                                allOf(withId(R.id.LayoutPostJob),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(""));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edtCompany),
                        childAtPosition(
                                allOf(withId(R.id.LayoutPostJob),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edtCompany),
                        childAtPosition(
                                allOf(withId(R.id.LayoutPostJob),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edtCompany),
                        childAtPosition(
                                allOf(withId(R.id.LayoutPostJob),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("GilC"), closeSoftKeyboard());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.SpinJobsType1), withText("????? ?????"),
                        childAtPosition(
                                allOf(withId(R.id.LayoutPostJob),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                5),
                        isDisplayed()));
        materialTextView.perform(click());

        DataInteraction materialTextView2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)))
                .atPosition(1);
        materialTextView2.perform(click());

        ViewInteraction materialTextView3 = onView(
                allOf(withId(R.id.SpinJobsLocation1), withText("?????"),
                        childAtPosition(
                                allOf(withId(R.id.LayoutPostJob),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                8),
                        isDisplayed()));
        materialTextView3.perform(click());

        ViewInteraction materialTextView4 = onView(
                allOf(withId(R.id.SpinJobsLocation1), withText("?????"),
                        childAtPosition(
                                allOf(withId(R.id.LayoutPostJob),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                8),
                        isDisplayed()));
        materialTextView4.perform(click());

        DataInteraction materialTextView5 = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)))
                .atPosition(0);
        materialTextView5.perform(click());

        DataInteraction materialTextView6 = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)))
                .atPosition(0);
        materialTextView6.perform(click());

        DataInteraction materialTextView7 = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)))
                .atPosition(0);
        materialTextView7.perform(click());

        DataInteraction materialTextView8 = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)))
                .atPosition(0);
        materialTextView8.perform(click());

        DataInteraction materialTextView9 = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)))
                .atPosition(0);
        materialTextView9.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.edtJobDescription),
                        childAtPosition(
                                allOf(withId(R.id.NestedScrollView1),
                                        childAtPosition(
                                                withId(R.id.LayoutPostJob),
                                                10)),
                                0),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("????? ????"), closeSoftKeyboard());

        pressBack();

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.BtnUpJobDesc), withText("??? ?????"),
                        childAtPosition(
                                allOf(withId(R.id.LayoutPostJob),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                11),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.btnHome),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navButtons),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction materialTextView10 = onView(
                allOf(withId(R.id.SpinJobsType),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        2),
                                1),
                        isDisplayed()));
        materialTextView10.perform(click());

        DataInteraction materialTextView11 = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)))
                .atPosition(1);
        materialTextView11.perform(click());

        ViewInteraction materialTextView12 = onView(
                allOf(withId(R.id.SpinJobsLocation),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        2),
                                2),
                        isDisplayed()));
        materialTextView12.perform(click());

        DataInteraction materialTextView13 = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                2)))
                .atPosition(0);
        materialTextView13.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.BtnSearch), withContentDescription("????? ?????"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        2),
                                4),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.contactsRecView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                6)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.contactsRecView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                6)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.contactsRecView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                6)));
        recyclerView3.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.txtCompanyName), withText("GilC"),
                        withParent(allOf(withId(R.id.RelativeLayoutSize),
                                withParent(withId(R.id.parent)))),
                        isDisplayed()));
        textView.check(matches(withText("GilC")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.txtCompanyName), withText("GilC"),
                        withParent(allOf(withId(R.id.RelativeLayoutSize),
                                withParent(withId(R.id.parent)))),
                        isDisplayed()));
        textView2.check(matches(withText("GilC")));
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
