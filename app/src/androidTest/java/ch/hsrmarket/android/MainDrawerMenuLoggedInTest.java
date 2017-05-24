package ch.hsrmarket.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hsrmarket.android.model.Account;
import ch.hsrmarket.android.model.Address;
import ch.hsrmarket.android.model.Article;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class MainDrawerMenuLoggedInTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void SetSharedPref(){
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.pref_credentials), Context.MODE_PRIVATE).edit();

        Account account = new Account(6,-1492832,"Fanni","Shortell",new Address("Scofield","0",418077,"San Agustín"),"fshortell5@imgur.com","8417-9675","0d6b7ff7c000803ed007e148861c874ca99c7ad9ae3e5a0ac8a4997cdb68255b",false);

        editor.putString(context.getString(R.string.secret_account), account.getJsonObject());
        editor.commit();
    }

    @AfterClass
    public static void CleanSharedPref(){
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.pref_credentials),Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit();
    }

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

        onView(withId(R.id.nav_name)).check(matches(withText("Fanni Shortell")));
    }

    @Test
    public void AddressCheck(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_email)).check(matches(withText("fshortell5@imgur.com")));
    }

    @Test
    public void MyArticlesIntentCheck() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_articles));
        intended(allOf(
                hasComponent(MyListActivity.class.getName()),
                hasExtra("appointedMylist",R.id.nav_articles)
        ));

    }

    @Test
    public void MySalesIntentCheck() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sales));
        intended(allOf(
                hasComponent(MyListActivity.class.getName()),
                hasExtra("appointedMylist",R.id.nav_sales)
        ));

    }

    @Test
    public void MyPurchasesIntentCheck() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_purchases));
        intended(allOf(
                hasComponent(MyListActivity.class.getName()),
                hasExtra("appointedMylist",R.id.nav_purchases)
        ));

    }

    @Test
    public void AddArticleIntentCheck() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_add_article));
        intended(allOf(
                hasComponent(ArticleActivity.class.getName()),
                hasExtra("displayMode",ArticleActivity.DISPLAY_ADD)
        ));

    }

    @Test
    public void LogoutCheck() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));

        Context context = getInstrumentation().getTargetContext();
        SharedPreferences sharedPref= context.getSharedPreferences(context.getString(R.string.pref_credentials), Context.MODE_PRIVATE);
        String json = sharedPref.getString(context.getString(R.string.secret_account),"");

        assertEquals(json,"");

        SetSharedPref();

    }
}

