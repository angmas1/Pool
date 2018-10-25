package com.example.anunay.pool;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Anunay on 17-03-2018.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {
    TextView subtype,title,description,strength,timer;
    ImageView icon;
    CardView cardView;
    public EventViewHolder(View itemview)
    {
        super(itemview);
        subtype=itemview.findViewById(R.id.subtype);
        title=itemview.findViewById(R.id.title);
        description=itemview.findViewById(R.id.description);
        strength=itemview.findViewById(R.id.strength);
        timer=itemview.findViewById(R.id.timer);
        icon=itemview.findViewById(R.id.imageView);
        cardView=itemView.findViewById(R.id.rvm_item);
    }
}
