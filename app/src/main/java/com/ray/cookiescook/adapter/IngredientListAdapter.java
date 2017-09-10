package com.ray.cookiescook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.cookiescook.R;
import com.ray.cookiescook.database.IngredientsColumns;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ray on 08/09/2017.
 */

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder>{

    private Cursor mCursor;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String strQuantity = Cursors.getString(mCursor, IngredientsColumns.QUANTITY);
        String strMeasure = Cursors.getString(mCursor, IngredientsColumns.MEASURE);
        String strIngredient = Cursors.getString(mCursor, IngredientsColumns.INGREDIENT);

        holder.textNumber.setText(position+1 + "");
        holder.textIngredient.setText(strIngredient);
        holder.textMeasure.setText(strQuantity + " " + strMeasure);
    }

    public void setCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        else return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.text_number)
        TextView textNumber;
        @BindView(R.id.text_measure)
        TextView textMeasure;
        @BindView(R.id.text_ingredient)
        TextView textIngredient;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
