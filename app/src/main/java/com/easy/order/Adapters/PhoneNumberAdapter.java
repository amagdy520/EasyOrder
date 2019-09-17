package com.easy.order.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easy.order.FirebaseModels.PhoneNumberModel;
import com.easy.order.R;

import java.util.List;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder> {

    private Context mContext;
    private List<PhoneNumberModel> mUsers;

    public PhoneNumberAdapter(Context mContext, List<PhoneNumberModel> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public PhoneNumberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_phone_numbers, viewGroup, false);
        return new PhoneNumberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneNumberAdapter.ViewHolder viewHolder, int i) {
        PhoneNumberModel dataModel = mUsers.get(i);
        viewHolder.mPhoneTxt.setText(dataModel.getPhoneNumber());
        viewHolder.itemView.setOnClickListener(view -> mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataModel.getPhoneNumber()))));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mPhoneTxt;

        public ViewHolder(View itemView) {
            super(itemView);

            mPhoneTxt = itemView.findViewById(R.id.row_txt);
        }
    }
}
