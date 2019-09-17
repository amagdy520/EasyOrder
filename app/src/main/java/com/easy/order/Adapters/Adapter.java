package com.easy.order.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easy.order.FirebaseModels.GalleryModel;
import com.easy.order.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends PagerAdapter {

    private List<GalleryModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public Adapter(List<GalleryModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        ZoomageView zoomageView = view.findViewById(R.id.zoomImage);

        Picasso.with(context).load(models.get(position).getImage()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.empty)
                .into(zoomageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(models.get(position).getImage()).placeholder(R.drawable.empty).into(zoomageView);
                    }
                });


        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
