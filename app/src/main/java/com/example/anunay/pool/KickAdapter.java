package com.example.anunay.pool;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KickAdapter extends RecyclerView.Adapter<KickViewHolder>{

    private ArrayList<ParticipantsFormat> participantsFormats = new ArrayList<>();
    private DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("users");
    private ArrayList<ParticipantsFormat> result = new ArrayList<>();

    public KickAdapter(ArrayList<ParticipantsFormat> participantsFormats) {
        this.participantsFormats = participantsFormats;
    }

    @Override
    public KickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.kickviewholder,parent,false);
        KickViewHolder kickViewHolder =new KickViewHolder(view);
        return kickViewHolder;
    }

    @Override
    public void onBindViewHolder(final KickViewHolder holder, final int position) {
        ParticipantsFormat participant=new ParticipantsFormat(participantsFormats.get(position).getUid(),participantsFormats.get(position).getState());
        db.child(participantsFormats.get(position).getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.kickname.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.kickradio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    result.add(participant);
                }
                else
                {
                    result.remove(participant);
                }
            }
        });

    }

    public ArrayList<ParticipantsFormat> getResult() {
        return result;
    }

    @Override
    public int getItemCount() {
        return participantsFormats.size();
    }

}
