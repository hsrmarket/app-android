package ch.hsrmarket.android;

import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.NavigationMenuView;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class MainDrawerMenuLoggedOutTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void recordIntent(){
        Intents.init();
    }

    @After
    public void unleashIntent(){
        Intents.release();
    }

    @Test
    public void isHiddenCheck(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)));
    }

    @Test
    public void NameCheck(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_name)).check(matches(withText("HSRmarket")));
    }

    @Test
    public void AddressCheck(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_email)).check(matches(withText("web.hsrmarket.ch:3000")));
    }

    @Test
    public void LoginIntentCheck() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_login));
        intended(hasComponent(LoginActivity.class.getName()));

    }
}

