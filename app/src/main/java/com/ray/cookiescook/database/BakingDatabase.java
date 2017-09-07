package com.ray.cookiescook.database;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Olis on 9/7/2017.
 */

@Database(version = BakingDatabase.VERSION, packageName = "com.ray.cookiescook.provider")
public final class BakingDatabase {
    public static final int VERSION = 1;

    @Table(RecipeColumns.class) public static final String RECIPES = "recipes";
    @Table(IngredientsColumns.class) public static final String INGREDIENTS = "ingredients";
    @Table(StepsColumns.class) public static final String STEPS = "steps";
}