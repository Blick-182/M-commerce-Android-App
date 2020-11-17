package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MerchantSignUpActivity extends AppCompatActivity {

    private TextView merchant_title, merchant_subtitle, other_merchant_sign_up_options;
    private EditText sign_up_merchant_store_name_input, sign_up_merchant_phone_input, sign_up_merchant_password_input;
    private Button sign_up_new_merchant_btn, send_to_sign_up_new_customer_btn;
    private ImageView sign_up_merchant_with_google, sign_up_merchant_with_twitter;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_sign_up);

        merchant_title = findViewById(R.id.create_merchant_account);
        merchant_subtitle = findViewById(R.id.sign_up_start_selling);
        send_to_sign_up_new_customer_btn = findViewById(R.id.sign_up_customer_option);
        other_merchant_sign_up_options = findViewById(R.id.sign_up_merchant_options);
        sign_up_merchant_store_name_input = findViewById(R.id.sign_up_merchant_name);
        sign_up_merchant_phone_input = findViewById(R.id.sign_up_merchant_phone_number);
        sign_up_merchant_password_input = findViewById(R.id.sign_up_merchant_password);
        sign_up_new_merchant_btn = findViewById(R.id.sign_up_merchant_button);
        sign_up_merchant_with_google = findViewById(R.id.sign_up_merchant_with_google_social);
        sign_up_merchant_with_twitter = findViewById(R.id.sign_up_merchant_with_twitter_social);

        loadingBar = new ProgressDialog(this);
    }

    public void signUpNewMerchantAccount(View view) {

        CreateNewMerchantStore();
    }

    private void CreateNewMerchantStore() {

        String merchant_storeName = sign_up_merchant_store_name_input.getText().toString();
        String merchant_phoneNumber = sign_up_merchant_phone_input.getText().toString();
        String merchant_password = sign_up_merchant_password_input.getText().toString();

        if (TextUtils.isEmpty(merchant_storeName)){
            Toast.makeText(this, "What should we call you?", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(merchant_phoneNumber)){
            Toast.makeText(this, "Please type in your phone number", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(merchant_password)){
            Toast.makeText(this, "But you need a password", Toast.LENGTH_SHORT).show();
        }


        else {

            loadingBar.setTitle("Signing Up Your New Account");
            loadingBar.setMessage("Please wait while we verify your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateNewUser(merchant_storeName, merchant_phoneNumber, merchant_password);
        }
    }

    private void ValidateNewUser(String merchant_storeName, String merchant_phoneNumber, String merchant_password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Merchants").child(merchant_phoneNumber).exists())){


                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("merchantName", merchant_storeName);
                    userdataMap.put("merchant_phoneNumber", merchant_phoneNumber);
                    userdataMap.put("merchant_password", merchant_password);

                    RootRef.child("Merchants").child(merchant_phoneNumber).updateChildren(userdataMap).addOnCompleteListener(task -> {

                        if (task.isSuccessful()){

                            Toast.makeText(MerchantSignUpActivity.this, "Congratulations, your merchant account has been created...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MerchantSignUpActivity.this, MerchantStoreActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        else {
                            Toast.makeText(MerchantSignUpActivity.this, "Network error, please try again...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    });

                }

                else {
                    Toast.makeText(MerchantSignUpActivity.this, "This " + merchant_phoneNumber + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(MerchantSignUpActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MerchantSignUpActivity.this, "There seems to be a problem. Please try again later...", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }

    public void sendToSignUpNewCustomerAccount(View view) {

        Intent intent = new Intent(MerchantSignUpActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();

    }
}