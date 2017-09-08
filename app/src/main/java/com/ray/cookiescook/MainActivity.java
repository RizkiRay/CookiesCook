package com.ray.cookiescook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ray.cookiescook.database.BakingProvider;
import com.ray.cookiescook.database.IngredientsColumns;
import com.ray.cookiescook.database.RecipeColumns;
import com.ray.cookiescook.database.StepsColumns;
import com.ray.cookiescook.model.Baking;
import com.ray.cookiescook.model.IngredientsItem;
import com.ray.cookiescook.model.StepsItem;
import com.ray.cookiescook.network.NetworkClient;
import com.ray.cookiescook.network.NetworkInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private int ingredient_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testInsertContentProvider();

    }

    private void testInsertContentProvider() {
        NetworkInterface apiClient = NetworkClient.getClient().create(NetworkInterface.class);
        Call<List<Baking>> call = apiClient.getRecipes();
        call.enqueue(new Callback<List<Baking>>() {
            @Override
            public void onResponse(Call<List<Baking>> call, Response<List<Baking>> response) {
                List<Baking> recipes = response.body();
                for (final Baking recipe : recipes) {
                    List<IngredientsItem> ingredients = recipe.getIngredients();
                    List<StepsItem> steps = recipe.getSteps();

                    final ContentValues[] ingredientValues = new ContentValues[ingredients.size()];
                    final ContentValues[] stepValues = new ContentValues[steps.size()];

                    for (int i = 0; i < ingredients.size(); i++) {
                        IngredientsItem ingredient = ingredients.get(i);

                        ingredient_id++;
                        ContentValues cvIngredient = new ContentValues();
                        cvIngredient.put(IngredientsColumns._ID, ingredient_id);
                        cvIngredient.put(IngredientsColumns.INGREDIENT, ingredient.getIngredient());
                        cvIngredient.put(IngredientsColumns.MEASURE, ingredient.getMeasure());
                        cvIngredient.put(IngredientsColumns.QUANTITY, ingredient.getQuantity());
                        cvIngredient.put(IngredientsColumns.RECIPE_ID, recipe.getId());
                        ingredientValues[i] = cvIngredient;
                    }

                    for (int i = 0; i < steps.size(); i++) {
                        StepsItem step = steps.get(i);
                        ContentValues cvStep = new ContentValues();
                        cvStep.put(StepsColumns.ID, step.getId());
                        Log.i(TAG, "onResponse: step id " + step.getId());
                        cvStep.put(StepsColumns.DESCRIPTION, step.getDescription());
                        cvStep.put(StepsColumns.SHORT_DESCRIPTION, step.getShortDescription());
                        cvStep.put(StepsColumns.THUMBNAIL_URL, step.getThumbnailURL());
                        cvStep.put(StepsColumns.VIDEO_URL, step.getVideoURL());
                        cvStep.put(StepsColumns.RECIPE_ID, recipe.getId());
                        stepValues[i] = cvStep;
                    }

                    ContentValues cvRecipe = new ContentValues();
                    cvRecipe.put(RecipeColumns.ID, recipe.getId());
                    cvRecipe.put(RecipeColumns.IMAGE, recipe.getImage());
                    cvRecipe.put(RecipeColumns.NAME, recipe.getName());
                    cvRecipe.put(RecipeColumns.SERVINGS, recipe.getServings());
                    try {
                        getContentResolver().insert(BakingProvider.Recipes.CONTENT_URI, cvRecipe);
                        getContentResolver().bulkInsert(BakingProvider.Ingredients.CONTENT_URI, ingredientValues);
                        getContentResolver().bulkInsert(BakingProvider.Steps.CONTENT_URI, stepValues);
                    } catch (SQLiteConstraintException e){
                        Log.e(TAG, "onResponse: " + e.toString() );
                    }
                }
//                getSupportLoaderManager().initLoader(10, null, MainActivity.this);
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }

            @Override
            public void onFailure(Call<List<Baking>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

//    @Override
//    public Loader onCreateLoader(int id, Bundle args) {
//        return new CursorLoader(this, BakingProvider.Ingredients.recipeIngredients(2), INGREDIENTS_PROJECTION, null, null, null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        if (data != null) {
//            while (!data.isLast()) {
//                data.moveToNext();
//                Log.i(TAG, "onLoadFinished: id " + Cursors.getString(data, IngredientsColumns._ID));
//                Log.i(TAG, "onLoadFinished: name " + Cursors.getString(data, IngredientsColumns.INGREDIENT));
//            }
//        } else {
//            Log.e(TAG, "onLoadFinished: cursor return null");
//        }
//    }
//
//
//    @Override
//    public void onLoaderReset(Loader loader) {
//
//    }
}
