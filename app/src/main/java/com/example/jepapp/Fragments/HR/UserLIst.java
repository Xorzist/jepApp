package com.example.jepapp.Fragments.HR;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.HR.HRAdapter;
import com.example.jepapp.GMailSender;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserLIst extends Fragment{

    private RecyclerView hrrecyclerView;
    HRAdapter adapter;
    private List<UserCredentials> userlist;
    private Button update_all;
    LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    ProgressDialog progressDialog;
    private TextView emptyView;
    DatabaseReference databaseReference;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private String subject = "Account balance update";
    private Toolbar toolbar;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hr_userlist_layout, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        emptyView = rootView.findViewById(R.id.empty_view);
        userlist = new ArrayList<>();
        update_all = rootView.findViewById(R.id.update_all);
        hrrecyclerView = (RecyclerView) rootView.findViewById(R.id.hr_people_recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(hrrecyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new HRAdapter(getContext(),userlist);
        hrrecyclerView.setLayoutManager(linearLayoutManager);
        hrrecyclerView.addItemDecoration(dividerItemDecoration);
        hrrecyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);
        getUserData();
//        toolbar = rootView.findViewById(R.id.toolbar_frag);
//        toolbar = rootView.findViewById(R.id.toolbar_frag);
//        toolbar.inflateMenu(R.menu.user);
//
//
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_search:
//                        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
////
//                        if (item != null) {
//                            searchView = (SearchView) item.getActionView();
//                        }
//                        if (searchView != null) {
//                            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//
//                            queryTextListener = new SearchView.OnQueryTextListener() {
//                                @Override
//                                public boolean onQueryTextSubmit(String query) {
//                                    searchView.clearFocus();
//                                    return true;
//                                }
//
//                                @Override
//                                public boolean onQueryTextChange(String newText) {
//
//                                    Log.d("Query", newText);
//                                    String userInput = newText.toLowerCase();
//                                    List<UserCredentials> newList = new ArrayList<>();
//
//                                    // for (com.example.jepapp.Models.Orders orders : allorderslist) {
//
//                                    //if (!searchView.isIconified()) {
//                                    getActivity().onSearchRequested();
//                                    //  com.example.jepapp.Models.Orders orders;
//                                    for (int i = 0; i < userlist.size(); i++) {
//
//                                        if (userlist.get(i).getUsername().toLowerCase().contains(userInput) || userlist.get(i).getEmpID().toLowerCase().contains(userInput)) {
//
//                                            newList.add(userlist.get(i));
//
//                                        }
//
//                                        // }
//
//                                    }
//                                    adapter.updateList(newList);
//                                    return true;
//                                }
//                            };
//                            searchView.setOnQueryTextListener(queryTextListener);
//                            return false;
//                        }
//
//                }
//                searchView.setOnQueryTextListener(queryTextListener);
//                return UserLIst.super.onOptionsItemSelected(item);
//            }
//        });

        update_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());

                View promptsView = li.inflate(R.layout.update_user_balance, null);
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setView(promptsView);
                builder1.setTitle("Update All User Balances");
                builder1.setMessage("Please note the value entered below will be added to all users' current balance");
                builder1.setCancelable(true);
                final EditText new_balance = promptsView.findViewById(R.id.new_balance_alertdialog);
                builder1.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!new_balance.getText().toString().isEmpty()){

                            databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");

                            final int user_balance_to_add = Integer.parseInt(new_balance.getText().toString());
                            for (int i=0; i<userlist.size(); i++){
                                int balance = Integer.parseInt(userlist.get(i).getBalance());
                                String key = userlist.get(i).getEmail();
                                final int value= balance + user_balance_to_add;
                                String message = "$"+user_balance_to_add+" has been added to your account. Your new balance is $" + value +".";
                                sendEmail(key,message, subject);

                                Query update= databaseReference.orderByChild("email").equalTo(key);
                                update.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        //UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);
                                        dataSnapshot.getRef().child("balance").setValue(String.valueOf(value));

                                    }
                                    adapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                }
                            });
                        }}
                        else{
                            Toast toast = Toast.makeText(getContext(),"Please enter an amount", Toast.LENGTH_LONG);
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


        return  rootView;
    }
    private void sendEmail(String email, String message, String subject) {

        //Creating SendMail object
        GMailSender sm = new GMailSender(getContext(), email, message, subject);

        //Executing sendmail to send email
        sm.execute();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        inflater.inflate(R.menu.user, menu);
        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
//
        if (searchItem != null){
            searchView = (SearchView)searchItem.getActionView();
        }
        if(searchView != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    Log.d("Query", newText);
                    String userInput = newText.toLowerCase();
                    List<UserCredentials> newList = new ArrayList<>();

                    // for (com.example.jepapp.Models.Orders orders : allorderslist) {

                    //if (!searchView.isIconified()) {
                    getActivity().onSearchRequested();
                    //  com.example.jepapp.Models.Orders orders;
                    for (int i = 0; i< userlist.size(); i++){

                        if (userlist.get(i).getUsername().toLowerCase().contains(userInput)|| userlist.get(i).getEmpID().toLowerCase().contains(userInput)) {

                            newList.add(userlist.get(i));

                        }

                        // }

                    }
                    adapter.updateList(newList);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:

                return false;
            default:
                break;

        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    private void getUserData() {
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Users");

        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);

                    userlist.add(allusers);

                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }
}
