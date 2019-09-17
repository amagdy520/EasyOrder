package com.easy.order.MainScreens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easy.order.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CodeCheckActivity extends AppCompatActivity {

    private EditText mCode;
    private Button mActivate;
    private TextView mHintTxt;
    private String mCodeTxt, mNumber;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_check);
        mNumber = getIntent().getExtras().getString("number");
        mCodeTxt = getIntent().getExtras().getString("code");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mCode = findViewById(R.id.codeActivate);
        mActivate = findViewById(R.id.activateButton);
        mHintTxt = findViewById(R.id.hintTxt);
        mHintTxt.setText("برجاء إدخال الكود الذي تم إرساله إلي الرقم"+ mNumber);

        mActivate.setOnClickListener(view -> verifyCode());


    }

    public void verifyCode() {

        String code = mCode.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mCodeTxt, code);

        DatabaseReference databaseReference = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).getRef();
        databaseReference.child("PhoneNumber").setValue(mNumber);

        CodeCheckActivity.this.finish();
    }
}
