package com.example.jepapp.Fragments.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Admin.AdminMadeMenuAdapter;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Make_Menu extends Fragment {

    private RecyclerView recyclerView, recyclerView2;
    AdminMadeMenuAdapter adapter, adapter2;
    private List<Admin_Made_Menu> admin_made_menu, admin_made_menulunch;
    private List<MItems> menu_itemslist;
    private Button selectButton;
    private FloatingActionButton breakfast_add, breakfast_delete, lunch_add, lunch_delete;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    private DividerItemDecoration dividerItemDecoration;
    private TextView emptyView;

    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
   // ArrayList<String> arrayList;
    //private List<MItems> MenuItemsList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.admin_make_menu, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        admin_made_menu = new ArrayList<>();
        admin_made_menulunch = new ArrayList<>();
        menu_itemslist = new ArrayList<>();
        breakfast_delete = rootView.findViewById(R.id.breakfast_delete_fab);
        breakfast_add = rootView.findViewById(R.id.breakfast_add_fab);
        lunch_delete = rootView.findViewById(R.id.lunch_delete_fab);
        lunch_add = rootView.findViewById(R.id.lunch_add_fab);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.admin_make_menu_recyclerView);
        recyclerView2 = rootView.findViewById(R.id.admin_make_menu_recyclerView2);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager2 = new  LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new AdminMadeMenuAdapter(getContext(),admin_made_menu);
        adapter2 = new AdminMadeMenuAdapter(getContext(),admin_made_menulunch);

        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView2.setAdapter(adapter2);
        recyclerView.setAdapter(adapter);

        getBreakfastData();
        getLunchData();
        getAllMenuItems();
     //   buildRecyclerView();


        lunch_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("clicked","clicked");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you wish to delete this menu? NB The entire menu will be deleted.");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");
                        databaseReference.removeValue();

                    }
                });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
            }
        });
        breakfast_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you wish to delete this menu? NB THe entire menu will be deleted");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
                        databaseReference.removeValue();

                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
            }
        });

        breakfast_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final Spinner
                LayoutInflater li = LayoutInflater.from(getContext());

                View promptsView = li.inflate(R.layout.activity_menu_add_single_item, null);
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setView(promptsView);
                builder1.setTitle("Select the item you which to add");
                builder1.setCancelable(true);

                final Spinner mSpinner = promptsView.findViewById(R.id.spinner_menu);
                ArrayAdapter<MItems> mItemsArrayAdapter = new ArrayAdapter<MItems>(getContext(),android.R.layout.simple_spinner_item,menu_itemslist);
               mItemsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(mItemsArrayAdapter);
                final EditText mEdittext = promptsView.findViewById(R.id.new_menuitem_quantity);
                //final FloatingActionButton mfab = promptsView.findViewById(R.id.fab_for_new_menuItem);
              //  final String[] title = new String[1];
                //final String quantity;
                final MItems[] add_this = new MItems[1];

                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        add_this[0] = (MItems) parent.getSelectedItem();

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                builder1.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mEdittext.getText().toString().isEmpty()){
                            String type = "Breakfast";
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
                            String quantity = mEdittext.getText().toString();
                            addtoDB(add_this,type, databaseReference, quantity);

                            // Log.e("Title",add_this[0].getTitle());
                        }
                        else {
                            Toast toast = Toast.makeText(getContext(),"Please enter a quantity", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();

        }
                                              });

        lunch_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());

                View promptsView = li.inflate(R.layout.activity_menu_add_single_item, null);
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setView(promptsView);
                builder1.setTitle("Select the item you which to add");
                builder1.setCancelable(true);

                final Spinner mSpinner = promptsView.findViewById(R.id.spinner_menu);
                ArrayAdapter<MItems> mItemsArrayAdapter = new ArrayAdapter<MItems>(getContext(),android.R.layout.simple_spinner_item,menu_itemslist);
                mItemsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(mItemsArrayAdapter);
                final EditText mEdittext = promptsView.findViewById(R.id.new_menuitem_quantity);
                //final FloatingActionButton mfab = promptsView.findViewById(R.id.fab_for_new_menuItem);
                //  final String[] title = new String[1];
                //final String quantity;
                final MItems[] add_this = new MItems[1];

                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        add_this[0] = (MItems) parent.getSelectedItem();

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                builder1.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mEdittext.getText().toString().isEmpty()){
                            String type = "Lunch";
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");
                            String quantity = mEdittext.getText().toString();
                            addtoDB(add_this,type, databaseReference, quantity);

                            // Log.e("Title",add_this[0].getTitle());
                        }
                        else {
                            Toast toast = Toast.makeText(getContext(),"Please enter a quantity", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();

            }
        });



        return rootView;

    }

    private void addtoDB(MItems[] add_this, String type, DatabaseReference databaseReference, String quantityval) {
        String title = add_this[0].getTitle();
        String quantity = quantityval;
        String ingredients = add_this[0].getIngredients();
        String id = add_this[0].getId();
        Float price = add_this[0].getPrice();
        String image = add_this[0].getImage();
        String key = databaseReference.push().getKey();
        Admin_Made_Menu mItems = new Admin_Made_Menu(quantity, ingredients, id, title, price, image,key, type);
        databaseReference
                .child(key)
                .setValue(mItems);
        Log.e("Start Adding", quantity);


    }

    private void getAllMenuItems() {
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               menu_itemslist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MItems allmenuitems = dataSnapshot.getValue(MItems.class);

                    menu_itemslist.add(allmenuitems);
                }
//
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getLunchData() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Comments from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                admin_made_menulunch.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu lunchDetails = dataSnapshot.getValue(Admin_Made_Menu.class);

                    admin_made_menulunch.add(lunchDetails);
                   //  Log.d("SIZERZ", String.valueOf(admin_made_menulunch.get(0).getTitle()));
                }

//                adapter = new SelectMenuItemsAdaptertest(SelectMenuItems.this, balanceList);
//
//                recyclerView.setAdapter(adapter);
                adapter2.notifyDataSetChanged();

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });
    }


    private void getBreakfastData() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Comments from Firebase Database");

        //progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                admin_made_menu.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu breakfastDetails = dataSnapshot.getValue(Admin_Made_Menu.class);

                    admin_made_menu.add(breakfastDetails);
                    // Log.d("SIZERZ", String.valueOf(balanceList.get(0).getTitle()));
                }

//                adapter = new SelectMenuItemsAdaptertest(SelectMenuItems.this, balanceList);
//
//                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                //progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

    }

//    private void saveData() {
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json2 = gson.toJson(arrayList);
//        editor.putString("task balanceList", json2);
//        editor.apply();
//    }

    private void buildRecyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (admin_made_menu.size()>0) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            AdminMadeMenuAdapter adapter = new AdminMadeMenuAdapter(getContext(), admin_made_menu);

            //setting adapter to recyclerview
            recyclerView.setAdapter(adapter);
        }
        else {

            recyclerView.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

  //  public void helper(String f){};
//    private  void loadData(){
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("task balanceList", null);
//        Type type = new TypeToken<ArrayList<Comments>>() {}.getType();
//        arrayList = gson.fromJson(json, type);
//
//        if (arrayList == null) {
//            arrayList = new ArrayList<>();
//        }
//
//
//    }

}
