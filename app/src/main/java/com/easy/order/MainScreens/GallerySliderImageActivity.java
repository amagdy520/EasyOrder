package com.easy.order.MainScreens;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.easy.order.Adapters.Adapter;
import com.easy.order.FirebaseModels.FirebaseAdsModel;
import com.easy.order.FirebaseModels.GalleryModel;
import com.easy.order.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GallerySliderImageActivity extends AppCompatActivity {

    private String mKey;
    private DatabaseReference mDatabase;
//    private SliderLayout mSlider;
    private List<GalleryModel> image_list;
//    private String id;

    private String position;

    ValueEventListener valueEventListener;

    Adapter adapter;

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_slider_image);
        mKey = getIntent().getExtras().getString("key");
        position = getIntent().getExtras().getString("position");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Clients").child(mKey).child("Gallery");
        mDatabase.keepSynced(true);
//        mSlider = findViewById(R.id.slider);



        viewPager = findViewById(R.id.viewPager);


        image_list = new ArrayList<>();
        valueEventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                image_list.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    GalleryModel banner = postSnapShot.getValue(GalleryModel.class);
                    image_list.add(banner);
                }

                adapter = new Adapter(image_list, GallerySliderImageActivity.this);
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(Integer.parseInt(position));
//                for (String key : image_list) {
//
//                    id = key;
//
//                    final DefaultSliderView defaultSliderView = new DefaultSliderView(GallerySliderImageActivity.this);
//                    defaultSliderView
//                            .image(key)
//                            .error(R.drawable.easy_order_splash)
//                            .empty(R.drawable.easy_order_splash)
//                            .setScaleType(BaseSliderView.ScaleType.CenterInside);
////                            .setOnSliderClickListener(slider -> {
////                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
////                                builder.setMessage("هل حقاً تريد الإعلان في التطبيق!؟");
////                                // add a button
////                                builder.setPositiveButton("نعم", (dialog, which) -> {
////                                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "01204454077")));
////                                    dialog.cancel();
////                                });
////
////                                builder.setNegativeButton("لا", (dialogInterface, i) -> dialogInterface.cancel());
////                                // create and show the alert dialog
////                                AlertDialog dialog = builder.create();
////                                dialog.show();
////                            });
//                    defaultSliderView.bundle(new Bundle());
//                    defaultSliderView.getBundle().putString("ID", id);
//                    mSlider.addSlider(defaultSliderView);
//                    mDatabase.removeEventListener(this);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        mSlider.setPresetTransformer(SliderLayout.Transformer.Default);
//        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mSlider.stopAutoCycle();

//        Toast.makeText(this, position, Toast.LENGTH_SHORT).show();

//        mSlider.setCurrentPosition(Integer.parseInt(position) + 1);
//        mSlider.setCustomAnimation(new DescriptionAnimation());
//        mSlider.setDuration(5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.removeEventListener(valueEventListener);
    }
}
