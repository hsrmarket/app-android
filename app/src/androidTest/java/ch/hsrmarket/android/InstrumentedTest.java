package ch.hsrmarket.android;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static android.support.test.InstrumentationRegistry.getInstrumentation;


@RunWith(Suite.class)
@Suite.SuiteClasses({ArticleActivityBookTest.class , ArticleActivityElectronicTest.class, ArticleActivityOfficeTest.class, ArticleActivityOtherTest.class})
public class InstrumentedTest {

    //TODO
    // * a CategoryFragmentTest
    // * missing Buy a ArticleTest
    // * without internet test

    @BeforeClass
    public static void setUrl(){
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.pref_url), Context.MODE_PRIVATE).edit();

        // 10.0.2.2 is IP from Host
        editor.putString(context.getString(R.string.secrel_url), "http://10.0.2.2:9000/api");
        editor.commit();
    }

    public static Matcher<View> hasTextInputLayoutHintText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getHint();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
