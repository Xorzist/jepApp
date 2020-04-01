package com.example.jepapp.Adapters.Users;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;


    //we are storing all the products in a list
    private List<FoodItem> foodItemList;
    private DatabaseReference myDBRef;
    private FirebaseAuth mAuth;
    private ArrayList<UserCredentials> Userslist;
    private DatabaseReference usersdatabaseReference;
    private String username;


    //getting the context and product list with constructor
    public FoodListAdapter(Context mCtx, List<FoodItem> foodItemList) {
        this.mCtx = mCtx;
        this.foodItemList = foodItemList;

    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.breakfast_products, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        mAuth = FirebaseAuth.getInstance();
        Userslist = new ArrayList<>();
        usersdatabaseReference = myDBRef.child("Users");
        mAuth = FirebaseAuth.getInstance();

        usersdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);

                    Userslist.add(allusers);

                    // Log.d("SIZERZ", String.valueOf(list.get(0).getTitle()));
                }
                for (int i = 0; i < Userslist.size(); i++) {
                    if (mAuth.getUid().equals(Userslist.get(i).getUserID()))
                        username = Userslist.get(i).getUsername();
                }

//                adapter = new SelectMenuItemsAdaptertest(SelectMenuItems.this, list);
//
//                recyclerView.setAdapter(adapter);



            }@Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        //getting the item of the specified position
        final FoodItem item = foodItemList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewIngredients.setText(item.getIngredients());
        holder.textViewPrice.setText(String.valueOf(item.getPrice()));
        holder.textViewQuantity.setText(String.valueOf(item.getQuantity()));


        Picasso.with(mCtx)
                .load(item.getImage())
                .into(holder.imageView);
        Picasso.with(mCtx)
                .load(item.getImage())
                .transform(new ProductViewHolder.CircleTransform()).into(holder.imageView);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.addcartlayout.getVisibility()==View.VISIBLE){
                    holder.addcartlayout.setVisibility(View.GONE);
                }
                else{
                    //Set layout to visisble
                    holder.addcartlayout.setVisibility(View.VISIBLE);

                }

            }
        });
        //Function to increment the desired quantity by 1
        holder.plusquantity.setOnClickListener(new View.OnClickListener() {
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
        holder.minusquantity.setOnClickListener(new View.OnClickListener() {
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

        holder.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Statement to check if the value enter is less than or equal to zero or exceeds the quantity
                if ((holder.addquantity.getText().toString().isEmpty()) )
                {
                    Toast.makeText(mCtx,"Please enter an actual amount",Toast.LENGTH_SHORT).show();

                }

                else{
                    if((Integer.valueOf(holder.addquantity.getText().toString()) <= 0) ||
                            (Integer.valueOf(holder.addquantity.getText().toString() )> Integer.valueOf(holder.textViewQuantity.getText().toString()))) {
                        Toast.makeText(mCtx, "The amount requested is not available", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String dishquantity = holder.addquantity.getText().toString();
                        String dishtitle = holder.textViewTitle.getText().toString().trim();
                        String dishprice = holder.textViewPrice.getText().toString().trim();
                        String dishtype = item.getType().toString();
                        String dishimage = item.getImage().toString();
                        //String dishpaidby = paidby.getSelectedItem().toString().trim();


                        if (dishtype.toLowerCase().equals("breakfast")) {
                            Cart cartbreakfast = new Cart(dishprice, dishimage, dishtitle, dishquantity, dishtype, username);
                            getDb().child("BreakfastCart")
                                    .child(mAuth.getCurrentUser().getEmail().replace(".", ""))
                                    .child(dishtitle)
                                    .setValue(cartbreakfast);
                            Log.d("Start Adding", "Your order has been made");
                            Toast.makeText(mCtx, "Your item has been placed in the cart", Toast.LENGTH_SHORT).show();
                            holder.addcartlayout.setVisibility(View.GONE);


                        } else {
                            Cart cartlunch = new Cart(dishprice, dishimage, dishtitle, dishquantity, dishtype, username);
                            getDb().child("LunchCart")
                                    .child(mAuth.getCurrentUser().getEmail().replace(".", ""))
                                    .child(dishtitle)
                                    .setValue(cartlunch);
                            Log.d("Start Adding", "Your order has been made");
                            Toast.makeText(mCtx, "Your item has been placed in the cart", Toast.LENGTH_SHORT).show();
                            holder.addcartlayout.setVisibility(View.GONE);
                        }
                    }

                    }

                }

        });
    }

    public DatabaseReference getDb() {
        return myDBRef;
    }


    @Override
    public int getItemCount() {
        return foodItemList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewIngredients, textViewPrice, textViewQuantity;
        ImageView imageView;

        LinearLayout parentLayout,addcartlayout;
        Button addcart,plusquantity,minusquantity;;
        EditText addquantity;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.title);
            textViewIngredients = itemView.findViewById(R.id.ingredients);
            textViewPrice = itemView.findViewById(R.id.price);
            textViewQuantity = itemView.findViewById(R.id.quantity);
            imageView = itemView.findViewById(R.id.foodImage);
            parentLayout = itemView.findViewById(R.id.parent_layoutbreakfast);
            addcartlayout = itemView.findViewById(R.id.addcartlayout);
            addcart = itemView.findViewById(R.id.addtocart);
            addquantity = itemView.findViewById(R.id.addquantity);
            plusquantity = itemView.findViewById(R.id.plusquantity);
            minusquantity = itemView.findViewById(R.id.minusquantity);


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
