package com.example.jepapp.Adapters.Admin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Admin.EditItemActivity;
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
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AllitemsAdapter extends RecyclerView.Adapter<AllitemsAdapter.AllitemsViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    public List<MItems> MenuItemList;
    private static DatabaseReference databaseReference;
    private static int currentPosition = -1;
    private int GALLERY = 1, CAMERA = 2;
    private Uri downloadUrl;
    private Uri contentURI;
    DatabaseReference myDBRef;
    //private DatabaseReference myDBRef;


    //getting the context and product list with constructor
    public AllitemsAdapter(Context mCtx, List<MItems> MenuItemList) {
        this.mCtx = mCtx;
        this.MenuItemList = MenuItemList;


    }


    @Override
    public AllitemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        //myDBRef = FirebaseDatabase.getInstance().getReference();
        View view = inflater.inflate(R.layout.all_menu_items_recylayout, null);
        AllitemsViewHolder holder = new AllitemsViewHolder(view);
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");
        return holder;
    }


    @Override
    public void onBindViewHolder(final AllitemsAdapter.AllitemsViewHolder holder, final int position) {
        //getting the item of the specified position
        final MItems item = MenuItemList.get(position);
        //binding the data with the viewholder views
        holder.Title.setText(item.getTitle());

        holder.Prices.setText(String.valueOf(item.getPrice()));
        //holder.Imageurl.setText(item.getImage());

        Picasso.with(mCtx)
                .load(item.getImage())
                .transform(new AllitemsViewHolder.CircleTransform())


                .into(holder.itempics);

        holder.buttonslinearlayout.setVisibility(View.GONE);

        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

            //toggling visibility
            holder.buttonslinearlayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.buttonslinearlayout.startAnimation(slideDown);
        }
        else if (currentPosition == -1){
            Animation slideUp = AnimationUtils.loadAnimation(mCtx, R.anim.slide_up);
            holder.buttonslinearlayout.setVisibility(View.GONE);

            //adding sliding effect
            holder.buttonslinearlayout.startAnimation(slideUp);

        }
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                if(currentPosition==position){
                    currentPosition = -1;

                }
                else if (currentPosition!=position){
                    currentPosition = position;
                }


                //reloding the list
                notifyItemChanged(position);
            }

        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                alertDialogBuilder.setTitle("Delete Item");
                alertDialogBuilder.setMessage("Do you want to delete " + item.getTitle()+ " ?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteItem(item);

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(item, position);
                notifyItemChanged(position);
            }
        });

//        holder.edit.setOnClickListener(new View.OnClickListener() {

//        holder.deletbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LayoutInflater li = LayoutInflater.from(mCtx);
//
//                View promptsView = li.inflate(R.layout.admin_create_food_item, null);
//                final android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(mCtx);
//                builder1.setView(promptsView);
//                builder1.setTitle("Edit");
//                builder1.setCancelable(true);
//                final EditText dish_name,dish_ingredients,item_price;
//                final Button createbtn;
//                final ImageView imageview;
//                dish_name = promptsView.findViewById(R.id.dish_name);
//                dish_ingredients = promptsView.findViewById(R.id.dish_ingredients);
//                item_price = promptsView.findViewById(R.id.pricer);
//                imageview = (ImageView) promptsView.findViewById(R.id.iv);
//                createbtn = promptsView.findViewById(R.id.create_dish);
//                dish_name.setText(item.getTitle());
//                dish_ingredients.setText(item.getIngredients());
//                item_price.setText(item.getPrice().toString());
//                if (item.getImage().equals("Empty")){
//                    imageview.setBackgroundResource(R.drawable.upload);
//                }
//                else{
//                    Picasso.with(mCtx)
//                            .load(item.getImage())
//                            .into(imageview);
//                }
//                imageview.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showPictureDialog();
//                    }
//                });
//               // String new_image = imageview.
//                builder1.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        String DishName=dish_name.getText().toString().trim();
//                        String DishIng=dish_ingredients.getText().toString().trim();
//                        String itemprice=item_price.getText().toString().trim();
//                        //String Image = imageview.get
////                if (getDownloadUrl() == null)
////                    Log.d("Image Statement :  ", String.valueOf(getDownloadUrl()));
////                downloadUrl= Uri.parse("Empty");
//
//                        if(DishName.isEmpty()||DishName.length()>100){
//                            Log.d("Checker", "Name Checked");
//                            Toast.makeText(mCtx, "Title field is empty or contains too many characters ", Toast.LENGTH_LONG).show();
//                        }
//                        else if (DishIng.isEmpty()||DishIng.length()>400){
//                            Log.d("Checker", "Empty Amount Checked");
//                            Toast.makeText(mCtx, "Ingredients field is empty or contains too many characters ", Toast.LENGTH_LONG).show();
//
//                        }
//                        else if (itemprice.isEmpty()||itemprice.length()>9){
//                            Log.d("Checker", "Empty Amount Checked");
//                            Toast.makeText(mCtx, "Item Cost field is empty or contains too many values ", Toast.LENGTH_LONG).show();
//
//                        }
//
//
//                        else{
//                            ItemCreator(DishName,DishIng,itemprice,item);
//
//
//                        }dialog.dismiss();
//
//                    }
//                });
//
//                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                android.app.AlertDialog alertDialog = builder1.create();
//                alertDialog.show();
//
//            }
//        });

    }

    public void deleteItem(MItems item) {

        databaseReference.child(item.getKey()).removeValue();
    }

    public void editItem(MItems item, int position) {
        String key = item.getKey();
        String  title = item.getTitle();
        String  ingredients = item.getIngredients();
        String  image = item.getImage();
        String price = String.valueOf(item.getPrice());
        Intent intent = new Intent(mCtx, EditItemActivity.class);
        intent.putExtra("key", String.valueOf(key));
        intent.putExtra("title", title);
        intent.putExtra("ingredients", ingredients);
        intent.putExtra("price", price);
        intent.putExtra("image", image);

        mCtx.startActivity(intent);

    }
//    public DatabaseReference getDb() {

//        return myDBRef;
//    }
    public void EditItems(MItems item) {
        databaseReference.child(item.getKey()).removeValue();
    }

    private void ItemCreator(String dishName, String dishIng, String itemprice, MItems item) {
        MItems mItems;
        String newimage= item.getImage();
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String key = item.getKey();
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
        Activity origin = (Activity)mCtx;
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        origin.startActivityForResult(galleryIntent, GALLERY);
    }

    private void requestMultiplePermissions() {
        Activity origin = (Activity)mCtx;
        Dexter.withActivity((origin))
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
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(mCtx);
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
        Activity origin = (Activity)mCtx;
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        origin.startActivityForResult(intent, CAMERA);
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Context context= mCtx;
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GALLERY) {
//            if (data != null) {
//                //Transforms image data to a uri
//                setContentURI(data.getData());
//                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//                //Create a folder called images in storage
//                StorageReference imagesRef = storageRef.child("images");
//                StorageReference userRef = imagesRef.child(mAuth.getUid());
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                String filename = mAuth.getUid() + "_" + timeStamp;
//                final StorageReference fileRef = userRef.child(filename);
//                final ProgressDialog progressDialog = new ProgressDialog(EditItemActivity.this);
//                progressDialog.setMax(100);
//                progressDialog.setMessage("Uploading Item");
//
//
//                //Commence attempt to upload to firebase
//                UploadTask uploadTask = fileRef.putFile(getContentURI());
//                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                        progressDialog.show();
//                        if (!task.isSuccessful()) {
//                            throw task.getException();
//                        }
//
//                        // Continue with the task to get the download URL
//
//                        return fileRef.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        progressDialog.dismiss();
//                        if (task.isSuccessful()) {
//                            setDownloadUrl(task.getResult());
//                            Log.d("Uploader", "Success " + String.valueOf(getDownloadUrl()));
//                            //Toast.makeText(CreatingItem.this, "Uploading Image", Toast.LENGTH_LONG).show();
//                        } else {
//
//                            Log.d("Uploader", "Failure");
//
//                        }
//                    }
//                });
//
//
//
//                //Try and catch clause for putting image into the image view widget.
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), getContentURI());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                imageview.setImageBitmap(bitmap);
//            }
//
//        } else if (requestCode == CAMERA) {
//            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            setContentURI(getImageUri(this,thumbnail));
//
//            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//            //Create a folder called images in storage
//            StorageReference imagesRef = storageRef.child("images");
//            StorageReference userRef = imagesRef.child(mAuth.getUid());
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            String filename = mAuth.getUid() + "_" + timeStamp;
//            final StorageReference fileRef = userRef.child(filename);
//            final ProgressDialog progressDialog = new ProgressDialog(EditItemActivity.this);
//            progressDialog.setMax(100);
//            progressDialog.setMessage("Uploading Item");
//
//
//            //Commence attempt to upload to firebase
//            UploadTask uploadTask = fileRef.putFile(getContentURI());
//            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    progressDialog.show();
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//
//                    // Continue with the task to get the download URL
//
//                    return fileRef.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    progressDialog.dismiss();
//                    if (task.isSuccessful()) {
//                        setDownloadUrl(task.getResult());
//                        Log.d("Uploader", "Success " + String.valueOf(getDownloadUrl()));
//                        //Toast.makeText(CreatingItem.this, "Uploading Image", Toast.LENGTH_LONG).show();
//                    } else {
//
//                        Log.d("Uploader", "Failure");
//
//                    }
//                }
//            });
//            //Try and  catch clause for putting image into the image view widget.
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), getContentURI());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String path = saveImage(thumbnail);
//            imageview.setImageBitmap(thumbnail);
////            imageview.setImageBitmap(thumbnail);
////            saveImage(thumbnail);
//            saveImage(thumbnail);
//            Toast.makeText(this.getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
//        }
//    }
    public DatabaseReference getDb() {
        return myDBRef;
    }
//    private Uri getImageUri(Context context, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
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


//

    @Override
    public int getItemCount() {
        return MenuItemList.size();
    }


    static class AllitemsViewHolder extends RecyclerView.ViewHolder {
        TextView Title, Prices, Imageurl;
        ImageView deletbtn, itempics;
        Button edit, delete;
        LinearLayout parentLayout, buttonslinearlayout;

        public AllitemsViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.itemtitle);
            // deletbtn=itemView.findViewById(R.id.deleteitem);
            itempics = itemView.findViewById(R.id.itempic);
            Prices = itemView.findViewById(R.id.prices);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            Imageurl = itemView.findViewById(R.id.imageurl);
            parentLayout = itemView.findViewById(R.id.allitemslinearLayout);
            buttonslinearlayout = itemView.findViewById(R.id.buttonslinearLayout);

        }


        public static class CircleTransform implements Transformation {
            @Override
            public Bitmap transform(Bitmap source) {
                int size = Math.min(source.getWidth(), source.getHeight());

                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;

                Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                if (squaredBitmap != source) {
                    source.recycle();
                }

                Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                paint.setShader(shader);
                paint.setAntiAlias(true);

                float r = size / 2f;
                canvas.drawCircle(r, r, r, paint);

                squaredBitmap.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "circle";
            }
        }
    }
}
