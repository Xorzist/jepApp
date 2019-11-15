package com.example.jepapp.Fragments.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.jepapp.Activities.Admin.CreatingItem;
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

    public AllitemsAdapter adapter ;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.all_imenu_items, container, false);
        recyclerView = rootView.findViewById(R.id.allmenuitems);
        list = new ArrayList<>();
        adapter = new AllitemsAdapter(getContext(),list);
        swipeControl = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                deleteItem(list.get(position));
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position,adapter.getItemCount());
               Log.e("LOL","Hush" );

            }
        });

        swipeControl = new SwipeControl(new SwipeControllerActions() {
            @Override
            public void SnapBack(int position) {
                adapter.notifyItemChanged(position);
                Log.e("LOL","Seet deh");
//                adapter.deleteItem(position);
//                adapter.notifyDataSetChanged();
//                Log.e("LOL","Hush" );
            }
        });

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


        fabcreatebtn=rootView.findViewById(R.id.createitembtn);
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



        return  rootView;

    }

    public void deleteItem(MItems mItems){
        databaseReference.child(mItems.getKey()).removeValue();

    }

}
