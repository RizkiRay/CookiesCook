package com.ray.cookiescook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

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
    private int mPosition;
    private Cursor mCursor;
    private int mRecipeId;


    private static final String[] PROJECTION = new String[]{
            StepsColumns.ID, StepsColumns.DESCRIPTION, StepsColumns.SHORT_DESCRIPTION, StepsColumns.THUMBNAIL_URL,
            StepsColumns.VIDEO_URL, StepsColumns.RECIPE_ID};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        mRecipeId = i.getIntExtra(RecipeColumns.ID, 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(StepsColumns.RECIPE_ID, mRecipeId);
            StepListFragment fragment = new StepListFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    @Override
    public void onDataPass(Bundle bundle, int position) {
        mPosition = position;
        Fragment fragment;
        if (position == 0) fragment = new IngredientsFragment();
        else fragment = new StepDetailFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().addToBackStack("step").replace(R.id.container, fragment).commit();
    }

    @Override
    public void onCursorChanged(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNextPressed() {
        mPosition += 1;
        mCursor.moveToPosition(mPosition - 1);
        Fragment fragment = new StepDetailFragment();
        String description = Cursors.getString(mCursor, StepsColumns.DESCRIPTION);
        String url = Cursors.getString(mCursor, StepsColumns.VIDEO_URL);
        String title = Cursors.getString(mCursor, StepsColumns.SHORT_DESCRIPTION);
        Bundle bundle = new Bundle();
        bundle.putString(StepsColumns.DESCRIPTION, description);
        bundle.putString(StepsColumns.VIDEO_URL, url);
        bundle.putString(getResources().getString(R.string.text_title), title);

        if (mPosition == mCursor.getCount()) {
            bundle.putBoolean("islast", true);
        } else bundle.putBoolean("islast", false);

        fragment.setArguments(bundle);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("step").commit();
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

        if (mPosition == 0) {
            fragment = new IngredientsFragment();
            bundle.putInt(StepsColumns.RECIPE_ID, mRecipeId);
            bundle.putString(getResources().getString(R.string.text_title), getResources().getString(R.string.text_ingredients));
        } else {
            mCursor.moveToPosition(mPosition - 1);
            String description = Cursors.getString(mCursor, StepsColumns.DESCRIPTION);
            String url = Cursors.getString(mCursor, StepsColumns.VIDEO_URL);
            String title = Cursors.getString(mCursor, StepsColumns.SHORT_DESCRIPTION);
            bundle.putString(StepsColumns.DESCRIPTION, description);
            bundle.putString(getResources().getString(R.string.text_title), title);
            bundle.putString(StepsColumns.VIDEO_URL, url);
            fragment = new StepDetailFragment();
        }

        fragment.setArguments(bundle);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("step").commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BakingProvider.Steps.recipeSteps(mRecipeId), PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            mCursor = data;
            Log.i(TAG, "onLoadFinished: " + "ga null");
        } else Log.i(TAG, "onLoadFinished: null");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }
}
