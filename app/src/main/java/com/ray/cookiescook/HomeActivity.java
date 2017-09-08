package com.ray.cookiescook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.ray.cookiescook.adapter.RecipeListAdapter;
import com.ray.cookiescook.database.BakingProvider;
import com.ray.cookiescook.database.IngredientsColumns;
import com.ray.cookiescook.database.RecipeColumns;

import net.simonvt.schematic.Cursors;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, RecipeListAdapter.RecyclerClickListener {

    private static final String TAG = "HomeActivity";


    public static final String RECIPE_PROJECTION[] = new String[]{
            RecipeColumns.ID, RecipeColumns.IMAGE, RecipeColumns.NAME, RecipeColumns.SERVINGS
    };

    public static final String INGREDIENTS_PROJECTION[] = new String[]{
            IngredientsColumns._ID, IngredientsColumns.INGREDIENT, IngredientsColumns.MEASURE, IngredientsColumns.QUANTITY
    };

    @BindView(R.id.recycler_recipe)
    RecyclerView mRecyclerRecipe;
    private RecipeListAdapter adapter;

    @BindString(R.string.text_baking_app)
    String activityTitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        toolbar.setTitle(activityTitle);
        setSupportActionBar(toolbar);

        adapter = new RecipeListAdapter(this);
        mRecyclerRecipe.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerRecipe.setAdapter(adapter);

        getSupportLoaderManager().initLoader(10, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BakingProvider.Recipes.CONTENT_URI, RECIPE_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            while (!data.isLast()) {
                data.moveToNext();
                Log.i(TAG, "onLoadFinished: data " + Cursors.getString(data, RecipeColumns.NAME));
            }
            adapter.setCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(null);
    }

    @Override
    public void onItemClickedListener(int position) {
        //nothing
    }

    @Override
    public void onItemClickedListener(int position, int recipeId) {
        Intent i = new Intent(this, RecipeActivity.class);
        i.putExtra(RecipeColumns.ID, recipeId);
        startActivity(i);
    }
}
