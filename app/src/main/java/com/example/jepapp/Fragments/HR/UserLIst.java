package com.example.jepapp.Fragments.HR;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UserLIst extends Fragment{

    private RecyclerView hrrecyclerView, hrnewpeoplerecyclerView;
    HRAdapter adapter, adapternewpeps;
    //HRAdapter adapternewpeople;
    private List<UserCredentials> userlist;
    private List<UserCredentials> newpeoplelist;
    private Button update_all;
    LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    private DividerItemDecoration dividerItemDecoration, dividerItemDecoration1;
    ProgressDialog progressDialog;
    private TextView emptyView;
    DatabaseReference databaseReference;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private String subject = "Account balance update";
    private Toolbar toolbar;
    SharedPreferences sharedPreferences;
    private DatabaseReference databaseReferenceuserdata;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hr_userlist_layout, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        //emptyView = rootView.findViewById(R.id.empty_view);
        userlist = new ArrayList<>();
        newpeoplelist = new ArrayList<>();
        update_all = rootView.findViewById(R.id.update_all);
        hrrecyclerView = (RecyclerView) rootView.findViewById(R.id.hr_people_recyclerView);
        hrnewpeoplerecyclerView = rootView.findViewById(R.id.hr_new_people_recyclerView2);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager1 = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(hrrecyclerView.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration1 = new DividerItemDecoration(hrnewpeoplerecyclerView.getContext(), linearLayoutManager1.getOrientation());
        adapter = new HRAdapter(getContext(),userlist);
        adapternewpeps = new HRAdapter(getContext(), newpeoplelist);
        hrrecyclerView.setLayoutManager(linearLayoutManager);
        hrrecyclerView.addItemDecoration(dividerItemDecoration);
        hrrecyclerView.setAdapter(adapter);
        hrnewpeoplerecyclerView.setLayoutManager(linearLayoutManager1);
        hrnewpeoplerecyclerView.addItemDecoration(dividerItemDecoration1);
        hrnewpeoplerecyclerView.setAdapter(adapternewpeps);
        setHasOptionsMenu(true);
        sharedPreferences = getContext().getSharedPreferences("test",MODE_PRIVATE);
        databaseReferenceuserdata = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        getUserData();
        getnewUserData();


        update_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());

                View promptsView = li.inflate(R.layout.update_user_balance, null);
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setView(promptsView);
                builder1.setTitle("Update All User CancelledOrders");
                builder1.setMessage("Please note the value entered below will be used on all users' current balance");
                builder1.setCancelable(true);
                final EditText new_balance = promptsView.findViewById(R.id.new_balance_alertdialog);
                builder1.setPositiveButton("Add", new DialogInterface.OnClickListener() {
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
                                update.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        //UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);
                                        dataSnapshot.getRef().child("balance").setValue(String.valueOf(value));

                                    }
                                    adapter.notifyDataSetChanged();
                                    Log.e("Notify","has been notified" );
                                  //  progressDialog.dismiss();
                                    //try the update all function for me


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
                builder1.setNegativeButton("Subtract", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!new_balance.getText().toString().isEmpty()) {

                            databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");

                            final int user_balance_to_add = Integer.parseInt(new_balance.getText().toString());
                            for (int i = 0; i < userlist.size(); i++) {
                                int balance = Integer.parseInt(userlist.get(i).getBalance());
                                String key = userlist.get(i).getEmail();
                                final int value = balance - user_balance_to_add;
                                String message = "$" + user_balance_to_add + " has been subtracted from your account. Your new balance is $" + value + ".";
                                sendEmail(key, message, subject);

                                Query update = databaseReference.orderByChild("email").equalTo(key);
                                update.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            //UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);
                                            dataSnapshot.getRef().child("balance").setValue(String.valueOf(value));

                                        }
                                        adapter.notifyDataSetChanged();
                                        Log.e("Notify", "has been notified");
                                        //  progressDialog.dismiss();
                                        //try the update all function for me
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                       // progressDialog.dismiss();
                                    }
                                });
                            }
                        } else {
                            Toast toast = Toast.makeText(getContext(), "Please enter an amount", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                builder1.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
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

    private void getnewUserData() {
        final ProgressDialog progressDialog1 = new ProgressDialog(getContext());
        progressDialog1.setMessage("Getting New User Data");
        progressDialog1.show();
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("NewUserBalance");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newpeoplelist.clear();
                for (DataSnapshot newusers : dataSnapshot.getChildren()){

                    UserCredentials newpeople = newusers.getValue(UserCredentials.class);

                    newpeoplelist.add(newpeople);

                }
                Log.e("GetNewUser", String.valueOf(newpeoplelist.size()));
                adapternewpeps.notifyDataSetChanged();

                progressDialog1.cancel();
//
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//
//                editor.putInt("number",newpeoplelist.size());
//                // editor.putBoolean("IsLogin",true);
//                editor.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            progressDialog1.cancel();
            }
        });

    }

    private void sendEmail(String email, String message, String subject) {

        //Creating SendMail object
        GMailSender sm = new GMailSender(getContext(), email, message, subject);

        //Executing sendmail to send email
        sm.execute();
    }

private void getUserData() {
    final ProgressDialog progressDialog2 = new ProgressDialog(getContext());
    progressDialog2.setMessage("Getting User Data");
    progressDialog2.show();


    databaseReferenceuserdata.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            userlist.clear();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);

                userlist.add(allusers);

            }
            Log.e("getUserData", String.valueOf(userlist.size()));
            adapter.notifyDataSetChanged();
            progressDialog2.cancel();
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//
//                editor.putInt("number",userlist.size());
//               // editor.putBoolean("IsLogin",true);
//                editor.commit();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog2.cancel();
        }
    });

}

    @Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
   menu.clear();
//    final List<com.example.jepapp.Models.Orders> combinedlist = new ArrayList<>();
//    combinedlist.addAll(allordersbreakfast);
//    combinedlist.addAll(allorderslunch);
    inflater.inflate(R.menu.main_menu, menu);
    android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
   // SearchView searchView = SearchView SearchView(();
    // searchItem.setVisible(false);
    //getActivity().invalidateOptionsMenu(); Removed because of scrolling toolbar animation
    SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
//        searchView.setIconified(false);
    if (searchItem != null){
        searchView = (SearchView)searchItem.getActionView();
    }
    if(searchView != null){
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Query", newText);
                String userInput = newText.toLowerCase();
                List<UserCredentials> newList = new ArrayList<>();
                List<UserCredentials> newListNewUsers = new ArrayList<>();

                getActivity().onSearchRequested();

                for (int i = 0; i< userlist.size(); i++){

                    if (userlist.get(i).getUsername().toLowerCase().contains(userInput)|| userlist.get(i).getEmpID().toLowerCase().contains(userInput))
                    {
                        newList.add(userlist.get(i));
                    }

                }for (int i = 0; i< newpeoplelist.size(); i++){
                    //Todo address this by uncommenting
                    if (newpeoplelist.get(i).getUsername().toLowerCase().contains(userInput)|| newpeoplelist.get(i).getEmpID().toLowerCase().contains(userInput))
                    {
                        newListNewUsers.add(newpeoplelist.get(i));
                    }

                }
                adapter.updateList(newList);
                adapternewpeps.updateList(newListNewUsers);


                return true;
            }

        };
        searchView.setOnQueryTextListener(queryTextListener);
    }super.onCreateOptionsMenu(menu,inflater);

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



}
