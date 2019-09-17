package com.easy.order.MainScreens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.easy.order.FirebaseModels.CategoryModel;
import com.easy.order.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class OtherCategoryActivity extends AppCompatActivity {

    private TextView mTitleTxt;
    private String mKey, mTitle;
    private DatabaseReference mDatabase;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_category);
        mKey = getIntent().getExtras().getString("key");
        mTitle = getIntent().getExtras().getString("category");
//        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mTitleTxt = findViewById(R.id.titleTxt);
        String fontPath = "fonts/andalus.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mTitleTxt.setTypeface(tf);
        mTitleTxt.setText(mTitle);
        mRecycler = findViewById(R.id.recyclerCategory);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        showOtherCategory();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showOtherCategory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showOtherCategory();
    }

    private void showOtherCategory() {
        FirebaseRecyclerAdapter<CategoryModel, OtherCategoryActivity.BlogViewHolder2> firebaseRecyclerAdapter2 = new
                FirebaseRecyclerAdapter<CategoryModel, OtherCategoryActivity.BlogViewHolder2>(
                        CategoryModel.class,
                        R.layout.row_category,
                        OtherCategoryActivity.BlogViewHolder2.class,
                        mDatabase.child("Category").child(mKey).child("OtherCategory")
                ) {
                    @Override
                    protected void populateViewHolder(final OtherCategoryActivity.BlogViewHolder2 viewHolder, final CategoryModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setCategoryImage(OtherCategoryActivity.this, model.getCategoryImage());
                        viewHolder.setCategoryName(model.getCategoryName());
                        viewHolder.mView.setOnClickListener(view -> mDatabase.child("Category").child(mKey)
                                .child("OtherCategory").child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("OtherCategory").exists()) {
                                    Intent intent = new Intent(OtherCategoryActivity.this, OtherCategoryActivity.class);
                                    intent.putExtra("key", post_key);
                                    intent.putExtra("category", model.getCategoryName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(OtherCategoryActivity.this, ClientsActivity.class);
                                    intent.putExtra("key", post_key);
                                    intent.putExtra("category", model.getCategoryName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }));
                    }
                };
        firebaseRecyclerAdapter2.startListening();
        mRecycler.setAdapter(firebaseRecyclerAdapter2);
    }

    public static class BlogViewHolder2 extends RecyclerView.ViewHolder {

        View mView;
        Typeface tf;
        String fontPath;
        private final static int FADE_DURATION = 100;

        private DatabaseReference mDatabase;

        TextView mIcons8Txt;
        ImageView mIcons8;

        public BlogViewHolder2(View itemView) {
            super(itemView);
            mView = itemView;
            fontPath = "fonts/andalus.ttf";
            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
            setFadeAnimation(mView);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);

            mIcons8Txt = mView.findViewById(R.id.icons8_number);
            mIcons8 = mView.findViewById(R.id.icons8);
        }

        public void setCategoryImage(final Context ctx, final String categoryImage) {
            final ImageView imageView = mView.findViewById(R.id.row_image);
//            Picasso.with(ctx).load(categoryImage).into(imageView);
            Picasso.with(ctx).load(categoryImage)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.easy_order_splash).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(categoryImage).placeholder(R.drawable.easy_order_splash).into(imageView);
                }
            });
        }

        public void setCategoryName(String categoryName) {
            TextView textView = mView.findViewById(R.id.row_txt);
            textView.setTypeface(tf);
            textView.setText(categoryName);

            mDatabase.child("Category").child(categoryName.replaceAll("\\s", ""))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child("OtherCategory").exists()) {
                                archive(categoryName);
                            } else {
                                account(categoryName);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        private void archive(String c) {
            mDatabase.child("Category").child(c.replaceAll("\\s", "")).child("OtherCategory").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mIcons8.setImageResource(R.drawable.icons8_archive);
                    mIcons8Txt.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void account(String c) {
            mDatabase.child("Clients").orderByChild("ClientCategory")
                    .equalTo(c).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mIcons8.setImageResource(R.drawable.icons8_account);
                    mIcons8Txt.setText(String.valueOf(dataSnapshot.getChildrenCount()));
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
