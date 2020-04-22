package com.example.jepapp.Fragments.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Make_Menu extends Fragment {

    private RecyclerView recyclerViewBF, recyclerViewLunch;
    private AdminMadeMenuAdapter adapterMenuBreakfast, adapterMenuLunch;
    private List<Admin_Made_Menu> admin_made_menu, admin_made_menulunch;
    private List<Cut_Off_Time> times;
    private List<MItems> menu_itemslist;
    private TextView emptyViewbreakfast, emptyViewlunch;
    private Button timepicker_breakfast, timepicker_lunch;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private int mHour, mMinute;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.admin_make_menu, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        //setting the title of the action bar
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Today's Menu");

        //instantiating variables
        timepicker_breakfast = rootView.findViewById(R.id.time_breakfast);
        emptyViewbreakfast = rootView.findViewById(R.id.empty_viewbreakfast);
        admin_made_menu = new ArrayList<>();
        admin_made_menulunch = new ArrayList<>();
        times = new ArrayList<>();
        menu_itemslist = new ArrayList<>();
        timepicker_lunch = rootView.findViewById(R.id.time_lunch);
        FloatingActionButton breakfast_delete = rootView.findViewById(R.id.breakfast_delete_fab);
        FloatingActionButton breakfast_add = rootView.findViewById(R.id.breakfast_add_fab);
        FloatingActionButton lunch_delete = rootView.findViewById(R.id.lunch_delete_fab);
        FloatingActionButton lunch_add = rootView.findViewById(R.id.lunch_add_fab);
        emptyViewlunch = rootView.findViewById(R.id.empty_viewlunch);
        recyclerViewBF = rootView.findViewById(R.id.admin_make_menu_recyclerViewBreakfast);
        recyclerViewLunch = rootView.findViewById(R.id.admin_make_menu_recyclerViewlunch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewBF.getContext(), linearLayoutManager.getOrientation());
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(recyclerViewBF.getContext(), linearLayoutManager.getOrientation());
        adapterMenuBreakfast = new AdminMadeMenuAdapter(getContext(), admin_made_menu);
        adapterMenuLunch = new AdminMadeMenuAdapter(getContext(), admin_made_menulunch);

        recyclerViewLunch.setLayoutManager(linearLayoutManager2);
        recyclerViewBF.setLayoutManager(linearLayoutManager);
        recyclerViewBF.addItemDecoration(dividerItemDecoration);
        recyclerViewLunch.addItemDecoration(dividerItemDecoration2);
        recyclerViewLunch.setAdapter(adapterMenuLunch);
        recyclerViewBF.setAdapter(adapterMenuBreakfast);
        setHasOptionsMenu(true);
        //get cut off time information from database
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                times.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Cut_Off_Time timeofcutoff = dataSnapshot.getValue(Cut_Off_Time.class);

                    times.add(timeofcutoff);

                }

               //assigns the cut off times to the buttons on view
                addcutofftime(times);
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }

        });
        //gets data from firebase
        getBreakfastData();
        getLunchData();
        getAllMenuItems();
        //launches the time picker dialog and assigns time value selected to database breakfast
        timepicker_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.datepicker,
                        new OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                int hour = hourOfDay % 12;
                                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");
                                String time;
                                time = (String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                        minute, hourOfDay < 12 ? "am" : "pm"));
                                String type = "Breakfast";
                                // adds time to database
                                com.example.jepapp.Models.Cut_Off_Time cut_off_time = new com.example.jepapp.Models.Cut_Off_Time(type,time);
                                dbref.child(type)
                                        .setValue(cut_off_time);

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });
        //launches the time picker dialog and assigns time value selected to database lunch
        timepicker_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.datepicker,
                        new OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                int hour = hourOfDay % 12;
                                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("JEP").child("Cut off time");
                                String time = (String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                        minute, hourOfDay < 12 ? "am" : "pm"));
                                String type = "Lunch";
                                // adds time to database
                                com.example.jepapp.Models.Cut_Off_Time cut_off_time = new com.example.jepapp.Models.Cut_Off_Time(type,time);
                                dbref.child(type)
                                        .setValue(cut_off_time);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();



            }
        });

        //Function to delete the entire lunch menu
        lunch_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create alert dialog
                AlertDialog.Builder deleteLunchDialog = new AlertDialog.Builder(getContext(),R.style.datepicker);
                deleteLunchDialog.setMessage("Are you sure you wish to delete this menu? NB The entire menu will be deleted.");
                deleteLunchDialog.setCancelable(true);
                deleteLunchDialog.setPositiveButton(
                        R.string.delete,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //delete item from database
                        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");
                        databaseReference.removeValue();

                    }
                });
                deleteLunchDialog.setNegativeButton(
                        R.string.cancel,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialogDeleteLunch = deleteLunchDialog.create();
                dialogDeleteLunch.show();
            }
        });
        //Function to delete the entire breakfast menu
        breakfast_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create alert dialog
                AlertDialog.Builder deleteBreakfastDialog = new AlertDialog.Builder(getContext(), R.style.datepicker);
                deleteBreakfastDialog.setMessage("Are you sure you wish to delete this menu? NB The entire menu will be deleted");
                deleteBreakfastDialog.setCancelable(true);
                deleteBreakfastDialog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete item from database
                        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
                        databaseReference.removeValue();

                    }
                });
                deleteBreakfastDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialogDeleteBreakfast = deleteBreakfastDialog.create();
                dialogDeleteBreakfast.show();
            }
        });
        //Function to add a single item to breakfast menu
        breakfast_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inflate alert dialog
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.activity_menu_add_single_item, null);
                final AlertDialog.Builder addbreakfastdialog = new AlertDialog.Builder(getContext(), R.style.datepicker);
                addbreakfastdialog.setView(promptsView);
                addbreakfastdialog.setTitle("Select the item you which to add");
                addbreakfastdialog.setCancelable(true);
                //add spinner items from database
                final Spinner mSpinner = promptsView.findViewById(R.id.spinner_menu);
                ArrayAdapter<MItems> mItemsArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),R.layout.myspinneritem,menu_itemslist);
                mItemsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(mItemsArrayAdapter);
                //allow user to enter quantity value
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
                addbreakfastdialog.setPositiveButton(R.string.dialogAdd, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //validate inputs
                        if (!mEdittext.getText().toString().isEmpty()){
                            String type = "Breakfast";
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("JEP").child("BreakfastMenu");
                            String quantity = mEdittext.getText().toString();
                            //adds item to database in breakfast menu
                            addtoDB(add_this,type, databaseReference, quantity);

                        }
                        else {
                            Toast toast = Toast.makeText(getContext(),"Please enter a quantity", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                addbreakfastdialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = addbreakfastdialog.create();
                alertDialog.show();

        }
                                              });

        lunch_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inflate custom alert dialog
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.activity_menu_add_single_item, null);
                final AlertDialog.Builder addLunchdialog = new AlertDialog.Builder(getContext());
                addLunchdialog.setView(promptsView);
                addLunchdialog.setTitle("Select the item you which to add");
                addLunchdialog.setCancelable(true);

                final Spinner mSpinner = promptsView.findViewById(R.id.spinner_menu);
                //assigns spinner the items from the menu items table
                ArrayAdapter<MItems> mItemsArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_item,menu_itemslist);
                mItemsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(mItemsArrayAdapter);
                //allows user to enter quantity
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
                addLunchdialog.setPositiveButton((R.string.dialogAdd), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mEdittext.getText().toString().isEmpty()){
                            String type = "Lunch";
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("JEP").child("Lunch");
                            String quantity = mEdittext.getText().toString();
                            //adds item to database in lunch menu
                            addtoDB(add_this,type, databaseReference, quantity);
                        }
                        else {
                            Toast toast = Toast.makeText(getContext(),"Please enter a quantity", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                addLunchdialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = addLunchdialog.create();
                alertDialog.show();

            }
        });

        return rootView;

    }

// add new item to existing menu
    private void addtoDB(MItems[] add_this, String type, DatabaseReference databaseReference, String quantityval) {
        // get the model item information
        String title = add_this[0].getTitle();
        String ingredients = add_this[0].getIngredients();
        String id = add_this[0].getId();
        Float price = add_this[0].getPrice();
        String image = add_this[0].getImage();
        String key = databaseReference.push().getKey();
        Admin_Made_Menu mItems = new Admin_Made_Menu(quantityval, ingredients, id, title, price, image,key, type);
        //add item to database
        assert key != null;
        databaseReference
                .child(key)
                .setValue(mItems);

    }
// gets all menu items from database
    private void getAllMenuItems() {
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("MenuItems");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
               menu_itemslist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MItems allmenuitems = dataSnapshot.getValue(MItems.class);

                    menu_itemslist.add(allmenuitems);
                }
//
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

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
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                admin_made_menulunch.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu lunchDetails = dataSnapshot.getValue(Admin_Made_Menu.class);

                    admin_made_menulunch.add(lunchDetails);
                }


                adapterMenuLunch.notifyDataSetChanged();
                if (admin_made_menulunch.size()>0) {
                    recyclerViewLunch.setVisibility(View.VISIBLE);
                    emptyViewlunch.setVisibility(View.GONE);
                }
                else{
                    recyclerViewLunch.setVisibility(View.GONE);
                    emptyViewlunch.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

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
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                admin_made_menu.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Admin_Made_Menu breakfastDetails = dataSnapshot.getValue(Admin_Made_Menu.class);

                    admin_made_menu.add(breakfastDetails);
                }


                adapterMenuBreakfast.notifyDataSetChanged();
                // check if any breakfast data is available
                if (admin_made_menu.size()>0) {
                    recyclerViewBF.setVisibility(View.VISIBLE);
                    emptyViewbreakfast.setVisibility(View.GONE);}
                else{
                    recyclerViewBF.setVisibility(View.GONE);
                    emptyViewbreakfast.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

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
            if (times.size()==0){
                final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
                timepicker_breakfast.startAnimation(myAnim);
                timepicker_lunch.startAnimation(myAnim);
            }

        }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //clears the any existing menus attached
        menu.clear();
    }
}
