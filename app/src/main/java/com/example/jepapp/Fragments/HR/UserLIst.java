package com.example.jepapp.Fragments.HR;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;



public class UserLIst extends Fragment{

    private RecyclerView hrrecyclerView, hrnewpeoplerecyclerView;
    HRAdapter adapter, adapternewpeps;
    private List<UserCredentials> userlist;
    private List<UserCredentials> newpeoplelist;
    private Button update_all;
    LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    private DividerItemDecoration dividerItemDecoration, dividerItemDecoration1;
    ProgressDialog progressDialog;
    private TextView emptyViewUser, emptyViewNewUser ;
    DatabaseReference databaseReference;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private String subject = "Account balance update";
    private DatabaseReference databaseReferenceuserdata;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hr_userlist_layout, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        emptyViewNewUser = rootView.findViewById(R.id.EmptyViewNewUser);
        emptyViewUser = rootView.findViewById(R.id.EmptyViewUsers);
        userlist = new ArrayList<>();
        newpeoplelist = new ArrayList<>();
        update_all = rootView.findViewById(R.id.update_all);
        hrrecyclerView =  rootView.findViewById(R.id.hr_people_recyclerView);
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
        databaseReferenceuserdata = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        //gets data from firebase
        getUserData();
        getnewUserData();

        //updates the balance of all users
        update_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.update_user_balance, null);
                final AlertDialog.Builder updateAlldialog = new AlertDialog.Builder(getContext());
                updateAlldialog.setView(promptsView);
                updateAlldialog.setTitle("Update All User Balance");
                updateAlldialog.setMessage("Please note the value entered below will be used on all users' current balance");
                updateAlldialog.setCancelable(true);
                final EditText new_balance = promptsView.findViewById(R.id.new_balance_alertdialog);
                updateAlldialog.setPositiveButton(getString(R.string.Add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!new_balance.getText().toString().isEmpty()){

                            databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
                            final int user_balance_to_add = Integer.parseInt(new_balance.getText().toString());
                            for (int i=0; i<userlist.size(); i++){
                                //gets user data
                                int balance = Integer.parseInt(userlist.get(i).getBalance());
                                int avail_balance = Integer.parseInt(userlist.get(i).getAvailable_balance());
                                final String key = userlist.get(i).getEmail();
                                //calculates the balances
                                final int avail_val = avail_balance + user_balance_to_add;
                                final int value= balance + user_balance_to_add;
                                //updates balance and available balance in firebase
                                Query update= databaseReference.orderByChild("email").equalTo(key);
                                update.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NotNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        dataSnapshot.getRef().child("balance").setValue(String.valueOf(value));
                                        dataSnapshot.getRef().child("available_balance").setValue(String.valueOf(avail_val));
                                    }
                                    //reloads adapter
                                    adapter.notifyDataSetChanged();


                                }

                                @Override
                                public void onCancelled(@NotNull DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                }
                            });
                                //sends all user an email with their current available balance
                                String message = "$"+user_balance_to_add+" has been added to your account. Your new available balance is $" + avail_val +"."+"\nThank you for using our system and happy eating";
                                sendEmail(key,message, subject);
                            }
                        }

                        else{
                            Toast toast = Toast.makeText(getContext(),"Please enter an amount", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                updateAlldialog.setNegativeButton(getString(R.string.Subtract), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!new_balance.getText().toString().isEmpty()) {

                            databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");

                            final int user_balance_to_add = Integer.parseInt(new_balance.getText().toString());
                            for (int i = 0; i < userlist.size(); i++) {
                                //gets user data
                                int balance = Integer.parseInt(userlist.get(i).getBalance());
                                int avail_balance = Integer.parseInt(userlist.get(i).getAvailable_balance());
                                String key = userlist.get(i).getEmail();
                                //calculates the balances
                                final int avail_val = avail_balance - user_balance_to_add;
                                final int value = balance - user_balance_to_add;
                                //updates balance and available balance in firebase
                                Query update = databaseReference.orderByChild("email").equalTo(key);
                                update.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NotNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            dataSnapshot.getRef().child("balance").setValue(String.valueOf(value));
                                            dataSnapshot.getRef().child("available_balance").setValue(String.valueOf(avail_val));
                                        }
                                        //reloads the adapter
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NotNull DatabaseError databaseError) {
                                    }
                                });
                                //sends all user an email with their current available balance
                                String message = "$" + user_balance_to_add + " has been subtracted from your account. Your new available balance is $" + avail_val + "."+"\nThank you for using our system and happy eating";
                                sendEmail(key, message, subject);
                            }
                        } else {
                            Toast toast = Toast.makeText(getContext(), "Please enter an amount", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
                updateAlldialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = updateAlldialog.create();
                alertDialog.show();
            }
        });


        return  rootView;
    }

    private void getnewUserData() {
        final ProgressDialog newUserProgress = new ProgressDialog(getContext());
        newUserProgress.setMessage("Getting New User Data");
        newUserProgress.show();
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("Users").orderByChild("balance").equalTo("new");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newpeoplelist.clear();
                for (DataSnapshot newusers : dataSnapshot.getChildren()){

                    UserCredentials newpeople = newusers.getValue(UserCredentials.class);

                    newpeoplelist.add(newpeople);

                }
                //assigns text field if no information is retrieved from the database
                if (newpeoplelist.size()==0){
                    emptyViewNewUser.setVisibility(View.VISIBLE);
                    hrnewpeoplerecyclerView.setVisibility(View.GONE);
                }else{
                    emptyViewNewUser.setVisibility(View.GONE);
                    hrnewpeoplerecyclerView.setVisibility(View.VISIBLE);
                }
                //loads the adapter
                adapternewpeps.notifyDataSetChanged();
                newUserProgress.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            newUserProgress.cancel();
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
    final ProgressDialog userProgress = new ProgressDialog(getContext());
    userProgress.setMessage("Getting User Data");
    userProgress.show();
    databaseReferenceuserdata.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NotNull DataSnapshot snapshot) {
            userlist.clear();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);

                userlist.add(allusers);

            }
            //assigns text field if no information is retrieved from the database
            if (userlist.size()==0){
                emptyViewUser.setVisibility(View.VISIBLE);
                hrrecyclerView.setVisibility(View.GONE);
            }else{
                emptyViewUser.setVisibility(View.GONE);
                hrrecyclerView.setVisibility(View.VISIBLE);
            }
            //loads adapter
            adapter.notifyDataSetChanged();
            userProgress.cancel();
        }

        @Override
        public void onCancelled(@NotNull DatabaseError databaseError) {
            userProgress.cancel();
        }
    });

}

    @Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //deletes any existing menus
       menu.clear();
       //inflates menu
        inflater.inflate(R.menu.main_menu, menu);
        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null){
            searchView = (SearchView)searchItem.getActionView();
        }
        if(searchView != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    String userInput = newText.toLowerCase();
                    List<UserCredentials> newList = new ArrayList<>();
                    List<UserCredentials> newListNewUsers = new ArrayList<>();

                    getActivity().onSearchRequested();

                    for (int i = 0; i< userlist.size(); i++){
                        //searches userlist for matches
                        if (userlist.get(i).getUsername().toLowerCase().contains(userInput)|| userlist.get(i).getEmpID().toLowerCase().contains(userInput))
                        {
                            newList.add(userlist.get(i));
                        }

                    }for (int i = 0; i< newpeoplelist.size(); i++){
                        //searches newusers for matches
                        if (newpeoplelist.get(i).getUsername().toLowerCase().contains(userInput)|| newpeoplelist.get(i).getEmpID().toLowerCase().contains(userInput))
                        {
                            newListNewUsers.add(newpeoplelist.get(i));
                        }

                    }
                    //reloads adapter with new lists containing only matches
                    adapter.updateList(newList);
                    adapternewpeps.updateList(newListNewUsers);


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



}
