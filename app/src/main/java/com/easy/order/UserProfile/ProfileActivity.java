package com.easy.order.UserProfile;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.order.Adapters.UserAdapter;
import com.easy.order.FirebaseModels.ChatlistModel;
import com.easy.order.FirebaseModels.ClientIDModel;
import com.easy.order.FirebaseModels.ClientsModel;
import com.easy.order.FirebaseModels.GalleryModel;
import com.easy.order.MainScreens.ChatListActivity;
import com.easy.order.MainScreens.ClientProfileActivity;
import com.easy.order.MainScreens.FavouriteActivity;
import com.easy.order.MainScreens.MainActivity;
import com.easy.order.MainScreens.PhoneActivateActivity;
import com.easy.order.MainScreens.ZoomActivity;
import com.easy.order.R;
import com.easy.order.SplashActivity;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CircleImageView mProfile;
    private TextView mUsername, mPhoneNumber;
    private Button mActivate;
    private ImageView mLogout, mSetting;
//    private boolean mChatListExist;
    private RecyclerView mRecyclerFavourite;
    private RelativeLayout mFaveLayout;

    ValueEventListener valueEventListener;
    private String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                mAddress = mLocation(location.getLatitude(), location.getLongitude());
                // Toast.makeText(this, mAddress, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Gps is turned off", Toast.LENGTH_SHORT).show();
            }
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mProfile = findViewById(R.id.profile);
        mUsername = findViewById(R.id.username);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mActivate = findViewById(R.id.phoneActivate);
        mLogout = findViewById(R.id.outProfile);
//        mChatList = findViewById(R.id.chatList);

        mFaveLayout = findViewById(R.id.favLayout);

        mSetting = findViewById(R.id.setting);
        mSetting.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        mRecyclerFavourite = findViewById(R.id.recyclerFav);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerFavourite.setLayoutManager(linearLayoutManager);
        mRecyclerFavourite.setHasFixedSize(true);

        valueEventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mChatListExist = dataSnapshot.child("Chatlist").child(mAuth.getCurrentUser().getUid()).exists();
//                if (!mChatListExist){
//                    mChatList.setVisibility(View.GONE);
//                }else{
//                    mChatList.setVisibility(View.VISIBLE);
//                }

                if (!dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("PhoneNumber").exists()){
                    mActivate.setVisibility(View.VISIBLE);
                    mPhoneNumber.setVisibility(View.GONE);
                }else{
                    mActivate.setVisibility(View.GONE);
                    mPhoneNumber.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("PhoneNumber").getValue().toString());
                    mPhoneNumber.setVisibility(View.VISIBLE);
                }

                if (!dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Favourite").exists()){
                    mFaveLayout.setVisibility(View.GONE);
                }else{
                    mFaveLayout.setVisibility(View.VISIBLE);
                    showClients();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (mAddress != null){
            DatabaseReference databaseReference = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReference.child("Address").setValue(mAddress);
        }


        mUsername.setText(mAuth.getCurrentUser().getDisplayName());
        Picasso.with(this).load(mAuth.getCurrentUser().getPhotoUrl()+"?height=500").networkPolicy(NetworkPolicy.OFFLINE)
                .resize(500, 500)
                .error(R.drawable.easy_order_splash)
                .placeholder(R.drawable.easy_order_splash).into(mProfile, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(ProfileActivity.this).load(mAuth.getCurrentUser().getPhotoUrl()+"?height=500")
                        .resize(500, 500)
                        .error(R.drawable.easy_order_splash)
                        .placeholder(R.drawable.easy_order_splash).into(mProfile);
            }
        });

//        mChatList.setOnClickListener(view -> {
//            Intent intent = new Intent(ProfileActivity.this, ChatListActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        });

        mLogout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setMessage("Logout!?");
            // add a button
            builder.setPositiveButton("Yes", (dialog, which) -> {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, SplashActivity.class));
                finish();
                dialog.cancel();
            });

            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        mActivate.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, PhoneActivateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

//        mRecyclerSponsor = findViewById(R.id.sponsors);
//        mRecyclerSponsor.setLayoutManager(new GridLayoutManager(this, 4));
//        mRecyclerSponsor.setHasFixedSize(true);
//        showSponsors();


    }

    @Override
    protected void onStart() {
        super.onStart();
//        showSponsors();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        showSponsors();
        valueEventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mChatListExist = dataSnapshot.child("Chatlist").child(mAuth.getCurrentUser().getUid()).exists();
//                if (!mChatListExist){
//                    mChatList.setVisibility(View.GONE);
//                }else{
//                    mChatList.setVisibility(View.VISIBLE);
//                }

                if (!dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("PhoneNumber").exists()){
                    mActivate.setVisibility(View.VISIBLE);
                    mPhoneNumber.setVisibility(View.GONE);
                }else{
                    mActivate.setVisibility(View.GONE);
                    mPhoneNumber.setText(dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("PhoneNumber").getValue().toString());
                    mPhoneNumber.setVisibility(View.VISIBLE);
                }

                if (!dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Favourite").exists()){
                    mFaveLayout.setVisibility(View.GONE);
                }else{
                    mFaveLayout.setVisibility(View.VISIBLE);
                    showClients();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showClients(){
        FirebaseRecyclerAdapter<ClientIDModel, ProfileActivity.BlogViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ClientIDModel, ProfileActivity.BlogViewHolder>(
                        ClientIDModel.class,
                        R.layout.row_clients2,
                        ProfileActivity.BlogViewHolder.class,
                        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Favourite")
                ) {
                    @Override
                    protected void populateViewHolder(final ProfileActivity.BlogViewHolder viewHolder, final ClientIDModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setClientID(ProfileActivity.this, model.getClientID());
                        viewHolder.mLike.setOnClickListener(view -> viewHolder.likeFunction(post_key));
                        viewHolder.mView.setOnClickListener(view -> viewHolder.itemClick(ProfileActivity.this, post_key));
                        viewHolder.checkLike(post_key);
                    }
                };
        mRecyclerFavourite.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Typeface tf;
        String fontPath;
        private final static int FADE_DURATION = 500;
        private DatabaseReference mDatabase;
        private String count;
        ImageView mLike;

        private FirebaseAuth mAuth;

        private String mUserId;


        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            fontPath = "fonts/andalus.ttf";
            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
            setFadeAnimation(mView);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);

            mUserId = mAuth.getCurrentUser().getUid();

            mLike = mView.findViewById(R.id.likeClient);
        }

        public void setClientID(Context ctx, String clientID){
            mDatabase.child("Clients").child(clientID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    setClientImageCover(ctx, dataSnapshot.child("ClientImageCover").getValue().toString());
                    setClientName(dataSnapshot.child("ClientName").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void itemClick(Context ctx, String client){
            mDatabase.child("Clients").child(client).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("ClientAvailability").getValue().toString().equals("yes")){
                        setClientVisitor(client);
                        Intent intent = new Intent(ctx, ClientProfileActivity.class);
                        intent.putExtra("key", client);
                        intent.putExtra("name", dataSnapshot.child("ClientName").getValue().toString());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ctx.startActivity(intent);
                    }else{
                        Toast.makeText(ctx, "هذا العميل غير متاح الأن", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void setClientImageCover(final Context ctx, final String categoryImage) {
            final ImageView imageView = mView.findViewById(R.id.row_image);
//            Picasso.with(ctx).load(categoryImage).into(imageView);
            Picasso.with(ctx).load(categoryImage).networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(600, 300)
                    .error(R.drawable.easy_order_splash)
                    .placeholder(R.drawable.easy_order_splash).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(categoryImage)
                            .resize(600, 300)
                            .error(R.drawable.easy_order_splash)
                            .placeholder(R.drawable.easy_order_splash).into(imageView);
                }
            });
        }

        private void setClientName(String categoryName) {
            TextView textView = mView.findViewById(R.id.row_txt);
            textView.setTypeface(tf);
            textView.setText(categoryName);
        }

        private void setClientVisitor(String client) {
            mDatabase.child("Clients").child(client).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count = dataSnapshot.child("ClientVisitor").getValue().toString();
                    DatabaseReference databaseReference = dataSnapshot.getRef();
                    databaseReference.child("ClientVisitor").setValue(String.valueOf(Integer.parseInt(count)+1));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }

        public void checkLike(String mClient){
            mDatabase.child("Users").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Favourite").child(mClient).exists()){
                        mLike.setImageResource(R.drawable.icons8_hearts_red);
                    }else{
                        mLike.setImageResource(R.drawable.icons8_hearts_white);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void likeFunction(String mClient){
            mDatabase.child("Users").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Favourite").child(mClient).exists()){
                        mLike.setImageResource(R.drawable.icons8_hearts_white);
                        mDatabase.child("Users").child(mUserId).child("Favourite").child(mClient).removeValue();
                    }else{
                        mLike.setImageResource(R.drawable.icons8_hearts_red);
                        DatabaseReference databaseReference = mDatabase.child("Users").child(mUserId).child("Favourite").child(mClient);
                        databaseReference.child("ClientID").setValue(mClient);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

//    private void showSponsors(){
//        FirebaseRecyclerAdapter<GalleryModel, ProfileActivity.BlogViewHolderSponsors> firebaseRecyclerAdapter = new
//                FirebaseRecyclerAdapter<GalleryModel, ProfileActivity.BlogViewHolderSponsors>(
//                        GalleryModel.class,
//                        R.layout.row_sponsor,
//                        ProfileActivity.BlogViewHolderSponsors.class,
//                        mDatabase.child("Sponsors")
//                ) {
//                    @Override
//                    protected void populateViewHolder(final ProfileActivity.BlogViewHolderSponsors viewHolder, final GalleryModel model, final int position) {
//                        final String post_key = getRef(position).getKey();
//                        viewHolder.setImage(getApplicationContext(), model.getImage());
//                        viewHolder.mView.setOnClickListener(view -> {
//                            Intent intent = new Intent(ProfileActivity.this, ZoomActivity.class);
//                            intent.putExtra("key", model.getImage());
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                        });
//                    }
//                };
//        mRecyclerSponsor.setAdapter(firebaseRecyclerAdapter);
//    }

//    public static class BlogViewHolderSponsors extends RecyclerView.ViewHolder {
//        View mView;
//        Typeface tf;
//        String fontPath;
//        private final static int FADE_DURATION = 100;
//
//        public BlogViewHolderSponsors(View itemView) {
//            super(itemView);
//            mView = itemView;
//            fontPath = "fonts/bziba.ttf";
//            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
//            setFadeAnimation(mView);
//        }
//
//        public void setImage(final Context ctx, final String categoryImage) {
//            final ImageView imageView = mView.findViewById(R.id.imageGallery);
////            Picasso.with(ctx).load(categoryImage).into(imageView);
//
//            Picasso.with(ctx).load(categoryImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.easy_order_splash)
//                    .into(imageView, new Callback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onError() {
//                            Picasso.with(ctx).load(categoryImage).placeholder(R.drawable.easy_order_splash).into(imageView);
//                        }
//                    });
//        }
//
//        private void setFadeAnimation(View view) {
//            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
//            anim.setDuration(FADE_DURATION);
//            view.startAnimation(anim);
//        }
//    }


    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.removeEventListener(valueEventListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        mAddress = mLocation(location.getLatitude(), location.getLongitude());
                        //Toast.makeText(this, city, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(this, "Gps is turned off", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permissions not generated", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private String mLocation(double lat, double lon){
        String cityName = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat, lon, 10);
            if(addresses.size() > 0){
                for(Address adr: addresses){
                    if(adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getAddressLine(0);
                        break;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return cityName;
    }
}
