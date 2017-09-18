package com.ray.cookiescook;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_recipe_widget);
////        views.setTextViewText(R.id.appwidget_text, widgetText);
//
//        Intent i = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
//
////        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.layout.favorite_recipe_widget);

        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.favorite_recipe_widget);
        Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
//        Cursor cursor = context.getContentResolver().query(BakingProvider.Ingredients.CONTENT_URI, new String[]{"count(*)"}, null, null, null);
//        cursor.moveToFirst();
//        view.setTextViewText(R.id.text_recipe, String.valueOf(cursor.getInt(0)));
        Intent intent = new Intent(context, MyWidgetRemoteViewService.class);
        intent.putExtra(RecipeColumns.NAME, context.getSharedPreferences("COOKIES",0).getString(RecipeColumns.NAME, ""));
        intent.putExtra(RecipeColumns.ID, context.getSharedPreferences("COOKIES",0).getInt(RecipeColumns.ID, 0));
        view.setTextViewText(R.id.text_title, context.getSharedPreferences("COOKIES",0).getString(RecipeColumns.NAME, "") + " Ingredients");
        view.setRemoteAdapter(R.id.list_ingredient_widget, intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_ingredient_widget);
        appWidgetManager.updateAppWidget(appWidgetId, view);
    }

    public static void sendRefreshBroadcast(Context context){
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, FavoriteRecipeWidget.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            AppWidgetManager awm = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, FavoriteRecipeWidget.class);
//            awm.notifyAppWidgetViewDataChanged(awm.getAppWidgetIds(cn), R.id.list_ingredient_widget);

//            RemoteViews remoteV = new RemoteViews(context.getPackageName(), R.layout.favorite_recipe_widget);

//            Intent intentSync = new Intent(context, FavoriteRecipeWidget.class);
//            intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE); //You need to specify the action for the intent. Right now that intent is doing nothing for there is no action to be broadcasted.
//            PendingIntent pendingSync = PendingIntent.getBroadcast(context,0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT); //You need to specify a proper flag for the intent. Or else the intent will become deleted.
//            remoteV.setTextViewText(R.id.text_title, context.getSharedPreferences("COOKIES",0).getString(RecipeColumns.NAME, "") + " Ingredients");
            onUpdate(context, awm, awm.getAppWidgetIds(cn));
        }
//        final String action = intent.getAction();
//        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
//            // refresh all your widgets
//            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//            ComponentName cn = new ComponentName(context, FavoriteRecipeWidget.class);
//            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.list_ingredient_widget);
//            onUpdate(context, mgr, mgr.getAppWidgetIds(cn));
//        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

//        for (int appWidgetId : appWidgetIds) {
//            RemoteViews views = new RemoteViews(
//                    context.getPackageName(),
//                    R.layout.favorite_recipe_widget
//            );
//            Intent intent = new Intent(context, FavoriteRecipeWidget.class);
//            intent.putExtra(RecipeColumns.NAME, context.getSharedPreferences("COOKIES",0).getString(RecipeColumns.NAME, ""));
//            intent.putExtra(RecipeColumns.ID, context.getSharedPreferences("COOKIES",0).getInt(RecipeColumns.ID, 0));
//            views.setTextViewText(R.id.text_title, context.getSharedPreferences("COOKIES",0).getString(RecipeColumns.NAME, "") + " Ingredients");
//            views.setRemoteAdapter(R.id.list_ingredient_widget, intent);
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

