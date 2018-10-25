package com.example.anunay.pool;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class ChatRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ChatItemFormats> chatFormats;
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


    public ChatRVAdapter(ArrayList<ChatItemFormats> chatFormats) {
        this.chatFormats = chatFormats;
    }

    @Override
    public int getItemViewType(int position) {
        if(chatFormats.get(position).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType)
        {
            case -1:/*LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View contactView = inflater.inflate(R.layout.chat_format_left, parent, false);
                return new LeftViewHolder(contactView);*/
                viewHolder=new LeftViewHolder(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.chat_format_left, parent, false));
                break;
            case 1:viewHolder=new RightViewHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.chat_format_right, parent, false));
                break;
            default:viewHolder=null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType=holder.getItemViewType();
        ChatItemFormats message = chatFormats.get(position);
        SimpleDateFormat format = new SimpleDateFormat("MMM dd  hh:mm a", Locale.US);
        switch (viewType)
        {
                    case -1:
                        ((LeftViewHolder)holder).timeleft.setText(format.format(message.getTimeDate()));
                        ((LeftViewHolder)holder).nameleft.setText(message.getName());
                        ((LeftViewHolder)holder).messageleft.setText(message.getMessage());
                        Picasso.get().load(message.getPhotourl()).transform(new CircleTransform()).into(((LeftViewHolder) holder).chaticonl);
                        break;

                    case 1:
                        ((RightViewHolder)holder).timeright.setText(format.format(message.getTimeDate()));
                        ((RightViewHolder)holder).nameright.setText(message.getName());
                        ((RightViewHolder)holder).messageright.setText(message.getMessage());
                        Picasso.get().load(message.getPhotourl()).transform(new CircleTransform()).into(((RightViewHolder) holder).chaticonr);
                        break;
        }

    }

    @Override
    public int getItemCount() {
        return chatFormats.size();
    }

    class LeftViewHolder extends RecyclerView.ViewHolder {

        TextView messageleft, nameleft, timeleft;
        ImageView chaticonl;

        public LeftViewHolder(View itemView) {
            super(itemView);
            nameleft = itemView.findViewById(R.id.nameleft);
            messageleft = itemView.findViewById(R.id.messageleft);
            timeleft = itemView.findViewById(R.id.timeleft);
            chaticonl =itemView.findViewById(R.id.chatiml);
        }
    }

    class RightViewHolder extends RecyclerView.ViewHolder {

        TextView messageright, nameright, timeright;
        ImageView chaticonr;

        public RightViewHolder(View itemView) {
            super(itemView);
            nameright = itemView.findViewById(R.id.nameright);
            messageright = itemView.findViewById(R.id.messageright);
            timeright = itemView.findViewById(R.id.timeright);
            chaticonr = itemView.findViewById(R.id.chatimr);
        }
    }
}
