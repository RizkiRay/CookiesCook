package com.ray.cookiescook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.cookiescook.R;
import com.ray.cookiescook.database.RecipeColumns;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ray on 9/8/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private RecyclerClickListener mOnClickListener;
    private Context mContext;
    private Cursor mCursor;

    public interface RecyclerClickListener{
        void onItemClickedListener(int position);
        void onItemClickedListener(int position, int recipeId);
    }

    public RecipeListAdapter(RecyclerClickListener clickListener){
        mOnClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_baking, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String recipeName = Cursors.getString(mCursor, RecipeColumns.NAME);
        String initialName = recipeName.substring(0,1);

        holder.textInitial.setText(initialName);
        holder.textRecipe.setText(recipeName);
    }

//    public Cursor swapCursor(Cursor cursor){
//        if (mCursor == cursor) return null;
//        Cursor temp = mCursor;
//        mCursor = cursor;
//        if (cursor != null) this.notifyDataSetChanged();
//        return temp;
//    }
    public void setCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)return 0;
        else return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text_initial_name)
        TextView textInitial;

        @BindView(R.id.text_recipe_name)
        TextView textRecipe;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            mOnClickListener.onItemClickedListener(position, Cursors.getInt(mCursor, RecipeColumns.ID));
        }
    }
}
