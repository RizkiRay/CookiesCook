package com.ray.cookiescook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ray.cookiescook.R;
import com.ray.cookiescook.database.StepsColumns;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ray on 9/8/2017.
 */

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder> {


    public interface RecyclerClickListener{
        void onItemClickListener(int position, String title);
    }

    StepListAdapter.RecyclerClickListener mClickListener;
    private Cursor mCursor;
    private Context mContext;

    public StepListAdapter(StepListAdapter.RecyclerClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text_number)
        TextView textNumber;
        @BindView(R.id.text_step_description)
        TextView textDescription;
        @BindView(R.id.card_step)
        CardView cardStep;
        @BindView(R.id.container)
        LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClickListener(getAdapterPosition(), textDescription.getText().toString());
        }
    }
}
