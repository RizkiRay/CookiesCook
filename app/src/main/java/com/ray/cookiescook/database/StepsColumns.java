package com.ray.cookiescook.database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.References;

/**
 * Created by Ray on 9/7/2017.
 */

public interface StepsColumns {
    @DataType(DataType.Type.INTEGER)
    String ID = "_id";
    @DataType(DataType.Type.TEXT)
    String SHORT_DESCRIPTION = "shortDescription";
    @DataType(DataType.Type.TEXT)
    String DESCRIPTION = "description";
    @DataType(DataType.Type.TEXT)
    String VIDEO_URL = "videoURL";
    @DataType(DataType.Type.TEXT)
    String THUMBNAIL_URL = "thumbnailURL";

    @DataType(DataType.Type.INTEGER)
    @References(table = BakingDatabase.RECIPES, column = RecipeColumns.ID)
    String RECIPE_ID = "recipe_id";
}
