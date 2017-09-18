package com.ray.cookiescook;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Debug;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ray.cookiescook.database.BakingDatabase;
import com.ray.cookiescook.database.BakingProvider;
import com.ray.cookiescook.database.RecipeColumns;
import com.ray.cookiescook.service.MyWidgetRemoteViewService;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteRecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.favorite_recipe_widget);
        Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, MyWidgetRemoteViewService.class);
        intent.putExtra(RecipeColumns.NAME, context.getSharedPreferences("COOKIES", 0).getString(RecipeColumns.NAME, ""));
        intent.putExtra(RecipeColumns.ID, context.getSharedPreferences("COOKIES", 0).getInt(RecipeColumns.ID, 0));
        view.setTextViewText(R.id.text_title, context.getSharedPreferences("COOKIES", 0).getString(RecipeColumns.NAME, "") + " Ingredients");
        view.setRemoteAdapter(R.id.list_ingredient_widget, intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_ingredient_widget);
        appWidgetManager.updateAppWidget(appWidgetId, view);
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, FavoriteRecipeWidget.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager awm = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, FavoriteRecipeWidget.class);
            onUpdate(context, awm, awm.getAppWidgetIds(cn));
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Debug.stopMethodTracing();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

