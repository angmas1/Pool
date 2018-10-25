package com.example.anunay.pool;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder>{

    private ArrayList<EventFormat> events = new ArrayList<>();
    private Boolean isClickable=true;

    public void setClickable(Boolean clickable) {
        isClickable = clickable;
    }

    public EventAdapter(ArrayList<EventFormat> events) {
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recyclerviewholder,parent,false);
        EventViewHolder eventViewHolder =new EventViewHolder(view);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, final int position) {
        //holder.tvb.setText(bm[position].getBookname());
        //holder.tva.setText(bm[position].getBookauthor());
        holder.subtype.setText(events.get(position).getSubtype());
        holder.title.setText(events.get(position).getEventname());
        holder.description.setText(events.get(position).getDescription());
        holder.timer.setText(events.get(position).getEndsin());
        holder.strength.setText(String.valueOf(events.get(position).getNoofpeople()));
        switch (events.get(position).getType())
        {
            case "fabtrip":holder.icon.setImageResource(R.drawable.beach);
                break;
            case "fabcsgo":holder.icon.setImageResource(R.drawable.csgo);
                break;
            case "fabfood":holder.icon.setImageResource(R.drawable.fork);
                break;
            case "fabsports":holder.icon.setImageResource(R.drawable.football);
                break;
            case "fabothers":holder.icon.setImageResource(R.drawable.others);
                break;
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClickable) {
                    Intent intent = new Intent(v.getContext(), PoolPage.class);
                    intent.putExtra("key", events.get(position).getPushid());
                    //Toast.makeText(v.getContext(),events.get(position).getPushid(),Toast.LENGTH_SHORT).show();
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

}
