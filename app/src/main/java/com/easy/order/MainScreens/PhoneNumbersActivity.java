package com.easy.order.MainScreens;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.order.Adapters.PhoneNumberAdapter;
import com.easy.order.FirebaseModels.DataModel;
import com.easy.order.FirebaseModels.PhoneNumberModel;
import com.easy.order.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PhoneNumbersActivity extends AppCompatActivity {

    private String mKey;
    private DatabaseReference mDatabase;
    private RecyclerView mRecycler;

//    private PhoneNumberAdapter phoneNumberAdapter;
//    private List<PhoneNumberModel> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_numbers);
        mKey = getIntent().getExtras().getString("key");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mRecycler = findViewById(R.id.phoneRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);

//        mUsers = new ArrayList<>();
//        mDatabase = FirebaseDatabase.getInstance().getReference("Clients").child(mKey).child("Phone");
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUsers.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    PhoneNumberModel chatlist = snapshot.getValue(PhoneNumberModel.class);
//                    mUsers.add(chatlist);
//                }
//                phoneNumberAdapter = new PhoneNumberAdapter(PhoneNumbersActivity.this, mUsers);
//                mRecycler.setAdapter(phoneNumberAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        showPhoneNumbers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showPhoneNumbers();
    }

    private void showPhoneNumbers(){
        FirebaseRecyclerAdapter<DataModel, PhoneNumbersActivity.BlogViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<DataModel, PhoneNumbersActivity.BlogViewHolder>(
                        DataModel.class,
                        R.layout.row_phone_numbers,
                        PhoneNumbersActivity.BlogViewHolder.class,
                        mDatabase.child("Clients").child(mKey).child("Phone")
                ) {
                    @Override
                    protected void populateViewHolder(final PhoneNumbersActivity.BlogViewHolder viewHolder, final DataModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setData(model.getData());
                        viewHolder.mView.setOnClickListener(view -> {
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getData())));
                        });
                    }
                };
        mRecycler.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        private final static int FADE_DURATION = 100;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            setFadeAnimation(mView);
        }

        public void setData(String data) {
            TextView textView = mView.findViewById(R.id.row_txt);
            textView.setText(data);
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }

}
