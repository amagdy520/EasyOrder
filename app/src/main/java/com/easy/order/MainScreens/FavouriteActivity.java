package com.easy.order.MainScreens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.order.FirebaseModels.ClientIDModel;
import com.easy.order.FirebaseModels.ClientsModel;
import com.easy.order.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class FavouriteActivity extends AppCompatActivity {

    private TextView mTitleTxt;
    private DatabaseReference mDatabase;
    private RecyclerView mRecycler;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mTitleTxt = findViewById(R.id.titleTxt);
        String fontPath = "fonts/a_massir_ballpoint.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mTitleTxt.setTypeface(tf);
        mRecycler = findViewById(R.id.recyclerClientsFavourite);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mRecycler.setHasFixedSize(true);
        showClients();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showClients();
    }

    private void showClients(){
        FirebaseRecyclerAdapter<ClientIDModel, FavouriteActivity.BlogViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ClientIDModel, FavouriteActivity.BlogViewHolder>(
                        ClientIDModel.class,
                        R.layout.row_clients,
                        FavouriteActivity.BlogViewHolder.class,
                        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Favourite")
                ) {
                    @Override
                    protected void populateViewHolder(final FavouriteActivity.BlogViewHolder viewHolder, final ClientIDModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setClientID(FavouriteActivity.this, model.getClientID());
                        viewHolder.mLike.setOnClickListener(view -> viewHolder.likeFunction(post_key));
                        viewHolder.mView.setOnClickListener(view -> viewHolder.itemClick(FavouriteActivity.this, post_key));
                        viewHolder.checkLike(post_key);
                    }
                };
        mRecycler.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Typeface tf;
        String fontPath;
        private final static int FADE_DURATION = 500;
        private DatabaseReference mDatabase;
        private String count;
        ImageView mLike;

        private FirebaseAuth mAuth;

        private String mUserId;


        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            fontPath = "fonts/a_massir_ballpoint.ttf";
            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
            setFadeAnimation(mView);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);

            mUserId = mAuth.getCurrentUser().getUid();

            mLike = mView.findViewById(R.id.likeClient);
        }

        public void setClientID(Context ctx, String clientID){
            mDatabase.child("Clients").child(clientID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    setClientImageCover(ctx, dataSnapshot.child("ClientImageCover").getValue().toString());
                    setClientName(dataSnapshot.child("ClientName").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void itemClick(Context ctx, String client){
            mDatabase.child("Clients").child(client).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("ClientAvailability").getValue().toString().equals("yes")){
                        setClientVisitor(client);
                        Intent intent = new Intent(ctx, ClientProfileActivity.class);
                        intent.putExtra("key", client);
                        intent.putExtra("name", dataSnapshot.child("ClientName").getValue().toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ctx.startActivity(intent);
                    }else{
                        Toast.makeText(ctx, "هذا العميل غير متاح الأن", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setClientImageCover(final Context ctx, final String categoryImage) {
            final ImageView imageView = mView.findViewById(R.id.row_image);
//            Picasso.with(ctx).load(categoryImage).into(imageView);
            Picasso.with(ctx).load(categoryImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.mipmap.launcher).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(categoryImage).placeholder(R.mipmap.launcher).into(imageView);
                }
            });
        }

        public void setClientName(String categoryName) {
            TextView textView = mView.findViewById(R.id.row_txt);
            textView.setTypeface(tf);
            textView.setText(categoryName);
        }

        public void setClientVisitor(String client) {
            mDatabase.child("Clients").child(client).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count = dataSnapshot.child("ClientVisitor").getValue().toString();
                    DatabaseReference databaseReference = dataSnapshot.getRef();
                    databaseReference.child("ClientVisitor").setValue(String.valueOf(Integer.parseInt(count)+1));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }

        public void checkLike(String mClient){
            mDatabase.child("Users").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Favourite").child(mClient).exists()){
                        mLike.setImageResource(R.drawable.icons8_hearts_red);
                    }else{
                        mLike.setImageResource(R.drawable.icons8_hearts_white);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        public void likeFunction(String mClient){
            mDatabase.child("Users").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Favourite").child(mClient).exists()){
                        mLike.setImageResource(R.drawable.icons8_hearts_white);
                        mDatabase.child("Users").child(mUserId).child("Favourite").child(mClient).removeValue();
                    }else{
                        mLike.setImageResource(R.drawable.icons8_hearts_red);
                        DatabaseReference databaseReference = mDatabase.child("Users").child(mUserId).child("Favourite").child(mClient);
                        databaseReference.child("ClientID").setValue(mClient);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
