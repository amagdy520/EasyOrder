package com.easy.order.MainScreens;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.easy.order.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateAddressActivity extends AppCompatActivity {

    private EditText mAddressEdit;
    private Button mAddAddress;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mAddressEdit = findViewById(R.id.addressTxt);
        mAddAddress = findViewById(R.id.addAddressButton);

        valueEventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Address").exists()){
                    mAddressEdit.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid())
                            .child("Address").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAddAddress.setOnClickListener(view -> {
            if (mAddressEdit.getText().toString().isEmpty()){
                mAddressEdit.setError("برجاء إضافة عنوان");
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAddressActivity.this);
                builder.setTitle("هل هذا هو عنوانك!؟");
                builder.setMessage(mAddressEdit.getText().toString());
                // add a button
                builder.setPositiveButton("نعم", (dialog, which) -> {
                    DatabaseReference databaseReference = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
                    databaseReference.child("Address").setValue(mAddressEdit.getText().toString());
                    dialog.cancel();
                    UpdateAddressActivity.this.finish();
                });

                builder.setNegativeButton("لأ", (dialogInterface, i) -> dialogInterface.cancel());
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        valueEventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Address").exists()){
                    mAddressEdit.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid())
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
                if (dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Address").exists()){
                    mAddressEdit.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid())
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
