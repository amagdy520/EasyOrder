package com.easy.order.MainScreens;

import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easy.order.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinUsActivity extends AppCompatActivity {

    private TextView mTitleTxt;
    private Button mJoinUs;
    private DatabaseReference mDatabase;
    private EditText mName, mPhone, mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mTitleTxt = findViewById(R.id.titleTxt);
        mJoinUs = findViewById(R.id.joinUsNow);
        String fontPath = "fonts/andalus.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mTitleTxt.setTypeface(tf);
        mJoinUs.setTypeface(tf);

        mName = findViewById(R.id.input_name);
        mPhone = findViewById(R.id.input_number);
        mCategory = findViewById(R.id.input_category);

        mJoinUs.setOnClickListener(view -> {
            if (mName.getText().toString().isEmpty() || mPhone.getText().toString().isEmpty() || mCategory.getText().toString().isEmpty() || mPhone.getText().toString().length() != 11){
                if (mName.getText().toString().isEmpty()){
                    mName.setError("برجاء إضافة إسمك!");
                }else{
                    mName.setError(null);
                }

                if (mCategory.getText().toString().isEmpty()){
                    mCategory.setError("برجاء إضافة نوع عملك!");
                }else{
                    mCategory.setError(null);
                }

                if (mPhone.getText().toString().isEmpty() || mPhone.getText().toString().length() != 11){
                    mPhone.setError("برجاء إدخال رقم هاتفك بشكل صحيح!");
                }else{
                    mPhone.setError(null);
                }

            }else{

                DatabaseReference databaseReference = mDatabase.child("JoinUs").push();
                databaseReference.child("Name").setValue(mName.getText().toString());
                databaseReference.child("Phone").setValue(mPhone.getText().toString());
                databaseReference.child("Category").setValue(mCategory.getText().toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(JoinUsActivity.this);
                builder.setTitle("تم إرسال طلبك بنجاح");
                builder.setMessage("نشكرك لرغبتك في الإنضمام إلينا سوف يتواصل معك أحد من ممثلي خدمة العملاء قريباً جداً.");
                // add a button
                builder.setPositiveButton("حسناً", (dialog, which) -> {
                    dialog.cancel();
                    onBackPressed();
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
}
