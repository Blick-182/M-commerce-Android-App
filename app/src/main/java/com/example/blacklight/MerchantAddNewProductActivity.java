package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MerchantAddNewProductActivity extends AppCompatActivity {

    private String categoryName, newProductName, newProductDescription, newProductPrice, saveCurrentDate, saveCurrentTime;

    private Toolbar addNewProductToolbar;
    private ImageView addNewProductImage;
    private Button inputNewProductImage_btn, saveNewProduct_btn;
    private EditText inputNewProductTitle, inputNewProductDescription, inputNewProductPrice;
    private ProgressDialog loadingBar;

    private Uri ImageUri;
    private static final int GalleryPick = 1000;
    private static final int PERMISSION_CODE = 1001;
    private String productRandomKey, downloadProductImageURL;

    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_add_new_product);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        categoryName = getIntent().getExtras().get("category").toString();

        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        addNewProductToolbar = findViewById(R.id.merchant_new_inventory_toolbar);
        addNewProductImage = findViewById(R.id.add_new_product_image);
        inputNewProductImage_btn = findViewById(R.id.add_new_product_image_button);
        inputNewProductTitle = findViewById(R.id.add_new_product_title);
        inputNewProductDescription = findViewById(R.id.add_new_product_description);
        inputNewProductPrice = findViewById(R.id.add_new_product_price);
        saveNewProduct_btn = findViewById(R.id.save_new_product_button);
        loadingBar = new ProgressDialog(this);

        addNewProductImage.setOnClickListener(v -> {
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

        saveNewProduct_btn.setOnClickListener(v -> ValidateNewInventoryData());
    }


    public void cancelAndRedirectToChooseInventory(View view) {
        Intent intent = new Intent(MerchantAddNewProductActivity.this, MerchantChooseInventoryCategoryActivity.class);
        startActivity(intent);
        finish();
    }

    public void chooseProductImage(View view) {
        OpenGallery();
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
            addNewProductImage.setImageURI(ImageUri);

           // addNewProductImage.setImageURI(data.getData());
        }

      /*  if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null){

            Uri selectImage = data.getData();
            InputStream inputStream = null;
            try {
                assert selectImage != null;
                inputStream = getContentResolver().openInputStream(selectImage);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }

            BitmapFactory.decodeStream(inputStream);
            addNewProductImage.setImageURI(selectImage);
        }  */
    }

    private void ValidateNewInventoryData() {

        newProductName = inputNewProductTitle.getText().toString();
        newProductDescription = inputNewProductDescription.getText().toString();
        newProductPrice = inputNewProductPrice.getText().toString();

        if (ImageUri == null){
            Toast.makeText(this, "Please add a product image...", Toast.LENGTH_SHORT).show();
        }
        else if (ImageUri != null){
            Toast.makeText(this, "Image added...", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(newProductName)){
            Toast.makeText(this, "Type the product name", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.isEmpty(newProductName))){
            Toast.makeText(this, "Product name added", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(newProductDescription)){
            Toast.makeText(this, "Type the product description", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.isEmpty(newProductDescription))){
            Toast.makeText(this, "Product description added", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(newProductPrice)){
            Toast.makeText(this, "Type the product price", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.isEmpty(newProductPrice))){

            Toast.makeText(this, "Product price added", Toast.LENGTH_SHORT).show();

            SaveNewProductData();
        }
    }

    private void SaveNewProductData() {

        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Please wait while we add the new product...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + " " + saveCurrentTime;

        StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(e -> {
            String message = e.toString();
            Toast.makeText(MerchantAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(MerchantAddNewProductActivity.this, "Image uploaded successfully...", Toast.LENGTH_SHORT).show();

            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {

                if (!task.isSuccessful()){
                    throw task.getException();
                }

                downloadProductImageURL = filePath.getDownloadUrl().toString();
                return filePath.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()){

                    downloadProductImageURL = task.getResult().toString();

                    Toast.makeText(MerchantAddNewProductActivity.this, "Product Image URL successfully saved...", Toast.LENGTH_SHORT).show();

                    SavingNewProductDataToDatabase();
                }
            });
        });
    }

    private void SavingNewProductDataToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("name", newProductName);
        productMap.put("description", newProductDescription);
        productMap.put("price", newProductPrice);
        productMap.put("image", downloadProductImageURL);
        productMap.put("category", categoryName);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);

        ProductsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                Intent intent = new Intent(MerchantAddNewProductActivity.this, MerchantStoreActivity.class);
                startActivity(intent);
                finish();

                loadingBar.dismiss();
                Toast.makeText(this, "Product added successfully...", Toast.LENGTH_SHORT).show();
            }
            else {
                loadingBar.dismiss();
                String message = task.getException().toString();
                Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }


}