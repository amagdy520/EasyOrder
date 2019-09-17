package com.easy.order.LoginScreens;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.easy.order.MainScreens.MainActivity;
import com.easy.order.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
//    FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mDatabase;

    LoginButton loginButton;

    CallbackManager callbackManager;

    String deviceToken;
//    private static final String EMAIL = "email";
    private static final String Profile = "public_profile";
//    AccessToken accessToken;

    GoogleSignInClient mGoogleSignInClient;

    SignInButton mSignInButton;

//    ValueEventListener valueEventListener;

    Button mFacebookClick, mGoogleClick;


    private static final long delay = 2000L;
    private boolean mRecentlyBackPressed = false;
    private Handler mExitHandler = new Handler();
    private Runnable mExitRunnable = () -> mRecentlyBackPressed = false;

    CardView mFace, mGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Profile);

        // If you are using in a fragment, call loginButton.setFragment(this);
        mFace = findViewById(R.id.face);
        mGoogle = findViewById(R.id.google);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        AnimationSet animation1 = new AnimationSet(false); //change to false
        animation1.addAnimation(fadeIn);

        AnimationSet animation2 = new AnimationSet(false); //change to false
        animation2.addAnimation(fadeOut);

        mFace.setVisibility(View.VISIBLE);
        mFace.setAnimation(animation1);
        mGoogle.setVisibility(View.VISIBLE);
        mGoogle.setAnimation(animation1);



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "تم إلغاء تسجيل الدخول", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "حدث خطأ أثناء التسجيل", Toast.LENGTH_SHORT).show();
            }
        });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mSignInButton = findViewById(R.id.google_button);
        mSignInButton.setOnClickListener(view -> signIn());


        mFacebookClick = findViewById(R.id.facebook_click);
        mFacebookClick.setOnClickListener(view -> loginButton.callOnClick());

        mGoogleClick = findViewById(R.id.google_click);
        mGoogleClick.setOnClickListener(view -> signIn());

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void goToMain(){
//        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (!dataSnapshot.exists()) {
//                            DatabaseReference databaseReference = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
//                            databaseReference.child("Name").setValue(mAuth.getCurrentUser().getDisplayName());
//                            databaseReference.child("ProfilePicture").setValue(mAuth.getCurrentUser().getPhotoUrl());
//                            databaseReference.child("UserId").setValue(mAuth.getCurrentUser().getUid());
//                            if (!mAuth.getCurrentUser().getEmail().isEmpty()){
//                                databaseReference.child("Email").setValue(mAuth.getCurrentUser().getEmail());
//                            }else{
//                                databaseReference.child("Email").setValue(mAuth.getCurrentUser().getDisplayName().toLowerCase().replaceAll("\\s+","")+"@facebook.com");
//                            }
//                            new Handler().postDelayed(() -> {
//                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                finish();
//                            }, 2000);
//                        }else{
//                            if (!dataSnapshot.child("UserId").exists() || mAuth.getCurrentUser().getEmail().isEmpty()) {
//                                DatabaseReference databaseReference = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
//                                databaseReference.child("UserId").setValue(mAuth.getCurrentUser().getUid());
//                                if (!mAuth.getCurrentUser().getEmail().isEmpty()){
//                                    databaseReference.child("Email").setValue(mAuth.getCurrentUser().getEmail());
//                                }else{
//                                    databaseReference.child("Email").setValue(mAuth.getCurrentUser().getDisplayName().toLowerCase().replaceAll("\\s+","")+"@facebook.com");
//                                }
//                                new Handler().postDelayed(() -> {
//                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                    finish();
//                                }, 2000);
//                            }else{
//                                new Handler().postDelayed(() -> {
//                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                    finish();
//                                }, 2000);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "فشل تسجيل الدخول", Toast.LENGTH_SHORT).show();
                    } else {
                        new Handler().postDelayed(() -> mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).exists()){
                                    goToMain();
                                }else{
                                    DatabaseReference databaseReference = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
                                    databaseReference.child("Name").setValue(mAuth.getCurrentUser().getDisplayName());
                                    databaseReference.child("ProfilePicture").setValue(mAuth.getCurrentUser().getPhotoUrl());
                                    databaseReference.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                                    if (!mAuth.getCurrentUser().getEmail().isEmpty()){
                                        databaseReference.child("Email").setValue(mAuth.getCurrentUser().getEmail());
                                    }else{
                                        databaseReference.child("Email").setValue(mAuth.getCurrentUser().getDisplayName().toLowerCase()
                                                .replaceAll("\\s+","")+"@facebook.com");
                                    }
                                    goToMain();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }),100);
                    }
                });


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "فشل تسجيل الدخول", Toast.LENGTH_SHORT).show();
                    } else {
                        new Handler().postDelayed(() -> mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).exists()){
                                    goToMain();
                                }else{
                                    DatabaseReference databaseReference = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
                                    databaseReference.child("Name").setValue(mAuth.getCurrentUser().getDisplayName());
                                    databaseReference.child("ProfilePicture").setValue(mAuth.getCurrentUser().getPhotoUrl());
                                    databaseReference.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                                    if (!mAuth.getCurrentUser().getEmail().isEmpty()){
                                        databaseReference.child("Email").setValue(mAuth.getCurrentUser().getEmail());
                                    }else{
                                        databaseReference.child("Email").setValue(mAuth.getCurrentUser().getDisplayName().toLowerCase()
                                                .replaceAll("\\s+","")+"@google.com");
                                    }
                                    goToMain();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }),100);
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        } else {
            callbackManager.onActivityResult(requestCode,
                    resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mDatabase.removeEventListener(valueEventListener);
    }

    //    public static void printHashKey(Context context) {
//        try {
//            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
//            for (android.content.pm.Signature signature : info.signatures) {
//                final MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                final String hashKey = new String(Base64.encode(md.digest(), 0));
//                Log.i("AppLog", "key:" + hashKey + "=");
//            }
//        } catch (Exception e) {
//            Log.e("AppLog", "error:", e);
//        }
//    }



    @Override
    public void onBackPressed() {
        //You may also add condition if (doubleBackToExitPressedOnce || fragmentManager.getBackStackEntryCount() != 0) // in case of Fragment-based add
        if (mRecentlyBackPressed) {
            mExitHandler.removeCallbacks(mExitRunnable);
            mExitHandler = null;
            super.onBackPressed();
        } else {
            mRecentlyBackPressed = true;
            Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();
            mExitHandler.postDelayed(mExitRunnable, delay);
        }
    }
}
