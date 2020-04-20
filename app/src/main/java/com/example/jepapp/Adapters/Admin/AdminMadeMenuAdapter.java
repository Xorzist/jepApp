package com.example.jepapp.Adapters.Admin;


import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.util.List;


public class AdminMadeMenuAdapter extends RecyclerView.Adapter<AdminMadeMenuAdapter.ProductViewHolder> {


    private List<Admin_Made_Menu> madeMenuList;
    //this context we will use to inflate the layout
    private Context mCtx;

    //getting the context and menu list with constructor
    public AdminMadeMenuAdapter(Context mCtx, List<Admin_Made_Menu> madeMenuList) {
        this.mCtx = mCtx;
        this.madeMenuList = madeMenuList;


    }


    @NotNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.admin_make_menu_layout_admin, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        //getting the item of the specified position

        final Admin_Made_Menu item = madeMenuList.get(position);
        //binding the data with the viewholder views
        holder.Title.setText(item.getTitle());
        holder.Quantity.setText(String.valueOf(item.getQuantity()));
        //Function used to change the quantity value of an item in an existing menu
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(mCtx);
                taskEditText.setText(holder.Quantity.getText());
                taskEditText.setGravity(Gravity.CENTER);
                // inflating dialog
                AlertDialog menuquanityUpdate = new AlertDialog.Builder(mCtx,R.style.datepicker)
                        .setTitle(holder.Title.getText())
                        .setMessage(
                                " You may change the Quantity value below")
                        .setView(taskEditText)
                        .setPositiveButton((R.string.update), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String value = String.valueOf(taskEditText.getText());
                                String type = item.getType();
                                editQuantityDialog(item, type, value);
                            }


                        })
                        .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
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
                menuquanityUpdate.show();
            }
        });
    }

    //Function to delete the item from the menu
    private void deleteItem(DatabaseReference dbRef, Admin_Made_Menu item) {
            dbRef.child(item.getKey()).removeValue();

    }


    @Override
    public int getItemCount() {
        return madeMenuList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Quantity ;
        LinearLayout parentLayout;

        ProductViewHolder(View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.admin_menu_title);
            Quantity=itemView.findViewById(R.id.admin_menu_quantity);
            parentLayout = itemView.findViewById(R.id.parent_layout2);

        }

    }
    //Function to update the item quantity in the corresponding menus
    private void editQuantityDialog(final Admin_Made_Menu edits, String type, final String value) {
        if (type.equals("Breakfast")){
            DatabaseReference update = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
            doUpdate(update,edits,value);
        } else{
            DatabaseReference update = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");
            doUpdate(update,edits,value);

        }


    }
    //Function to find the item in the database and replace the existing quantity value with the new desired value
    private void doUpdate(DatabaseReference update, Admin_Made_Menu edits, final String value) {
        Query update_Quantity = update.orderByChild("key").equalTo(edits.getKey());
        update_Quantity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot updateQuantity: dataSnapshot.getChildren()){
                    updateQuantity.getRef().child("quantity").setValue(value);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
