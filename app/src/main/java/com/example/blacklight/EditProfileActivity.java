  package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blacklight.Prevalent.Prevalent;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import io.paperdb.Paper;

public class EditProfileActivity extends AppCompatActivity {

    private Toolbar editProfileToolBar;
    private TextView editProfileTitle, editProfileShippingDetails;
    private CardView editProfileCardImage;
    private ImageView editProfileImage;
    private Button editProfileAddImageBtn, editProfileSaveNowBtn;
    private EditText editProfileFirstName, editProfileLastName, editProfileCounty, editProfileSubCounty, editProfileCity, editProfileApartment;
    private ProgressDialog loadingBar;

    private Uri ImageUri;
    private static final int GalleryPick = 1000;
    private static final int PERMISSION_CODE = 1001;

    private String downloadProfileImageURL;
    private String addFirstName, addLastName, addCounty, addSubCounty, addCity, addApartment;

    private StorageReference ProfileImagesRef;
    private DatabaseReference CustomersShippingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Paper.init(this);
        Prevalent.currentOnlineCustomer.getPhoneNumber();

        ProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Customer Profile Images");
        CustomersShippingRef = FirebaseDatabase.getInstance().getReference().child("Shipping Details");

        editProfileToolBar = findViewById(R.id.edit_profile_toolbar);
        editProfileTitle = findViewById(R.id.edit_profile_title);
        editProfileCardImage = findViewById(R.id.edit_profile_card_image);
        editProfileImage = findViewById(R.id.edit_profile_image);
        editProfileAddImageBtn = findViewById(R.id.edit_profile_add_image);
        editProfileShippingDetails = findViewById(R.id.edit_profile_shipping_details);
        editProfileFirstName = findViewById(R.id.edit_profile_first_name);
        editProfileLastName = findViewById(R.id.edit_profile_last_name);
        editProfileCounty = findViewById(R.id.edit_profile_county);
        editProfileSubCounty = findViewById(R.id.edit_profile_sub_county);
        editProfileCity = findViewById(R.id.edit_profile_town_or_city);
        editProfileApartment = findViewById(R.id.edit_profile_apartment);
        editProfileSaveNowBtn = findViewById(R.id.edit_profile_save_now);
        loadingBar = new ProgressDialog(this);


        setSupportActionBar(editProfileToolBar);

        editProfileImage.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else {
                    OpenGallery();
                }
            }
            else {
                OpenGallery();
            }
        });



    }

    public void getCustomerShippingDetails(View view) {
        validateCustomerShippingDetails();
    }

    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    OpenGallery();
                }
                else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null){

            ImageUri = data.getData();
            editProfileImage.setImageURI(ImageUri);
        }
    }

    private void validateCustomerShippingDetails() {

        addFirstName = editProfileFirstName.getText().toString();
        addLastName = editProfileLastName.getText().toString();
        addCounty = editProfileCounty.getText().toString();
        addSubCounty = editProfileSubCounty.getText().toString();
        addCity = editProfileCity.getText().toString();
        addApartment = editProfileApartment.getText().toString();

        if (ImageUri == null){
            Toast.makeText(this, "Please add a profile image...", Toast.LENGTH_SHORT).show();
        }
        else if (ImageUri != null){
            Toast.makeText(this, "Profile image added...", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(addFirstName)){
            Toast.makeText(this, "Type in your first name", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.isEmpty(addFirstName))){
            Toast.makeText(this, "First name added", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(addLastName)){
            Toast.makeText(this, "Type in your last name", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.isEmpty(addLastName))){
            Toast.makeText(this, "Last name added", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(addCounty)){
            Toast.makeText(this, "Which County are you from?", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.isEmpty(addCounty))){
            Toast.makeText(this, "County added", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(addSubCounty)){
            Toast.makeText(this, "Which Sub-County are you from?", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.isEmpty(addSubCounty))){
            Toast.makeText(this, "Sub-County added", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(addCity)){
            Toast.makeText(this, "Which is the closest town or city?", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.isEmpty(addCity))){
            Toast.makeText(this, "Town added", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(addApartment)){
            Toast.makeText(this, "Where will you pick your orders?", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.isEmpty(addApartment))){

            Toast.makeText(this, "Pick up spot added", Toast.LENGTH_SHORT).show();

            SaveCustomerShippingData();
        }

    }

    private void SaveCustomerShippingData() {

        loadingBar.setTitle("Adding Shipping Details");
        loadingBar.setMessage("Please wait while we save your shipping details...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        StorageReference filePath = ProfileImagesRef.child(ImageUri.getLastPathSegment() + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(e -> {
            String message = e.toString();
            Toast.makeText(EditProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(EditProfileActivity.this, "Image uploaded successfully...", Toast.LENGTH_SHORT).show();

            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {

                if (!task.isSuccessful()){
                    throw task.getException();
                }

                downloadProfileImageURL = filePath.getDownloadUrl().toString();
                return filePath.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()){

                    downloadProfileImageURL = task.getResult().toString();

                    Toast.makeText(EditProfileActivity.this, "Profile Image URL successfully saved...", Toast.LENGTH_SHORT).show();

                    SavingNewCustomerShippingDataToDatabase();
                }
            });
        });
    }

    private void SavingNewCustomerShippingDataToDatabase() {

        HashMap<String, Object> shippingdataMap = new HashMap<>();
        shippingdataMap.put("firstName", addFirstName);
        shippingdataMap.put("lastName", addLastName);
        shippingdataMap.put("county", addCounty);
        shippingdataMap.put("image", downloadProfileImageURL);
        shippingdataMap.put("subCounty", addSubCounty);
        shippingdataMap.put("city", addCity);
        shippingdataMap.put("pickUpSpot", addApartment);

        CustomersShippingRef.child(Prevalent.currentOnlineCustomer.getPhoneNumber()).updateChildren(shippingdataMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

                loadingBar.dismiss();
                Toast.makeText(this, "Profile edited successfully...", Toast.LENGTH_SHORT).show();
            }
            else {
                loadingBar.dismiss();
                String message = task.getException().toString();
                Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_profile_appbar_menu, menu);
        return true;
    }

    public void sendToMarketplace(MenuItem item) {

        Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}