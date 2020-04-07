package com.example.jepapp.Adapters.Admin;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Admin.AdminCart;
import com.example.jepapp.Activities.Admin.EditItemActivity;
import com.example.jepapp.Models.Admin;
import com.example.jepapp.Models.Cart;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.List;

public class AllitemsAdapter extends RecyclerView.Adapter<AllitemsAdapter.AllitemsViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    public List<MItems> MenuItemList;
    private static DatabaseReference databaseReference;

    //used to set the adapter position to null
    private static int currentPosition = -1;
    String person;

    DatabaseReference myDBRef;


    //getting the context and product list with constructor
    public AllitemsAdapter(Context mCtx, List<MItems> MenuItemList) {
        this.mCtx = mCtx;
        this.MenuItemList = MenuItemList;


    }

    public AllitemsAdapter(Context applicationContext, List<MItems> foodItemList, String admin) {
        this.mCtx = applicationContext;
        this.MenuItemList = foodItemList;
        this.person = admin;

    }



    @Override
    public AllitemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.all_menu_items_recylayout, null);
        AllitemsViewHolder holder = new AllitemsViewHolder(view);
        //calling the firebase nodes
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");
        MultiDex.install(mCtx);
        //return view holder
        return holder;
    }


    @Override
    public void onBindViewHolder(final AllitemsAdapter.AllitemsViewHolder holder, final int position) {
        //getting the item of the specified position

        final MItems item = MenuItemList.get(position);
        //binding the data with the viewholder views
        holder.Title.setText(item.getTitle());
        holder.Prices.setText(String.valueOf(item.getPrice()));
        Picasso.with(mCtx)
                .load(item.getImage())
                .transform(new AllitemsViewHolder.CircleTransform()).into(holder.itempics);


        holder.buttonslinearlayout.setVisibility(View.GONE);
        holder.addcartlayout.setVisibility(View.GONE);

        if (person != null) {
            if (currentPosition == position) {
                //creating an animation
                Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

                //toggling visibility
                holder.addcartlayout.setVisibility(View.VISIBLE);

                //adding sliding effect
                holder.addcartlayout.startAnimation(slideDown);
            } else if (currentPosition == -1) {
                Animation slideUp = AnimationUtils.loadAnimation(mCtx, R.anim.slide_up);
                holder.addcartlayout.setVisibility(View.GONE);

                //adding sliding effect
                holder.addcartlayout.startAnimation(slideUp);

            }
        } else {
            // using holder position to display/hide buttons on holder
            if (currentPosition == position) {
                //creating an animation
                Animation slideDown = AnimationUtils.loadAnimation(mCtx, R.anim.slide_down);

                //toggling visibility
                holder.buttonslinearlayout.setVisibility(View.VISIBLE);

                //adding sliding effect
                holder.buttonslinearlayout.startAnimation(slideDown);
            } else if (currentPosition == -1) {
                Animation slideUp = AnimationUtils.loadAnimation(mCtx, R.anim.slide_up);
                holder.buttonslinearlayout.setVisibility(View.GONE);

                //adding sliding effect
                holder.buttonslinearlayout.startAnimation(slideUp);

            }
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                if (currentPosition == position) {
                    currentPosition = -1;

                } else if (currentPosition != position) {
                    currentPosition = position;
                }

                //reloading the list
                notifyItemChanged(position);
            }

        });

        //delete function for holder item
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating alert dialog to confirm/cancel item deletion
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
        //editing an item on the holder
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(item, position);
                notifyItemChanged(position);
            }
        });

        holder.plusquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldvalue = holder.addquantity.getText().toString();
                String newvalue = String.valueOf((Integer.valueOf(oldvalue)+1));
                holder.addquantity.setText(newvalue);
            }
        });
        //Function to decrement the desired quantity by 1
        holder.minusquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldvalue = holder.addquantity.getText().toString();
                String newvalue = String.valueOf((Integer.valueOf(oldvalue)-1));
                holder.addquantity.setText(newvalue);
                Toast.makeText(mCtx,"Minus clicked",Toast.LENGTH_SHORT).show();
            }
        });

        holder.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Statement to check if the value enter is less than or equal to zero or exceeds the quantity
                if ((Integer.valueOf(holder.addquantity.getText().toString()) <= 0)){
                    Toast.makeText(mCtx,"Please correct the value entered",Toast.LENGTH_SHORT).show();

                }
                else{

                    String type = "Breakfast";
                    String username = "Admin";
                    com.example.jepapp.Models.Cart cart = new Cart(item.getPrice().toString(),item.getImage(),item.getTitle(), holder.addquantity.getText().toString(),type,username);
                    FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastCart")
                            .child(username)
                            .child(item.getTitle())
                            .setValue(cart);
                    notifyItemChanged(position);
//                    Intent mIntent= new Intent(mCtx, AdminCart.class);
//                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mCtx.startActivity(mIntent);
                    ((Activity) mCtx).finish();
     //               mCtx.startActivity(new Intent(mCtx, AdminCart.class));

                }

            }

        });

    }

    public void deleteItem(MItems item) {
        //deletes item from database
        databaseReference.child(item.getKey()).removeValue();
    }

    public void editItem(MItems item, int position) {
        String key = item.getKey();
        String  title = item.getTitle();
        String  ingredients = item.getIngredients();
        String  image = item.getImage();
        String price = String.valueOf(item.getPrice());
        //sends information to intent class
        Intent intent = new Intent(mCtx, EditItemActivity.class);
        intent.putExtra("key", String.valueOf(key));
        intent.putExtra("title", title);
        intent.putExtra("ingredients", ingredients);
        intent.putExtra("price", price);
        intent.putExtra("image", image);

        mCtx.startActivity(intent);

    }


    @Override
    //determines the number of items to be reflected in the recycler view
    public int getItemCount() {
        return MenuItemList.size();
    }


    static class AllitemsViewHolder extends RecyclerView.ViewHolder {
        TextView Title, Prices, Imageurl;
        ImageView itempics;
        EditText addquantity;
        Button edit, delete, addcart,plusquantity,minusquantity;
        LinearLayout parentLayout, buttonslinearlayout, addcartlayout;

        public AllitemsViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.itemtitle);
            itempics = itemView.findViewById(R.id.itempic);
            Prices = itemView.findViewById(R.id.prices);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            Imageurl = itemView.findViewById(R.id.imageurl);
            parentLayout = itemView.findViewById(R.id.allitemslinearLayout);
            buttonslinearlayout = itemView.findViewById(R.id.buttonslinearLayout);
            addcartlayout = itemView.findViewById(R.id.addcartlayout);
            addcart = itemView.findViewById(R.id.adminaddtocart);
            addquantity = itemView.findViewById(R.id.adminaddquantity);
            plusquantity = itemView.findViewById(R.id.adminplusquantity);
            minusquantity = itemView.findViewById(R.id.adminminusquantity);

        }

        //gives image a circular shape
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
