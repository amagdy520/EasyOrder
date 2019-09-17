package com.easy.order.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.easy.order.FirebaseModels.MenuCategoryModel;
import com.easy.order.MainScreens.ClientProfileActivity;
import com.easy.order.R;

import java.util.List;

public class ProfileMenuCategoryAdapter extends RecyclerView.Adapter<ProfileMenuCategoryAdapter.ViewHolder>  {

    private Context mContext;
    private List<MenuCategoryModel> mCategory;
    private ClientProfileActivity clientProfileActivity;

    public ProfileMenuCategoryAdapter(Context mContext, List<MenuCategoryModel> mCategory) {
        this.mContext = mContext;
        this.mCategory = mCategory;
        clientProfileActivity = new ClientProfileActivity();
    }

    @NonNull
    @Override
    public ProfileMenuCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_category_menu, viewGroup, false);
        return new ProfileMenuCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileMenuCategoryAdapter.ViewHolder viewHolder, int i) {
        MenuCategoryModel menuCategoryModel = mCategory.get(i);
        viewHolder.textView.setText(menuCategoryModel.getCategoryName());
        viewHolder.textView.setTypeface(viewHolder.tf);
        viewHolder.itemView.setOnClickListener(view -> clientProfileActivity.showMenuCategory(menuCategoryModel.getCategoryName()));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        Typeface tf;
        String fontPath;
        private final static int FADE_DURATION = 100;

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            fontPath = "fonts/bziba.ttf";
            textView = mView.findViewById(R.id.txt_menu_category);
            textView.setTypeface(tf);
            tf = Typeface.createFromAsset(mView.getContext().getAssets(), fontPath);
            setFadeAnimation(mView);
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }
}
