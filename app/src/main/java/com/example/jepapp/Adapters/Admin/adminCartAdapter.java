package com.example.jepapp.Adapters.Admin;

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

import com.example.jepapp.Models.Cart;
import com.example.jepapp.Models.FoodItem;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class adminCartAdapter extends RecyclerView.Adapter<adminCartAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;
    private static int currentPosition = -1;
    //we are storing all the products in a list
    private List<Cart> foodItemList;
    private static DatabaseReference databasebreakfastreference;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private List<FoodItem> breakfastitemsList,validbreakfastlist,lunchitemsList,validlunchList;
    private DatabaseReference databasereference;
    private DatabaseReference databaselunchreference;
    private DatabaseReference usersdatabaseReference;
    private List<UserCredentials> Userslist;
    private String username;


    //getting the context and product list with constructor

    public adminCartAdapter(Context mCtx, List<Cart> foodItemList) {
        this.mCtx = mCtx;
        this.foodItemList = foodItemList;

    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.admin_make_menu_layout_admin, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        databasereference = FirebaseDatabase.getInstance().getReference("JEP");
        mAuth = FirebaseAuth.getInstance();
        usersdatabaseReference = databasereference.child("Users");
        Userslist = new ArrayList<>();
        breakfastitemsList = new ArrayList<>();
        validbreakfastlist = new ArrayList<>();
        lunchitemsList = new ArrayList<>();
        validlunchList = new ArrayList<>();

        //Function to get the username of current user
        getUsername();

        databasebreakfastreference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
        //Function to get only the items in the BreakfastMenu table that are in the cart
        getBreakfastMenu();

        databaselunchreference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");
        //Function to get only the items in the Lunch table  that are in the cart
        getLunchMenu();



        return holder;
    }


    private void getUsername() {
        usersdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);

                    Userslist.add(allusers);

                }
                for (int i = 0; i < Userslist.size(); i++) {
                    if (mAuth.getUid().equals(Userslist.get(i).getUserID()))
                        username = Userslist.get(i).getUsername();
                }

            }@Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
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
                            validlunchList.add(lunchitem);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        //getting the item of the specified position
        final Cart item = foodItemList.get(position);

        //binding the data with the viewholder views
        holder.itemtitle.setText(item.getOrdertitle());
        holder.textViewQuantity.setText(String.valueOf(item.getQuantity()));
        holder.addquantity.setText(item.getQuantity());


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
                                ((Activity) mCtx).finish();
                                ((Activity) mCtx).startActivity(((Activity) mCtx).getIntent());

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
                if (oldvalue.isEmpty()){
                    String newvalue = String.valueOf((1));
                    holder.addquantity.setText(newvalue);
                }
                else {
                    String newvalue = String.valueOf((Integer.valueOf(oldvalue) + 1));
                    holder.addquantity.setText(newvalue);
                }
            }
        });
        //Function to decrement the desired quantity by 1
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldvalue = holder.addquantity.getText().toString();
                if (oldvalue.isEmpty() || oldvalue.equals("0")){
                    String newvalue = String.valueOf((1));
                    holder.addquantity.setText(newvalue);
                }
                else {
                    String newvalue = String.valueOf((Integer.valueOf(oldvalue) - 1));
                    holder.addquantity.setText(newvalue);
                }

            }
        });


        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //Statement to check if the entered value is negative,null or 0
                    if (holder.addquantity.getText().toString().isEmpty() || holder.addquantity.getText().toString().equals("0")
                            || (Integer.valueOf(holder.addquantity.getText().toString()) < 0)) {
                        holder.addquantity.setError("Please enter a valid quantity");

                    }
                    int desiredquantity = Integer.valueOf(holder.addquantity.getText().toString());
                    if (desiredquantity > 0) {
                        if (item.getType().toLowerCase().equals("breakfast")){
                            Cart cartbreakfast = new Cart(item.getCost(), item.getImage(), item.getOrdertitle(),
                                    String.valueOf(desiredquantity), item.getType(), item.getUsername(), item.getIngredients(),item.getID());
                            databasereference.child("BreakfastCart")
                                    .child(item.getUsername())
                                    .child(item.getOrdertitle())
                                    .setValue(cartbreakfast);
                            ((Activity) mCtx).finish();
                            ((Activity) mCtx).startActivity(((Activity) mCtx).getIntent());

                        }else{
                            Cart cartlunch = new Cart(item.getCost(), item.getImage(), item.getOrdertitle(),
                                    String.valueOf(desiredquantity), item.getType(), item.getUsername(),item.getIngredients(),item.getID());
                            databasereference.child("LunchCart")
                                    .child(item.getUsername())
                                    .child(item.getOrdertitle())
                                    .setValue(cartlunch);
                            ((Activity) mCtx).finish();
                            ((Activity) mCtx).startActivity(((Activity) mCtx).getIntent());
                        }

                    }
                }

        });

    }
    public void deleteItem(Cart item) {
        //deletes item from database
        //dolieth added

        if (item.getType().toLowerCase().equals("breakfast")) {
                databasereference.child("BreakfastCart").child("Admin Menu").child(item.getOrdertitle()).removeValue();
        } else {

                databasereference.child("LunchCart").child("Admin Menu").child(item.getOrdertitle()).removeValue();
            }

    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView itemtitle, textViewQuantity;
       // ImageView imageView;
        EditText addquantity;
        LinearLayout parentLayout,buttonslayout;
        Button delete,minus,plus,update;

        public ProductViewHolder(View itemView) {
            super(itemView);

            itemtitle = itemView.findViewById(R.id.admin_menu_title);
//            ingredients = itemView.findViewById(R.id.cartingredients);
//            textViewPrice = itemView.findViewById(R.id.cartprice);
            textViewQuantity = itemView.findViewById(R.id.admin_menu_quantity);
//            imageView = itemView.findViewById(R.id.cartImage);
            parentLayout = itemView.findViewById(R.id.parent_layout2);
            buttonslayout = itemView.findViewById(R.id.editdeletebuttons);
            delete = itemView.findViewById(R.id.deletecartitem);
            addquantity = itemView.findViewById(R.id.addcartquantity);
            minus = itemView.findViewById(R.id.minuscart);
            plus = itemView.findViewById(R.id.pluscart);
            update =itemView.findViewById(R.id.updatecart);



        }



    }
}
