package com.example.jepapp.Adapters.Admin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.Models.Cart;
import com.example.jepapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class adminCartAdapter extends RecyclerView.Adapter<adminCartAdapter.ProductViewHolder> {

    private Context mCtx;
    private static int currentPosition = -1;
    private List<Cart> cartList;
    private DatabaseReference databasereference;


    public adminCartAdapter(Context mCtx, List<Cart> cartList) {
        this.mCtx = mCtx;
        this.cartList = cartList;

    }


    @NotNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.admin_make_menu_layout_admin, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        databasereference = FirebaseDatabase.getInstance().getReference("JEP");


        return holder;
    }


    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        //getting the item of the specified position
        final Cart item = cartList.get(position);

        //binding the data with the viewholder views
        holder.itemtitle.setText(item.getOrdertitle());
        holder.textViewQuantity.setText(String.valueOf(item.getQuantity()));
        holder.addquantity.setText(item.getQuantity());

        //setting linear layout to not display when the class is first launched
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
                if(currentPosition == position){
                    currentPosition = -1;

                }
                else {
                    currentPosition = position;
                }

                //reloading the list
                notifyItemChanged(position);
            }

        });


        // function to delete item from cart
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating alert dialog to confirm/cancel item deletion
                AlertDialog.Builder alertDialogDelete = new AlertDialog.Builder(mCtx,R.style.datepicker);
                alertDialogDelete.setTitle("Delete Item");
                alertDialogDelete.setMessage("Do you want to delete " + item.getOrdertitle()+ " ?");
                alertDialogDelete.setPositiveButton(R.string.dialogYes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteItem(item);
                                //reloads the cart
                                ReloadIt();

                            }
                        });

                alertDialogDelete.setNegativeButton(R.string.dialogNo,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialogDelete = alertDialogDelete.create();
                dialogDelete.show();

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

        //Function to update the quantity desired in the database
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //Statement to check if the entered value is negative,null or 0
                    if (holder.addquantity.getText().toString().isEmpty() || holder.addquantity.getText().toString().equals("0")
                            || (Integer.valueOf(holder.addquantity.getText().toString()) < 0)) {
                        holder.addquantity.setError("Please enter a valid quantity");

                    }
                    //update the quantity of item in corresponding database cart with the new desired quantity value
                    int desiredquantity = Integer.valueOf(holder.addquantity.getText().toString());
                        if (item.getType().toLowerCase().equals("breakfast")){
                            Cart cartbreakfast = new Cart(item.getCost(), item.getImage(), item.getOrdertitle(),
                                    String.valueOf(desiredquantity), item.getType(), item.getUsername(), item.getIngredients(),item.getID());
                            databasereference.child("BreakfastCart")
                                    .child(item.getUsername())
                                    .child(item.getOrdertitle())
                                    .setValue(cartbreakfast);

                            //Reloads cart
                            ReloadIt();


                        }else{
                            Cart cartlunch = new Cart(item.getCost(), item.getImage(), item.getOrdertitle(),
                                    String.valueOf(desiredquantity), item.getType(), item.getUsername(),item.getIngredients(),item.getID());
                            databasereference.child("LunchCart")
                                    .child(item.getUsername())
                                    .child(item.getOrdertitle())
                                    .setValue(cartlunch);
                            //Reloads cart
                            ReloadIt();
                        }

                    }


        });

    }

    private void ReloadIt() {
        ((Activity) mCtx).finish();
        currentPosition = -1;
        (mCtx).startActivity(((Activity) mCtx).getIntent());
    }

    //deletes item from database
    private void deleteItem(Cart item) {
       //checks which item type the item to be deleted is and deletes it from the corresponding cart.
        if (item.getType().toLowerCase().equals("breakfast")) {
                databasereference.child("BreakfastCart").child("Admin Menu").child(item.getOrdertitle()).removeValue();
        } else {

                databasereference.child("LunchCart").child("Admin Menu").child(item.getOrdertitle()).removeValue();
            }

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView itemtitle, textViewQuantity;
        EditText addquantity;
        LinearLayout parentLayout,buttonslayout;
        Button delete,minus,plus,update;

        ProductViewHolder(View itemView) {
            super(itemView);

            itemtitle = itemView.findViewById(R.id.admin_menu_title);
            textViewQuantity = itemView.findViewById(R.id.admin_menu_quantity);
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
