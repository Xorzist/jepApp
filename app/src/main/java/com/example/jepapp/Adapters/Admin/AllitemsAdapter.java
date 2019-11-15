package com.example.jepapp.Adapters.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllitemsAdapter extends RecyclerView.Adapter<AllitemsAdapter.AllitemsViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    public List<MItems> MenuItemList;
    private static DatabaseReference databaseReference;
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
                .into(holder.itempics);
//        holder.deletbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
//                alertDialogBuilder.setTitle("Delete A post");
//                alertDialogBuilder.setMessage("Do you want to delete this post ");
//                alertDialogBuilder.setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                deleteItem(item);
//                                //mActivity.startActivity(mActivity.getIntent());
//
//                            }
//                        });
//
//                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//
//
//            }
//        });

    }

    public void deleteItem(MItems item) {
        databaseReference.child(item.getKey()).removeValue();
    }
//    public DatabaseReference getDb() {
//        return myDBRef;
//    }
public void EditItems(MItems item) {
    databaseReference.child(item.getKey()).removeValue();
}


    @Override
    public int getItemCount() {
        return MenuItemList.size();
    }


    class AllitemsViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Prices,Imageurl ;
        ImageView deletbtn,itempics;
        LinearLayout parentLayout;

        public AllitemsViewHolder(View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.itemtitle);
           // deletbtn=itemView.findViewById(R.id.deleteitem);
            itempics=itemView.findViewById(R.id.itempic);
            Prices=itemView.findViewById(R.id.prices);
            Imageurl = itemView.findViewById(R.id.imageurl);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }
}
