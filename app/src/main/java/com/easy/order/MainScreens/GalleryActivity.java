package com.easy.order.MainScreens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.easy.order.FirebaseModels.CategoryModel;
import com.easy.order.FirebaseModels.GalleryModel;
import com.easy.order.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class GalleryActivity extends AppCompatActivity {

    private TextView mTitleTxt;
    private String mKey, mTitle;
    private DatabaseReference mDatabase;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mKey = getIntent().getExtras().getString("key");
        mTitle = getIntent().getExtras().getString("name");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mTitleTxt = findViewById(R.id.titleTxt);
        String fontPath = "fonts/a_massir_ballpoint.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mTitleTxt.setTypeface(tf);
        mTitleTxt.setText(mTitle);
        mRecycler = findViewById(R.id.recycler);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    protected void onStart() {
        super.onStart();
        showGallery();
    }

    private void showGallery(){
        FirebaseRecyclerAdapter<GalleryModel, GalleryActivity.BlogViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<GalleryModel, GalleryActivity.BlogViewHolder>(
                        GalleryModel.class,
                        R.layout.row_gallery,
                        GalleryActivity.BlogViewHolder.class,
                        mDatabase.child("Clients").child(mKey).child("Gallery")
                ) {
                    @Override
                    protected void populateViewHolder(final GalleryActivity.BlogViewHolder viewHolder, final GalleryModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                        viewHolder.mView.setOnClickListener(view -> {
                            Intent intent = new Intent(GalleryActivity.this, ZoomActivity.class);
                            intent.putExtra("key", model.getImage());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
                    }
                };
        mRecycler.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Typeface tf;
        String fontPath;
        private final static int FADE_DURATION = 100;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            fontPath = "fonts/bziba.ttf";
            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
            setFadeAnimation(mView);
        }

        public void setImage(final Context ctx, final String categoryImage) {
            final ImageView imageView = mView.findViewById(R.id.imageGallery);
//            Picasso.with(ctx).load(categoryImage).into(imageView);

            Picasso.with(ctx).load(categoryImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.mipmap.launcher)
                    .into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(categoryImage).placeholder(R.mipmap.launcher).into(imageView);
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
