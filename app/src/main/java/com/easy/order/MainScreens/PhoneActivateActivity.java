package com.easy.order.MainScreens;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.easy.order.R;
import com.easy.order.SplashActivity;
import com.easy.order.UserProfile.ProfileActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneActivateActivity extends AppCompatActivity {

    private EditText mPhoneNumber;
    private Button mActivate;

    private static final String TAG = "PhoneAuth";

//    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
//    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth mAuth;


    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_activate);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mActivate = findViewById(R.id.activateButton);

        mActivate.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PhoneActivateActivity.this);
            builder.setTitle("هل هذا هو رقم هاتفك!؟");
            builder.setMessage("+2"+mPhoneNumber.getText().toString().replaceAll("\\s",""));
            // add a button
            builder.setPositiveButton("نعم", (dialog, which) -> {
                sendCode();
                dialog.cancel();
            });

            builder.setNegativeButton("لأ", (dialogInterface, i) -> dialogInterface.cancel());
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    public void sendCode() {

        String phoneNumber = "+2"+mPhoneNumber.getText().toString().replaceAll("\\s","");

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);


    }

    private void setUpVerificatonCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {

                        DatabaseReference databaseReference = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).getRef();
                        databaseReference.child("PhoneNumber").setValue("+2"+mPhoneNumber.getText().toString().replaceAll("\\s",""));

                        PhoneActivateActivity.this.finish();

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Log.d(TAG, "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.d(TAG, "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

//                        phoneVerificationId = verificationId;
//                        resendToken = token;
                        Intent intent = new Intent(PhoneActivateActivity.this, CodeCheckActivity.class);
                        intent.putExtra("code", verificationId);
                        intent.putExtra("number", "+2"+mPhoneNumber.getText().toString().replaceAll("\\s",""));
                        startActivity(intent);
                        PhoneActivateActivity.this.finish();
                    }
                };
    }

//    public void verifyCode() {
//
//        String code = mCode.getText().toString();
//
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
//        signInWithPhoneAuthCredential(credential);
//    }
//
//    public void resendCode() {
//
//        String phoneNumber = mPhoneNumber.getText().toString();
//
//        setUpVerificatonCallbacks();
//
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,
//                60,
//                TimeUnit.SECONDS,
//                this,
//                verificationCallbacks,
//                resendToken);
//    }

}
