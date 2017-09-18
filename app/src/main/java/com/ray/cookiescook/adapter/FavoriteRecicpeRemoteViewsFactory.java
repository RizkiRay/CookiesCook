package com.ray.cookiescook.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.ray.cookiescook.R;
import com.ray.cookiescook.database.BakingProvider;
import com.ray.cookiescook.database.IngredientsColumns;
import com.ray.cookiescook.database.RecipeColumns;

import net.simonvt.schematic.Cursors;

/**
 * Created by Olis on 9/14/2017.
 */

public class FavoriteRecicpeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private static final String TAG = "FavoriteRecicpeRemoteVi";
    public static final String[] PROJECTION = new String[]{
            IngredientsColumns._ID, IngredientsColumns.INGREDIENT, IngredientsColumns.MEASURE,
            IngredientsColumns.QUANTITY, IngredientsColumns.RECIPE_ID
    };
    private Intent mIntent;
    Context mContext;
    Cursor mCursor;
    private int recipeId;

    public FavoriteRecicpeRemoteViewsFactory(Context applicationContext,Intent intent){
        mContext = applicationContext;
        mIntent = intent;
        recipeId = intent.getIntExtra(RecipeColumns.ID,0);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) mCursor.close();

        final long identityToken = Binder.clearCallingIdentity();
        Log.i(TAG, "onDataSetChanged: recipeId " + recipeId);
        Uri uri = BakingProvider.Ingredients.recipeIngredients(recipeId);
        mCursor = mContext.getContentResolver().query(uri, PROJECTION, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (mCursor!=null) mCursor.close();
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
        String recipeName = Cursors.getString(mCursor, IngredientsColumns.INGREDIENT);
        view.setTextViewText(R.id.text_recipe, recipeName);
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return mCursor.moveToPosition(i)?Cursors.getInt(mCursor, RecipeColumns.ID):i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
