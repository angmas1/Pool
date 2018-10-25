package com.example.anunay.pool;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    private Calendar calendar;
    private RecyclerView chatView;
    private String phoneStr,whatsAppStr;
    private DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private FloatingActionButton sendbtn,votebtn,addbtn;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("events");
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private TextView tvvote;
    private Boolean isaddopen =false;
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
        setContentView(R.layout.activity_chat);
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
                                new MaterialDialog.Builder(ChatActivity.this)
                                        .title("Feedback")
                                        .content("Your feedback is very important to us.")
                                        .positiveText("Send")
                                        .inputRangeRes(2, 300, R.color.red)
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
        final String key=getIntent().getStringExtra("key");
        chatView=findViewById(R.id.chatList);
        sendbtn=findViewById(R.id.sendBtn);
        votebtn=findViewById(R.id.vote);
        tvvote=findViewById(R.id.tvvote);
        addbtn=findViewById(R.id.add);
        votebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoteBuilder voteBuilder = new VoteBuilder(ChatActivity.this,key);
                AlertDialog dialog = voteBuilder.create();
                dialog.show();
                closeAdd();
            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isaddopen)
                {
                    closeAdd();
                }
                else
                {
                    openAdd();
                }
            }
        });
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toolbar.setTitle(dataSnapshot.child(key).child("eventname").getValue(String.class));
                setSupportActionBar(toolbar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        calendar = Calendar.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);
        chatView.setLayoutManager(linearLayoutManager);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText typer = ((EditText)findViewById(R.id.typer));
                String text = typer.getText().toString();
                if(TextUtils.isEmpty(text)){
                    Toast.makeText(getApplication(),"Message is empty.",Toast.LENGTH_SHORT).show();
                    return;
                }
                ChatItemFormats message = new ChatItemFormats();
                message.setTimeDate(calendar.getTimeInMillis());
                message.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                message.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                message.setPhotourl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                message.setMessage(text);
                message.setType(0);
                message.setOptions(null);
                message.setQuestion(null);
                String msgkey=databaseReference.child(key).child("chat").push().getKey();
                databaseReference.child(key).child("chat").child(msgkey).setValue(message);
                typer.setText(null);
                loadMessages(key);
            }
        });
        loadMessages(key);
        chatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isaddopen){
                    closeAdd();
                }
                return false;
            }
        });
    }

    private void openAdd() {
        isaddopen=true;
        tvvote.setVisibility(View.VISIBLE);
        tvvote.animate().setDuration(150).alpha(1);
        chatView.animate().setDuration(150).alpha((float) 0.25);
        addbtn.animate().rotation(135).setDuration(150);
        votebtn.animate().translationY(-getResources().getDimension(R.dimen.standard_50)).setDuration(150);
    }

    private void closeAdd() {
        isaddopen=false;
        tvvote.setVisibility(View.INVISIBLE);
        tvvote.animate().setDuration(150).alpha(0);
        chatView.animate().setDuration(150).alpha((float) 1);
        addbtn.animate().rotation(0).setDuration(150);
        votebtn.animate().translationY(0).setDuration(150);
    }

    @Override
    public void onBackPressed() {
        if(isaddopen)
        {
            closeAdd();
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void loadMessages(String key) {

        databaseReference.child(key).child("chat").addValueEventListener(new ValueEventListener() {
            ArrayList<ChatItemFormats> messages=new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot childsnapshot:dataSnapshot.getChildren()){
                    ChatItemFormats cif=new ChatItemFormats();
                    if(childsnapshot.child("type").getValue(Integer.class)==0) {
                        cif.setMessage(childsnapshot.child("message").getValue(String.class));
                        cif.setTimeDate((long) childsnapshot.child("timeDate").getValue());
                        cif.setName(childsnapshot.child("name").getValue(String.class));
                        cif.setPhotourl(childsnapshot.child("photourl").getValue(String.class));
                        cif.setUid(childsnapshot.child("uid").getValue(String.class));
                        cif.setOptions(null);
                        cif.setType(0);
                        cif.setQuestion(null);
                    }
                    else if(childsnapshot.child("type").getValue(Integer.class)==1)
                    {
                        cif.setTimeDate((long) childsnapshot.child("timeDate").getValue());
                        cif.setName(childsnapshot.child("name").getValue(String.class));
                        cif.setPhotourl(childsnapshot.child("photourl").getValue(String.class));
                        cif.setUid(childsnapshot.child("uid").getValue(String.class));
                        cif.setQuestion(childsnapshot.child("question").getValue(String.class));
                        cif.setType(1);
                        cif.setMessage(null);
                        cif.setOptions(childsnapshot.child("options").getValue(ArrayList.class));
                    }
                    messages.add(cif);
                }
                ChatRVAdapter adapter=new ChatRVAdapter(messages);
                chatView.setAdapter(adapter);
                chatView.scrollToPosition(messages.size()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showPhone()
    {
        PhoneBuilder phoneBuilder=new PhoneBuilder(ChatActivity.this);
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
                                    Toast.makeText(ChatActivity.this,"Enter a Valid WhatsApp Number",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(ChatActivity.this,"Enter a Valid Phone Number",Toast.LENGTH_SHORT).show();
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

}
