package com.ray.cookiescook;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ray.cookiescook.Util.atPosition;
import static org.hamcrest.Matchers.anything;

/**
 * Created by ray on 9/18/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeTest {
    public static final String FIRST_DATA = "Ingredients";

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);


    @Test
    public void clickItemRecipeOpenStepListActivity() {
        onView(withId(R.id.recycler_recipe))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.recycler_step)).check(matches(isDisplayed()));

    }

    @Test
    public void checkItemCount() {
        onView(withId(R.id.recycler_recipe)).check(new RecyclerViewItemCountAssertion(4));
    }

    @Test
    public void checkFirstItem() {
        onView(withId(R.id.recycler_recipe))
                .check(matches(atPosition(1, hasDescendant(withText("Brownies")))));

    }

    @Test
    public void checkStepsCount(){
        onView(withId(R.id.recycler_recipe))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recycler_step)).check(new RecyclerViewItemCountAssertion(8));
    }

    @Test
    public void checkStepsIngredientText(){
        onView(withId(R.id.recycler_recipe))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recycler_step))
                .check(matches(atPosition(0, hasDescendant(withText("Ingredients")))));
    }

    @Test
    public void checkIngredientsCount(){
        onView(withId(R.id.recycler_recipe))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.recycler_step))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recycler_ingredient)).check(new RecyclerViewItemCountAssertion(9));
    }

    @Test
    public void checkIngredientFirstItem(){
        onView(withId(R.id.recycler_recipe))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.recycler_step))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recycler_ingredient))
                .check(matches(atPosition(0, hasDescendant(withText("Graham Cracker crumbs")))));
    }



    @Test
    public void checkFirstStep(){
        onView(withId(R.id.recycler_recipe))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.recycler_step))
                .perform(actionOnItemAtPosition(1, click()));

        onView(withId(R.id.text_step)).check(matches(withText("Recipe Introduction")));

    }

}
