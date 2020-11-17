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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blacklight.Models.Customers;
import com.example.blacklight.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {

    private TextView sign_in_title, sign_in_subtitle, sign_in_forgot_password_option, other_sign_in_options;
    private EditText sign_in_phone_input, sign_in_password_input;
    private Button sign_in_btn, sign_in_as_a_merchant_instead_btn;
    private CheckBox sign_in_remember_me_checkbox;
    private ImageView sign_in_with_google, sign_in_with_twitter;

    private ProgressDialog loadingBar;


    private final String parentDBName = "Customers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sign_in_title = findViewById(R.id.welcome_back);
        sign_in_subtitle = findViewById(R.id.continue_shopping);
        sign_in_phone_input = findViewById(R.id.sign_in_phone_number);
        sign_in_password_input = findViewById(R.id.sign_in_password);
        sign_in_remember_me_checkbox = findViewById(R.id.sign_in_checkbox);
        sign_in_forgot_password_option = findViewById(R.id.sign_in_forgot_password);
        sign_in_btn = findViewById(R.id.sign_in_button);
        sign_in_as_a_merchant_instead_btn = findViewById(R.id.sign_in_merchant_option);
        other_sign_in_options = findViewById(R.id.sign_in_options);
        sign_in_with_google = findViewById(R.id.sign_in_google_social);
        sign_in_with_twitter = findViewById(R.id.sign_in_twitter_social);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);
    }

    public void signInExistingCustomerAccount (View view){

        SignInExistingCustomer();
    }


    private void SignInExistingCustomer() {

        String phoneNumber = sign_in_phone_input.getText().toString();
        String password = sign_in_password_input.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "Please type in your phone number", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "But you need a password", Toast.LENGTH_SHORT).show();
        }

        else {

            loadingBar.setTitle("Signing Into Your Account");
            loadingBar.setMessage("Please wait while we verify your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToExistingUser(phoneNumber, password);


        }
    }

    private void AllowAccessToExistingUser(String phoneNumber, String password) {

        if (sign_in_remember_me_checkbox.isChecked()){
            Paper.book().write(Prevalent.CustomerPhoneKey, phoneNumber);
            Paper.book().write(Prevalent.CustomerPasswordKey, password);
        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDBName).child(phoneNumber).exists()){

                    Customers customersData = snapshot.child(parentDBName).child(phoneNumber).getValue(Customers.class);

                    if (customersData.getPhoneNumber().equals(phoneNumber)){

                        if (customersData.getPassword().equals(password)){

                            if (parentDBName.equals("Customers")){

                                Toast.makeText(SignInActivity.this, "Signed in successfully...", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineCustomer = customersData;
                                startActivity(intent);
                                finish();

                            }
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(SignInActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else {
                    Toast.makeText(SignInActivity.this, "This " + phoneNumber + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(SignInActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
                Toast.makeText(SignInActivity.this, "Unexpected error. Please try again later...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendToSignInExistingMerchantAccount(View view) {

        Intent intent = new Intent(SignInActivity.this, MerchantSignInActivity.class);
        startActivity(intent);
        finish();
    }
}
