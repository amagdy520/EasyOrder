package com.easy.order.MainScreens;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.easy.order.FirebaseModels.ClientCategoryModel;
import com.easy.order.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DialogFilter extends AppCompatActivity {

    RecyclerView mRecyclerCategory;
    DatabaseReference mDatabase;
    String mKey;

    ClientProfileActivity clientProfileActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter);
        mKey = getIntent().getExtras().getString("key");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mRecyclerCategory = findViewById(R.id.listCategory);
        mRecyclerCategory.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerCategory.setHasFixedSize(true);

        clientProfileActivity = new ClientProfileActivity();
        showCategory();
    }

    public void showCategory() {
        FirebaseRecyclerAdapter<ClientCategoryModel, DialogFilter.BlogViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ClientCategoryModel, DialogFilter.BlogViewHolder>(
                        ClientCategoryModel.class,
                        R.layout.row_category_menu,
                        DialogFilter.BlogViewHolder.class,
                        mDatabase.child("Clients").child(mKey).child("Category")
                ) {
                    @Override
                    protected void populateViewHolder(final DialogFilter.BlogViewHolder viewHolder, final ClientCategoryModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setCategoryName(model.getCategoryName());
                        viewHolder.mView.setOnClickListener(view -> clientProfileActivity.showMenuCategory(model.getCategoryName()));
                    }
                };
        mRecyclerCategory.setAdapter(firebaseRecyclerAdapter);
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

//        public void setCategoryImage(final Context ctx, final String categoryImage) {
//            final ImageView imageView = mView.findViewById(R.id.row_image);
////            Picasso.with(ctx).load(categoryImage).into(imageView);
//
//            Picasso.with(ctx).load(categoryImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.mipmap.launcher).into(imageView, new Callback() {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onError() {
//                    Picasso.with(ctx).load(categoryImage).placeholder(R.mipmap.launcher).into(imageView);
//                }
//            });
//        }

        public void setCategoryName(String categoryName) {
            TextView textView = mView.findViewById(R.id.txt_menu_category);
            textView.setTypeface(tf);
            textView.setText(categoryName);
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }
}

