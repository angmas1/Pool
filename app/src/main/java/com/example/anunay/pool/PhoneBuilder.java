package com.example.anunay.pool;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.rimoto.intlphoneinput.IntlPhoneInput;

public class PhoneBuilder extends AlertDialog.Builder {

    CheckBox checkBox;
    IntlPhoneInput phoneInput,whatsAppInput;
    //private String phoneStr,whatsAppStr;
    //private DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    public PhoneBuilder(Context context) {
            super(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View viewDialog = inflater.inflate(R.layout.phone_dialog, null, false);

            /*TextView titleTextView = (TextView)viewDialog.findViewById(R.id.title);
            titleTextView.setText(title);
            TextView messageTextView = (TextView)viewDialog.findViewById(R.id.message);
            messageTextView.setText(message);*/

            checkBox=viewDialog.findViewById(R.id.check);
            phoneInput=viewDialog.findViewById(R.id.phone);
            whatsAppInput=viewDialog.findViewById(R.id.whatsApp);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(whatsAppInput.getVisibility()==View.GONE) {
                        whatsAppInput.setVisibility(View.VISIBLE);
                    }
                    else {
                        whatsAppInput.setVisibility(View.GONE);
                    }
                }
            });
            this.setTitle("Enter Phone Number").setNegativeButton("Later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            this.setCancelable(false);
            this.setView(viewDialog);

        }
}
