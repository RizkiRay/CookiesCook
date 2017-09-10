package com.ray.cookiescook;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.cookiescook.adapter.IngredientListAdapter;
import com.ray.cookiescook.database.BakingProvider;
import com.ray.cookiescook.database.IngredientsColumns;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ray on 08/09/2017.
 */

public class IngredientsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String PROECTION [] = new String[]{
            IngredientsColumns._ID, IngredientsColumns.INGREDIENT, IngredientsColumns.MEASURE,
            IngredientsColumns.QUANTITY, IngredientsColumns.RECIPE_ID
    };

    private static final String TAG = "IngredientsFragment";

    @BindView(R.id.recycler_ingredient)
    RecyclerView mRecyclerIngredient;

    IngredientListAdapter adapter;
    private int recipeId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipeId = getArguments().getInt(IngredientsColumns.RECIPE_ID,0);
        adapter = new IngredientListAdapter();
        mRecyclerIngredient.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerIngredient.setAdapter(adapter);

        getActivity().getSupportLoaderManager().initLoader(20, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BakingProvider.Ingredients.recipeIngredients(recipeId), PROECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            adapter.setCursor(data);
            Log.i(TAG, "onLoadFinished: " + "data tidak kosong");
        } else
            Log.i(TAG, "onLoadFinished: " + "data kosong");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(null);
    }
}
