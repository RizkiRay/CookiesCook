package com.ray.cookiescook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ray.cookiescook.database.RecipeColumns;
import com.ray.cookiescook.database.StepsColumns;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements StepListFragment.FragmentDataPassing{

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent i = getIntent();

        if (savedInstanceState == null){
            Bundle bundle = new Bundle();
            bundle.putInt(StepsColumns.RECIPE_ID, i.getIntExtra(RecipeColumns.ID, 0));
            StepListFragment fragment = new StepListFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    @Override
    public void onDataPass(Bundle bundle, int position) {
        Fragment fragment;
        if (position == 0) fragment = new IngredientsFragment();
        else fragment = new StepDetailFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().addToBackStack("step").replace(R.id.container, fragment).commit();
    }
}
