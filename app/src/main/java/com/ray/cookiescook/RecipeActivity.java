package com.ray.cookiescook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ray.cookiescook.database.RecipeColumns;
import com.ray.cookiescook.database.StepsColumns;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements StepListFragment.FragmentDataPassing,
        StepDetailFragment.NavigationListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int mPosition;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent i = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(StepsColumns.RECIPE_ID, i.getIntExtra(RecipeColumns.ID, 0));
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
        mCursor.moveToPosition(mPosition);
        Fragment fragment = new StepDetailFragment();
        String description = Cursors.getString(mCursor, StepsColumns.DESCRIPTION);
        String url = Cursors.getString(mCursor, StepsColumns.VIDEO_URL);
        Bundle bundle = new Bundle();
        bundle.putString(StepsColumns.DESCRIPTION, description);
        bundle.putString(StepsColumns.VIDEO_URL, url);

        if (mPosition == mCursor.getCount() - 1) {
            bundle.putBoolean("islast", true);
        } else bundle.putBoolean("islast", false);

        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onPrevPressed() {
        mPosition -= 1;
        mCursor.moveToPosition(mPosition);
        Fragment fragment = new StepDetailFragment();
        String description = Cursors.getString(mCursor, StepsColumns.DESCRIPTION);
        String url = Cursors.getString(mCursor, StepsColumns.VIDEO_URL);
        Bundle bundle = new Bundle();
        bundle.putString(StepsColumns.DESCRIPTION, description);
        bundle.putString(StepsColumns.VIDEO_URL, url);

        if (mPosition == 0) {
            fragment = new IngredientsFragment();
        }

        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
