package com.ray.cookiescook.database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Olis on 9/7/2017.
 */

public interface RecipeColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    String ID = "id";

    @DataType(DataType.Type.TEXT)
    String NAME = "name";

    @DataType(DataType.Type.INTEGER)
    String SERVINGS = "servings";

    @DataType(DataType.Type.TEXT)
    String IMAGE = "image";
}
