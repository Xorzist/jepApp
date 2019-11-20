package com.example.jepapp.Fragments.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.jepapp.Activities.Admin.CreatingItem;
import com.example.jepapp.Activities.Admin.EditItemActivity;
import com.example.jepapp.Adapters.Admin.AllitemsAdapter;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.example.jepapp.SwipeController;
import com.example.jepapp.SwipeControllerActions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Allitems extends Fragment {


    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<MItems> list = new ArrayList<>();

    RecyclerView recyclerView;

    FloatingActionButton fabcreatebtn;

    private RequestQueue mRequestq;
    private Bitmap bitmap;
    private static CreateItem createiteminstance;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    SwipeController swipeControl = null;
    private FirebaseStorage mFirebaseStorage= FirebaseStorage.getInstance();


    public AllitemsAdapter adapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.all_imenu_items, container, false);
        recyclerView = rootView.findViewById(R.id.allmenuitems);
        list = new ArrayList<>();
        adapter = new AllitemsAdapter(getContext(), list);
        swipeControl = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you want to delete this item?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteItem(list.get(position));
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position,adapter.getItemCount());
                                Toast toast = Toast.makeText(getContext(),
                                        "Item has been deleted",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                                Log.e("LOL","Hush" );

                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            @Override
            public void onLeftClicked(int position) {
                editItem(position);
                adapter.notifyItemChanged(position);
                Log.e("OLC", "Clicked");

        }}
            );



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeControl);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeControl.onDraw(c);
            }
        });
        recyclerView.setAdapter(adapter);


        fabcreatebtn = rootView.findViewById(R.id.createitembtn);
        fabcreatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreatingItem.class);
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MItems studentDetails = dataSnapshot.getValue(MItems.class);


                    list.add(studentDetails);
                }
                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });


        return rootView;

    }

    public void deleteItem(MItems mItems) {
        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(mItems.getImage());
        photoRef.delete();
        databaseReference.child(mItems.getKey()).removeValue();

    }

    public void editItem(int position) {
        String key = list.get(position).getKey();
        String  title = list.get(position).getTitle();
        String  ingredients = list.get(position).getIngredients();
        String  image = list.get(position).getImage();
        String price = String.valueOf(list.get(position).getPrice());
        Intent intent = new Intent(getContext(), EditItemActivity.class);
        intent.putExtra("key", String.valueOf(key));
        intent.putExtra("title", title);
        intent.putExtra("ingredients", ingredients);
        intent.putExtra("price", price);
        intent.putExtra("image", image);

        startActivity(intent);

    }
}
