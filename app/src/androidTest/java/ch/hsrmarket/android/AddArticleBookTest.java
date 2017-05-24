package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import ch.hsrmarket.android.model.Article;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.hsrmarket.android.InstrumentedTest.hasTextInputLayoutHintText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class AddArticleBookTest {

    @Rule
    public ActivityTestRule<ArticleActivity> mActivityRule = new ActivityTestRule<ArticleActivity>(ArticleActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = getInstrumentation().getTargetContext();

            Intent intent = new Intent(targetContext, ArticleActivity.class);
            intent.putExtra("displayMode",ArticleActivity.DISPLAY_ADD);

            return intent;
        }
    };

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
    public void selectSpinner(){
        onView(withId(R.id.article_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Book"))).perform(click());
    }

    @Test
    public void TitleCheck(){
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText("Adding new Article")));
    }

    @Test
    public void NameCheck(){
        onView(withId(R.id.fab_buy)).perform(click());
        onView(withId(R.id.article_name))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void DescriptionCheck(){
        onView(withId(R.id.fab_buy)).perform(click());
        onView(withId(R.id.article_description))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void PriceCheck(){
        onView(withId(R.id.fab_buy)).perform(click());
        onView(withId(R.id.article_price))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void ConditionCheck(){
        onView(withId(R.id.fab_buy)).perform(click());
        onView(withId(R.id.article_condition))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void CreatedAtCheck(){
        onView(withId(R.id.article_created_at))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void IdCheck(){
        onView(withId(R.id.article_id))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void Extra1Check(){
        fillBaseFields();
        onView(withId(R.id.fab_buy)).perform(click());
        onView(withId(R.id.article_extra1))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void Extra2Check(){
        fillBaseFields();
        onView(withId(R.id.fab_buy)).perform(click());
        onView(withId(R.id.article_extra2))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void Extra3Check(){
        fillBaseFields();
        onView(withId(R.id.fab_buy)).perform(click());
        onView(withId(R.id.article_extra3))
                .check(matches(hasErrorText("It can't be empty.")));
    }

    @Test
    public void Extra1HintCheck(){
        onView(withId(R.id.article_hint_extra1))
                .check(matches(hasTextInputLayoutHintText("Author")));
    }

    @Test
    public void Extra2HintCheck(){
        onView(withId(R.id.article_hint_extra2))
                .check(matches(hasTextInputLayoutHintText("Publisher")));
    }

    @Test
    public void Extra3HintCheck(){
        onView(withId(R.id.article_hint_extra3))
                .check(matches(hasTextInputLayoutHintText("ISBN")));
    }

    public void fillBaseFields(){
        onView(withId(R.id.article_name)).perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.article_description)).perform(typeText("description"), closeSoftKeyboard());
        onView(withId(R.id.article_price)).perform(typeText("99.99"), closeSoftKeyboard());
        onView(withId(R.id.article_condition)).perform(typeText("3"), closeSoftKeyboard());
    }

}

