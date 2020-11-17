package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blacklight.Models.Customers;
import com.example.blacklight.Models.Merchants;
import com.example.blacklight.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {

    ImageView main_logo_image;
    TextView main_retailer_intro, main_customer_intro;
    Button main_sign_in_btn, main_sign_up_btn;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ;
        main_logo_image = findViewById(R.id.main_logo);
        main_retailer_intro = findViewById(R.id.retailer_introduction);
        main_customer_intro = findViewById(R.id.customer_introduction);
        main_sign_in_btn = findViewById(R.id.main_sign_in);
        main_sign_up_btn = findViewById(R.id.main_sign_up);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        String CustomerPhoneKey = Paper.book().read(Prevalent.CustomerPhoneKey);
        String CustomerPasswordKey = Paper.book().read(Prevalent.CustomerPasswordKey);

        String MerchantPhoneKey = Paper.book().read(Prevalent.MerchantPhoneKey);
        String MerchantPasswordKey = Paper.book().read(Prevalent.MerchantPasswordKey);

        if (CustomerPhoneKey != "" && CustomerPasswordKey != ""){
            if (!TextUtils.isEmpty(CustomerPhoneKey) && !TextUtils.isEmpty(CustomerPhoneKey)){
                AllowCustomerAccess(CustomerPhoneKey, CustomerPasswordKey);

                loadingBar.setTitle("Already Signed");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

        else if (MerchantPhoneKey != "" && MerchantPasswordKey != ""){
            if (!TextUtils.isEmpty(MerchantPhoneKey) && !TextUtils.isEmpty(MerchantPasswordKey)){
                AllowMerchantAccess(MerchantPhoneKey, MerchantPasswordKey);

                loadingBar.setTitle("Already Signed");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }

    private void AllowMerchantAccess(final String merchantPhoneKey, final String merchantPasswordKey) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Merchants").child(merchantPhoneKey).exists()){

                    Merchants merchantsData = snapshot.child("Merchants").child(merchantPhoneKey).getValue(Merchants.class);

                    if (merchantsData.getMerchant_phoneNumber().equals(merchantPhoneKey)){

                        if (merchantsData.getMerchant_password().equals(merchantPasswordKey)){

                            Toast.makeText(MainActivity.this, "Welcome Back...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, MerchantStoreActivity.class);
                            Prevalent.currentOnlineMerchant = merchantsData;
                            startActivity(intent);
                            finish();
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else {
                    Toast.makeText(MainActivity.this, "This " + merchantPhoneKey + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
                Toast.makeText(MainActivity.this, "Unexpected error. Please try again later...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void AllowCustomerAccess(final String customerPhoneKey, final String customerPasswordKey) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Customers").child(customerPhoneKey).exists()){

                    Customers customersData = snapshot.child("Customers").child(customerPhoneKey).getValue(Customers.class);

                    if (customersData.getPhoneNumber().equals(customerPhoneKey)){

                        if (customersData.getPassword().equals(customerPasswordKey)){

                            Toast.makeText(MainActivity.this, "Welcome Back...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineCustomer = customersData;
                            startActivity(intent);
                            finish();
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else {
                    Toast.makeText(MainActivity.this, "This " + customerPhoneKey + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
                Toast.makeText(MainActivity.this, "Unexpected error. Please try again later...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendToSignIn(View view){
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void sendToSignUp(View view){
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void startShopping (View view){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}