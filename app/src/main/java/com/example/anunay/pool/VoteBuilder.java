package com.example.anunay.pool;

import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import java.util.ArrayList;
import java.util.Calendar;

public class VoteBuilder extends AlertDialog.Builder {

    private RecyclerView rvopt;
    private EditText question;
    private ConstraintLayout conadd;
    private EditText option;
    private FloatingActionButton add;
    private String opt;
    private ArrayList<String> options = new ArrayList<>();

    public VoteBuilder(Context context,String key) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View viewDialog = inflater.inflate(R.layout.votedialog, null, false);
        rvopt=viewDialog.findViewById(R.id.voterv);
        question=viewDialog.findViewById(R.id.question);
        option=viewDialog.findViewById(R.id.opt);
        conadd=viewDialog.findViewById(R.id.conadd);
        add=viewDialog.findViewById(R.id.fabadd);
        rvopt.setLayoutManager(new LinearLayoutManager(getContext()));
        /*conadd.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(options.size()>5)
                {
                    conadd.setVisibility(View.INVISIBLE);
                }
                else
                {
                    conadd.setVisibility(View.VISIBLE);
                }
            }
        });*/
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("events").child(key).child("chat");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opt=option.getText().toString();
                if(opt.equals(""))
                {
                    Toast.makeText(getContext(),"Enter an option",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    options.add(opt);
                    option.setText("");
                    VoteAdapter adapter = new VoteAdapter(options);
                    rvopt.setAdapter(adapter);
                }
            }
        });
        this.setTitle("Create a Poll").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options.size()>1&&!(question.getText().toString().equals(""))) {
                    String pid = databaseReference.push().getKey();
                    databaseReference.child(pid).child("question").setValue(question.getText().toString());
                    databaseReference.child(pid).child("options").setValue(options);
                    databaseReference.child(pid).child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    databaseReference.child(pid).child("photourl").setValue(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
                    databaseReference.child(pid).child("timeDate").setValue(Calendar.getInstance().getTimeInMillis());
                    databaseReference.child(pid).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.child(pid).child("type").setValue(1);
                }
                else
                {
                    Toast.makeText(getContext(),"Enter fields correctly",Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.setCancelable(false);
        this.setView(viewDialog);

    }

}
