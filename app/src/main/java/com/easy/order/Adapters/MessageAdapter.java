package com.easy.order.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easy.order.FirebaseModels.ChatModel;
import com.easy.order.MainScreens.ZoomActivity;
import com.easy.order.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<ChatModel> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<ChatModel> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        ChatModel chat = mChat.get(position);

        final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(chat.getMessage());//replace with string to compare
        if(m.find()) {
            holder.show_image.setVisibility(View.VISIBLE);
            holder.show_message.setVisibility(View.GONE);
            Picasso.with(mContext).load(chat.getMessage()).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.easy_order_splash).into(holder.show_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mContext).load(chat.getMessage()).placeholder(R.drawable.easy_order_splash)
                            .into(holder.show_image);
                }
            });
        }else{
            holder.show_message.setText(chat.getMessage());
            holder.show_image.setVisibility(View.GONE);
            holder.show_message.setVisibility(View.VISIBLE);
        }

        holder.show_image.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ZoomActivity.class);
            intent.putExtra("key", chat.getMessage());
            mContext.startActivity(intent);
        });

        if (imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.launcher);
        } else {
//            Glide.with(mContext).load(imageurl).into(holder.profile_image);

            Picasso.with(mContext).load(imageurl).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.mipmap.launcher).into(holder.profile_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mContext).load(imageurl).placeholder(R.mipmap.launcher).into(holder.profile_image);
                }
            });
        }

        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;
        public ImageView show_image;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            show_image = itemView.findViewById(R.id.show_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}