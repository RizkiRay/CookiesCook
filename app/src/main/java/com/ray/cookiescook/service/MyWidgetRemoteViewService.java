package com.ray.cookiescook.service;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ray.cookiescook.adapter.FavoriteRecicpeRemoteViewsFactory;

/**
 * Created by ray on 9/17/17.
 */

public class MyWidgetRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoriteRecicpeRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
