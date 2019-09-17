package com.easy.order.UserProfile;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easy.order.MainScreens.PhoneActivateActivity;
import com.easy.order.MainScreens.UpdateAddressActivity;
import com.easy.order.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {

    private CardView mPhoneLayout, mAddressLayout;
    private TextView mPhoneTxt, mPhoneNumber, mAddressTxt, mAddress;
    private Button mUpdatePhone, mUpdateAddress;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("الإعدادات");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            // and this
            onBackPressed();
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mPhoneLayout = findViewById(R.id.phoneNumberLayout);
        mAddressLayout = findViewById(R.id.addressLayout);

        String fontPath = "fonts/andalus.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        mPhoneTxt = findViewById(R.id.phoneNumberTxt);
        mPhoneTxt.setTypeface(tf);
        mPhoneNumber = findViewById(R.id.phoneNumberSetting);
        mAddressTxt = findViewById(R.id.addressTxt);
        mAddressTxt.setTypeface(tf);
        mAddress = findViewById(R.id.addressSetting);


        mUpdateAddress = findViewById(R.id.updateAddress);
        mUpdateAddress.setTypeface(tf);
        mUpdatePhone = findViewById(R.id.updatePhoneNumber);
        mUpdatePhone.setTypeface(tf);

        mUpdatePhone.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, PhoneActivateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        valueEventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("PhoneNumber").exists()){
                    mPhoneLayout.setVisibility(View.GONE);
                }else{
                    mPhoneLayout.setVisibility(View.VISIBLE);
                    mPhoneNumber.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid())
                            .child("PhoneNumber").getValue().toString());
                }

                if (dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Address").exists()){
                    mAddress.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid())
                            .child("Address").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUpdateAddress.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, UpdateAddressActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        valueEventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("PhoneNumber").exists()){
                    mPhoneLayout.setVisibility(View.GONE);
                }else{
                    mPhoneLayout.setVisibility(View.VISIBLE);
                    mPhoneNumber.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid())
                            .child("PhoneNumber").getValue().toString());
                }

                if (dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Address").exists()){
                    mAddress.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid())
                            .child("Address").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        valueEventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("PhoneNumber").exists()){
                    mPhoneLayout.setVisibility(View.GONE);
                }else{
                    mPhoneLayout.setVisibility(View.VISIBLE);
                    mPhoneNumber.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid())
                            .child("PhoneNumber").getValue().toString());
                }

                if (dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Address").exists()){
                    mAddress.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid())
                            .child("Address").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.removeEventListener(valueEventListener);
    }
}
