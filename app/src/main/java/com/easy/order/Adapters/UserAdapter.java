package com.easy.order.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.easy.order.FirebaseModels.ChatModel;
import com.easy.order.FirebaseModels.ClientsModel;
import com.easy.order.FirebaseModels.DataModel;
import com.easy.order.MainScreens.BlockListModel;
import com.easy.order.MainScreens.ChatActivity;
import com.easy.order.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<ClientsModel> mUsers;
    private boolean ischat;
    private DatabaseReference mDatabase;
    private String count;

    String theLastMessage;

    public UserAdapter(Context mContext, List<ClientsModel> mUsers, boolean ischat){
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_chat_list, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ClientsModel user = mUsers.get(position);
        holder.username.setText(user.getClientName());
        if (user.getClientImageProfile().equals("null")){
            holder.profile_image.setImageResource(R.drawable.easy_order_splash);
        } else {
//            Glide.with(mContext).load(user.getClientImageProfile()).into(holder.profile_image);

            Picasso.with(mContext).load(user.getClientImageProfile()).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.easy_order_splash).into(holder.profile_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mContext).load(user.getClientImageProfile()).placeholder(R.drawable.easy_order_splash).into(holder.profile_image);
                }
            });
        }


        holder.mPhone.setOnClickListener(view -> {
            mDatabase.child("Clients").child(user.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count = dataSnapshot.child("PhoneClick").getValue().toString();
                    DatabaseReference databaseReference = dataSnapshot.getRef();
                    databaseReference.child("PhoneClick").setValue(String.valueOf(Integer.parseInt(count) + 1));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            new Handler().postDelayed(() -> {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clients").child(user.getUsername());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String mNumber = null;
                        for (DataSnapshot snapshot : dataSnapshot.child("Phone").getChildren()) {
                            DataModel model = snapshot.getValue(DataModel.class);
                            mNumber = model.getData();
                        }
                        mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mNumber)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }, 100);
        });

        if (ischat){
            lastMessage(user.getUsername(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("BlockList").exists()){
                        for (DataSnapshot snapshot : dataSnapshot.child("BlockList").getChildren()){
                            BlockListModel blockListModel = snapshot.getValue(BlockListModel.class);
                            if (blockListModel.getClient().equals(user.getUsername())){
                                Toast.makeText(mContext, "لقد تم حظرك من هذه المحادثة", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(mContext, ChatActivity.class);
                                intent.putExtra("key", user.getUsername());
                                mContext.startActivity(intent);
                            }
                        }
                    }else{
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        intent.putExtra("key", user.getUsername());
                        mContext.startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        public ImageView mPhone;
        private TextView last_msg;


        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            mPhone = itemView.findViewById(R.id.call);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    //check for last message
    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatModel chat = snapshot.getValue(ChatModel.class);
                   if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }

//                switch (theLastMessage){
//                    case  "default":
//                        last_msg.setText("No Message");
//                        break;
//
//                    default:
//                        last_msg.setText(theLastMessage);
//                        break;
//                }
                final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

                Pattern p = Pattern.compile(URL_REGEX);
                Matcher m = p.matcher(theLastMessage);
                if (theLastMessage.equals("default")){
                    last_msg.setText("No Message");
                }else if (m.find()){
                    last_msg.setText("Image");
                }else{
                    last_msg.setText(theLastMessage);
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
