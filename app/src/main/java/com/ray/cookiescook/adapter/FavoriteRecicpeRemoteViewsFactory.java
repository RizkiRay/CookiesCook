package com.ray.cookiescook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ray.cookiescook.database.BakingProvider;
import com.ray.cookiescook.database.IngredientsColumns;

/**
 * Created by Olis on 9/14/2017.
 */

public class FavoriteRecicpeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, LoaderManager.LoaderCallbacks<Cursor> {

    private final static String PROECTION[] = new String[]{
            IngredientsColumns._ID, IngredientsColumns.INGREDIENT, IngredientsColumns.MEASURE,
            IngredientsColumns.QUANTITY, IngredientsColumns.RECIPE_ID
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
        return null;
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
        return new CursorLoader(mContext, BakingProvider.Ingredients.recipeIngredients(recipeId), PROECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
