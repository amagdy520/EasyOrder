package com.easy.order.MainScreens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.order.FirebaseModels.ClientIDModel;
import com.easy.order.FirebaseModels.DataModel;
import com.easy.order.FirebaseModels.OffersModel;
import com.easy.order.Notifications.Token;
import com.easy.order.R;
import com.easy.order.UserProfile.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OffersActivity extends AppCompatActivity {

    private TextView mTitleTxt;
    private DatabaseReference mDatabase;
    private RecyclerView mRecycler;
    private FirebaseAuth mAuth;
    int countBlock;
    private String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mTitleTxt = findViewById(R.id.titleTxt);
        String fontPath = "fonts/andalus.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mTitleTxt.setTypeface(tf);
        mRecycler = findViewById(R.id.recyclerOffers);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        showOffers();
    }
    @Override
    protected void onStart() {
        super.onStart();
        showOffers();
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(mAuth.getUid()).setValue(token1);
    }

    private void showOffers(){
        FirebaseRecyclerAdapter<OffersModel, OffersActivity.BlogViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<OffersModel, OffersActivity.BlogViewHolder>(
                        OffersModel.class,
                        R.layout.row_offers,
                        OffersActivity.BlogViewHolder.class,
                        mDatabase.child("Offers")
                ) {
                    @Override
                    protected void populateViewHolder(final OffersActivity.BlogViewHolder viewHolder, final OffersModel model, final int position) {
                        final String post_key = getRef(position).getKey();
                        viewHolder.setClient(OffersActivity.this, model.getClient());
                        viewHolder.setOfferContent(model.getOfferContent());
                        viewHolder.setOfferTitle(model.getOfferTitle());
                        viewHolder.setOfferImage(OffersActivity.this, model.getOfferImage());

                        viewHolder.mView.setOnClickListener(view -> {
//                            if (model.getClientAvailability().equals("yes")) {
//                                viewHolder.setClientVisitor(post_key);
//                                Intent intent = new Intent(OffersActivity.this, ClientProfileActivity.class);
//                                intent.putExtra("key", model.getClient());
//                                intent.putExtra("name", model.getClientName());
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(OffersActivity.this, "هذا العميل غير متاح الأن", Toast.LENGTH_SHORT).show();
//                            }
                            viewHolder.goToClient(OffersActivity.this, model.getClient());
                        });

                        viewHolder.mOfferContent.setOnClickListener(view -> viewHolder.goToClient(OffersActivity.this, model.getClient()));

                        viewHolder.imageView.setOnClickListener(view -> viewHolder.goToClient(OffersActivity.this, model.getClient()));

                        viewHolder.textView.setOnClickListener(view -> viewHolder.goToClient(OffersActivity.this, model.getClient()));

                        viewHolder.textView1.setOnClickListener(view -> viewHolder.goToClient(OffersActivity.this, model.getClient()));

                        viewHolder.mMessage.setOnClickListener(view -> {
                            updateToken(FirebaseInstanceId.getInstance().getToken());
                            Intent intent = new Intent(OffersActivity.this, ChatActivity.class);
                            intent.putExtra("key", model.getClient());
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
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OffersActivity.this);
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
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(OffersActivity.this);
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
                                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(OffersActivity.this);
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
                                                    AlertDialog.Builder builder4= new AlertDialog.Builder(OffersActivity.this);
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
                                        AlertDialog.Builder builder4= new AlertDialog.Builder(OffersActivity.this);
                                        builder4.setMessage("برجاء الذهاب وتفعيل حسابك اولا");
                                        // add a button
                                        builder4.setPositiveButton("حسناً", (dialog4, which) -> {
                                            dialog4.cancel();
                                            Intent intent = new Intent(OffersActivity.this, ProfileActivity.class);
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
                            mDatabase.child("Clients")
                                    .child(model.getClient()).addListenerForSingleValueEvent(new ValueEventListener() {
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

//                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clients").child(post_key);
//                                reference.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        String mNumber = null;
//                                        for (DataSnapshot snapshot : dataSnapshot.child("Phone").getChildren()) {
//                                            DataModel model = snapshot.getValue(DataModel.class);
//                                            mNumber = model.getData();
//                                        }
//                                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mNumber)));
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                                    }
//                                });
                                Intent intent = new Intent(OffersActivity.this, PhoneNumbersActivity.class);
                                intent.putExtra("key", model.getClient());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }, 100);
                        });
                    }
                };
        mRecycler.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        private final static int FADE_DURATION = 500;

        private DatabaseReference mDatabase;

        private ImageView mPhone, mMessage;

        private String count;

        private RelativeLayout mOfferContent;

        private ImageView imageView;
        private TextView textView , textView1;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);
            setFadeAnimation(mView);
            mPhone = mView.findViewById(R.id.call);
            mMessage = mView.findViewById(R.id.message);
            mOfferContent = mView.findViewById(R.id.offerContent);
            imageView = mView.findViewById(R.id.offerImage);
            textView = mView.findViewById(R.id.offerTitle);
            textView1 = mView.findViewById(R.id.offerDetails);
        }


        public void setOfferImage(Context ctx, String offerImage) {
//            Picasso.with(ctx).load(commercialImage).into(imageView);
            Picasso.with(ctx).load(offerImage).networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(600, 300)
                    .error(R.drawable.easy_order_splash)
                    .placeholder(R.drawable.easy_order_splash).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(offerImage)
                            .resize(600, 300)
                            .error(R.drawable.easy_order_splash)
                            .placeholder(R.drawable.easy_order_splash).into(imageView);
                }
            });
        }


        public void setOfferTitle(String offerTitle) {
            textView.setText(offerTitle);
        }


        public void setOfferContent(String offerContent) {
            textView1.setText(offerContent);
        }


        public void setClient(Context ctx, String client) {
            CircleImageView circleImageView = mView.findViewById(R.id.profileImage);
            TextView textView = mView.findViewById(R.id.profileName);

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Clients").child(client).exists()){

                        if (!dataSnapshot.child("Clients").child(client).child("ClientImageProfile").getValue().toString().equals("null")){
                            Picasso.with(ctx).load(dataSnapshot.child("Clients").child(client).child("ClientImageProfile")
                                    .getValue().toString())
                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                    .resize(500, 500)
                                    .error(R.drawable.easy_order_splash)
                                    .placeholder(R.drawable.easy_order_splash).into(circleImageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(ctx).load(dataSnapshot.child("Clients").child(client).child("ClientImageProfile")
                                            .getValue().toString())
                                            .resize(500, 500)
                                            .error(R.drawable.easy_order_splash)
                                            .placeholder(R.drawable.easy_order_splash)
                                            .into(circleImageView);
                                }
                            });
                        }else{
                            Picasso.with(ctx).load(dataSnapshot.child("Clients").child(client).child("ClientImageCover")
                                    .getValue().toString())
                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                    .resize(500, 500)
                                    .error(R.drawable.easy_order_splash)
                                    .placeholder(R.drawable.easy_order_splash).into(circleImageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(ctx).load(dataSnapshot.child("Clients").child(client).child("ClientImageCover")
                                            .getValue().toString())
                                            .resize(500, 500)
                                            .error(R.drawable.easy_order_splash)
                                            .placeholder(R.drawable.easy_order_splash)
                                            .into(circleImageView);
                                }
                            });
                        }

                        textView.setText(dataSnapshot.child("Clients").child(client).child("ClientName")
                                .getValue().toString());

                        if (!dataSnapshot.child("Clients").child(client)
                                .child("PhoneAvailability").getValue().toString().equals("yes")){
                            mPhone.setVisibility(View.GONE);
                        }else{
                            mPhone.setVisibility(View.VISIBLE);
                        }

                        if (!dataSnapshot.child("Clients").child(client)
                                .child("ChatAvailability").getValue().toString().equals("yes")){
                            mMessage.setVisibility(View.GONE);
                        }else{
                            mMessage.setVisibility(View.VISIBLE);
                        }


                    }
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

        public void goToClient(Context ctx, String client) {

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Clients").child(client).exists()) {

                        if (dataSnapshot.child("Clients").child(client)
                                .child("ClientAvailability").getValue().toString().equals("yes")) {
                            setClientVisitor(client);
                            Intent intent = new Intent(ctx, ClientProfileActivity.class);
                            intent.putExtra("key", client);
                            intent.putExtra("name", dataSnapshot.child("Clients").child(client).child("ClientName").getValue().toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ctx.startActivity(intent);
                        } else {
                            Toast.makeText(ctx, "هذا العميل غير متاح الأن", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });
        }

        public void setClientVisitor(String client) {
            mDatabase.child("Clients").child(client).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count = dataSnapshot.child("ClientVisitor").getValue().toString();
                    DatabaseReference databaseReference = dataSnapshot.getRef();
                    databaseReference.child("ClientVisitor").setValue(String.valueOf(Integer.parseInt(count) + 1));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
