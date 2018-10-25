package com.example.anunay.pool;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class VoteViewHolder extends RecyclerView.ViewHolder {

    TextView opttv;
    FloatingActionButton fabremove;

    public VoteViewHolder(View itemView) {
        super(itemView);
        fabremove = itemView.findViewById(R.id.fabminus);
        opttv = itemView.findViewById(R.id.optvh);
    }
}