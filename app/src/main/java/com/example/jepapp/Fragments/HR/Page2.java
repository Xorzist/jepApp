package com.example.jepapp.Fragments.HR;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.HR.HRAdapterRequests;
import com.example.jepapp.GMailSender;
import com.example.jepapp.Models.HR.Requests;
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

public class Page2 extends Fragment {
    private RecyclerView hrrecyclerView;
    HRAdapterRequests adapter;
    private List<com.example.jepapp.Models.HR.Requests> requestlist, newr;
    private List<UserCredentials> userlist;
    private Button accept_all;
    LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    ProgressDialog progressDialog;
    private TextView emptyView;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    private String subject = "RE: Request for balance addition";
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hr_page2layout, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        //emptyView = rootView.findViewById(R.id.empty_view);
        requestlist = new ArrayList<>();
        userlist = new ArrayList<>();
        newr = new ArrayList<>();
        accept_all = rootView.findViewById(R.id.accept_all);
        hrrecyclerView = (RecyclerView) rootView.findViewById(R.id.hr_requests_recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(hrrecyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new HRAdapterRequests(getContext(),requestlist,userlist);
        hrrecyclerView.setLayoutManager(linearLayoutManager);
        hrrecyclerView.addItemDecoration(dividerItemDecoration);
        hrrecyclerView.setAdapter(adapter);
        sharedPreferences = getContext().getSharedPreferences("requests",MODE_PRIVATE);
        getUserData();
        getRequestData();
        setHasOptionsMenu(true);
       // recyclerCount();

        accept_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < requestlist.size(); i++){
                    //checking userlist for model that has same userID
                    for(int a=0; a<userlist.size(); a++){
                        if(userlist.get(a).getUserID().equals(requestlist.get(i).getUserID())){
                            final UserCredentials userinfo = userlist.get(a);
                            //adding request amount to users current balance
                            int current_balance = Integer.parseInt(userinfo.getBalance());
                            int value = Integer.parseInt(requestlist.get(i).getamount());
                            int new_balance = current_balance+value;
                            //update users balance in the database
                            doupdate(String.valueOf(new_balance),userinfo);
                            String state = "accepted";
                            String message = "Dear "+ userinfo.getUsername() +",\n"+"Your request of $"+ requestlist.get(i).getamount()+" has been "+ state +"."  + " Please check your balance for updates";
                            //send user an email with the new status of their application
                            sendEmail(userinfo.getEmail(), message, subject);
                            // update the state of request in database
                            updateRequest(requestlist.get(i), state, databaseReference);

                        }
                    }

                }
                Log.e("userlist",String.valueOf(userlist.size()));
                Log.e ("requestlist", String.valueOf(requestlist.size()));
            }
        });


        return  rootView;
    }


    private void getRequestData() {
        final ProgressDialog progressDialog2 = new ProgressDialog(getContext());
        progressDialog2.setMessage("Getting User Data");
        progressDialog2.show();
        // checking for pending requests and adding them to a list to be attached to the adapter
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("Requests")
                .orderByChild("status").equalTo("pending");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                requestlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Requests pendingrequests = dataSnapshot.getValue(Requests.class);
                    requestlist.add(pendingrequests);

                }
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//
//                editor.putInt("request number",requestlist.size());
//                // editor.putBoolean("IsLogin",true);
//                editor.commit();
                adapter.notifyDataSetChanged();
                newr.addAll(requestlist);
                progressDialog2.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog2.dismiss();

            }
        });

   }
    private void doupdate(final String value, UserCredentials user) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        Query update_Balance= databaseReference.orderByChild("email").equalTo(user.getEmail());
        update_Balance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot updateQuantity: dataSnapshot.getChildren()){
                    updateQuantity.getRef().child("balance").setValue(value);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendEmail(String email, String message, String subject) {

        //Creating SendMail object
        GMailSender sm = new GMailSender(getContext(), email, message, subject);

        //Executing sendmail to send email
        sm.execute();
    }
    public void updateRequest(Requests request, final String state, DatabaseReference databaseReference){
        Query update_Status= databaseReference.orderByChild("key").equalTo(request.getKey());
        update_Status.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot updateStatus: dataSnapshot.getChildren()){
                    updateStatus.getRef().child("status").setValue(state);

                } adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        Toast toast = Toast.makeText(getContext(),
                "Request has been processed",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void getUserData() {

        DatabaseReference databaseReference;
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.user, menu);
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
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    Log.d("Query", newText);
                    String userInput = newText.toLowerCase();
                    List<Requests> newList = new ArrayList<>();

                    getActivity().onSearchRequested();

                    for (int i = 0; i< requestlist.size(); i++){

                        if (requestlist.get(i).getUsername().toLowerCase().contains(userInput)|| requestlist.get(i).getdate().toLowerCase().contains(userInput)) {

                            newList.add(requestlist.get(i));

                        }

                        // }

                    }
                    adapter.notifyDataSetChanged();
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

//    private int recyclerCount(){
//        int count = 0;
//        if (adapter != null) {
//            count = adapter.getItemCount();
//        }
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//
//        editor.putInt("request number",count);
//        // editor.putBoolean("IsLogin",true);
//        editor.commit();
//        return count;
//    }




}
