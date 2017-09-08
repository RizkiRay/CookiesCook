package com.ray.cookiescook.database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by Ray on 9/7/2017.
 */

public interface IngredientsColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    String _ID = "id";

    @DataType(DataType.Type.REAL)
    String QUANTITY = "quantity";

    @DataType(DataType.Type.TEXT)
    String MEASURE = "measure";

    @DataType(DataType.Type.TEXT)
    String INGREDIENT = "ingredient";

    @DataType(DataType.Type.INTEGER)
    @References(table = BakingDatabase.RECIPES, column = RecipeColumns.ID)
    String RECIPE_ID = "recipe_id";
}
