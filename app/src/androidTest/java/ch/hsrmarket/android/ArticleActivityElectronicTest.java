package ch.hsrmarket.android;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static ch.hsrmarket.android.InstrumentedTest.hasTextInputLayoutHintText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hsrmarket.android.model.Article;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class ArticleActivityElectronicTest {

    @Rule
    public ActivityTestRule<ArticleActivity> mActivityRule = new ActivityTestRule<ArticleActivity>(ArticleActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, ArticleActivity.class);
            result.putExtra("requestedId", 182);
            result.putExtra("requestedType", Article.Type.ELECTRONIC_DEVICE);
            return result;
        }
    };

    @Test
    public void TitleCheck(){
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText("ROG Notebook")));
    }

    @Test
    public void DescriptionCheck(){
        onView(withId(R.id.article_description))
                .check(matches(withText("[For Android Test - do not delete it] a great laptop")));
    }

    @Test
    public void PriceCheck(){
        onView(withId(R.id.article_price))
                .check(matches(withText("999.00")));
    }

    @Test
    public void ConditionCheck(){
        onView(withId(R.id.article_condition))
                .check(matches(withText("3")));
    }

    @Test
    public void CreatedAtCheck(){
        onView(withId(R.id.article_created_at))
                .check(matches(withText("2017-04-23")));
    }

    @Test
    public void IdCheck(){
        onView(withId(R.id.article_id))
                .check(matches(withText("182")));
    }

    // Extra Fields with hint checks

    @Test
    public void Extra1Check(){
        onView(withId(R.id.article_extra1))
                .check(matches(withText("Asus Tek")));
    }

    @Test
    public void Extra2Check(){
        onView(withId(R.id.article_extra2))
                .check(matches(withText("G77w")));
    }

    @Test
    public void Extra3Check(){
        onView(withId(R.id.article_extra3))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void Extra1HintCheck(){
        onView(withId(R.id.article_hint_extra1))
                .check(matches(hasTextInputLayoutHintText("Manufacturer")));
    }

    @Test
    public void Extra2HintCheck(){
        onView(withId(R.id.article_hint_extra2))
                .check(matches(hasTextInputLayoutHintText("Model No.")));
    }

    @Test
    public void Extra3HintCheck(){
        onView(withId(R.id.article_hint_extra3))
                .check(matches(not(isDisplayed())));
    }
}

