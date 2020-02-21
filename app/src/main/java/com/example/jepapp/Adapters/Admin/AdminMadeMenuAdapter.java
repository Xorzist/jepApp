package com.example.jepapp.Adapters.Admin;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Fragments.Admin.Make_Menu;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class AdminMadeMenuAdapter extends RecyclerView.Adapter<AdminMadeMenuAdapter.ProductViewHolder> {


    private List<Admin_Made_Menu> madeMenuList;
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
//    private List<String> madeMenuList = new ArrayList<>();
   // private List<MItems> MenuItemList;

    //getting the context and product list with constructor
    public AdminMadeMenuAdapter(Context mCtx, List<Admin_Made_Menu> madeMenuList) {
        this.mCtx = mCtx;
        this.madeMenuList = madeMenuList;

    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.admin_make_menu_layout_admin, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        //getting the item of the specified position
        //final Admin_Made_Menu item = madeMenuList.get(position);

        //binding the data with the viewholder views

        final Admin_Made_Menu item = madeMenuList.get(position);
        //binding the data with the viewholder views
        holder.Title.setText(item.getTitle());
        holder.Quantity.setText(String.valueOf(item.getQuantity()));

        //holder.checkBox.setChecked(false);
        //holder.Imageurl.setText(item.getImage());
//        Picasso.with(mCtx)
//                .load(String.valueOf(item.getImage()))
//                .into(holder.itempics);

       // holder.textViewTitle.setText(madeMenuList.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(mCtx);
                taskEditText.setText(holder.Quantity.getText());
                AlertDialog dialog = new AlertDialog.Builder(mCtx)
                        .setTitle(holder.Title.getText())
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String value = String.valueOf(taskEditText.getText());
                                String type = item.getType();
                                editQuantityDialog(item, type, value);
                            }


                        })
                        .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String type = item.getType();
                                if (type.equals("Breakfast")){
                                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
                                    deleteItem(dbRef, item);
                                }
                                else{
                                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");
                                    deleteItem(dbRef, item);
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }

    private void deleteItem(DatabaseReference dbRef, Admin_Made_Menu item) {
            dbRef.child(item.getKey()).removeValue();
           // Log.e( "deleteItem: ",.getKey() );


    }


    @Override
    public int getItemCount() {
        return madeMenuList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Quantity,Imageurl ;
        ImageView deletbtn,itempics;
        LinearLayout parentLayout;
        CheckBox checkBox;

        public ProductViewHolder(View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.admin_menu_title);
           // checkBox=itemView.findViewById(R.id.checkbox1);
           // deletbtn=itemView.findViewById(R.id.deleteitem);
           // itempics=itemView.findViewById(R.id.itempic);
            Quantity=itemView.findViewById(R.id.admin_menu_quantity);
           // Imageurl = itemView.findViewById(R.id.imageurl);
            parentLayout = itemView.findViewById(R.id.parent_layout2);

        }

    }

    private void editQuantityDialog(final Admin_Made_Menu edits, String type, final String value) {
        if (type.equals("Breakfast")){
            DatabaseReference update = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
            doUpdate(update,edits,value);

        } else{

            DatabaseReference update = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");
            doUpdate(update,edits,value);

        }


    }

    private void doUpdate(DatabaseReference update, Admin_Made_Menu edits, final String value) {
        Query update_Quantity = update.orderByChild("key").equalTo(edits.getKey());
        update_Quantity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot updateQuantity: dataSnapshot.getChildren()){
                    updateQuantity.getRef().child("quantity").setValue(value.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
