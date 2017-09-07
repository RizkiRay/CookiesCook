package com.ray.cookiescook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import net.simonvt.schematic.Cursors;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String PROJECTION[] = new String[]{
        RecipeColumns.ID, RecipeColumns.IMAGE, RecipeColumns.NAME, RecipeColumns.SERVINGS
    };

    public static final String INGREDIENTS_PROJECTION[] = new String[]{
            IngredientsColumns._ID, IngredientsColumns.INGREDIENT, IngredientsColumns.MEASURE, IngredientsColumns.QUANTITY
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testInsertContentProvider();

    }

    private void testInsertContentProvider(){
        NetworkInterface apiClient = NetworkClient.getClient().create(NetworkInterface.class);
        Call<List<Baking>> call = apiClient.getRecipes();
        call.enqueue(new Callback<List<Baking>>() {
            @Override
            public void onResponse(Call<List<Baking>> call, Response<List<Baking>> response) {
                List<Baking> recipes = response.body();
                for(final Baking recipe : recipes){
                    List<IngredientsItem> ingredients = recipe.getIngredients();
                    List<StepsItem> steps = recipe.getSteps();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentValues cv = new ContentValues();
                            cv.put(RecipeColumns.ID, recipe.getId());
                            cv.put(RecipeColumns.IMAGE, recipe.getImage());
                            cv.put(RecipeColumns.NAME, recipe.getName());
                            cv.put(RecipeColumns.SERVINGS, recipe.getServings());
                            try {
                                getContentResolver().insert(BakingProvider.Recipes.CONTENT_URI, cv);
                            } catch (SQLiteConstraintException e){
                                Log.e(TAG, "run: " + e.toString() );
                            }
                        }
                    }).start();

                    for (final IngredientsItem ingredient : ingredients) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ContentValues cv = new ContentValues();
                                cv.put(IngredientsColumns.INGREDIENT, ingredient.getIngredient());
                                cv.put(IngredientsColumns.MEASURE, ingredient.getMeasure());
                                cv.put(IngredientsColumns.QUANTITY, ingredient.getQuantity());
                                cv.put(IngredientsColumns.RECIPE_ID, recipe.getId());
                                try {
                                    getContentResolver().insert(BakingProvider.Ingredients.CONTENT_URI, cv);
                                } catch (SQLiteConstraintException e){
                                    Log.e(TAG, "run: " + e.toString() );
                                }
                            }
                        }).start();
                    }

                    for (final StepsItem step : steps) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ContentValues cv = new ContentValues();
                                cv.put(StepsColumns.ID, step.getId());
                                cv.put(StepsColumns.DESCRIPTION, step.getDescription());
                                cv.put(StepsColumns.SHORT_DESCRIPTION, step.getShortDescription());
                                cv.put(StepsColumns.THUMBNAIL_URL, step.getThumbnailURL());
                                cv.put(StepsColumns.VIDEO_URL, step.getVideoURL());
                                cv.put(StepsColumns.RECIPE_ID, recipe.getId());
                                try {
                                    getContentResolver().insert(BakingProvider.Steps.CONTENT_URI, cv);
                                } catch (SQLiteConstraintException e){
                                    Log.e(TAG, "run: " + e.toString() );
                                }
                            }
                        }).start();
                    }
                }
                getSupportLoaderManager().initLoader(10, null, MainActivity.this);
            }

            @Override
            public void onFailure(Call<List<Baking>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BakingProvider.Ingredients.recipeIngredients(2), INGREDIENTS_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data!=null) {
            while (!data.isLast()) {
                data.moveToNext();
                Log.i(TAG, "onLoadFinished: name " + Cursors.getString(data, IngredientsColumns.INGREDIENT));
            }
        } else {
            Log.e(TAG, "onLoadFinished: cursor return null");
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }
}
