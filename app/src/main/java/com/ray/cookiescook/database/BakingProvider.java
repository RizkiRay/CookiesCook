package com.ray.cookiescook.database;

import android.net.Uri;
import android.util.Log;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Ray on 9/7/2017.
 */

@ContentProvider(authority = BakingProvider.AUTHORITY, database = BakingDatabase.class,
        packageName = "com.ray.cookiescook.provider")
public final class BakingProvider {
    public static final String AUTHORITY = "com.ray.cookiescook.provider.BakingProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String RECIPES = "recipes";
        String INGREDIENTS = "ingredients";
        String STEPS = "steps";
        String WIDGET_INGREDIENTS = "widget_ingredients";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) builder.appendPath(path);
        return builder.build();
    }

    @TableEndpoint(table = BakingDatabase.RECIPES)
    public static class Recipes {
        @ContentUri(
                path = Path.RECIPES,
                type = "vnd.android.cursor.dir/recipe",
                defaultSort = RecipeColumns.ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.RECIPES);

        @InexactContentUri(
                path = Path.RECIPES + "/#",
                name = "RECIPE_ID",
                type = "vnd.android.cursor.item/recipe",
                whereColumn = RecipeColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.RECIPES, String.valueOf(id));
        }

        static final String RECIPE_COUNT = "(SELECT COUNT(*) FROM "
                + BakingDatabase.RECIPES + ")";
    }

    @TableEndpoint(table = BakingDatabase.INGREDIENTS)
    public static class Ingredients {
        @ContentUri(path = Path.INGREDIENTS, type = "vnd.android.cursor.dir/ingredients", defaultSort = IngredientsColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.INGREDIENTS);

        @InexactContentUri(
                path = Path.INGREDIENTS + "/" + Path.RECIPES + "/#",
                name = "INGREDIENT_RECIPE_ID",
                type = "vnd.android.cursor.dir/recipe/ingredients",
                whereColumn = IngredientsColumns.RECIPE_ID,
                pathSegment = 2)
        public static Uri recipeIngredients(long recipeId) {
            return buildUri(Path.INGREDIENTS, Path.RECIPES, String.valueOf(recipeId));
        }

        static final String INGREDIENTS_COUNT = "(SELECT COUNT(*) FROM "
                + BakingDatabase.INGREDIENTS + " WHERE "
                + BakingDatabase.INGREDIENTS + "." + IngredientsColumns.RECIPE_ID + " = "
                + BakingDatabase.RECIPES + "." + RecipeColumns.ID + ")";
    }

    @TableEndpoint(table = BakingDatabase.STEPS)
    public static class Steps {
        @ContentUri(path = Path.STEPS, type = "vnd.android.cursor.dir/steps", defaultSort = StepsColumns.ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.STEPS);

        @InexactContentUri(
                path = Path.STEPS + "/" + Path.RECIPES + "/#",
                name = "STEPS_RECIPE_ID",
                type = "vnd.android.cursor.dir/recipe/steps",
                whereColumn = StepsColumns.RECIPE_ID,
                pathSegment = 2)
        public static Uri recipeSteps(long recipeId) {
            return buildUri(Path.STEPS, Path.RECIPES, String.valueOf(recipeId));
        }

        static final String STEPS_COUNT = "(SELECT COUNT(*) FROM "
                + BakingDatabase.STEPS + " WHERE "
                + BakingDatabase.STEPS + "." + StepsColumns.RECIPE_ID + " = "
                + BakingDatabase.RECIPES + "." + RecipeColumns.ID + ")";
    }
}
