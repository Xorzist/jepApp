package com.example.jepapp.Activities.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


public class CreateMenu extends AppCompatActivity {
    List<MItems> menuItemList;
    AllitemsAdapter createMenuadapter;
    DatabaseReference databaseReference;
    ProgressDialog createMenuprogress;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createmenu);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Today's Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menuItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.selectmenuitems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        createMenuadapter = new AllitemsAdapter(getApplicationContext(), menuItemList,"Menu");
        recyclerView.setAdapter(createMenuadapter);
        createMenuprogress = new ProgressDialog(CreateMenu.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");

        getMenuItems();


    }

    private void getMenuItems() {
        createMenuprogress.setMessage("Getting Menu Items");
        createMenuprogress.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MItems allMenuItems = dataSnapshot.getValue(MItems.class);

                    menuItemList.add(allMenuItems);
                }


                createMenuadapter.notifyDataSetChanged();
                createMenuprogress.cancel();

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                createMenuprogress.cancel();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.justcart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        if (item.getItemId() == R.id.justcart) {
            // Open admin menu cart page
            Intent intent = new Intent(this, AdminMenuCart.class);
            startActivity(intent);
        }
        return false;
    }
}
