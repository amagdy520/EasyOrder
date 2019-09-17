package com.easy.order.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easy.order.FirebaseModels.ClientsModel;
import com.easy.order.FirebaseModels.DataModel;
import com.easy.order.MainScreens.ChatActivity;
import com.easy.order.MainScreens.ClientProfileActivity;
import com.easy.order.MainScreens.PhoneNumbersActivity;
import com.easy.order.Notifications.Token;
import com.easy.order.R;
import com.easy.order.UserProfile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.BlogViewHolder> {


    private Context mContext;
    private List<ClientsModel> mUsers;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private int countBlock;
    private String count;

    public ClientsAdapter(Context mContext, List<ClientsModel> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }

    @NonNull
    @Override
    public ClientsAdapter.BlogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_clients, viewGroup, false);
        return new ClientsAdapter.BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientsAdapter.BlogViewHolder viewHolder, final int i) {
        final String post_key = mUsers.get(i).getUsername();
        ClientsModel model = mUsers.get(i);
        viewHolder.setClientImageCover(mContext, model.getClientImageCover());
        viewHolder.setClientName(model.getClientName());
        viewHolder.setUsername(mContext, model.getUsername());
        viewHolder.checkLike(post_key);

        viewHolder.mView.setOnClickListener(view -> {
            if (model.getClientAvailability().equals("yes")) {
                viewHolder.setClientVisitor(model.getUsername());
                Intent intent = new Intent(mContext, ClientProfileActivity.class);
                intent.putExtra("key", post_key);
                intent.putExtra("name", model.getClientName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            } else {
                Toast.makeText(mContext, "هذا العميل غير متاح الأن", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.mLike.setOnClickListener(view -> viewHolder.likeFunction(post_key));

        viewHolder.mMessage.setOnClickListener(view -> {
            updateToken(FirebaseInstanceId.getInstance().getToken());
            Intent intent = new Intent(mContext, ChatActivity.class);
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
                                    mContext.startActivity(intent);
                                    break;
                                case 1:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    builder.setMessage("قدم أحد العملاء شكوي ضدك إحترس حتي لا يتم اغلاق حسابك!");
                                    // add a button
                                    builder.setPositiveButton("حسناً", (dialog, which) -> {
                                        mContext.startActivity(intent);
                                        dialog.cancel();
                                    });
                                    // create and show the alert dialog
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    break;
                                case 2:
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                                    builder2.setMessage("التحذير الثاني إحترس حتي لا يتم اغلاق حسابك!");
                                    // add a button
                                    builder2.setPositiveButton("حسناً", (dialog2, which) -> {
                                        mContext.startActivity(intent);
                                        dialog2.cancel();
                                    });
                                    // create and show the alert dialog
                                    AlertDialog dialog2 = builder2.create();
                                    dialog2.show();
                                    break;
                                case 3:
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(mContext);
                                    builder3.setMessage("التحذير الاخير إحترس حتي لا يتم اغلاق حسابك!");
                                    // add a button
                                    builder3.setPositiveButton("حسناً", (dialog3, which) -> {
                                        mContext.startActivity(intent);
                                        dialog3.cancel();
                                    });
                                    // create and show the alert dialog
                                    AlertDialog dialog3 = builder3.create();
                                    dialog3.show();
                                    break;
                                default:
                                    AlertDialog.Builder builder4= new AlertDialog.Builder(mContext);
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
                            mContext.startActivity(intent);
                        }
                    }else {
                        AlertDialog.Builder builder4= new AlertDialog.Builder(mContext);
                        builder4.setMessage("برجاء الذهاب وتفعيل حسابك اولا");
                        // add a button
                        builder4.setPositiveButton("حسناً", (dialog4, which) -> {
                            dialog4.cancel();
                            Intent intent = new Intent(mContext, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);
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
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clients").child(post_key);
//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String mNumber = null;
//                        for (DataSnapshot snapshot : dataSnapshot.child("Phone").getChildren()) {
//                            DataModel model = snapshot.getValue(DataModel.class);
//                            mNumber = model.getData();
//                        }
//                        mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mNumber)));
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });

                Intent intent = new Intent(mContext, PhoneNumbersActivity.class);
                intent.putExtra("key", model.getUsername());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }, 100);
        });
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(mAuth.getUid()).setValue(token1);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        //        Typeface tf;
//        String fontPath;
        private final static int FADE_DURATION = 100;
        private DatabaseReference mDatabase;
        private String count;

        ImageView mLike;

        private FirebaseAuth mAuth;

        private String mUserId;

        public ImageView mPhone, mMessage, mDeliveryIcon, mAvailableIcon;

        private TextView mVisitorNumTxt, mDeliveryTxt, mAvailableTxt;

        private CircleImageView mProfile;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
//            fontPath = "fonts/andalus.ttf";
//            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
            setFadeAnimation(mView);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);

            mUserId = mAuth.getCurrentUser().getUid();

            mLike = mView.findViewById(R.id.likeClient);

            mPhone = mView.findViewById(R.id.call);
            mMessage = mView.findViewById(R.id.message);

            mDeliveryIcon = mView.findViewById(R.id.deliveryIcon);
            mAvailableIcon = mView.findViewById(R.id.availableIcon);

            mVisitorNumTxt = mView.findViewById(R.id.visitorNum);
            mDeliveryTxt = mView.findViewById(R.id.delivery);
            mAvailableTxt = mView.findViewById(R.id.available);

            mProfile = mView.findViewById(R.id.imageProfile);

        }

        public void setUsername(Context ctx, String username) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child("Clients").child(username)
                            .child("PhoneAvailability").getValue().toString().equals("yes")){
                        mPhone.setVisibility(View.GONE);
                    }else{
                        mPhone.setVisibility(View.VISIBLE);
                    }

                    if (!dataSnapshot.child("Clients").child(username)
                            .child("ChatAvailability").getValue().toString().equals("yes")){
                        mMessage.setVisibility(View.GONE);
                    }else{
                        mMessage.setVisibility(View.VISIBLE);
                    }

                    if (!dataSnapshot.child("Clients").child(username)
                            .child("ClientAvailability").getValue().toString().equals("yes")){
                        mAvailableIcon.setVisibility(View.GONE);
                        mAvailableTxt.setVisibility(View.GONE);
                    }else{
                        mAvailableIcon.setVisibility(View.VISIBLE);
                        mAvailableTxt.setVisibility(View.VISIBLE);
                    }

                    if (dataSnapshot.child("Clients").child(username)
                            .child("Delivery").exists()){
                        if (!dataSnapshot.child("Clients").child(username)
                                .child("Delivery").getValue().toString().equals("yes")){
                            mDeliveryIcon.setVisibility(View.GONE);
                            mDeliveryTxt.setVisibility(View.GONE);
                        }else{
                            mDeliveryIcon.setVisibility(View.VISIBLE);
                            mDeliveryTxt.setVisibility(View.VISIBLE);
                        }
                    }else{
                        mDeliveryIcon.setVisibility(View.GONE);
                        mDeliveryTxt.setVisibility(View.GONE);
                    }

                    if (!dataSnapshot.child("Clients").child(username)
                            .child("ClientImageProfile").getValue().toString().equals("null")){
                        Picasso.with(ctx).load(dataSnapshot.child("Clients").child(username)
                                .child("ClientImageProfile").getValue().toString())
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.easy_order_splash)
                                .into(mProfile, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(ctx).load(dataSnapshot.child("Clients").child(username)
                                                .child("ClientImageProfile").getValue().toString())
                                                .placeholder(R.drawable.easy_order_splash).into(mProfile);
                                    }
                                });
                    }

                    mVisitorNumTxt.setText(dataSnapshot.child("Clients").child(username).child("ClientVisitor").getValue().toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setClientImageCover(final Context ctx, final String categoryImage) {
            final ImageView imageView = mView.findViewById(R.id.row_image);
//            Picasso.with(ctx).load(categoryImage).into(imageView);
            Picasso.with(ctx).load(categoryImage)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
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

        public void setClientName(String categoryName) {
            TextView textView = mView.findViewById(R.id.row_txt);
//            textView.setTypeface(tf);
            textView.setText(categoryName);
        }

        public void setClientVisitor(String client) {
            new Handler().postDelayed(() -> mDatabase.child("Clients").child(client).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count = dataSnapshot.child("ClientVisitor").getValue().toString();
                    DatabaseReference databaseReference = dataSnapshot.getRef();
                    databaseReference.child("ClientVisitor").setValue(String.valueOf(Integer.parseInt(count) + 1));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }),100);

        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }

        public void checkLike(String mClient) {
            mDatabase.child("Users").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Favourite").child(mClient).exists()) {
                        mLike.setImageResource(R.drawable.icons8_hearts_red);
                    } else {
                        mLike.setImageResource(R.drawable.icons8_hearts_white);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        public void likeFunction(String mClient) {
            mDatabase.child("Users").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Favourite").child(mClient).exists()) {
                        mLike.setImageResource(R.drawable.icons8_hearts_white);
                        mDatabase.child("Users").child(mUserId).child("Favourite").child(mClient).removeValue();
                    } else {
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
}
