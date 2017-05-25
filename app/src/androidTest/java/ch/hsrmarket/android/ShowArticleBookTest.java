package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hsrmarket.android.model.Article;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.hsrmarket.android.InstrumentedTest.hasTextInputLayoutHintText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;


@RunWith(AndroidJUnit4.class)
public class ShowArticleBookTest {

    @Rule
    public ActivityTestRule<ArticleActivity> mActivityRule = new ActivityTestRule<ArticleActivity>(ArticleActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = getInstrumentation().getTargetContext();

            Intent intent = new Intent(targetContext, ArticleActivity.class);
            intent.putExtra("requestedId",11);
            intent.putExtra("requestedType",Article.Type.BOOK);
            intent.putExtra("displayMode",ArticleActivity.DISPLAY_WITH_BUY);

            return intent;
        }
    };

    @Test
    public void TitleCheck(){
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText("system-worthy")));
    }

    @Test
    public void DescriptionCheck(){
        onView(withId(R.id.article_description))
                .check(matches(withText("Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat.")));
    }

    @Test
    public void PriceCheck(){
        onView(withId(R.id.article_price))
                .check(matches(withText("93.72")));
    }

    @Test
    public void ConditionCheck(){
        onView(withId(R.id.article_condition))
                .check(matches(withText("\uD83D\uDD25\uD83D\uDD25")));
    }

    @Test
    public void CreatedAtCheck(){
        onView(withId(R.id.article_created_at))
                .check(matches(withText("2016-12-03")));
    }

    @Test
    public void IdCheck(){
        onView(withId(R.id.article_id))
                .check(matches(withText("11")));
    }

    // Extra Fields with hint checks

    @Test
    public void Extra1Check(){
        onView(withId(R.id.article_extra1))
                .check(matches(withText("Isacco Suckling")));
    }

    @Test
    public void Extra2Check(){
        onView(withId(R.id.article_extra2))
                .check(matches(withText("Wolf-Hilll")));
    }

    @Test
    public void Extra3Check(){
        onView(withId(R.id.article_extra3))
                .check(matches(withText("785110873-X")));
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

}

