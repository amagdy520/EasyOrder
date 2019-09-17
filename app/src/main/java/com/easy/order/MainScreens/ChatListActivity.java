package com.easy.order.MainScreens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.order.Adapters.UserAdapter;
import com.easy.order.FirebaseModels.ChatModel;
import com.easy.order.FirebaseModels.ChatlistModel;
import com.easy.order.FirebaseModels.ClientsModel;
import com.easy.order.FirebaseModels.DataModel;
import com.easy.order.FirebaseModels.TestModel;
import com.easy.order.FirebaseModels.UserModel;
import com.easy.order.Notifications.Token;
import com.easy.order.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

//    private UserAdapter userAdapter;
//    private List<ClientsModel> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference, mDatabase;


    private String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("الرسائل");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            // and this
            onBackPressed();
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

//        usersList = new ArrayList<>();
//
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                usersList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    ChatlistModel chatlist = snapshot.getValue(ChatlistModel.class);
//                    usersList.add(chatlist);
//                }
//
//                chatList();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        Message();
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Message();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Message();
    }

//    private void chatList() {
//        mUsers = new ArrayList<>();
//        reference = FirebaseDatabase.getInstance().getReference("Clients");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUsers.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    ClientsModel user = snapshot.getValue(ClientsModel.class);
//                    for (ChatlistModel chatlist : usersList) {
//                        if (user.getUsername().equals(chatlist.getId())) {
//                            mUsers.add(user);
//                        }
//                    }
//                }
//                userAdapter = new UserAdapter(ChatListActivity.this, mUsers, true);
//                recyclerView.setAdapter(userAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


    public void Message() {
        FirebaseRecyclerAdapter<ChatlistModel, ChatListActivity.BlogViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ChatlistModel, ChatListActivity.BlogViewHolder>(
                        ChatlistModel.class,
                        R.layout.row_chat_list,
                        ChatListActivity.BlogViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(final ChatListActivity.BlogViewHolder viewHolder, final ChatlistModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setId(ChatListActivity.this, model.getId());
                        viewHolder.mView.setOnClickListener(view -> {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("BlockList").exists()){
                                        for (DataSnapshot snapshot : dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("BlockList").getChildren()){
                                            BlockListModel blockListModel = snapshot.getValue(BlockListModel.class);
                                            if (blockListModel.getClient().equals(model.getId())){
                                                Toast.makeText(ChatListActivity.this, "لقد تم حظرك من هذه المحادثة", Toast.LENGTH_SHORT).show();
                                            }else{
                                                if (dataSnapshot.child("Clients").child(model.getId()).child("ChatAvailability").exists()){
                                                    if (dataSnapshot.child("Clients").child(model.getId()).child("ChatAvailability").getValue().toString().equals("no")){
                                                        Toast.makeText(ChatListActivity.this, "هذا العميل غير متاح الأن", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                                                        intent.putExtra("key", model.getId());
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                        }
                                    }else{
                                        if (dataSnapshot.child("Clients").child(model.getId()).child("ChatAvailability").exists()){
                                            if (dataSnapshot.child("Clients").child(model.getId()).child("ChatAvailability").getValue().toString().equals("no")){
                                                Toast.makeText(ChatListActivity.this, "هذا العميل غير متاح الأن", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                                                intent.putExtra("key", model.getId());
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        });

                        viewHolder.mPhone.setOnClickListener(view -> {
                            mDatabase.child("Clients").child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clients")
                                        .child(model.getId());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String mNumber = null;
                                        for (DataSnapshot snapshot : dataSnapshot.child("Phone").getChildren()) {
                                            DataModel model = snapshot.getValue(DataModel.class);
                                            mNumber = model.getData();
                                        }
                                       startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mNumber)));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }, 100);
                        });
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Typeface tf;
        String fontPath;
        private final static int FADE_DURATION = 100;
        private DatabaseReference mDatabase;
        String theLastMessage;

        ImageView mPhone;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            fontPath = "fonts/andalus.ttf";
            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);
            mPhone = itemView.findViewById(R.id.call);
            setFadeAnimation(mView);
        }

        public void setId(Context ctx, String id) {
            CircleImageView circleImageView = mView.findViewById(R.id.profile_image);
            TextView textView = mView.findViewById(R.id.username);
            TextView textView1 = mView.findViewById(R.id.last_msg);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Clients").child(id).exists()){
                        if (!dataSnapshot.child("Clients").child(id).child("ClientImageProfile").getValue().toString().equals("null")){
                            Picasso.with(ctx).load(dataSnapshot.child("Clients").child(id).child("ClientImageProfile").getValue().toString()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.easy_order_splash)
                                    .into(circleImageView, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Picasso.with(ctx).load(dataSnapshot.child("Clients").child(id).child("ClientImageProfile").getValue().toString())
                                                    .placeholder(R.drawable.easy_order_splash).into(circleImageView);
                                        }
                                    });
                        }else{
                            circleImageView.setImageResource(R.drawable.easy_order_splash);
                        }

                        textView.setText(dataSnapshot.child("Clients").child(id).child("ClientName").getValue().toString());
                        lastMessage(id, textView1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


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

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }

}
