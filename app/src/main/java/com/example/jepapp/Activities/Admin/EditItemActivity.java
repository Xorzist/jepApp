package com.example.jepapp.Activities.Admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditItemActivity  extends AppCompatActivity {
    private static final Object TAG ="Creating An Item Class";


    ProgressBar progressBar;
    private ImageView imageview;
    //private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;

    String imagestatement;
    EditText dish_name,dish_ingredients,item_price;
    Button createbtn;
    private Bitmap bitmap;
    private StorageReference mStorageRef;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    private Uri downloadUrl;
    private Uri contentURI;
    private String newkey;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_create_food_item);
        //Firebase Storage
        //mStorageRef = FirebaseStorage.getInstance().getReference();
        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");

        //Intent data
        String newdishtitle= getIntent().getExtras().getString("title");
        String newingredients= getIntent().getExtras().getString("ingredients");
        String newprice= getIntent().getExtras().getString("price");
        String newimage= getIntent().getExtras().getString("image");
        newkey= getIntent().getExtras().getString("key");

        progressBar=findViewById(R.id.progressor);
        requestMultiplePermissions();
        dish_name = findViewById(R.id.dish_name);
        dish_ingredients = findViewById(R.id.dish_ingredients);
        item_price = findViewById(R.id.pricer);
        imageview = (ImageView) findViewById(R.id.iv);
        createbtn = findViewById(R.id.create_dish);
        dish_name.setText(newdishtitle);
        dish_ingredients.setText(newingredients);
        item_price.setText(newprice);
        if (newimage.equals("Empty")){
            imageview.setBackgroundResource(R.drawable.upload);
        }
        else{
            Picasso.with(getApplicationContext())
                    .load(newimage)
                    .into(imageview);
            }

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        createbtn.setText("Update Item");
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String DishName=dish_name.getText().toString().trim();
                String DishIng=dish_ingredients.getText().toString().trim();
                String itemprice=item_price.getText().toString().trim();
//                if (getDownloadUrl() == null)
//                    Log.d("Image Statement :  ", String.valueOf(getDownloadUrl()));
//                downloadUrl= Uri.parse("Empty");

                if(DishName.isEmpty()||DishName.length()>100){
                    Log.d("Checker", "Name Checked");
                    Toast.makeText(getApplicationContext(), "Title field is empty or contains too many characters ", Toast.LENGTH_LONG).show();
                }
                else if (DishIng.isEmpty()||DishIng.length()>400){
                    Log.d("Checker", "Empty Amount Checked");
                    Toast.makeText(getApplicationContext(), "Ingredients field is empty or contains too many characters ", Toast.LENGTH_LONG).show();

                }
                else if (itemprice.isEmpty()||itemprice.length()>9){
                    Log.d("Checker", "Empty Amount Checked");
                    Toast.makeText(getApplicationContext(), "Item Cost field is empty or contains too many values ", Toast.LENGTH_LONG).show();

                }


                else{
                    ItemCreator(DishName,DishIng,itemprice);
                    onBackPressed();
//                    Intent intent = new Intent(getApplicationContext(), AdminPageforViewPager.class);
//                    startActivity(intent);





                }


            }
        });
    }

    private void ItemCreator(String dishName, String dishIng, String itemprice) {
        MItems mItems;
        String newimage= getIntent().getExtras().getString("image");
        String key = getIntent().getExtras().getString("key");
        Log.e("DAMN", key);
        if (getDownloadUrl() == null){
            if (newimage.equals("Empty")){
                mItems = new MItems(key,mAuth.getUid(),dishName,dishIng,Float.valueOf(itemprice),"Empty");
            }
            else{
                mItems = new MItems(key,mAuth.getUid(),dishName,dishIng,Float.valueOf(itemprice),newimage);
            }
        }
        else{
            mItems = new MItems(key,mAuth.getUid(),dishName,dishIng,Float.valueOf(itemprice),getDownloadUrl().toString());
        }
        getDb().child("MenuItems")
                .child(key)
                .setValue(mItems);
        Log.d("Start Adding","START!");
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        File wallpaperDirectory = new File(
//                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
//        // have the object build the directory structure, if needed.
//        if (!wallpaperDirectory.exists()) {
//            wallpaperDirectory.mkdirs();
//        }
//
//        try {
//            File f = new File(wallpaperDirectory, Calendar.getInstance()
//                    .getTimeInMillis() + ".jpg");
//            f.createNewFile();
//            FileOutputStream fo = new FileOutputStream(f);
//            fo.write(bytes.toByteArray());
//            MediaScannerConnection.scanFile(this.getApplicationContext(),
//                    new String[]{f.getPath()},
//                    new String[]{"image/jpeg"}, null);
//            fo.close();
//            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());
//
//            return f.getAbsolutePath();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
        return "";
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity((this))
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //  Toast.makeText(CreateItem.getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        //  Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Context context= this.getApplicationContext();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY) {
            if (data != null) {
                //Transforms image data to a uri
                setContentURI(data.getData());
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                //Create a folder called images in storage
                StorageReference imagesRef = storageRef.child("images");
                StorageReference userRef = imagesRef.child(mAuth.getUid());
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String filename = mAuth.getUid() + "_" + timeStamp;
                final StorageReference fileRef = userRef.child(filename);
                final ProgressDialog progressDialog = new ProgressDialog(EditItemActivity.this);
                progressDialog.setMax(100);
                progressDialog.setMessage("Uploading Item");


                //Commence attempt to upload to firebase
                UploadTask uploadTask = fileRef.putFile(getContentURI());
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        progressDialog.show();
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL

                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            setDownloadUrl(task.getResult());
                            Log.d("Uploader", "Success " + String.valueOf(getDownloadUrl()));
                            //Toast.makeText(CreatingItem.this, "Uploading Image", Toast.LENGTH_LONG).show();
                        } else {

                            Log.d("Uploader", "Failure");

                        }
                    }
                });



                //Try and catch clause for putting image into the image view widget.
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), getContentURI());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageview.setImageBitmap(bitmap);
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            setContentURI(getImageUri(this,thumbnail));

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            //Create a folder called images in storage
            StorageReference imagesRef = storageRef.child("images");
            StorageReference userRef = imagesRef.child(mAuth.getUid());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = mAuth.getUid() + "_" + timeStamp;
            final StorageReference fileRef = userRef.child(filename);
            final ProgressDialog progressDialog = new ProgressDialog(EditItemActivity.this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading Item");


            //Commence attempt to upload to firebase
            UploadTask uploadTask = fileRef.putFile(getContentURI());
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    progressDialog.show();
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        setDownloadUrl(task.getResult());
                        Log.d("Uploader", "Success " + String.valueOf(getDownloadUrl()));
                        //Toast.makeText(CreatingItem.this, "Uploading Image", Toast.LENGTH_LONG).show();
                    } else {

                        Log.d("Uploader", "Failure");

                    }
                }
            });
            //Try and  catch clause for putting image into the image view widget.
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), getContentURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path = saveImage(thumbnail);
            imageview.setImageBitmap(thumbnail);
//            imageview.setImageBitmap(thumbnail);
//            saveImage(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(this.getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
    public DatabaseReference getDb() {
        return myDBRef;
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public Uri getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(Uri downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Uri getContentURI() {
        return contentURI;
    }

    public void setContentURI(Uri contentURI) {
        this.contentURI = contentURI;
    }
}
