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
import android.widget.Toast;

import com.ray.cookiescook.adapter.RecipeListAdapter;
import com.ray.cookiescook.adapter.StepListAdapter;
import com.ray.cookiescook.database.BakingProvider;
import com.ray.cookiescook.database.StepsColumns;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ray on 9/8/2017.
 */

public class StepListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RecipeListAdapter.RecyclerClickListener {

    private static final String TAG = "StepListFragment";
    private static final String[] PROJECTION = new String[]{
        StepsColumns.ID, StepsColumns.DESCRIPTION, StepsColumns.SHORT_DESCRIPTION, StepsColumns.THUMBNAIL_URL,
        StepsColumns.VIDEO_URL, StepsColumns.RECIPE_ID};

    @BindView(R.id.recycler_step)
    RecyclerView mRecyclerStep;

    private StepListAdapter adapter;
    private int recipeId = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recipeId = getArguments().getInt(StepsColumns.RECIPE_ID);

        adapter = new StepListAdapter(this);
        mRecyclerStep.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerStep.setAdapter(adapter);

        getActivity().getSupportLoaderManager().initLoader(30, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BakingProvider.Steps.recipeSteps(recipeId), PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data!= null){
            adapter.setCursor(data);
            Log.i(TAG, "onLoadFinished: " + "ga null" );
            while (!data.isLast()){
                data.moveToNext();
                Log.i(TAG, "onLoadFinished: data step " + Cursors.getString(data, StepsColumns.DESCRIPTION));
            }
        } else Log.i(TAG, "onLoadFinished: null");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(null);
    }

    @Override
    public void onItemClickedListener(int position) {
        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickedListener(int position, int recipeId) {
        //nothing
    }
}
