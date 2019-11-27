package com.example.reach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ExampleViewHolder>  {
    private ArrayList<Item> mExampleList;
    private onClickListener mclickListener;

    public interface onClickListener {
        void onMyEventClick(Item item);
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        onClickListener clickListener;

        public ExampleViewHolder(View itemView, onClickListener clickListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imgCardView);
            mTextView1 = itemView.findViewById(R.id.txtTopText);
            mTextView2 = itemView.findViewById(R.id.txtBottomText);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Item item = mExampleList.get(getAdapterPosition());
            clickListener.onMyEventClick(item);
        }
    }

    public EventAdapter(ArrayList<Item> exampleList, onClickListener mclickListener) {
        mExampleList = exampleList;
        this.mclickListener = mclickListener;
    }


    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mclickListener);
        return evh;
    }


    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Item currentItem = mExampleList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
