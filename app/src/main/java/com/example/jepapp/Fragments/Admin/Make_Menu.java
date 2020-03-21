package com.example.jepapp.Fragments.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jepapp.Adapters.Admin.AdminMadeMenuAdapter;
import com.example.jepapp.Models.Admin_Made_Menu;
import com.example.jepapp.Models.Cut_Off_Time;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Make_Menu extends Fragment {

    private RecyclerView recyclerView, recyclerView2;
    AdminMadeMenuAdapter adapter, adapter2;
    private List<Admin_Made_Menu> admin_made_menu, admin_made_menulunch;
    private List<Cut_Off_Time> times;
    private List<MItems> menu_itemslist;
    private FloatingActionButton breakfast_add, breakfast_delete, lunch_add, lunch_delete;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    private DividerItemDecoration dividerItemDecoration;
    private TextView emptyView, emptyView2;
    private Button timepicker_breakfast, timepicker_lunch;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    private int mHour, mMinute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.admin_make_menu, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        //instantiating variables
        timepicker_breakfast = rootView.findViewById(R.id.time_breakfast);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        admin_made_menu = new ArrayList<>();
        admin_made_menulunch = new ArrayList<>();
        times = new ArrayList<>();
        menu_itemslist = new ArrayList<>();
        timepicker_lunch = rootView.findViewById(R.id.time_lunch);
        breakfast_delete = rootView.findViewById(R.id.breakfast_delete_fab);
        breakfast_add = rootView.findViewById(R.id.breakfast_add_fab);
        lunch_delete = rootView.findViewById(R.id.lunch_delete_fab);
        lunch_add = rootView.findViewById(R.id.lunch_add_fab);
        emptyView2 = rootView.findViewById(R.id.empty_view2);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.admin_make_menu_recyclerView);
        recyclerView2 = rootView.findViewById(R.id.admin_make_menu_recyclerView2);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager2 = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new AdminMadeMenuAdapter(getContext(), admin_made_menu);
        adapter2 = new AdminMadeMenuAdapter(getContext(), admin_made_menulunch);

        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView2.setAdapter(adapter2);
        recyclerView.setAdapter(adapter);

        //get cut off time information from database
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                times.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Cut_Off_Time timeofcutoff = dataSnapshot.getValue(Cut_Off_Time.class);

                    times.add(timeofcutoff);

                }

               // notify();
                addcutofftime(times);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        getBreakfastData();
        getLunchData();
        getAllMenuItems();

        timepicker_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                int hour = hourOfDay % 12;
                                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");
                                String time = (String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                        minute, hourOfDay < 12 ? "am" : "pm"));
                                String type = "Breakfast";

                                com.example.jepapp.Models.Cut_Off_Time cut_off_time = new com.example.jepapp.Models.Cut_Off_Time(type,time);
                                dbref.child(type)
                                        .setValue(cut_off_time);

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();



            }
        });
        timepicker_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                int hour = hourOfDay % 12;
                                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");
                                String time = (String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                        minute, hourOfDay < 12 ? "am" : "pm"));
                                String type = "Lunch";

                                com.example.jepapp.Models.Cut_Off_Time cut_off_time = new com.example.jepapp.Models.Cut_Off_Time(type,time);
                                dbref.child(type)
                                        .setValue(cut_off_time);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();



            }
        });

        lunch_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("clicked","clicked");
                //create alert dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you wish to delete this menu? NB The entire menu will be deleted.");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //delete item from database
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
                //create alert dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you wish to delete this menu? NB THe entire menu will be deleted");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete item from database
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
                //inflate alert dialog
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.activity_menu_add_single_item, null);
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setView(promptsView);
                builder1.setTitle("Select the item you which to add");
                builder1.setCancelable(true);
                //add spinner items from database
                final Spinner mSpinner = promptsView.findViewById(R.id.spinner_menu);
                ArrayAdapter<MItems> mItemsArrayAdapter = new ArrayAdapter<MItems>(getContext(),android.R.layout.simple_spinner_item,menu_itemslist);
               mItemsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(mItemsArrayAdapter);
                final EditText mEdittext = promptsView.findViewById(R.id.new_menuitem_quantity);
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
                        //validate inputs
                        if (!mEdittext.getText().toString().isEmpty()){
                            String type = "Breakfast";
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
                            String quantity = mEdittext.getText().toString();
                            addtoDB(add_this,type, databaseReference, quantity);

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
                //inflate custom alert dialog
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.activity_menu_add_single_item, null);
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setView(promptsView);
                builder1.setTitle("Select the item you which to add");
                builder1.setCancelable(true);

                final Spinner mSpinner = promptsView.findViewById(R.id.spinner_menu);
                //assigns spinner the items from the menu items table
                ArrayAdapter<MItems> mItemsArrayAdapter = new ArrayAdapter<MItems>(getContext(),android.R.layout.simple_spinner_item,menu_itemslist);
                mItemsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(mItemsArrayAdapter);
                final EditText mEdittext = promptsView.findViewById(R.id.new_menuitem_quantity);

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

// add new item to existing menu
    private void addtoDB(MItems[] add_this, String type, DatabaseReference databaseReference, String quantityval) {
        // get the model item information
        String title = add_this[0].getTitle();
        String quantity = quantityval;
        String ingredients = add_this[0].getIngredients();
        String id = add_this[0].getId();
        Float price = add_this[0].getPrice();
        String image = add_this[0].getImage();
        String key = databaseReference.push().getKey();
        Admin_Made_Menu mItems = new Admin_Made_Menu(quantity, ingredients, id, title, price, image,key, type);
        //add item to database
        databaseReference
                .child(key)
                .setValue(mItems);

    }
// gets all menu items from database
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
//gets all lunch menu items
    private void getLunchData() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Menu from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                admin_made_menulunch.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu lunchDetails = dataSnapshot.getValue(Admin_Made_Menu.class);

                    admin_made_menulunch.add(lunchDetails);
                }


                adapter2.notifyDataSetChanged();
                if (admin_made_menulunch.size()>0) {
                    recyclerView2.setVisibility(View.VISIBLE);
                    emptyView2.setVisibility(View.GONE);
                }
                else{
                    recyclerView2.setVisibility(View.GONE);
                    emptyView2.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });
    }

// gets all breakfast menu items
    private void getBreakfastData() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Menu from Firebase Database");


        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                admin_made_menu.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu breakfastDetails = dataSnapshot.getValue(Admin_Made_Menu.class);

                    admin_made_menu.add(breakfastDetails);
                }


                adapter.notifyDataSetChanged();
                // check if any breakfast data is available
                if (admin_made_menu.size()>0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);}
                else{
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

    }
        private void addcutofftime(List<Cut_Off_Time> times){
    //assign the cut off time to the appropriate buttons

            if (times.size()>0) {
                for (int i=0; i<times.size(); i++){
                    if (times.get(i).getType().equals("Breakfast")) {
                        timepicker_breakfast.setText(String.valueOf(times.get(i).getTime()));
                    } else{
                    timepicker_lunch.setText(String.valueOf(times.get(i).getTime()));
                }
            }}
//                if (times.get(0).getType().equals("Breakfast") && times.get(1).getType().equals("Lunch")) {
//                    timepicker_breakfast.setText(String.valueOf(times.get(0).getTime()));
//                    timepicker_lunch.setText(String.valueOf(times.get(1).getTime()));
//                } if (times.get(0).getType().equals("Breakfast")) {
//                    timepicker_breakfast.setText(String.valueOf(times.get(0).getTime()));
//                }
//                if (times.get(0).getType().equals("Lunch")) {
//                    timepicker_lunch.setText(String.valueOf(times.get(0).getTime()));
//                }
            while (times.size()==0){
                final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
                timepicker_breakfast.startAnimation(myAnim);
                timepicker_lunch.startAnimation(myAnim);
            }

        }
}
