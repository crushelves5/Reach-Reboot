package com.example.reach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter Class for RecyclerViews
 * @Author Bethel Adaghe
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ExampleViewHolder>  {
    private ArrayList<Item> mExampleList;
    private onClickListener mclickListener;

    /**
     * onClickListener interface for RecyclerView
     */
    public interface onClickListener {
        void onMyEventClick(Item item);
    }

    /**
     * ViewHolder class for RecylcerView instances
     */
    public class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        onClickListener clickListener;

        /**
         * RecyclerView ViewHolder constructor
         * @param itemView
         * @param clickListener
         */
        public ExampleViewHolder(View itemView, onClickListener clickListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imgCardView);
            mTextView1 = itemView.findViewById(R.id.txtTopText);
            mTextView2 = itemView.findViewById(R.id.txtBottomText);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * Extracts item object from array
         * @param view
         */
        @Override
        public void onClick(View view) {
            Item item = mExampleList.get(getAdapterPosition());
            clickListener.onMyEventClick(item);
        }
    }

    /**
     * Class Constructor
     * @param exampleList event object array
     * @param mclickListener onClickListener object
     */
    public EventAdapter(ArrayList<Item> exampleList, onClickListener mclickListener) {
        mExampleList = exampleList;
        this.mclickListener = mclickListener;
    }

    /**
     * Inflates layout and instantiates ViewHolder object
     * @param parent parent ViewGroup
     * @param viewType int
     * @return
     */
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mclickListener);
        return evh;
    }

    /**
     * loads item object data into layout items
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Item currentItem = mExampleList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    /**
     *
     * @return size of event object Array List
     */
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
