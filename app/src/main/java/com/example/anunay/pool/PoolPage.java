package com.example.anunay.pool;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

public class PoolPage extends AppCompatActivity {
    private TextView title, desc, subtitle, timer;
    private ImageView imageView;
    private FloatingActionButton messages;
    private Button inout;
    private Integer num;
    private DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private String phoneStr, whatsAppStr;
    private RecyclerView recyclerView;
    private Boolean attachmenu = false;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private String globalkey;
    private ArrayList<ParticipantsFormat> participantsFormats = new ArrayList<>();
    private ArrayList<ParticipantsFormat> adminarray = new ArrayList<>();
    private ArrayList<ParticipantsFormat> inarray = new ArrayList<>();
    private ArrayList<ParticipantsFormat> leftarray = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("events");
    private MaterialDialog dialogkick;
    private GoogleApiClient mGoogleApiClient;

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
        setContentView(R.layout.poollayout);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
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
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                result.closeDrawer();
                                Intent i1 = new Intent(getApplication(), YourPools.class);
                                startActivity(i1);
                                break;
                            case 2:
                                result.closeDrawer();
                                showPhone();
                                break;
                            case 3:
                                result.closeDrawer();
                                Intent i2 = new Intent(getApplication(), About.class);
                                startActivity(i2);
                                break;
                            case 4:
                                result.closeDrawer();
                                new MaterialDialog.Builder(PoolPage.this)
                                        .title("Feedback")
                                        .content("Your feedback is very important to us.")
                                        .positiveText("Send")
                                        .inputRangeRes(2, 300, R.color.red)
                                        .input("Feedback", null, new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                                DatabaseReference feed = FirebaseDatabase.getInstance().getReference().child("feedback");
                                                String fk = feed.push().getKey();
                                                feed.child(fk).child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                feed.child(fk).child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                                feed.child(fk).child("feedback").setValue(String.valueOf(input));
                                                Toast.makeText(getApplication(), "Thank You for your feedback.", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                                                finishAndRemoveTask();
                                                //Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                                //startActivity(i);
                                            }
                                        });
                                break;
                            case 6:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Title of shared content");
                                intent.putExtra(Intent.EXTRA_TEXT, "Shared text");
                                startActivity(Intent.createChooser(intent, "Share using"));
                                break;
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.poollayouttitle);
        desc = findViewById(R.id.poollayoutdesc);
        subtitle = findViewById(R.id.poollayoutsubtitle);
        timer = findViewById(R.id.poollayouttimer);
        messages = findViewById(R.id.fab_messages);
        recyclerView = findViewById(R.id.poollayoutrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        inout = findViewById(R.id.poollayoutinout);
        final String key = getIntent().getStringExtra("key");
        globalkey = key;

        databaseReference.child(globalkey).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title.setText(dataSnapshot.child("eventname").getValue(String.class));
                desc.setText(dataSnapshot.child("description").getValue(String.class));
                subtitle.setText(dataSnapshot.child("subtype").getValue(String.class));
                if (dataSnapshot.child("isactive").getValue(Boolean.class) == true) {
                    timer.setText(dataSnapshot.child("endsin").getValue(String.class));
                } else {
                    timer.setText("Expired");
                }
                switch (dataSnapshot.child("type").getValue(String.class)) {
                    case "fabtrip":
                        imageView.setImageResource(R.drawable.beach);
                        break;
                    case "fabcsgo":
                        imageView.setImageResource(R.drawable.csgo);
                        break;
                    case "fabfood":
                        imageView.setImageResource(R.drawable.fork);
                        break;
                    case "fabsports":
                        imageView.setImageResource(R.drawable.football);
                        break;
                    case "fabothers":
                        imageView.setImageResource(R.drawable.others);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(globalkey).child("participants").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Intent intent = new Intent(PoolPage.this, ChatActivity.class);
                            intent.putExtra("key", globalkey);
                            startActivity(intent);
                        } else {
                            Toast.makeText(PoolPage.this, "Please join the event first.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        loadData();

        databaseReference.child(globalkey).child("participants").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").getValue(String.class).equals("left")) {
                        inout.setText("REJOIN");
                    } else if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").getValue(String.class).equals("kicked")){
                        inout.setText("You have been kicked");
                    }
                    else{
                        inout.setText("LEAVE");
                    }
                } else {
                    inout.setText("GET IN");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        inout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (inout.getText().toString()) {
                    case "REJOIN":
                        new MaterialDialog.Builder(PoolPage.this)
                                .title("Rejoin")
                                .content("Are you sure you want to rejoin this event?")
                                .positiveText("Yes").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                loadDataWithoutChange();
                                if (num.equals(0)) {
                                    Log.e("asa", "s0");
                                    databaseReference.child(globalkey).child("participants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").setValue("admin");
                                } else {
                                    Log.e("asa", "s1");
                                    databaseReference.child(globalkey).child("participants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").setValue("in");
                                }
                            }
                        }).negativeText("No").onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        }).show();
                        break;
                    case "GET IN":
                        new MaterialDialog.Builder(PoolPage.this)
                                .title("Get in")
                                .content("Are you sure you want to get in this event?")
                                .positiveText("Yes").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                loadDataWithoutChange();
                                ParticipantsFormat participantsFormat = new ParticipantsFormat();
                                if (num.equals(0)) {
                                    Log.e("asa", "s2");
                                    participantsFormat.setState("admin");
                                } else {
                                    Log.e("asa", "s3");
                                    participantsFormat.setState("in");
                                }
                                participantsFormat.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                databaseReference.child(globalkey).child("participants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(participantsFormat);
                                DatabaseReference yourpools = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pools");
                                String yk = yourpools.push().getKey();
                                yourpools.child(yk).child("pushid").setValue(globalkey);
                            }
                        }).negativeText("No").onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        }).show();
                        break;
                    case "LEAVE":
                        new MaterialDialog.Builder(PoolPage.this)
                                .title("Leave")
                                .content("Are you sure you want to leave this event?")
                                .positiveText("Yes").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                loadDataWithoutChange();
                                /*if (num > 1) {
                                    databaseReference.child(globalkey).child("participants").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").getValue(String.class).equals("admin")) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    if (snapshot.child("state").getValue(String.class).equals("in")) {
                                                        Log.e("asa", "s4");
                                                        databaseReference.child(globalkey).child("participants").child(snapshot.getKey()).child("state").setValue("admin");
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }*/
                                if(num>1&&adminarray.get(0).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                {
                                    databaseReference.child(globalkey).child("participants").child(inarray.get(0).getUid()).child("state").setValue("admin");
                                }
                                else {
                                    Log.e("asa", "s5");
                                    databaseReference.child(globalkey).child("participants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").setValue("left");
                                }
                            }
                        }).negativeText("No").onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        }).show();
                        break;
                    case "You have been kicked":Toast.makeText(getApplication(),"You cannot join this pool",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (attachmenu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.poolpage_menu, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iac:
                new MaterialDialog.Builder(this)
                        .title("Inactivate")
                        .content("Are you sure you want to inactivate this event?")
                        .positiveText("Yes").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        databaseReference.child(globalkey).child("isactive").setValue(false);
                    }
                }).negativeText("No").onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                }).show();
                return true;
            case R.id.kick:
                loadDataWithoutChange();
                KickBuilder kickBuilder = new KickBuilder(PoolPage.this,participantsFormats,globalkey);
                AlertDialog dialog = kickBuilder.create();
                dialog.show();
                loadData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPhone() {
        PhoneBuilder phoneBuilder = new PhoneBuilder(PoolPage.this);
        AlertDialog dialog = phoneBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (phoneBuilder.phoneInput.isValid()) {
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
                                } else {
                                    Toast.makeText(PoolPage.this, "Enter a Valid WhatsApp Number", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(PoolPage.this, "Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
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

    public void loadData(){
        databaseReference.child(globalkey).child("participants").addValueEventListener(new ValueEventListener() {

            ParticipantsFormat participant;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantsFormats.clear();
                adminarray.clear();
                inarray.clear();
                leftarray.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    participant = new ParticipantsFormat();
                    participant.setUid(snapshot.child("uid").getValue(String.class));
                    participant.setState(snapshot.child("state").getValue(String.class));
                    switch (participant.getState()) {
                        case "admin":
                            adminarray.add(participant);
                            break;
                        case "in":
                            inarray.add(participant);
                            break;
                        case "left":
                            leftarray.add(participant);
                            break;
                    }
                }
                num = adminarray.size() + inarray.size();
                databaseReference.child(globalkey).child("noofpeople").setValue(num);
                for (int i = 0; i < adminarray.size(); ++i) {
                    participantsFormats.add(adminarray.get(i));
                    if (adminarray.get(i).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        attachmenu = true;
                    }
                }
                for (int j = adminarray.size(); j < adminarray.size() + inarray.size(); ++j) {
                    participantsFormats.add(inarray.get(j - adminarray.size()));
                    if (inarray.get(j-adminarray.size()).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        attachmenu = false;
                    }
                }
                for (int k = adminarray.size() + inarray.size(); k < adminarray.size() + inarray.size() + leftarray.size(); ++k) {
                    participantsFormats.add(leftarray.get(k - adminarray.size() - inarray.size()));
                    if (leftarray.get(k-adminarray.size()-inarray.size()).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        attachmenu = false;
                    }
                }
                ParticipantsAdapter adapter = new ParticipantsAdapter(participantsFormats);
                recyclerView.setAdapter(adapter);
                invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadDataWithoutChange(){
        databaseReference.child(globalkey).child("participants").addValueEventListener(new ValueEventListener() {

            ParticipantsFormat participant;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantsFormats.clear();
                adminarray.clear();
                inarray.clear();
                leftarray.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    participant = new ParticipantsFormat();
                    participant.setUid(snapshot.child("uid").getValue(String.class));
                    participant.setState(snapshot.child("state").getValue(String.class));
                    switch (participant.getState()) {
                        case "admin":
                            adminarray.add(participant);
                            break;
                        case "in":
                            inarray.add(participant);
                            break;
                        case "left":
                            leftarray.add(participant);
                            break;
                    }
                }
                num = adminarray.size() + inarray.size();
                databaseReference.child(globalkey).child("noofpeople").setValue(num);
                for (int i = 0; i < adminarray.size(); ++i) {
                    participantsFormats.add(adminarray.get(i));
                    if (adminarray.get(i).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        attachmenu = true;
                    }
                }
                for (int j = adminarray.size(); j < adminarray.size() + inarray.size(); ++j) {
                    participantsFormats.add(inarray.get(j - adminarray.size()));
                    if (inarray.get(j-adminarray.size()).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        attachmenu = false;
                    }
                }
                for (int k = adminarray.size() + inarray.size(); k < adminarray.size() + inarray.size() + leftarray.size(); ++k) {
                    participantsFormats.add(leftarray.get(k - adminarray.size() - inarray.size()));
                    if (leftarray.get(k-adminarray.size()-inarray.size()).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        attachmenu = false;
                    }
                }
                //ParticipantsAdapter adapter = new ParticipantsAdapter(participantsFormats);
                //recyclerView.setAdapter(adapter);
                invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
