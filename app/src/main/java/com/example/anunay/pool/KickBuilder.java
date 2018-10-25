package com.example.anunay.pool;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import java.util.ArrayList;

public class KickBuilder extends AlertDialog.Builder {

    private RecyclerView kickrv;
    private ArrayList<ParticipantsFormat> result = new ArrayList<>();
    private DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("events");

    public KickBuilder(Context context,ArrayList<ParticipantsFormat> participantsFormats,String key) {
        super(context);
        db=db.child(key).child("participants");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View viewDialog = inflater.inflate(R.layout.kick_dialog, null, false);
        kickrv=viewDialog.findViewById(R.id.kickrv);
        kickrv.setLayoutManager(new LinearLayoutManager(getContext()));
        KickAdapter adapter=new KickAdapter(participantsFormats);
        kickrv.setAdapter(adapter);
        this.setTitle("Select People to Kick").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Kick", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result=adapter.getResult();
                for(int i=0;i<result.size();i++)
                {
                    db.child(result.get(i).getUid()).child("state").setValue("kicked");
                }
            }
        });
        this.setCancelable(false);
        this.setView(viewDialog);

    }
}
