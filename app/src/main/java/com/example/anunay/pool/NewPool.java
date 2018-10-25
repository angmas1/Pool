package com.example.anunay.pool;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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

public class NewPool extends AppCompatActivity {
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("events");
    //private DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users");
    private EditText name,desc,timer,subtype;    private AccountHeader headerResult = null;
    private Drawer result = null;
    private Button create;
    private String phoneStr,whatsAppStr;
    private DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
        setContentView(R.layout.newpoollayout);
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
                                new MaterialDialog.Builder(NewPool.this)
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
        final String type = getIntent().getStringExtra("tag");
        //Toast.makeText(NewPool.this,type,Toast.LENGTH_SHORT).show();
        name=findViewById(R.id.title);
        desc=findViewById(R.id.description);
        timer=findViewById(R.id.timer);
        subtype=findViewById(R.id.subtype);
        create=findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFormat eventFormat=new EventFormat();
                final ParticipantsFormat participantsFormat=new ParticipantsFormat();
                eventFormat.setEventname(name.getText().toString());
                eventFormat.setDescription(desc.getText().toString());
                eventFormat.setType(type);
                eventFormat.setEndsin(timer.getText().toString());
                eventFormat.setSubtype(subtype.getText().toString());
                eventFormat.setNoofpeople(1);
                eventFormat.setIsactive(true);
                final String key=databaseReference.push().getKey();
                //String key2=databaseReference.child("allEvents").push().getKey();
                eventFormat.setPushid(key);
                databaseReference.child(key).setValue(eventFormat);
                participantsFormat.setState("admin");
                participantsFormat.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.child(key).child("participants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(participantsFormat);
                DatabaseReference yourpools=FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pools");
                String yk=yourpools.push().getKey();
                yourpools.child(yk).child("pushid").setValue(key);
                Intent intent = new Intent(getApplication(),PoolPage.class);
                intent.putExtra("key",key);
                //Toast.makeText(v.getContext(),events.get(position).getPushid(),Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

    }

    public void showPhone()
    {
        PhoneBuilder phoneBuilder=new PhoneBuilder(NewPool.this);
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
                                    Toast.makeText(NewPool.this,"Enter a Valid WhatsApp Number",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(NewPool.this,"Enter a Valid Phone Number",Toast.LENGTH_SHORT).show();
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
