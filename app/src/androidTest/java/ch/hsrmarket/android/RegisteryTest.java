package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hsrmarket.android.model.Account;
import ch.hsrmarket.android.model.Address;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class RegisteryTest {

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);


    @Test
    public void StudentIdCheck(){
        onView(withId(R.id.register_layout)).perform(swipeUp());
        onView(withId(R.id.register_send)).perform(click());

        onView(withId(R.id.register_layout)).perform(swipeDown());
        onView(withId(R.id.register_student_id))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void FirstNameCheck(){
        onView(withId(R.id.register_layout)).perform(swipeUp());
        onView(withId(R.id.register_send)).perform(click());

        onView(withId(R.id.register_layout)).perform(swipeDown());
        onView(withId(R.id.register_first_name))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void LastNameCheck(){
        onView(withId(R.id.register_layout)).perform(swipeUp());
        onView(withId(R.id.register_send)).perform(click());

        onView(withId(R.id.register_layout)).perform(swipeDown());
        onView(withId(R.id.register_last_name))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void StreetCheck(){
        onView(withId(R.id.register_layout)).perform(swipeUp());
        onView(withId(R.id.register_send)).perform(click());

        onView(withId(R.id.register_layout)).perform(swipeDown());
        onView(withId(R.id.register_street))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void StreetNoCheck(){
        onView(withId(R.id.register_layout)).perform(swipeUp());
        onView(withId(R.id.register_send)).perform(click());

        onView(withId(R.id.register_layout)).perform(swipeDown());
        onView(withId(R.id.register_street_no))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void ZipCheck(){
        onView(withId(R.id.register_layout)).perform(swipeUp());
        onView(withId(R.id.register_send)).perform(click());

        onView(withId(R.id.register_layout)).perform(swipeDown());
        onView(withId(R.id.register_zip))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void CityCheck(){
        onView(withId(R.id.register_layout)).perform(swipeUp());
        onView(withId(R.id.register_send)).perform(click());

        onView(withId(R.id.register_layout)).perform(swipeDown());
        onView(withId(R.id.register_city))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void PhoneCheck(){
        onView(withId(R.id.register_layout)).perform(swipeUp());
        onView(withId(R.id.register_send)).perform(click());

        onView(withId(R.id.register_phone))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void EmailEmptyCheck(){
        onView(withId(R.id.register_layout)).perform(swipeUp());
        onView(withId(R.id.register_send)).perform(click());

        onView(withId(R.id.register_email))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void EmailValidCheck(){
        FillTopPart();
        onView(withId(R.id.register_layout)).perform(swipeUp());
        FillBottomPart();

        onView(withId(R.id.register_email)).perform(typeText("meow"), closeSoftKeyboard());
        onView(withId(R.id.register_password)).perform(typeText("meow"), closeSoftKeyboard());

        onView(withId(R.id.register_send)).perform(click());
        onView(withId(R.id.register_email))
                .check(matches(hasErrorText("It is an invalid email.")));
    }

    @Test
    public void PasswordEmptyCheck(){
        onView(withId(R.id.register_layout)).perform(swipeUp());
        onView(withId(R.id.register_send)).perform(click());

        onView(withId(R.id.register_password))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void PasswordValidCheck(){
        FillTopPart();
        onView(withId(R.id.register_layout)).perform(swipeUp());
        FillBottomPart();

        onView(withId(R.id.register_email)).perform(typeText("meow@rg.ch"), closeSoftKeyboard());
        onView(withId(R.id.register_password)).perform(typeText("meow"), closeSoftKeyboard());

        onView(withId(R.id.register_send)).perform(click());
        onView(withId(R.id.register_password))
                .check(matches(hasErrorText("It must be at least 8 Characters.")));
    }

    @Test
    public void PasswordMatchCheck(){
        FillTopPart();
        onView(withId(R.id.register_layout)).perform(swipeUp());
        FillBottomPart();

        onView(withId(R.id.register_email)).perform(typeText("meow@rg.ch"), closeSoftKeyboard());
        onView(withId(R.id.register_password)).perform(typeText("12345678"), closeSoftKeyboard());

        onView(withId(R.id.register_send)).perform(click());
        onView(withId(R.id.register_password_confirm))
                .check(matches(hasErrorText("It doesn't match with Password.")));
    }

    private void FillTopPart(){
        onView(withId(R.id.register_student_id)).perform(typeText("111"), closeSoftKeyboard());
        onView(withId(R.id.register_first_name)).perform(typeText("meow"), closeSoftKeyboard());
        onView(withId(R.id.register_last_name)).perform(typeText("meow"), closeSoftKeyboard());
        onView(withId(R.id.register_street)).perform(typeText("meow"), closeSoftKeyboard());
        onView(withId(R.id.register_street_no)).perform(typeText("1"), closeSoftKeyboard());
    }

    private void FillBottomPart(){
        onView(withId(R.id.register_zip)).perform(typeText("5555"), closeSoftKeyboard());
        onView(withId(R.id.register_city)).perform(typeText("meow"), closeSoftKeyboard());
        onView(withId(R.id.register_phone)).perform(typeText("666666"), closeSoftKeyboard());
    }

}

