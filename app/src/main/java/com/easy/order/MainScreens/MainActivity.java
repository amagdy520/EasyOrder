package com.easy.order.MainScreens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.easy.order.FirebaseModels.CategoryModel;
import com.easy.order.FirebaseModels.ClientsModel;
import com.easy.order.FirebaseModels.DataModel;
import com.easy.order.FirebaseModels.FirebaseAdsModel;
import com.easy.order.FirebaseModels.GalleryModel;
import com.easy.order.FirebaseModels.SponsorModel;
import com.easy.order.Notifications.Token;
import com.easy.order.R;
import com.easy.order.UserProfile.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference banners, mDatabase;
//    private List<FirebaseAdsModel> image_list;
    private SliderLayout mSlider;
//    private String mUser;
    private RecyclerView mRecycler, mRecyclerSponsor;
    boolean doubleBackToExitPressedOnce = false;
    MaterialSearchBar searchBar;
    private DrawerLayout mDrawer;
    private RelativeLayout  mDrawerContent2;

    private CircleImageView mProfilePic, mProfilePicDrawer;

    ValueEventListener valueEventListener2;

//    private TextView mProfileName, mLikeCount, mOffersCount, mChatCount;
//    private LinearLayout mChat, mLikes, mOffers;
//    private TextView mMessageTitle, mMessage;
//    private ImageView mOpenDrawer, mLogout;
//    private boolean mChatListExist;

    private TextView mProfileName, mAccountGo;
    private FirebaseAuth mAuth;

    private ImageView mChat, mCloserDrawer;
    private RelativeLayout mOpenDrawer;
    private Button mOffers, mJoinUs;


    int countBlock;
    private String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
//        mMessageTitle = findViewById(R.id.good);
//        mMessage = findViewById(R.id.wordGood);
        mDrawer = findViewById(R.id.drawer_layout);
        mDrawerContent2 = findViewById(R.id.whatYouWantInLeftDrawer);


        mProfilePic = findViewById(R.id.profile);
        mProfilePicDrawer = findViewById(R.id.profile2);
        mProfileName = findViewById(R.id.accountTxt);
//        mUserId = mAuth.getCurrentUser().getUid();
        mProfileName.setText(mAuth.getCurrentUser().getDisplayName());

//        Picasso.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).into(mProfilePic);

        Picasso.with(this).load(mAuth.getCurrentUser().getPhotoUrl()+"?height=500")
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(500, 500)
                .error(R.drawable.easy_order_splash)
                .placeholder(R.drawable.easy_order_splash).into(mProfilePic, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(MainActivity.this).load(mAuth.getCurrentUser().getPhotoUrl()+"?height=500")
                        .resize(500, 500)
                        .error(R.drawable.easy_order_splash)
                        .placeholder(R.drawable.easy_order_splash).into(mProfilePic);
            }
        });

        Picasso.with(this).load(mAuth.getCurrentUser().getPhotoUrl()+"?height=500")
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(500, 500)
                .error(R.drawable.easy_order_splash)
                .placeholder(R.drawable.easy_order_splash).into(mProfilePicDrawer, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(MainActivity.this).load(mAuth.getCurrentUser().getPhotoUrl()+"?height=500")
                        .resize(500, 500)
                        .error(R.drawable.easy_order_splash)
                        .placeholder(R.drawable.easy_order_splash).into(mProfilePicDrawer);
            }
        });

//        mProfilePic.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        });


//        valueEventListener = mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.child(mAuth.getCurrentUser().getUid()).exists()){
//                    DatabaseReference databaseReference = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getRef();
//                    databaseReference.child("Name").setValue(mAuth.getCurrentUser().getDisplayName().toString());
//                    databaseReference.child("ProfilePicture").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
//                    databaseReference.child("Email").setValue(mAuth.getCurrentUser().getEmail().toString());
//                    databaseReference.child("UserId").setValue(mAuth.getCurrentUser().getUid().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


//        mLogout = findViewById(R.id.outProfile);
//        mLogout.setOnClickListener(view -> {
//            LoginManager.getInstance().logOut();
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(MainActivity.this, SplashActivity.class));
//            finish();
//        });
//
//
//        Calendar c = Calendar.getInstance();
//        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
//
//        if (timeOfDay >= 0 && timeOfDay < 12) {
//            mMessageTitle.setText(R.string.morning);
//            mMessage.setText(R.string.morningTxt);
//        } else if (timeOfDay >= 12 && timeOfDay < 18) {
//            mMessageTitle.setText(R.string.afternoon);
//            mMessage.setText(R.string.afternoonTxt);
//        } else if (timeOfDay >= 18 && timeOfDay < 24) {
//            mMessageTitle.setText(R.string.evening);
//            mMessage.setText(R.string.eveningTxt);
//        }

        mSlider = findViewById(R.id.sliderAds);
        setupSlider();

        mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);

        showCategory();

        mRecyclerSponsor = findViewById(R.id.sponsors);
        mRecyclerSponsor.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerSponsor.setHasFixedSize(true);

        showSponsors();

        searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (searchBar.getText().equals("")){
                    showCategory();
                }else {
                    clientsFilterFirebase(searchBar.getText().toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + searchBar.getText());
                if (searchBar.getText().equals("")){
                    showCategory();
                }else {
                    clientsFilterFirebase(searchBar.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchBar.getText().equals("")){
                    showCategory();
                }else {
                    clientsFilterFirebase(searchBar.getText().toString());
                }
            }

        });

//        mDrawerContent = findViewById(R.id.last);
//
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.open, R.string.close) {
//            private float scaleFactor = 6f;
//
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//                float slideX = drawerView.getWidth() * slideOffset;
//                mDrawerContent.setTranslationX(1 - slideX);
//                mDrawerContent.setScaleX(1 - (slideOffset / scaleFactor));
//                mDrawerContent.setScaleY(1 - (slideOffset / scaleFactor));
//            }
//        };
//        mDrawer.setScrimColor(Color.TRANSPARENT);
//        mDrawer.setDrawerElevation(0f);
//        mDrawer.addDrawerListener(actionBarDrawerToggle);

        mOpenDrawer = findViewById(R.id.openDrawer);
        mOpenDrawer.setOnClickListener(view -> mDrawer.openDrawer(mDrawerContent2));

        mCloserDrawer = findViewById(R.id.closeDrawer);
        mCloserDrawer.setOnClickListener(view -> mDrawer.closeDrawer(mDrawerContent2));

//
//
//        mLikeCount = findViewById(R.id.likeCount);
//        mOffersCount = findViewById(R.id.offersCount);
//        mChatCount = findViewById(R.id.messageCount);
//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mLikeCount.setText(String.valueOf(dataSnapshot.child("Users").child(mUserId).child("Favourite").getChildrenCount()));
//                mOffersCount.setText(String.valueOf(dataSnapshot.child("Offers").getChildrenCount()));
//                int unread = 0;
//                for (DataSnapshot snapshot : dataSnapshot.child("Chats").getChildren()){
//                    ChatModel chat = snapshot.getValue(ChatModel.class);
//                    if (chat.getReceiver().equals(mAuth.getUid()) && !chat.isIsseen()){
//                        unread++;
//                    }
//                }
//                mChatCount.setText(String.valueOf(unread));
//
//                mChatListExist = dataSnapshot.child("Chatlist").child(mAuth.getCurrentUser().getUid()).exists();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        mLikes = findViewById(R.id.love);
//        mLikes.setOnClickListener(view -> {
//            if (mLikeCount.getText().toString().equals("0")){
//                Toast.makeText(MainActivity.this, "لا يوجد مفضلة حتي الأن", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        });
//
        mChat = findViewById(R.id.message);
        mChat.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        mOffers = findViewById(R.id.offers);
        mOffers.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, OffersActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        mOffers.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/andalus.ttf"));

        mAccountGo = findViewById(R.id.accountGo);
        mAccountGo.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        mJoinUs = findViewById(R.id.joinUs);
        mJoinUs.setOnClickListener(view -> {
//            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "01204454077")));

            Intent intent = new Intent(MainActivity.this, JoinUsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setupSlider();
        showCategory();
        showSponsors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupSlider();
        showCategory();
        showSponsors();
//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mLikeCount.setText(String.valueOf(dataSnapshot.child("Users").child(mUserId).child("Favourite").getChildrenCount()));
//                mOffersCount.setText(String.valueOf(dataSnapshot.child("Offers").getChildrenCount()));
//                mChatCount.setText(String.valueOf(dataSnapshot.child("Chats").child(mUserId).getChildrenCount()));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void setupSlider() {
//        image_list = new ArrayList<>();
        banners = FirebaseDatabase.getInstance().getReference();
        banners.keepSynced(true);
        valueEventListener2 = banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.child("Commercial").getChildren()) {
                    FirebaseAdsModel banner = postSnapShot.getValue(FirebaseAdsModel.class);
//                    image_list.add(banner);

                    final DefaultSliderView defaultSliderView = new DefaultSliderView(MainActivity.this);
                    defaultSliderView
                            .image(banner.getCommercialImage())
                            .error(R.drawable.error_connection)
                            .empty(R.drawable.empty)
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(slider -> {
                                if (dataSnapshot.child("Clients").child(banner.getUsers()).exists()){
                                    Intent intent = new Intent(MainActivity.this, ClientProfileActivity.class);
                                    intent.putExtra("key", banner.getUsers());
                                    intent.putExtra("name", dataSnapshot.child("Clients").child(banner.getUsers()).child("ClientName").getValue().toString());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(MainActivity.this, "No Account Found", Toast.LENGTH_SHORT).show();
                                }

                            });
//                    defaultSliderView.bundle(new Bundle());
//                    defaultSliderView.getBundle().putString("Users", banner.getUsers());
                    mSlider.addSlider(defaultSliderView);
                    banners.removeEventListener(this);

                }
//                for (FirebaseAdsModel key : image_list) {
//
//                    final DefaultSliderView defaultSliderView = new DefaultSliderView(MainActivity.this);
//                    defaultSliderView
//                            .image(key.getCommercialImage())
//                            .error(R.drawable.error_connection)
//                            .empty(R.drawable.empty)
//                            .setScaleType(BaseSliderView.ScaleType.Fit)
//                            .setOnSliderClickListener(slider -> {
//                                if (dataSnapshot.child("Clients").child(key.getUsers()).exists()){
//                                    Intent intent = new Intent(MainActivity.this, ClientProfileActivity.class);
//                                    intent.putExtra("key", key.getUsers());
//                                    intent.putExtra("name", dataSnapshot.child("Clients").child(key.getUsers()).child("ClientName").getValue().toString());
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                }
//
//                            });
//                    defaultSliderView.bundle(new Bundle());
//                    defaultSliderView.getBundle().putString("Users", key.getUsers());
//                    mSlider.addSlider(defaultSliderView);
//                    banners.removeEventListener(this);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(6000);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(mDrawerContent2)){
            mDrawer.closeDrawer(mDrawerContent2);
        }else{

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "إضغط مجدداً للخروج", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
        }
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(mAuth.getUid()).setValue(token1);
    }

    public void clientsFilterFirebase(String searchTxt) {
        Query query = mDatabase.child("Clients").orderByChild("ClientName").startAt(searchTxt).endAt(searchTxt + "\uf8ff");
        FirebaseRecyclerAdapter<ClientsModel, ClientsActivity.BlogViewHolder> firebaseRecyclerAdapter2 = new
                FirebaseRecyclerAdapter<ClientsModel, ClientsActivity.BlogViewHolder>(
                        ClientsModel.class,
                        R.layout.row_clients,
                        ClientsActivity.BlogViewHolder.class,
                        query
                ) {
                    @Override
                    protected void populateViewHolder(final ClientsActivity.BlogViewHolder viewHolder, final ClientsModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setClientImageCover(MainActivity.this, model.getClientImageCover());
                        viewHolder.setClientName(model.getClientName());
                        viewHolder.setUsername(MainActivity.this, model.getUsername());
                        viewHolder.checkLike(post_key);
                        viewHolder.mView.setOnClickListener(view -> {
                            if (model.getClientAvailability().equals("yes")) {
                                viewHolder.setClientVisitor(post_key);
                                Intent intent = new Intent(MainActivity.this, ClientProfileActivity.class);
                                intent.putExtra("key", post_key);
                                intent.putExtra("name", model.getClientName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "هذا العميل غير متاح الأن", Toast.LENGTH_SHORT).show();
                            }
                        });

                        viewHolder.mLike.setOnClickListener(view -> viewHolder.likeFunction(post_key));

                        viewHolder.mMessage.setOnClickListener(view -> {
                            updateToken(FirebaseInstanceId.getInstance().getToken());
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            intent.putExtra("key", model.getUsername());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("PhoneNumber").exists()){
                                        if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Block").exists()){
                                            countBlock = Integer.parseInt(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Block").getValue().toString());
                                            switch (countBlock) {
                                                case 0:
                                                    startActivity(intent);
                                                    break;
                                                case 1:
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
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
                                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
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
                                                    AlertDialog.Builder builder4= new AlertDialog.Builder(MainActivity.this);
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
                                        }else{
                                            startActivity(intent);
                                        }
                                    }else {
                                        AlertDialog.Builder builder4= new AlertDialog.Builder(MainActivity.this);
                                        builder4.setMessage("برجاء الذهاب وتفعيل حسابك اولا");
                                        // add a button
                                        builder4.setPositiveButton("حسناً", (dialog4, which) -> {
                                            dialog4.cancel();
                                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
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

                        viewHolder.mPhone.setOnClickListener(view -> {
                            mDatabase.child("Clients").child(model.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    count = dataSnapshot.child("PhoneClick").getValue().toString();
                                    DatabaseReference databaseReference = dataSnapshot.getRef();
                                    databaseReference.child("PhoneClick").setValue(String.valueOf(Integer.parseInt(count) + 1));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            new Handler().postDelayed(() -> {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clients").child(post_key);
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String mNumber = null;
                                        for (DataSnapshot snapshot : dataSnapshot.child("Phone").getChildren()) {
                                            DataModel model = snapshot.getValue(DataModel.class);
                                            mNumber = model.getData();
                                        }
                                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mNumber)));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }, 100);
                        });
                    }
                };
        firebaseRecyclerAdapter2.startListening();
        mRecycler.setAdapter(firebaseRecyclerAdapter2);
    }

    private void showCategory(){
        FirebaseRecyclerAdapter<CategoryModel, MainActivity.BlogViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<CategoryModel, MainActivity.BlogViewHolder>(
                        CategoryModel.class,
                        R.layout.row_category,
                        MainActivity.BlogViewHolder.class,
                        mDatabase.child("Category").orderByChild("Priority")
                ) {
                    @Override
                    protected void populateViewHolder(final MainActivity.BlogViewHolder viewHolder, final CategoryModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setCategoryImage(MainActivity.this, model.getCategoryImage());
                        viewHolder.setCategoryName(model.getCategoryName());
                        viewHolder.mView.setOnClickListener(view -> {
                            mDatabase.child("Category").child(post_key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("OtherCategory").exists()) {
                                        Intent intent = new Intent(MainActivity.this, OtherCategoryActivity.class);
                                        intent.putExtra("key", post_key);
                                        intent.putExtra("category", model.getCategoryName());
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, ClientsActivity.class);
                                        intent.putExtra("key", post_key);
                                        intent.putExtra("category", model.getCategoryName());
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        });
                    }
                };
        firebaseRecyclerAdapter.startListening();
        mRecycler.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Typeface tf;
        String fontPath;
        private final static int FADE_DURATION = 100;
        private DatabaseReference mDatabase;
        private int count = 0;

        TextView mIcons8Txt;
        ImageView mIcons8;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            fontPath = "fonts/andalus.ttf";
            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);
            setFadeAnimation(mView);
            mIcons8Txt = mView.findViewById(R.id.icons8_number);
            mIcons8 = mView.findViewById(R.id.icons8);
        }

        public void setCategoryImage(final Context ctx, final String categoryImage) {
            final ImageView imageView = mView.findViewById(R.id.row_image);
//            Picasso.with(ctx).load(categoryImage).into(imageView);

            Picasso.with(ctx).load(categoryImage)
                    .networkPolicy(NetworkPolicy.OFFLINE)
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

        public void setCategoryName(String categoryName) {
            TextView textView = mView.findViewById(R.id.row_txt);
            textView.setTypeface(tf);
            textView.setText(categoryName);

            mDatabase.child("Category").child(categoryName.replaceAll("\\s", ""))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child("OtherCategory").exists()){
                        archive(categoryName);
                    }else{
                        account(categoryName);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        private void archive(String c){
            mDatabase.child("Category").child(c.replaceAll("\\s", "")).child("OtherCategory").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mIcons8.setImageResource(R.drawable.icons8_archive);
                    mIcons8Txt.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void account(String c){
            mDatabase.child("Clients").orderByChild("ClientCategory").equalTo(c).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mIcons8.setImageResource(R.drawable.icons8_account);
                    mIcons8Txt.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }

    private void showSponsors(){

        FirebaseRecyclerAdapter<SponsorModel, MainActivity.BlogViewHolderSponsors> firebaseRecyclerAdapter3 = new
                FirebaseRecyclerAdapter<SponsorModel, MainActivity.BlogViewHolderSponsors>(
                        SponsorModel.class,
                        R.layout.row_sponsor,
                        MainActivity.BlogViewHolderSponsors.class,
                        mDatabase.child("Sponsors")
                ) {
                    @Override
                    protected void populateViewHolder(final MainActivity.BlogViewHolderSponsors viewHolder, final SponsorModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                        viewHolder.mView.setOnClickListener(view -> mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("Clients").child(model.getUser()).exists()){
                                    Intent intent = new Intent(MainActivity.this, ClientProfileActivity.class);
                                    intent.putExtra("key", model.getUser());
                                    intent.putExtra("name", dataSnapshot.child("Clients")
                                            .child(model.getUser()).child("ClientName").getValue().toString());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(MainActivity.this, "No Account Found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }));
                    }
                };
        firebaseRecyclerAdapter3.startListening();
        mRecyclerSponsor.setAdapter(firebaseRecyclerAdapter3);
    }

    public static class BlogViewHolderSponsors extends RecyclerView.ViewHolder {
        View mView;
        Typeface tf;
        String fontPath;
        private final static int FADE_DURATION = 100;

        public BlogViewHolderSponsors(View itemView) {
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
                    .networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.easy_order_splash)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ctx).load(categoryImage).placeholder(R.drawable.easy_order_splash).into(imageView);
                        }
                    });
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        banners.removeEventListener(valueEventListener2);
    }
}
