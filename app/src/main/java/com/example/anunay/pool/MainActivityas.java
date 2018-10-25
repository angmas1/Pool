package com.example.anunay.pool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivityas extends AppCompatActivity {
    private FloatingActionButton fabmain,fabfood,fabsports,fabcsgo,fabothers,fabtrip;
    private boolean isFABOpen=false;
    private TextView tvfood,tvothers,tvgames,tvsports,tvtrip;
    private RecyclerView rvm;
    private boolean backcheck=false;
    ArrayList<EventFormat> evs =new ArrayList<>();
    //private DatabaseReference databaseReference;
    private String phn,wn,phoneStr,whatsAppStr;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("events");
    private DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private GoogleApiClient mGoogleApiClient;
    private EventAdapter adapter=new EventAdapter(evs);
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainas);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadImage();

        IProfile profile = new ProfileDrawerItem().withName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).withEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()).withIcon(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()).withIdentifier(100);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .withSavedInstance(savedInstanceState)
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Your Pools").withDescription("View pools you are/were a part of").withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName("Reset Phone number").withDescription("Change/add your phone number").withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName("Share").withDescription("Spread the word").withIdentifier(6).withSelectable(false),
                        new PrimaryDrawerItem().withName("About").withDescription("Know more about us").withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName("Feedback").withIdentifier(4).withSelectable(false),
                        new PrimaryDrawerItem().withName("Sign out").withIdentifier(5).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int)drawerItem.getIdentifier())
                        {
                            case 1:
                                result.closeDrawer();
                                Intent i1 = new Intent(getApplication(),YourPools.class);
                                startActivity(i1);
                                break;
                            case 2:
                                result.closeDrawer();
                                showPhone();
                                break;
                            case 3:
                                result.closeDrawer();
                                Intent i2 = new Intent(getApplication(),About.class);
                                startActivity(i2);
                                break;
                            case 4:
                                result.closeDrawer();
                                new MaterialDialog.Builder(MainActivityas.this)
                                        .title("Feedback")
                                        .content("Your feedback is very important to us.")
                                        .inputRangeRes(2, 300, R.color.red)
                                        .positiveText("Send")
                                        .input("Feedback", null, new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                                DatabaseReference feed = FirebaseDatabase.getInstance().getReference().child("feedback");
                                                String fk=feed.push().getKey();
                                                feed.child(fk).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                feed.child(fk).child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                                feed.child(fk).child("feedback").setValue(String.valueOf(input));
                                                Toast.makeText(getApplication(),"Thank You for your feedback.",Toast.LENGTH_SHORT).show();
                                            }
                                        }).show();
                                break;
                            case 5:
                                result.closeDrawer();
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                        new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(Status status) {
                                                FirebaseAuth.getInstance().signOut();
                                                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                                finishAndRemoveTask();
                                                //Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                                //startActivity(i);
                                            }
                                        });
                                break;
                            case 6:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT,"Title of shared content");
                                intent.putExtra(Intent.EXTRA_TEXT,"Shared text");
                                startActivity(Intent.createChooser(intent,"Share using"));
                                break;
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
        db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });









        fabtrip=findViewById(R.id.fab_trip);
        fabmain=findViewById(R.id.fab_main);
        fabfood=findViewById(R.id.fab_food);
        fabsports=findViewById(R.id.fab_sports);
        fabcsgo=findViewById(R.id.fab_games);
        fabothers=findViewById(R.id.fab_others);
        tvtrip=findViewById(R.id.tvtrip);
        tvfood=findViewById(R.id.tvfood);
        tvothers=findViewById(R.id.tvothers);
        tvgames=findViewById(R.id.tvgames);
        tvsports=findViewById(R.id.tvsports);
        rvm=findViewById(R.id.rv_main);
        rvm.setLayoutManager(new LinearLayoutManager(this));
        fabmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        rvm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!isFABOpen){
                    //showFABMenu();
                }else{
                    closeFABMenu();
                }
                return false;
            }
        });
        //databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        db2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phn=dataSnapshot.child("phoneNumber").getValue(String.class);
                wn=dataSnapshot.child("whatsAppNumber").getValue(String.class);
                if(wn.equals("null")||phn.equals("null"))
                {
                    /*PhoneDialogFragment phoneDialogFragment=new PhoneDialogFragment();
                    phoneDialogFragment.show(getSupportFragmentManager(),"phoneDialog");
                    phoneDialogFragment.setCancelable(false);*/
                    showPhone();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fabtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityas.this,NewPool.class);
                intent.putExtra("tag",fabtrip.getTag().toString());
                startActivity(intent);
                closeFABMenu();
            }
        });


        fabcsgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityas.this,NewPool.class);
                intent.putExtra("tag",fabcsgo.getTag().toString());
                startActivity(intent);
                closeFABMenu();
            }
        });


        fabfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityas.this,NewPool.class);
                intent.putExtra("tag",fabfood.getTag().toString());
                startActivity(intent);
                closeFABMenu();
            }
        });


        fabsports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityas.this,NewPool.class);
                intent.putExtra("tag",fabsports.getTag().toString());
                startActivity(intent);
                closeFABMenu();
            }
        });


        fabothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityas.this,NewPool.class);
                intent.putExtra("tag",fabothers.getTag().toString());
                startActivity(intent);
                closeFABMenu();
            }
        });
        /*ArrayList<EventFormat> event_formats =new ArrayList<>();
        EventFormat event=new EventFormat("name","desc","fabtrip","panjim",5,"10 min");
        event_formats.add(event);
        EventAdapter adapter=new EventAdapter(event_formats);
        rvm.setAdapter(adapter);
        private String eventname,description,type,subtype;
        //int type; //0 for food, 1 for sports, 2 for games, 3 for others
        private int noofpeople;
        private String endsin;*/
        databaseReference.addValueEventListener(new ValueEventListener() {
            ArrayList<EventFormat> event_formats =new ArrayList<>();
            EventFormat event;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                event_formats.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    event=new EventFormat();
                    if( snapshot.child("isactive").getValue(Boolean.class)==true) {
                        event.setEventname(snapshot.child("eventname").getValue(String.class));
                        event.setDescription(snapshot.child("description").getValue(String.class));
                        event.setType(snapshot.child("type").getValue(String.class));
                        event.setSubtype(snapshot.child("subtype").getValue(String.class));
                        event.setEndsin(snapshot.child("endsin").getValue(String.class));
                        event.setNoofpeople(snapshot.child("noofpeople").getValue(Integer.class));
                        event.setPushid(snapshot.child("pushid").getValue(String.class));
                        event.setIsactive(snapshot.child("isactive").getValue(Boolean.class));
                        event_formats.add(event);
                    }
                }
                adapter=new EventAdapter(event_formats);
                rvm.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadImage() {
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.get().load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.get().cancelRequest(imageView);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(isFABOpen||result.isDrawerOpen()) {
            if (result.isDrawerOpen()) {
                result.closeDrawer();
            }
            if (isFABOpen) {
                closeFABMenu();
            }
        }
        else{
            if (backcheck) {
                super.onBackPressed();
                return;
            }
            this.backcheck = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backcheck=false;
                }
            }, 2000);
        }
    }
    private void showFABMenu(){
        adapter.setClickable(false);
        rvm.setAdapter(adapter);
        isFABOpen=true;
        tvfood.setVisibility(View.VISIBLE);
        tvothers.setVisibility(View.VISIBLE);
        tvgames.setVisibility(View.VISIBLE);
        tvsports.setVisibility(View.VISIBLE);
        tvtrip.setVisibility(View.VISIBLE);
        rvm.animate().setDuration(150).alpha((float) 0.25);
        fabmain.animate().rotation(135).setDuration(150);
        fabothers.animate().translationY(-getResources().getDimension(R.dimen.standard_55)).setDuration(150);
        tvothers.animate().setDuration(200).alpha(1);
        fabcsgo.animate().translationY(-getResources().getDimension(R.dimen.standard_105)).setDuration(150);
        tvgames.animate().setDuration(200).alpha(1);
        fabsports.animate().translationY(-getResources().getDimension(R.dimen.standard_155)).setDuration(150);
        tvsports.animate().setDuration(200).alpha(1);
        fabfood.animate().translationY(-getResources().getDimension(R.dimen.standard_205)).setDuration(150);
        tvfood.animate().setDuration(200).alpha(1);
        fabtrip.animate().translationY(-getResources().getDimension(R.dimen.standard_255)).setDuration(150);
        tvtrip.animate().setDuration(200).alpha(1);
    }

    private void closeFABMenu(){
        adapter.setClickable(true);
        rvm.setAdapter(adapter);
        isFABOpen=false;
        tvfood.setVisibility(View.INVISIBLE);
        tvothers.setVisibility(View.INVISIBLE);
        tvgames.setVisibility(View.INVISIBLE);
        tvsports.setVisibility(View.INVISIBLE);
        tvtrip.setVisibility(View.INVISIBLE);
        rvm.animate().setDuration(150).alpha(1);
        fabmain.animate().rotation(0).setDuration(150);
        fabothers.animate().translationY(0).setDuration(150);
        tvothers.animate().setDuration(200).alpha(0);
        fabcsgo.animate().translationY(0).setDuration(150);
        tvgames.animate().setDuration(200).alpha(0);
        fabsports.animate().translationY(0).setDuration(150);
        tvsports.animate().setDuration(200).alpha(0);
        fabfood.animate().translationY(0).setDuration(150);
        tvfood.animate().setDuration(200).alpha(0);
        fabtrip.animate().translationY(0).setDuration(150);
        tvtrip.animate().setDuration(200).alpha(0);
    }

    public void showPhone()
    {
        PhoneBuilder phoneBuilder=new PhoneBuilder(MainActivityas.this);
        AlertDialog dialog=phoneBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(phoneBuilder.phoneInput.isValid()) {
                            phoneStr = phoneBuilder.phoneInput.getText();
                            if (phoneBuilder.whatsAppInput.getVisibility() == View.GONE) {
                                db2.child("phoneNumber").setValue(phoneStr);
                                db2.child("whatsAppNumber").setValue(phoneStr);
                                dialog.dismiss();
                                //listner.applyTexts(phoneStr,phoneStr);

                            } else {
                                if (phoneBuilder.whatsAppInput.isValid()) {
                                    whatsAppStr = phoneBuilder.whatsAppInput.getText();
                                    db2.child("phoneNumber").setValue(phoneStr);
                                    db2.child("whatsAppNumber").setValue(whatsAppStr);
                                    dialog.dismiss();
                                    //listner.applyTexts(phoneStr,whatsAppStr);
                                }
                                else{
                                    Toast.makeText(MainActivityas.this,"Enter a Valid WhatsApp Number",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(MainActivityas.this,"Enter a Valid Phone Number",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

}
