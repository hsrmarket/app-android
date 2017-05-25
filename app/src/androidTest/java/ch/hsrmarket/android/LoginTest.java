package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hsrmarket.android.model.Article;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class LoginTest {

    // Account 4

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void LoginCheck(){
        onView(withId(R.id.login_email))
                .perform(typeText("wwitherington3@cyberchimps.com"), closeSoftKeyboard());

        onView(withId(R.id.login_password))
                .perform(typeText("hsrmarket654"), closeSoftKeyboard());

        onView(withId(R.id.login_send)).perform(click());

        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @After
    public void CleanSharedPref(){
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.pref_credentials),Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit();
    }

}

