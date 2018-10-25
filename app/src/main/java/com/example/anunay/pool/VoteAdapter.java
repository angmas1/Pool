package com.example.anunay.pool;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.net.URLEncoder;
import java.util.ArrayList;

public class VoteAdapter extends RecyclerView.Adapter<VoteViewHolder> {

    private ArrayList<String> options=new ArrayList<>();

    public VoteAdapter(ArrayList<String> options) {
        this.options = options;
    }

    @Override
    public VoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.voteviehholder,parent,false);
        VoteViewHolder voteViewHolder =new VoteViewHolder(view);
        return voteViewHolder;
    }


    @Override
    public void onBindViewHolder(VoteViewHolder holder, int position) {
        holder.opttv.setText(options.get(position));
        holder.fabremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,options.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }
}
