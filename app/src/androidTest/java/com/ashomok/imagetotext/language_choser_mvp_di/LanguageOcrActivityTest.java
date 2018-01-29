package com.ashomok.imagetotext.language_choser_mvp_di;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.ActionBar;

import com.ashomok.imagetotext.R;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

/**
 * Created by iuliia on 10/31/17.
 */
//MINOR todo test activity fineshed properly using roboelectric https://stackoverflow.com/a/8990947/3627736
    //use dagger mocked sharedpreferances https://stackoverflow.com/a/29996385/3627736
public class LanguageOcrActivityTest {
    @Rule
    public ActivityTestRule<LanguageOcrActivity> mActivityRule = new ActivityTestRule<>(
            LanguageOcrActivity.class, true, true);

    @Test
    public void toolbarBackBtn() throws InterruptedException {
        ActionBar actionBar = mActivityRule.getActivity().getSupportActionBar();
        assertNotNull(actionBar);
    }

    @Test
    public void launchActivityTest() throws InterruptedException {

        final Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, LanguageOcrActivity.class);
        ArrayList<String> languageCodes = new ArrayList<String>() {{
            add("az");
        }};
        intent.putStringArrayListExtra(LanguageOcrActivity.CHECKED_LANGUAGE_CODES, languageCodes);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.auto))
                .check(matches(isDisplayed()));

        onView(withId(R.id.auto))
                .check(matches(isDisplayed()));

        onView(withId(R.id.auto_icon))
                .check(matches(isDisplayed()));

        onView(withId(R.id.all_languages))
                .check(matches(isDisplayed()));

        onView(withId(R.id.all_languages_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(3000);
    }

    /**
     * launch with auto by default
     * @throws InterruptedException
     */
    @Test
    public void launchActivityWithNoExtra() throws InterruptedException {

        final Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, LanguageOcrActivity.class);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.auto))
                .check(matches(isDisplayed()));

        onView(withId(R.id.auto_icon))
                .check((matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE))));

        Thread.sleep(3000);
    }
}