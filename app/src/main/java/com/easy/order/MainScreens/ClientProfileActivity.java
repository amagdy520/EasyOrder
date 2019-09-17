package com.easy.order.MainScreens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.easy.order.Adapters.PhoneNumberAdapter;
import com.easy.order.Adapters.ProfileMenuCategoryAdapter;
import com.easy.order.FirebaseModels.AddressModel;
import com.easy.order.FirebaseModels.CategoryModel;
import com.easy.order.FirebaseModels.ChatModel;
import com.easy.order.FirebaseModels.ClientCategoryModel;
import com.easy.order.FirebaseModels.DataModel;
import com.easy.order.FirebaseModels.DeliveryPlacesModel;
import com.easy.order.FirebaseModels.GalleryModel;
import com.easy.order.FirebaseModels.MenuCategoryModel;
import com.easy.order.FirebaseModels.MenuModel;
import com.easy.order.FirebaseModels.PhoneNumberModel;
import com.easy.order.Notifications.Token;
import com.easy.order.R;
import com.easy.order.SplashActivity;
import com.easy.order.UserProfile.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientProfileActivity extends AppCompatActivity {

    private TextView mTitleTxt, mAboutTxt;
    private String mKey, mTitle, mUserID, mLatitude, mLongitude;
    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerCategory, mRecyclerDeliveryPlaces, mRecycler;
    private Spinner mAddressSpinner;
    private ImageView mPhone, mAddress, mCoverPic, mFavourite, mGallery, mLocation;
    private CircleImageView mProfileImage;
    //    private FloatingActionButton mMessage;
    private FirebaseAuth mAuth;

    ValueEventListener seenListener;

    private Button  mMessage;

//    private ProfileMenuCategoryAdapter mPMCA;
//    private List<MenuCategoryModel> mUsers;

    private String profileLink = "null", coverLink = "null";
    int countBlock;

    RelativeLayout mDeliveryContent, mCategoryContent;

    private boolean check = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_profile);
        mKey = getIntent().getExtras().getString("key");
        mTitle = getIntent().getExtras().getString("name");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mUserID = mAuth.getCurrentUser().getUid();
        mTitleTxt = findViewById(R.id.titleTxt);
//        String fontPath = "fonts/a_massir_ballpoint.ttf";
//        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
//        String fontPath2 = "fonts/bziba.ttf";
//        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
//        mTitleTxt.setTypeface(tf);
        mTitleTxt.setText(mTitle);
//        mVisitorNum = findViewById(R.id.visitorNum);
        mAboutTxt = findViewById(R.id.aboutClient);
//        mAboutTxt.setTypeface(tf2);
        mAddressSpinner = findViewById(R.id.addressSpinner);
        mPhone = findViewById(R.id.call);
        mMessage = findViewById(R.id.message);
        mProfileImage = findViewById(R.id.profilePic);
        mCoverPic = findViewById(R.id.CoverPic);

        mAddress = findViewById(R.id.address);

        mLocation = findViewById(R.id.location);

//        mGallery = findViewById(R.id.gallery);

//        mCategorySpinner = findViewById(R.id.categoryMenu);

        mDeliveryContent = findViewById(R.id.deliveryContainer);
        mCategoryContent = findViewById(R.id.categoryContainer);

        mRecycler = findViewById(R.id.recycler);


        mRecyclerDeliveryPlaces = findViewById(R.id.recyclerDelivery);
        mRecyclerDeliveryPlaces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerDeliveryPlaces.setHasFixedSize(true);

        mGallery = findViewById(R.id.gallery);


        seenListener = mDatabase.child("Clients").child(mKey).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> nomeConsulta = new ArrayList<>();
                for (DataSnapshot areaSnapshot : dataSnapshot.child("Address").getChildren()) {
                    AddressModel consultaName = areaSnapshot.getValue(AddressModel.class);
                    nomeConsulta.add(consultaName.getData());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ClientProfileActivity.this,
                        R.layout.row_spinner, nomeConsulta);
                arrayAdapter.setDropDownViewResource(R.layout.row_spinner);
                mAddressSpinner.setAdapter(arrayAdapter);

//                final List<String> nomeConsulta2 = new ArrayList<>();
//                for (DataSnapshot areaSnapshot : dataSnapshot.child("Category").getChildren()) {
//                    ClientCategoryModel consultaName2 = areaSnapshot.getValue(ClientCategoryModel.class);
//                    nomeConsulta2.add(consultaName2.getCategoryName());
//                }
//                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(ClientProfileActivity.this,
//                        android.R.layout.simple_spinner_item, nomeConsulta2);
//                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mCategorySpinner.setAdapter(arrayAdapter2);

                coverLink = dataSnapshot.child("ClientImageCover").getValue().toString();
                Picasso.with(ClientProfileActivity.this)
                        .load(coverLink)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(600, 300)
                        .error(R.drawable.easy_order_splash)
                        .placeholder(R.drawable.easy_order_splash)
                        .into(mCoverPic, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ClientProfileActivity.this)
                                .load(coverLink)
                                .resize(600, 300)
                                .error(R.drawable.easy_order_splash)
                                .placeholder(R.drawable.easy_order_splash).into(mCoverPic);
                    }
                });

                if (!dataSnapshot.child("ClientImageProfile").getValue().toString().equals("null")) {

                    profileLink = dataSnapshot.child("ClientImageProfile").getValue().toString();

                    Picasso.with(ClientProfileActivity.this)
                            .load(profileLink)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .resize(500, 500)
                            .error(R.drawable.easy_order_splash)
                            .placeholder(R.drawable.easy_order_splash).into(mProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ClientProfileActivity.this)
                                    .load(profileLink)
                                    .resize(500, 500)
                                    .error(R.drawable.easy_order_splash)
                                    .placeholder(R.drawable.easy_order_splash).into(mProfileImage);
                        }
                    });
                } else {
                    mProfileImage.setImageResource(R.drawable.easy_order_splash);
                }


                if (dataSnapshot.child("ClientWord").exists()) {
                    mAboutTxt.setText(dataSnapshot.child("ClientWord").getValue().toString());
                } else {
                    mAboutTxt.setVisibility(View.GONE);
                }

                if (dataSnapshot.child("Address").exists()) {
                    mAddress.setVisibility(View.VISIBLE);
                } else {
                    mAddress.setVisibility(View.GONE);
                }

                if (dataSnapshot.child("PhoneAvailability").getValue().toString().equals("no")) {
                    mPhone.setVisibility(View.GONE);
                } else {
                    if (dataSnapshot.child("Phone").exists()){
                        mPhone.setVisibility(View.VISIBLE);
                    }else{
                        mPhone.setVisibility(View.GONE);
                    }

                }

                if (dataSnapshot.child("ChatAvailability").getValue().toString().equals("no")) {
                    mMessage.setVisibility(View.GONE);
                } else {
                    mMessage.setVisibility(View.VISIBLE);
                }

//                if (!dataSnapshot.child("Delivery").exists()) {
//                    mDeliveryContent.setVisibility(View.GONE);
//                } else {
//                    mDeliveryContent.setVisibility(View.VISIBLE);
//                }
                if (dataSnapshot.child("DeliveryPlaces").exists()) {
                    showPlaces();
                    mDeliveryContent.setVisibility(View.VISIBLE);
                }else{
                    mDeliveryContent.setVisibility(View.GONE);
                }

                if (dataSnapshot.child("Category").exists()) {
                    showCategory();
                    mCategoryContent.setVisibility(View.VISIBLE);
                } else {
                    mCategoryContent.setVisibility(View.GONE);
                }

                if (dataSnapshot.child("Gallery").exists()) {
                    showGallery();
                    mGallery.setVisibility(View.VISIBLE);
                } else {
                    showMenu();
                    mGallery.setVisibility(View.GONE);
                }

                if (dataSnapshot.child("Latitude").exists()){
                    mLocation.setVisibility(View.VISIBLE);
                    mLatitude = dataSnapshot.child("Latitude").getValue().toString();
                }else {
                    mLocation.setVisibility(View.GONE);
                }

                if (dataSnapshot.child("Longitude").exists()){
                    mLocation.setVisibility(View.VISIBLE);
                    mLongitude = dataSnapshot.child("Longitude").getValue().toString();
                }else {
                    mLocation.setVisibility(View.GONE);
                }

//                if (!dataSnapshot.child("Gallery").exists()) {
//                    mGallery.setVisibility(View.GONE);
//                } else {
//                    mGallery.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mLocation.setOnClickListener(view -> {
            String uri = "http://maps.google.com/maps?daddr=" + mLatitude + "," + mLongitude + " (" + mTitle + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });


        mFavourite = findViewById(R.id.likeClient);
        seenListener = mDatabase.child("Users").child(mUserID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.child("BlockList").getChildren()) {
                    BlockListModel blockListModel = snapshot.getValue(BlockListModel.class);
                    if (blockListModel.getClient().equals(mKey)) {
                        mMessage.setVisibility(View.GONE);
                    }
                }


                if (dataSnapshot.child("Favourite").child(mKey).exists()) {
                    mFavourite.setImageResource(R.drawable.icons8_romance_filled);
                } else {
                    mFavourite.setImageResource(R.drawable.icons8_romance_heart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mFavourite.setOnClickListener(view -> seenListener = mDatabase.child("Users").child(mUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Favourite").child(mKey).exists()) {
                    mFavourite.setImageResource(R.drawable.icons8_romance_heart);
                    mDatabase.child("Users").child(mUserID).child("Favourite").child(mKey).removeValue();
                } else {
                    mFavourite.setImageResource(R.drawable.icons8_romance_filled);
                    DatabaseReference databaseReference = mDatabase.child("Users").child(mUserID).child("Favourite").child(mKey);
                    databaseReference.child("ClientID").setValue(mKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));

        mRecyclerCategory = findViewById(R.id.recyclerCategory);
        mRecyclerCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerCategory.setHasFixedSize(true);



        mGallery.setOnClickListener(view -> showGallery());

        mPhone.setOnClickListener(view -> {
//            mDatabase.child("Clients").child(mKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    count = dataSnapshot.child("PhoneClick").getValue().toString();
//                    DatabaseReference databaseReference = dataSnapshot.getRef();
//                    databaseReference.child("PhoneClick").setValue(String.valueOf(Integer.parseInt(count) + 1));
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//            new Handler().postDelayed(() -> {
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clients").child(mKey);
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String mNumber = null;
//                        for (DataSnapshot snapshot : dataSnapshot.child("Phone").getChildren()) {
//                            DataModel model = snapshot.getValue(DataModel.class);
//                            mNumber = model.getData();
//                        }
//                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mNumber)));
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
//            }, 100);
            Intent intent = new Intent(this, PhoneNumbersActivity.class);
            intent.putExtra("key", mKey);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

//        mGallery.setOnClickListener(view -> {
//            Intent intent = new Intent(ClientProfileActivity.this, GalleryActivity.class);
//            intent.putExtra("key", mKey);
//            intent.putExtra("name", mTitle);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        });

        mMessage.setOnClickListener(view -> {
            updateToken(FirebaseInstanceId.getInstance().getToken());
            Intent intent = new Intent(ClientProfileActivity.this, ChatActivity.class);
            intent.putExtra("key", mKey);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("PhoneNumber").exists()) {
                        if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Block").exists()) {
                            countBlock = Integer.parseInt(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Block").getValue().toString());
                            switch (countBlock) {
                                case 0:
                                    startActivity(intent);
                                    break;
                                case 1:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ClientProfileActivity.this);
                                    builder.setMessage("قدم أحد العملاء شكوي ضدك إحترس حتي لا يتم اغلاق حسابك!");
                                    // add a button
                                    builder.setPositiveButton("حسناً", (dialog, which) -> {
                                        startActivity(intent);
                                        dialog.cancel();
                                    });
                                    // create and show the alert dialog
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    break;
                                case 2:
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ClientProfileActivity.this);
                                    builder2.setMessage("التحذير الثاني إحترس حتي لا يتم اغلاق حسابك!");
                                    // add a button
                                    builder2.setPositiveButton("حسناً", (dialog2, which) -> {
                                        startActivity(intent);
                                        dialog2.cancel();
                                    });
                                    // create and show the alert dialog
                                    AlertDialog dialog2 = builder2.create();
                                    dialog2.show();
                                    break;
                                case 3:
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(ClientProfileActivity.this);
                                    builder3.setMessage("التحذير الاخير إحترس حتي لا يتم اغلاق حسابك!");
                                    // add a button
                                    builder3.setPositiveButton("حسناً", (dialog3, which) -> {
                                        startActivity(intent);
                                        dialog3.cancel();
                                    });
                                    // create and show the alert dialog
                                    AlertDialog dialog3 = builder3.create();
                                    dialog3.show();
                                    break;
                                default:
                                    AlertDialog.Builder builder4 = new AlertDialog.Builder(ClientProfileActivity.this);
                                    builder4.setMessage("هذه الخدمة لم تعد متاحة لك لكثرة الشكاوي");
                                    // add a button
                                    builder4.setPositiveButton("حسناً", (dialog4, which) -> {
                                        dialog4.cancel();
                                    });
                                    // create and show the alert dialog
                                    AlertDialog dialog4 = builder4.create();
                                    dialog4.show();
                                    break;
                            }
                        } else {
                            startActivity(intent);
                        }
                    } else {
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(ClientProfileActivity.this);
                        builder4.setMessage("برجاء الذهاب وتفعيل حسابك اولا");
                        // add a button
                        builder4.setPositiveButton("حسناً", (dialog4, which) -> {
                            dialog4.cancel();
                            Intent intent = new Intent(ClientProfileActivity.this, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
                        // create and show the alert dialog
                        AlertDialog dialog4 = builder4.create();
                        dialog4.show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        mAddress.setOnClickListener(view -> {
            if (!check){
                mAddressSpinner.setVisibility(View.VISIBLE);
                check = true;
            }else{
                mAddressSpinner.setVisibility(View.GONE);
                check = false;
            }
        });

        mProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(this, ZoomActivity.class);
            intent.putExtra("key", profileLink);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        mCoverPic.setOnClickListener(view -> {
            Intent intent = new Intent(this, ZoomActivity.class);
            intent.putExtra("key", coverLink);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

//        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                showMenuCategory(adapterView.getItemAtPosition(i).toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                showMenu();
//            }
//        });
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(mAuth.getUid()).setValue(token1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        seenListener = mDatabase.child("Clients").child(mKey).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> nomeConsulta = new ArrayList<>();
                for (DataSnapshot areaSnapshot : dataSnapshot.child("Address").getChildren()) {
                    AddressModel consultaName = areaSnapshot.getValue(AddressModel.class);
                    nomeConsulta.add(consultaName.getData());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ClientProfileActivity.this,
                        R.layout.row_spinner, nomeConsulta);
                arrayAdapter.setDropDownViewResource(R.layout.row_spinner);
                mAddressSpinner.setAdapter(arrayAdapter);

//                final List<String> nomeConsulta2 = new ArrayList<>();
//                for (DataSnapshot areaSnapshot : dataSnapshot.child("Category").getChildren()) {
//                    ClientCategoryModel consultaName2 = areaSnapshot.getValue(ClientCategoryModel.class);
//                    nomeConsulta2.add(consultaName2.getCategoryName());
//                }
//                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(ClientProfileActivity.this,
//                        android.R.layout.simple_spinner_item, nomeConsulta2);
//                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mCategorySpinner.setAdapter(arrayAdapter2);

                Picasso.with(ClientProfileActivity.this)
                        .load(dataSnapshot.child("ClientImageCover").getValue().toString())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(600, 300)
                        .error(R.drawable.easy_order_splash)
                        .placeholder(R.drawable.easy_order_splash)
                        .into(mCoverPic, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(ClientProfileActivity.this)
                                        .load(dataSnapshot.child("ClientImageCover").getValue().toString())
                                        .resize(600, 300)
                                        .error(R.drawable.easy_order_splash)
                                        .placeholder(R.drawable.easy_order_splash).into(mCoverPic);
                            }
                        });


                if (!dataSnapshot.child("ClientImageProfile").getValue().toString().equals("null")) {
                    Picasso.with(ClientProfileActivity.this)
                            .load(dataSnapshot.child("ClientImageProfile").getValue().toString())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .resize(500, 500)
                            .error(R.drawable.easy_order_splash)
                            .placeholder(R.drawable.easy_order_splash).into(mProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ClientProfileActivity.this)
                                    .load(dataSnapshot.child("ClientImageProfile").getValue().toString())
                                    .resize(500, 500)
                                    .error(R.drawable.easy_order_splash)
                                    .placeholder(R.drawable.easy_order_splash).into(mProfileImage);
                        }
                    });
                } else {
                    mProfileImage.setImageResource(R.drawable.easy_order_splash);
                }


                if (dataSnapshot.child("ClientWord").exists()) {
                    mAboutTxt.setText(dataSnapshot.child("ClientWord").getValue().toString());
                } else {
                    mAboutTxt.setVisibility(View.GONE);
                }


                if (dataSnapshot.child("PhoneAvailability").getValue().toString().equals("no")) {
                    mPhone.setVisibility(View.GONE);
                } else {
                    mPhone.setVisibility(View.VISIBLE);
                }


                if (dataSnapshot.child("ChatAvailability").getValue().toString().equals("no")) {
                    mMessage.setVisibility(View.GONE);
                } else {
                    mMessage.setVisibility(View.VISIBLE);
                }

//                if (!dataSnapshot.child("Delivery").exists()) {
//                    mDeliveryContent.setVisibility(View.GONE);
//                } else {
//                    mDeliveryContent.setVisibility(View.VISIBLE);
//                }

                if (dataSnapshot.child("DeliveryPlaces").exists()) {
                    showPlaces();
                    mDeliveryContent.setVisibility(View.VISIBLE);
                }else{
                    mDeliveryContent.setVisibility(View.GONE);
                }

                if (dataSnapshot.child("Category").exists()) {
                    showCategory();
                    mCategoryContent.setVisibility(View.VISIBLE);
                } else {
                    mCategoryContent.setVisibility(View.GONE);
                }


                if (dataSnapshot.child("Gallery").exists()) {
                    showGallery();
                    mGallery.setVisibility(View.VISIBLE);
                } else {
                    showMenu();
                    mGallery.setVisibility(View.GONE);
                }

//                if (!dataSnapshot.child("Gallery").exists()) {
//                    mGallery.setVisibility(View.GONE);
//                } else {
//                    mGallery.setVisibility(View.VISIBLE);
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        seenListener = mDatabase.child("Clients").child(mKey).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> nomeConsulta = new ArrayList<>();
                for (DataSnapshot areaSnapshot : dataSnapshot.child("Address").getChildren()) {
                    AddressModel consultaName = areaSnapshot.getValue(AddressModel.class);
                    nomeConsulta.add(consultaName.getData());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ClientProfileActivity.this,
                        R.layout.row_spinner, nomeConsulta);
                arrayAdapter.setDropDownViewResource(R.layout.row_spinner);
                mAddressSpinner.setAdapter(arrayAdapter);

//                final List<String> nomeConsulta2 = new ArrayList<>();
//                for (DataSnapshot areaSnapshot : dataSnapshot.child("Category").getChildren()) {
//                    ClientCategoryModel consultaName2 = areaSnapshot.getValue(ClientCategoryModel.class);
//                    nomeConsulta2.add(consultaName2.getCategoryName());
//                }
//                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(ClientProfileActivity.this,
//                        android.R.layout.simple_spinner_item, nomeConsulta2);
//                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mCategorySpinner.setAdapter(arrayAdapter2);

                Picasso.with(ClientProfileActivity.this)
                        .load(dataSnapshot.child("ClientImageCover").getValue().toString())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(600, 300)
                        .error(R.drawable.easy_order_splash)
                        .placeholder(R.drawable.easy_order_splash)
                        .into(mCoverPic, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(ClientProfileActivity.this)
                                        .load(dataSnapshot.child("ClientImageCover").getValue().toString())
                                        .resize(600, 300)
                                        .error(R.drawable.easy_order_splash)
                                        .placeholder(R.drawable.easy_order_splash).into(mCoverPic);
                            }
                        });


                if (!dataSnapshot.child("ClientImageProfile").getValue().toString().equals("null")) {
                    Picasso.with(ClientProfileActivity.this)
                            .load(dataSnapshot.child("ClientImageProfile").getValue().toString())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .resize(500, 500)
                            .error(R.drawable.easy_order_splash)
                            .placeholder(R.drawable.easy_order_splash).into(mProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ClientProfileActivity.this)
                                    .load(dataSnapshot.child("ClientImageProfile").getValue().toString())
                                    .resize(500, 500)
                                    .error(R.drawable.easy_order_splash)
                                    .placeholder(R.drawable.easy_order_splash).into(mProfileImage);
                        }
                    });
                } else {
                    mProfileImage.setImageResource(R.drawable.easy_order_splash);
                }


                if (dataSnapshot.child("ClientWord").exists()) {
                    mAboutTxt.setText(dataSnapshot.child("ClientWord").getValue().toString());
                } else {
                    mAboutTxt.setVisibility(View.GONE);
                }


                if (dataSnapshot.child("PhoneAvailability").getValue().toString().equals("no")) {
                    mPhone.setVisibility(View.GONE);
                } else {
                    mPhone.setVisibility(View.VISIBLE);
                }


                if (dataSnapshot.child("ChatAvailability").getValue().toString().equals("no")) {
                    mMessage.setVisibility(View.GONE);
                } else {
                    mMessage.setVisibility(View.VISIBLE);
                }

//                if (!dataSnapshot.child("Delivery").exists()) {
//                    mDeliveryContent.setVisibility(View.GONE);
//                } else {
//                    mDeliveryContent.setVisibility(View.VISIBLE);
//                }

                if (dataSnapshot.child("DeliveryPlaces").exists()) {
                    showPlaces();
                    mDeliveryContent.setVisibility(View.VISIBLE);
                }else{
                    mDeliveryContent.setVisibility(View.GONE);
                }

                if (dataSnapshot.child("Category").exists()) {
                    showCategory();
                    mCategoryContent.setVisibility(View.VISIBLE);
                } else {
                    mCategoryContent.setVisibility(View.GONE);
                }


                if (dataSnapshot.child("Gallery").exists()) {
                    showGallery();
                    mGallery.setVisibility(View.VISIBLE);
                } else {
                    showMenu();
                    mGallery.setVisibility(View.GONE);
                }

//                if (!dataSnapshot.child("Gallery").exists()) {
//                    mGallery.setVisibility(View.GONE);
//                } else {
//                    mGallery.setVisibility(View.VISIBLE);
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        mDatabase.addValueEventListener(seenListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.removeEventListener(seenListener);
    }

    /*****************************************************/

    private void showPlaces() {
        FirebaseRecyclerAdapter<DeliveryPlacesModel, ClientProfileActivity.BlogViewHolderPlaces> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<DeliveryPlacesModel, ClientProfileActivity.BlogViewHolderPlaces>(
                        DeliveryPlacesModel.class,
                        R.layout.row_category_menu,
                        ClientProfileActivity.BlogViewHolderPlaces.class,
                        mDatabase.child("Clients").child(mKey).child("DeliveryPlaces")
                ) {
                    @Override
                    protected void populateViewHolder(final ClientProfileActivity.BlogViewHolderPlaces viewHolder,
                                                      final DeliveryPlacesModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setPlace(model.getPlace());

                    }
                };
        firebaseRecyclerAdapter.startListening();
        mRecyclerDeliveryPlaces.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolderPlaces extends RecyclerView.ViewHolder {
        View mView;
        private final static int FADE_DURATION = 500;

        public BlogViewHolderPlaces(View itemView) {
            super(itemView);
            mView = itemView;
            setFadeAnimation(mView);
        }


        public void setPlace(String place) {
            TextView textView = mView.findViewById(R.id.txt_menu_category);
            textView.setText(place);
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }

    /*****************************************************/

    private void showCategory() {

        FirebaseRecyclerAdapter<ClientCategoryModel, ClientProfileActivity.BlogViewHolder> firebaseRecyclerAdapter2 = new
                FirebaseRecyclerAdapter<ClientCategoryModel, ClientProfileActivity.BlogViewHolder>(
                        ClientCategoryModel.class,
                        R.layout.row_category_menu,
                        ClientProfileActivity.BlogViewHolder.class,
                        mDatabase.child("Clients").child(mKey).child("Category")
                ) {
                    @Override
                    protected void populateViewHolder(final ClientProfileActivity.BlogViewHolder viewHolder, final ClientCategoryModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setCategoryName(model.getCategoryName());
                        viewHolder.mView.setOnClickListener(view -> showMenuCategory(model.getCategoryName()));
                    }
                };
        firebaseRecyclerAdapter2.startListening();
        mRecyclerCategory.setAdapter(firebaseRecyclerAdapter2);
//        mUsers = new ArrayList<>();
//        mDatabase = FirebaseDatabase.getInstance().getReference("Clients").child(mKey).child("Category");
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUsers.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    MenuCategoryModel chatlist = snapshot.getValue(MenuCategoryModel.class);
//                    mUsers.add(chatlist);
//                }
//                mPMCA = new ProfileMenuCategoryAdapter(ClientProfileActivity.this, mUsers);
//                mRecyclerCategory.setAdapter(mPMCA);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Typeface tf;
        String fontPath;
        private final static int FADE_DURATION = 100;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            fontPath = "fonts/andalus.ttf";
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

    public void showMenuCategory(String category) {
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mRecycler.setHasFixedSize(true);


        FirebaseRecyclerAdapter<MenuModel, ClientProfileActivity.BlogViewHolderMenu> firebaseRecyclerAdapter3 = new
                FirebaseRecyclerAdapter<MenuModel, ClientProfileActivity.BlogViewHolderMenu>(
                        MenuModel.class,
                        R.layout.row_menu,
                        ClientProfileActivity.BlogViewHolderMenu.class,
                        mDatabase.child("Clients").child(mKey).child("Menu").orderByChild("Category").equalTo(category)
                ) {
                    @Override
                    protected void populateViewHolder(final ClientProfileActivity.BlogViewHolderMenu viewHolder, final MenuModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setCategoryName(model.getCategoryName());
                        viewHolder.setCategoryImage(ClientProfileActivity.this, model.getCategoryImage());
                        viewHolder.setCategoryPrice(model.getCategoryPrice());
                        viewHolder.mView.setOnClickListener(view -> {
                            Intent intent = new Intent(ClientProfileActivity.this, ZoomActivity.class);
                            intent.putExtra("key", model.getCategoryImage());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
                    }
                };
        firebaseRecyclerAdapter3.startListening();
        mRecycler.setAdapter(firebaseRecyclerAdapter3);
    }

    /********************************************************/

    private void showGallery(){
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3));

        FirebaseRecyclerAdapter<GalleryModel, ClientProfileActivity.BlogViewHolderGallery> firebaseRecyclerAdapter4 = new
                FirebaseRecyclerAdapter<GalleryModel, BlogViewHolderGallery>(
                        GalleryModel.class,
                        R.layout.row_gallery,
                        ClientProfileActivity.BlogViewHolderGallery.class,
                        mDatabase.child("Clients").child(mKey).child("Gallery")
                ) {
                    @Override
                    protected void populateViewHolder(final ClientProfileActivity.BlogViewHolderGallery viewHolder, final GalleryModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                        viewHolder.mView.setOnClickListener(view -> {
//                            Intent intent = new Intent(ClientProfileActivity.this, ZoomActivity.class);
//                            intent.putExtra("key", model.getImage());
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);

                            Intent intent = new Intent(ClientProfileActivity.this, GallerySliderImageActivity.class);
                            intent.putExtra("key", mKey);
                            intent.putExtra("position", String.valueOf(position));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
                    }
                };
        firebaseRecyclerAdapter4.startListening();
        mRecycler.setAdapter(firebaseRecyclerAdapter4);
    }

    public static class BlogViewHolderGallery extends RecyclerView.ViewHolder {
        View mView;
        Typeface tf;
        String fontPath;
        private final static int FADE_DURATION = 100;

        public BlogViewHolderGallery(View itemView) {
            super(itemView);
            mView = itemView;
            fontPath = "fonts/bziba.ttf";
            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
            setFadeAnimation(mView);
        }

        public void setImage(final Context ctx, final String categoryImage) {
            final ImageView imageView = mView.findViewById(R.id.imageGallery);
//            Picasso.with(ctx).load(categoryImage).into(imageView);

            Picasso.with(ctx).load(categoryImage)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(500, 500)
                    .error(R.drawable.easy_order_splash)
                    .placeholder(R.drawable.easy_order_splash)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ctx).load(categoryImage+"?width=32&height=32")
                                    .resize(500, 500)
                                    .error(R.drawable.easy_order_splash)
                                    .placeholder(R.drawable.easy_order_splash).into(imageView);
                        }
                    });
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }

    /********************************************************/

    private void showMenu() {
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mRecycler.setHasFixedSize(true);
        FirebaseRecyclerAdapter<MenuModel, ClientProfileActivity.BlogViewHolderMenu> firebaseRecyclerAdapter5 = new
                FirebaseRecyclerAdapter<MenuModel, ClientProfileActivity.BlogViewHolderMenu>(
                        MenuModel.class,
                        R.layout.row_menu,
                        ClientProfileActivity.BlogViewHolderMenu.class,
                        mDatabase.child("Clients").child(mKey).child("Menu")
                ) {
                    @Override
                    protected void populateViewHolder(final ClientProfileActivity.BlogViewHolderMenu viewHolder, final MenuModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setCategoryName(model.getCategoryName());
                        viewHolder.setCategoryImage(ClientProfileActivity.this, model.getCategoryImage());
                        viewHolder.setCategoryPrice(model.getCategoryPrice());

                        viewHolder.mView.setOnClickListener(view -> {
                            Intent intent = new Intent(ClientProfileActivity.this, ZoomActivity.class);
                            intent.putExtra("key", model.getCategoryImage());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
                    }
                };
        firebaseRecyclerAdapter5.startListening();
        mRecycler.setAdapter(firebaseRecyclerAdapter5);
    }

    public static class BlogViewHolderMenu extends RecyclerView.ViewHolder {
        View mView;
        Typeface tf;
        String fontPath;
        RelativeLayout mRelative;
        private final static int FADE_DURATION = 100;

        public BlogViewHolderMenu(View itemView) {
            super(itemView);
            mView = itemView;
            fontPath = "fonts/bziba.ttf";
            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
            mRelative = mView.findViewById(R.id.textMenu);
            setFadeAnimation(mView);
        }

        public void setCategoryImage(final Context ctx, final String categoryImage) {
            final ImageView imageView = mView.findViewById(R.id.row_image);
//            Picasso.with(ctx).load(categoryImage).into(imageView);

            Picasso.with(ctx).load(categoryImage)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(500, 500)
                    .error(R.drawable.easy_order_splash)
                    .placeholder(R.drawable.easy_order_splash).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(categoryImage)
                            .resize(500, 500)
                            .error(R.drawable.easy_order_splash)
                            .placeholder(R.drawable.easy_order_splash).into(imageView);
                }
            });
        }

        public void setCategoryName(String categoryName) {
            if (categoryName.equals("null")) {
                mRelative.setVisibility(View.GONE);
            } else {
                TextView textView = mView.findViewById(R.id.row_txt);
                textView.setTypeface(tf);
                textView.setText(categoryName);
            }
        }

        public void setCategoryPrice(String categoryPrice) {
            TextView textView = mView.findViewById(R.id.row_txt_price);
            textView.setTypeface(tf);
            textView.setText(categoryPrice + "ج");
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }

    /****************************************************************/

//    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {
//
//        if (tv.getTag() == null) {
//            tv.setTag(tv.getText());
//        }
//        ViewTreeObserver vto = tv.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onGlobalLayout() {
//                String text;
//                int lineEndIndex;
//                ViewTreeObserver obs = tv.getViewTreeObserver();
//                obs.removeGlobalOnLayoutListener(this);
//                if (maxLine == 0) {
//                    lineEndIndex = tv.getLayout().getLineEnd(0);
//                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
//                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
//                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
//                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
//                } else {
//                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
//                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
//                }
//                tv.setText(text);
//                tv.setMovementMethod(LinkMovementMethod.getInstance());
//                tv.setText(
//                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()),
//                                tv, lineEndIndex, expandText,
//                                viewMore), TextView.BufferType.SPANNABLE);
//            }
//        });
//
//    }
//
//    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
//                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
//        String str = strSpanned.toString();
//        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);
//
//        if (str.contains(spanableText)) {
//
//
//            ssb.setSpan(new MySpannable(false) {
//                @Override
//                public void onClick(View widget) {
//                    tv.setLayoutParams(tv.getLayoutParams());
//                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
//                    tv.invalidate();
//                    if (viewMore) {
//                        makeTextViewResizable(tv, -1, "less", false);
//                    } else {
//                        makeTextViewResizable(tv, 2, "more", true);
//                    }
//                }
//            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);
//
//        }
//        return ssb;
//
//    }

    /********************************************************************/
}
