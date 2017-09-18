package com.ray.cookiescook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ray.cookiescook.database.BakingProvider;
import com.ray.cookiescook.database.RecipeColumns;
import com.ray.cookiescook.database.StepsColumns;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements StepListFragment.FragmentDataPassing,
        StepDetailFragment.NavigationListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "RecipeActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int mPosition = 0;
    private Cursor mCursor;
    private int mRecipeId;
    private boolean mIsTwoPane;
    int detailContainerResId;
    private Intent i;


    private static final String[] PROJECTION = new String[]{
            StepsColumns.ID, StepsColumns.DESCRIPTION, StepsColumns.SHORT_DESCRIPTION, StepsColumns.THUMBNAIL_URL,
            StepsColumns.VIDEO_URL, StepsColumns.RECIPE_ID};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        i = getIntent();
        mRecipeId = i.getIntExtra(RecipeColumns.ID, 0);
        Debug.stopMethodTracing();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (findViewById(R.id.container_master_list) != null) {
            mIsTwoPane = true;
            detailContainerResId = R.id.container_detail;
        } else {
            mIsTwoPane = false;
            detailContainerResId = R.id.container;
        }
        if (savedInstanceState == null) {
            loadFragment(mIsTwoPane);
        }
    }

    private void loadFragment(boolean isTwoPane) {
        if (isTwoPane) {
            Bundle stepListBundle = new Bundle();
            stepListBundle.putInt(StepsColumns.RECIPE_ID, mRecipeId);
            StepListFragment fragmentStepList = new StepListFragment();
            fragmentStepList.setArguments(stepListBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_master_list, fragmentStepList).commit();

            Bundle ingredientsBundle = new Bundle();

            ingredientsBundle.putInt(StepsColumns.RECIPE_ID, mRecipeId);
            ingredientsBundle.putString(RecipeColumns.NAME, i.getStringExtra(RecipeColumns.NAME));
            ingredientsBundle.putInt("position", mPosition);
            ingredientsBundle.putBoolean("islast", false);
            IngredientsFragment fragmentIngredient = new IngredientsFragment();
            fragmentIngredient.setArguments(ingredientsBundle);
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().beginTransaction().replace(R.id.container_detail, fragmentIngredient).commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(StepsColumns.RECIPE_ID, mRecipeId);
            bundle.putString(RecipeColumns.NAME, i.getStringExtra(RecipeColumns.NAME));
            StepListFragment fragment = new StepListFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDataPass(Bundle bundle, int position) {
        mPosition = position;
        Fragment fragment;
        if (position == 0) fragment = new IngredientsFragment();
        else fragment = new StepDetailFragment();
        bundle.putBoolean("isTwoPane", mIsTwoPane);
        fragment.setArguments(bundle);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().addToBackStack("step").replace(detailContainerResId, fragment).commit();
    }

    @Override
    public void onCursorChanged(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_add_widget){
            SharedPreferences pref = getSharedPreferences("COOKIES", 0);
            pref.edit().putInt(RecipeColumns.ID, mRecipeId).apply();
            pref.edit().putString(RecipeColumns.NAME, i.getStringExtra(RecipeColumns.NAME)).apply();
            FavoriteRecipeWidget.sendRefreshBroadcast(this);
        }
        return true;
    }

    @Override
    public void onNextPressed() {
        mPosition += 1;
        mCursor.moveToPosition(mPosition - 1);
        Fragment fragment = new StepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", mPosition);
        bundle.putInt(StepsColumns.RECIPE_ID, mRecipeId);
        bundle.putBoolean("isTwoPane", mIsTwoPane);

        if (mPosition == mCursor.getCount()) {
            bundle.putBoolean("islast", true);
        } else bundle.putBoolean("islast", false);

        fragment.setArguments(bundle);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(detailContainerResId, fragment).addToBackStack("step").commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(40, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPosition = savedInstanceState.getInt("position");
    }

    @Override
    public void onPrevPressed() {
        mPosition -= 1;

        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putInt(StepsColumns.RECIPE_ID, mRecipeId);
        bundle.putBoolean("isTwoPane", mIsTwoPane);


        if (mPosition == 0) {
            fragment = new IngredientsFragment();
            bundle.putString(getResources().getString(R.string.text_title), getResources().getString(R.string.text_ingredients));
        } else {
            mCursor.moveToPosition(mPosition - 1);
            bundle.putInt("position", mPosition);
            fragment = new StepDetailFragment();
        }

        fragment.setArguments(bundle);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(detailContainerResId, fragment).addToBackStack("step").commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BakingProvider.Steps.recipeSteps(mRecipeId), PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            mCursor = data;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }
}
