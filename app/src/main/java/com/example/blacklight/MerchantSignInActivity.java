package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blacklight.Models.Merchants;
import com.example.blacklight.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MerchantSignInActivity extends AppCompatActivity {

    private TextView sign_in_merchant_title, sign_in_merchant_subtitle, sign_in_merchant_forgot_password, merchant_other_sign_options;
    private EditText sign_in_merchant_phone_input, sign_in_merchant_password_input;
    private Button sign_in_merchant_btn, sign_in_as_a_customer_instead_btn;
    private CheckBox sign_in_merchant_remember_me_checkbox;
    private ImageView sign_in_merchant_with_google, sign_in_merchant_with_twitter;

    private ProgressDialog loadingBar;


    private final String parentDBName = "Merchants";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_sign_in);

        sign_in_merchant_title = findViewById(R.id.merchant_welcome_back);
        sign_in_merchant_subtitle = findViewById(R.id.merchant_continue_selling);
        sign_in_merchant_phone_input = findViewById(R.id.sign_in_merchant_phone_number);
        sign_in_merchant_password_input = findViewById(R.id.sign_in_merchant_password);
        sign_in_merchant_remember_me_checkbox = findViewById(R.id.sign_in_merchant_checkbox);
        sign_in_merchant_forgot_password = findViewById(R.id.sign_in_merchant_forgot_password);
        sign_in_merchant_btn = findViewById(R.id.sign_in_merchant_button);
        sign_in_as_a_customer_instead_btn = findViewById(R.id.sign_in_customer_option);
        merchant_other_sign_options = findViewById(R.id.sign_in_merchant_extra_options);
        sign_in_merchant_with_google = findViewById(R.id.sign_in_merchant_with_google_social);
        sign_in_merchant_with_twitter = findViewById(R.id.sign_in_merchant_with_twitter_social);
        loadingBar = new ProgressDialog(this);

    }

    public void signInExistingMerchantAccount(View view) {

        SignInExistingMerchantStore();
    }

    private void SignInExistingMerchantStore() {

        String merchant_phoneNumber = sign_in_merchant_phone_input.getText().toString();
        String merchant_password = sign_in_merchant_password_input.getText().toString();

        if (TextUtils.isEmpty(merchant_phoneNumber)){
            Toast.makeText(this, "Please type in your phone number", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(merchant_password)){
            Toast.makeText(this, "But you need a password", Toast.LENGTH_SHORT).show();
        }

        else {

            loadingBar.setTitle("Signing Into Your Store");
            loadingBar.setMessage("Please wait while we verify your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToExistingMerchantStore(merchant_phoneNumber, merchant_password);

        }
    }

    private void AllowAccessToExistingMerchantStore(String merchant_phoneNumber, String merchant_password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDBName).child(merchant_phoneNumber).exists()){

                    Merchants merchantsData = snapshot.child(parentDBName).child(merchant_phoneNumber).getValue(Merchants.class);

                    if (merchantsData.getMerchant_phoneNumber().equals(merchant_phoneNumber)){

                        if (merchantsData.getMerchant_password().equals(merchant_password)){

                            Toast.makeText(MerchantSignInActivity.this, "Signed in successfully...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MerchantSignInActivity.this, MerchantStoreActivity.class);
                            Prevalent.currentOnlineMerchant = merchantsData;
                            startActivity(intent);
                            finish();
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MerchantSignInActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else {
                    Toast.makeText(MerchantSignInActivity.this, "This " + merchant_phoneNumber + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(MerchantSignInActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
                Toast.makeText(MerchantSignInActivity.this, "Unexpected error. Please try again later...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sentToCustomerSignIn(View view) {
        Intent intent = new Intent(MerchantSignInActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}