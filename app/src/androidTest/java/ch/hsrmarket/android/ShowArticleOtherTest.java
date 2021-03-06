package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hsrmarket.android.model.Article;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class ShowArticleOtherTest {

    @Rule
    public ActivityTestRule<ArticleActivity> mActivityRule = new ActivityTestRule<ArticleActivity>(ArticleActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

            Intent intent = new Intent(targetContext, ArticleActivity.class);
            intent.putExtra("requestedId",165);
            intent.putExtra("requestedType",Article.Type.OTHER);
            intent.putExtra("displayMode",ArticleActivity.DISPLAY_WITH_BUY);

            return intent;
        }
    };

    @Test
    public void TitleCheck(){
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText("instruction set")));
    }

    @Test
    public void DescriptionCheck(){
        onView(withId(R.id.article_description))
                .check(matches(withText("Integer ac leo. Pellentesque ultrices mattis odio. Donec vitae nisi.")));
    }

    @Test
    public void PriceCheck(){
        onView(withId(R.id.article_price))
                .check(matches(withText("33.84")));
    }

    @Test
    public void ConditionCheck(){
        onView(withId(R.id.article_condition))
                .check(matches(withText("\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25")));
    }

    @Test
    public void CreatedAtCheck(){
        onView(withId(R.id.article_created_at))
                .check(matches(withText("2016-07-22")));
    }

    @Test
    public void IdCheck(){
        onView(withId(R.id.article_id))
                .check(matches(withText("165")));
    }

    // Extra Fields with hint checks

    @Test
    public void Extra1Check(){
        onView(withId(R.id.article_extra1))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void Extra2Check(){
        onView(withId(R.id.article_extra2))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void Extra3Check(){
        onView(withId(R.id.article_extra3))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void Extra1HintCheck(){
        onView(withId(R.id.article_hint_extra1))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void Extra2HintCheck(){
        onView(withId(R.id.article_hint_extra2))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void Extra3HintCheck(){
        onView(withId(R.id.article_hint_extra3))
                .check(matches(not(isDisplayed())));
    }
}

