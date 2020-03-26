package com.example.jepapp.Adapters.Users;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
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

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.Admin;
import com.example.jepapp.Fragments.User.BreakfastList;
import com.example.jepapp.Models.Cart;
import com.example.jepapp.Models.FoodItem;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.math.BigDecimal;
import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;
    private static int currentPosition = -1;
    //we are storing all the products in a list
    private List<Cart> foodItemList;
    private static DatabaseReference databasebreakfastreference,databaseLunchreference;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private List<FoodItem> breakfastitemsList,validbreakfastlist,lunchitemsList,validlunchList;
    private DatabaseReference databasereference;
    private DatabaseReference databaselunchreference;


    //getting the context and product list with constructor

    public cartAdapter(Context mCtx, List<Cart> foodItemList) {
        this.mCtx = mCtx;
        this.foodItemList = foodItemList;

    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cartitemslayout, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        databasereference = FirebaseDatabase.getInstance().getReference("JEP");
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(mCtx);

        progressDialog.setMessage("Loading Breakfast Items from Firebase Database");

        progressDialog.show();
        //  foodItemList = new ArrayList<>();

        databasebreakfastreference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
        getBreakfastMenu();

        databaselunchreference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");
        getLunchMenu();




        return holder;
    }

    private void getLunchMenu() {
        databaselunchreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodItem lunchdetails = dataSnapshot.getValue(FoodItem.class);

                    lunchitemsList.add(lunchdetails);

                }

                for(FoodItem lunchitem : lunchitemsList){
                    for(Cart cartitem: foodItemList){
                        if (lunchitem.getTitle().equals(cartitem.getOrdertitle())){
                            validbreakfastlist.add(lunchitem);
                        }
                    }

                }

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

    }

    private void getBreakfastMenu() {
        databasebreakfastreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FoodItem breakfastDetails = dataSnapshot.getValue(FoodItem.class);

                    breakfastitemsList.add(breakfastDetails);

                }

                for(FoodItem breakfastitem : breakfastitemsList){
                    for(Cart cartitem: foodItemList){
                        if (breakfastitem.getTitle().equals(cartitem.getOrdertitle())){
                            validbreakfastlist.add(breakfastitem);
                        }
                    }

                }

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        //getting the item of the specified position
        final Cart item = foodItemList.get(position);

        //binding the data with the viewholder views
        holder.itemtitle.setText(item.getOrdertitle());
        holder.textViewPrice.setText(String.valueOf(item.getCost()));
        holder.textViewQuantity.setText(String.valueOf(item.getQuantity()));
        holder.addquantity.setText(item.getQuantity());

        Picasso.with(mCtx)
                .load(item.getImage())
                .into(holder.imageView);
        Picasso.with(mCtx)
                .load(item.getImage())
                .transform(new ProductViewHolder.CircleTransform()).into(holder.imageView);

        holder.buttonslayout.setVisibility(View.GONE);
        // using holder position to display/hide buttons on holder
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

            //toggling visibility
            holder.buttonslayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.buttonslayout.startAnimation(slideDown);
        }
        else if (currentPosition == -1){
            Animation slideUp = AnimationUtils.loadAnimation(mCtx, R.anim.slide_up);
            holder.buttonslayout.setVisibility(View.GONE);

            //adding sliding effect
            holder.buttonslayout.startAnimation(slideUp);

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

                //reloading the list
                notifyItemChanged(position);
            }

        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.buttonslayout.getVisibility() == View.VISIBLE){
                    holder.buttonslayout.setVisibility(View.GONE);
                }
                else{
                    holder.buttonslayout.setVisibility(View.VISIBLE);
                }


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating alert dialog to confirm/cancel item deletion
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                alertDialogBuilder.setTitle("Delete Item");
                alertDialogBuilder.setMessage("Do you want to delete " + item.getOrdertitle()+ " ?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteItem(item);
                                ((Activity)mCtx).finish();
                                ((Activity)mCtx).startActivity(((Activity) mCtx).getIntent());

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

        //Function to increment the desired quantity by 1
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldvalue = holder.addquantity.getText().toString();
                String newvalue = String.valueOf((Integer.valueOf(oldvalue)+1));
                holder.addquantity.setText(newvalue);
            }
        });
        //Function to decrement the desired quantity by 1
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldvalue = holder.addquantity.getText().toString();
                String newvalue = String.valueOf((Integer.valueOf(oldvalue)-1));
                holder.addquantity.setText(newvalue);
                Toast.makeText(mCtx,"Minus clicked",Toast.LENGTH_SHORT).show();
            }
        });


        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getType().toLowerCase().equals("breakfast")){
                    for (int i = 0;i<validbreakfastlist.size();i++) {
                        if (holder.itemtitle.getText().toString().equals(validbreakfastlist.get(i).getTitle())){
                            int desiredquantity = Integer.valueOf(holder.addquantity.getText().toString());
                            int actualquantity = Integer.valueOf(validbreakfastlist.get(i).getQuantity());
                            int difference = desiredquantity -actualquantity;
                            if(difference>0){


                            }

                        }
                    }

                }
                else if (item.getType().toLowerCase().equals("lunch")){


                }

            }
        });

    }
    public void deleteItem(Cart item) {
        //deletes item from database
        //dolieth added

        if (item.getType().toLowerCase().equals("breakfast")) {
            if (item.getUsername().equals("Admin")){
                databaseReference.child("BreakfastCart").child("Admin").child(item.getOrdertitle()).removeValue();
            }else {

                databaseReference.child("BreakfastCart").child(mAuth.getCurrentUser().getEmail().replace(".", "")).child(item.getOrdertitle()).removeValue();
            }
        }
        else{
            if (item.getUsername().equals("Admin")){
                databaseReference.child("LunchCart").child("Admin").child(item.getOrdertitle()).removeValue();
            }
            else {
                databaseReference.child("LunchCart").child(mAuth.getCurrentUser().getEmail().replace(".", "")).child(item.getOrdertitle()).removeValue();
            // }
            // databasereference.child("LunchCart").child(mAuth.getCurrentUser().getEmail().replace(".","")).child(item.getOrdertitle()).removeValue();

        }
    }
    public void UpdateMenu(Cart item) {
        //deletes item from database
        if (item.getType().toLowerCase().equals("breakfast")) {
            databasereference.child("BreakfastCart").child(mAuth.getCurrentUser().getEmail().replace(".","")).child(item.getOrdertitle()).removeValue();

        }
        else{
            databasereference.child("LunchCart").child(mAuth.getCurrentUser().getEmail().replace(".","")).child(item.getOrdertitle()).removeValue();

        }
    }



    @Override
    public int getItemCount() {
        return foodItemList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView itemtitle, ingredients, textViewPrice, textViewQuantity;
        ImageView imageView;
        EditText addquantity;
        LinearLayout parentLayout,buttonslayout;
        Button delete,minus,plus,update;

        public ProductViewHolder(View itemView) {
            super(itemView);

            itemtitle = itemView.findViewById(R.id.carttitle);
            ingredients = itemView.findViewById(R.id.cartingredients);
            textViewPrice = itemView.findViewById(R.id.cartprice);
            textViewQuantity = itemView.findViewById(R.id.cartquantity);
            imageView = itemView.findViewById(R.id.cartImage);
            parentLayout = itemView.findViewById(R.id.parent_layoutcart);
            buttonslayout = itemView.findViewById(R.id.editdeletebuttons);
            delete = itemView.findViewById(R.id.deletecartitem);
            addquantity = itemView.findViewById(R.id.addcartquantity);
            minus = itemView.findViewById(R.id.minuscart);
            plus = itemView.findViewById(R.id.pluscart);
            update =itemView.findViewById(R.id.updatecart);



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
                return null;
            }
        }

    }
}
