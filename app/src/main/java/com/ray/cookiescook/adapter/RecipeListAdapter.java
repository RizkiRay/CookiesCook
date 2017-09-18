package com.ray.cookiescook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Debug;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ray.cookiescook.R;
import com.ray.cookiescook.database.RecipeColumns;
import com.squareup.picasso.Picasso;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ray on 9/8/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private RecyclerClickListener mOnClickListener;
    private Context mContext;
    private Cursor mCursor;

    public interface RecyclerClickListener {
        void onItemClickedListener(int position, int recipeId, String foodName);
    }

    public RecipeListAdapter(RecyclerClickListener clickListener) {
        mOnClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        Debug.stopMethodTracing();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_baking, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String recipeName = Cursors.getString(mCursor, RecipeColumns.NAME);
        String initialName = recipeName.substring(0, 1);
        String image = Cursors.getString(mCursor, RecipeColumns.IMAGE);
        String serving = Cursors.getString(mCursor, RecipeColumns.SERVINGS);

        holder.textRecipe.setText(recipeName);
        holder.textServings.setText(serving + " " + mContext.getResources().getString(R.string.text_servings));
        if (!image.isEmpty()) Picasso.with(mContext)
                .load(image)
                .placeholder(R.drawable
                        .recipe_placeholder).fit().centerCrop().into(holder.imageDisplay);
        else
            Picasso.with(mContext).load(R.drawable.recipe_placeholder).fit().centerCrop().into(holder.imageDisplay);
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        else return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.image_display)
        ImageView imageDisplay;

        @BindView(R.id.text_recipe_name)
        TextView textRecipe;

        @BindView(R.id.text_servings)
        TextView textServings;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            mOnClickListener.onItemClickedListener(position, Cursors.getInt(mCursor, RecipeColumns.ID), Cursors.getString(mCursor, RecipeColumns.NAME));
        }
    }
}
