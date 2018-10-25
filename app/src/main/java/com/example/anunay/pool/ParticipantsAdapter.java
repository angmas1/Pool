package com.example.anunay.pool;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.net.URLEncoder;
import java.util.ArrayList;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsViewHolder> {

    private ArrayList<ParticipantsFormat> participantsFormats=new ArrayList<>();
    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    public ParticipantsAdapter(ArrayList<ParticipantsFormat> participantsFormats) {
        this.participantsFormats = participantsFormats;
    }

    @Override
    public ParticipantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.namelistviewholder,parent,false);
        ParticipantsViewHolder participantsViewHolder =new ParticipantsViewHolder(view);
        return participantsViewHolder;
    }


    @Override
    public void onBindViewHolder(final ParticipantsViewHolder holder, final int position) {
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        switch (participantsFormats.get(position).getState())
        {
            case "in":
                break;
            case "left":holder.cardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#23646466")));
                holder.name.setAlpha((float) 0.3);
                break;
            case "admin":holder.cardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1effd700")));
                break;
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(participantsFormats.get(position).getUid()).child("phoneNumber").getValue(String.class).equals("null"))
                {
                    holder.phone.setAlpha((float)0.3);
                    holder.phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(v.getContext(),"User has not entered his phone number.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    holder.phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+dataSnapshot.child(participantsFormats.get(position).getUid()).child("phoneNumber").getValue(String.class)));
                            v.getContext().startActivity(intent);
                        }
                    });
                }
                if (dataSnapshot.child(participantsFormats.get(position).getUid()).child("whatsAppNumber").getValue(String.class).equals("null"))
                {
                    holder.whatsApp.setAlpha((float)0.3);
                    holder.whatsApp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(v.getContext(),"User has not entered his WhatsApp number.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    holder.whatsApp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PackageManager packageManager = v.getContext().getPackageManager();
                            Intent i = new Intent(Intent.ACTION_VIEW);

                            try {
                                String url = "https://api.whatsapp.com/send?phone="+ dataSnapshot.child(participantsFormats.get(position).getUid()).child("whatsAppNumber").getValue(String.class) +"&text=" + URLEncoder.encode("", "UTF-8");
                                i.setPackage("com.whatsapp");
                                i.setData(Uri.parse(url));
                                if (i.resolveActivity(packageManager) != null) {
                                    v.getContext().startActivity(i);
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
                holder.name.setText(dataSnapshot.child(participantsFormats.get(position).getUid()).child("name").getValue(String.class));
                holder.status.setText(dataSnapshot.child(participantsFormats.get(position).getUid()).child("onlinestatus").getValue(String.class));
                Picasso.get().load(dataSnapshot.child(participantsFormats.get(position).getUid()).child("photoUrl").getValue(String.class)).transform(new CircleTransform()).into(holder.profilephoto);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    @Override
    public int getItemCount() {
        return participantsFormats.size();
    }
}
