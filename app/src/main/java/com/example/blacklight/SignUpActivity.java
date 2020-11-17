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

import com.example.blacklight.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private TextView sign_up_title, sign_up_subtitle, other_sign_up_options;
    private EditText sign_up_username_input, sign_up_phone_input, sign_up_password_input;
    private Button sign_up_btn, sign_up_new_merchant_btn;
    private ImageView sign_up_with_google, sign_up_with_twitter;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_up_title = findViewById(R.id.create_personal_account);
        sign_up_subtitle = findViewById(R.id.sign_up_start_shopping);
        sign_up_username_input = findViewById(R.id.sign_up_username);
        sign_up_phone_input = findViewById(R.id.sign_up_phone_number);
        sign_up_password_input = findViewById(R.id.sign_up_password);
        sign_up_btn = findViewById(R.id.sign_up_button);
        sign_up_new_merchant_btn = findViewById(R.id.sign_up_merchant_option);
        other_sign_up_options = findViewById(R.id.sign_up_options);
        sign_up_with_google = findViewById(R.id.sign_up_google_social);
        sign_up_with_twitter = findViewById(R.id.sign_up_twitter_social);
        loadingBar = new ProgressDialog(this);

    }

    public void signUpNewCustomerAccount (View view){

        CreateNewCustomerAccount();

    }

    private void CreateNewCustomerAccount() {

        String username = sign_up_username_input.getText().toString();
        String phoneNumber = sign_up_phone_input.getText().toString();
        String password = sign_up_password_input.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "What should we call you?", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "Please type in your phone number", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "But you need a password", Toast.LENGTH_SHORT).show();
        }


        else {

            loadingBar.setTitle("Signing Up Your New Account");
            loadingBar.setMessage("Please wait while we verify your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateNewUser(username, phoneNumber, password);
        }
    }

    private void ValidateNewUser(String username, String phoneNumber, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Customers").child(phoneNumber).exists())){

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("phoneNumber", phoneNumber);
                    userdataMap.put("password", password);

                    RootRef.child("Customers").child(phoneNumber).updateChildren(userdataMap).addOnCompleteListener(task -> {

                        if (task.isSuccessful()){

                            Toast.makeText(SignUpActivity.this, "Congratulations, your account has been created...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        else {
                            Toast.makeText(SignUpActivity.this, "Network error, please try again...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    });

                }
                else {
                    Toast.makeText(SignUpActivity.this, "This " + phoneNumber + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(SignUpActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpActivity.this, "There seems to be a problem. Please try again later...", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }

    public void sendToSignUpNewMerchantAccount(View view){

        Intent intent = new Intent(SignUpActivity.this, MerchantSignUpActivity.class);
        startActivity(intent);
        finish();
    }
}