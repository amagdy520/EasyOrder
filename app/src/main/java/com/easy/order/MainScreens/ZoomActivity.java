package com.easy.order.MainScreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.easy.order.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ZoomActivity extends AppCompatActivity {

    private String mKey;
    private ZoomageView zoomageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        mKey = getIntent().getExtras().getString("key");
        zoomageView = findViewById(R.id.menuImage);

        if (mKey.equals("null")){
            zoomageView.setImageResource(R.drawable.easy_order_splash);
        }else{
            Picasso.with(this).load(mKey).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.easy_order_splash)
                    .into(zoomageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ZoomActivity.this).load(mKey).placeholder(R.drawable.easy_order_splash).into(zoomageView);
                        }
                    });
        }

    }
}
