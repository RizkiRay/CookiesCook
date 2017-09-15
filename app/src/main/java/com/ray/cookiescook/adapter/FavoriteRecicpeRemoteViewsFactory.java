package com.ray.cookiescook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ray.cookiescook.R;
import com.ray.cookiescook.database.BakingProvider;
import com.ray.cookiescook.database.RecipeColumns;

import net.simonvt.schematic.Cursors;

/**
 * Created by Olis on 9/14/2017.
 */

public class FavoriteRecicpeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, LoaderManager.LoaderCallbacks<Cursor> {


    public static final String RECIPE_PROJECTION[] = new String[]{
            RecipeColumns.ID, RecipeColumns.IMAGE, RecipeColumns.NAME, RecipeColumns.SERVINGS
    };
    Context mContext;
    Cursor mCursor;
    private long recipeId;

    public FavoriteRecicpeRemoteViewsFactory(Context applicationContext,long recipeId){
        mContext = applicationContext;
        this.recipeId = recipeId;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        return mCursor==null?0:mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(i);
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.item_recipe_widget);
        String recipeName = Cursors.getString(mCursor, RecipeColumns.NAME);
        view.setTextViewText(R.id.text_recipe, recipeName);
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext, BakingProvider.Recipes.CONTENT_URI, RECIPE_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
