package com.ray.cookiescook.network;

import com.ray.cookiescook.model.Baking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Ray on 9/7/2017.
 */

public interface NetworkInterface {
    @GET("baking.json")
    Call<List<Baking>> getRecipes();
}
