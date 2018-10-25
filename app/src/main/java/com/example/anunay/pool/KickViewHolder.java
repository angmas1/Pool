package com.example.anunay.pool;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class KickViewHolder extends RecyclerView.ViewHolder{
    TextView kickname;
    CheckBox kickradio;
    public KickViewHolder(View itemview)
    {
        super(itemview);
        kickradio=itemview.findViewById(R.id.kickcheck);
        kickname=itemview.findViewById(R.id.kickname);
    }

}
