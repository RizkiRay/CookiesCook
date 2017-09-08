package com.ray.cookiescook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.cookiescook.R;
import com.ray.cookiescook.database.StepsColumns;

import net.simonvt.schematic.Cursors;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ray on 9/8/2017.
 */

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder> {


    RecipeListAdapter.RecyclerClickListener mClickListener;
    private Cursor mCursor;
    private Context mContext;

    @BindString(R.string.text_ingredients)
    String strIngredients;

    public StepListAdapter(RecipeListAdapter.RecyclerClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        ButterKnife.bind(v);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            holder.textNumber.setText("i");
            holder.textDescription.setText("Ingredients");
        } else {
            int newPos = position - 1;
            mCursor.moveToPosition(newPos);
            String strStepDesc = Cursors.getString(mCursor, StepsColumns.SHORT_DESCRIPTION);
            holder.textNumber.setText(position + "");
            holder.textDescription.setText(strStepDesc);
        }
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        else return mCursor.getCount() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_number)
        TextView textNumber;
        @BindView(R.id.text_step_description)
        TextView textDescription;
        @BindView(R.id.card_step)
        CardView cardStep;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClickedListener(getAdapterPosition());
                }
            });
        }
    }
}
