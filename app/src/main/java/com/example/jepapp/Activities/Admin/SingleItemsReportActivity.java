package com.example.jepapp.Activities.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.Adapters.Admin.AllitemsAdapter;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SingleItemsReportActivity extends AppCompatActivity {

    //a list to store all the items
    List<MItems> itemsList;
    AllitemsAdapter adapter;
    DatabaseReference databaseReference;
    ProgressDialog singleItemProgress;
    //the recyclerview
    RecyclerView reportRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menusrecycler);
        //setting the title of the action bar
        Objects.requireNonNull(getSupportActionBar()).setTitle("All items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemsList = new ArrayList<>();
        //getting the recyclerview from xml
        reportRecyclerView =  findViewById(R.id.menusrecyclerView);
        reportRecyclerView.setHasFixedSize(true);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllitemsAdapter(SingleItemsReportActivity.this, itemsList,"Report");
        reportRecyclerView.setAdapter(adapter);
        singleItemProgress = new ProgressDialog(SingleItemsReportActivity.this);

        singleItemProgress.setMessage("Loading Menu Items from Firebase");

        singleItemProgress.show();
        // retrieve data from Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MItems breakfastDetails = dataSnapshot.getValue(MItems.class);

                    itemsList.add(breakfastDetails);
                }


                adapter.notifyDataSetChanged();

                singleItemProgress.dismiss();

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

                singleItemProgress.dismiss();

            }
        });

    }

}

