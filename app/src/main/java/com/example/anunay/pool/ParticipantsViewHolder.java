package com.example.anunay.pool;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ParticipantsViewHolder extends RecyclerView.ViewHolder {
    TextView name,status;
    Button phone,whatsApp;
    CardView cardView;
    ImageView profilephoto;
    public ParticipantsViewHolder(View itemview)
    {
        super(itemview);
        name=itemview.findViewById(R.id.name);
        phone=itemview.findViewById(R.id.phoneNumber);
        whatsApp=itemview.findViewById(R.id.whatsAppNumber);
        cardView=itemView.findViewById(R.id.rvm_item_namelist);
        profilephoto=itemview.findViewById(R.id.profileimageview);
        status=itemview.findViewById(R.id.status);
    }
}
