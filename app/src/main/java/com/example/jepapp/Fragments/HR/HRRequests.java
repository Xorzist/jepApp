package com.example.jepapp.Fragments.HR;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
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


public class HRRequests extends Fragment {
    private RecyclerView hrrecyclerView;
    private HRAdapterRequests adapter;
    private List<com.example.jepapp.Models.HR.Requests> requestlist, newr;
    private List<UserCredentials> userlist;
    private Button accept_all;
    private TextView emptyViewrequest;
    DatabaseReference databaseReference;
    private String subject = "RE: Request for balance addition";
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hr_requestslayout, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        emptyViewrequest = rootView.findViewById(R.id.empty_viewrequest);
        requestlist = new ArrayList<>();
        userlist = new ArrayList<>();
        newr = new ArrayList<>();
        accept_all = rootView.findViewById(R.id.accept_all);
        hrrecyclerView =  rootView.findViewById(R.id.hr_requests_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(hrrecyclerView.getContext(), linearLayoutManager.getOrientation());
        adapter = new HRAdapterRequests(getContext(),requestlist,userlist);
        hrrecyclerView.setLayoutManager(linearLayoutManager);
        hrrecyclerView.addItemDecoration(dividerItemDecoration);
        hrrecyclerView.setAdapter(adapter);
        //retrieve data from firebase
        getUserData();
        getRequestData();
        setHasOptionsMenu(true);

        //accept all requests
        accept_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < requestlist.size(); i++){
                    //checking userlist for model that has same employee ID
                    for(int a=0; a<userlist.size(); a++){
                        if(userlist.get(a).getEmpID().equals(requestlist.get(i).getEmpID())){
                            final UserCredentials userinfo = userlist.get(a);
                            //adding request amount to users current balance
                            int current_balance = Integer.parseInt(userinfo.getBalance());
                            int avail_balance = Integer.parseInt(userinfo.getAvailable_balance());
                            int value = Integer.parseInt(requestlist.get(i).getamount());
                            int new_balance = current_balance+value;
                            int newavail_bal = avail_balance+value;
                            //update users balance in the database
                            doupdate(String.valueOf(new_balance),String.valueOf(newavail_bal),userinfo);
                            String state = "accepted";
                            String message = "Dear "+ userinfo.getUsername() +",\n"+"Your request of $"+ requestlist.get(i).getamount()+" has been "+ state +"."  + " Please check your balance for updates";
                            //send user an email with the new status of their application
                            sendEmail(userinfo.getEmail(), message, subject);
                            // update the state of request in database
                            updateRequest(requestlist.get(i), state, databaseReference);

                        }
                    }

                }
            }
        });
        return  rootView;
    }


    private void getRequestData() {
        final ProgressDialog requestDataDialog = new ProgressDialog(getContext());
        requestDataDialog.setMessage("Getting Request Data");
        requestDataDialog.show();
        // checking for pending requests and adding them to a list to be attached to the adapter
        Query query = FirebaseDatabase.getInstance().getReference("JEP").child("Requests")
                .orderByChild("status").equalTo("pending");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                //clears the list of any previous request data
                requestlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    com.example.jepapp.Models.HR.Requests pendingrequests = dataSnapshot.getValue(com.example.jepapp.Models.HR.Requests.class);
                    //adds all request data into list
                    requestlist.add(pendingrequests);

                }
                if(requestlist.isEmpty()){
                    emptyViewrequest.setVisibility(View.VISIBLE);
                    hrrecyclerView.setVisibility(View.GONE);
                }else{
                    emptyViewrequest.setVisibility(View.GONE);
                    hrrecyclerView.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                requestDataDialog.cancel();
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

                requestDataDialog.dismiss();

            }
        });

   }
    private void doupdate(final String value, final String s, UserCredentials user) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        Query update_Balance= databaseReference.orderByChild("email").equalTo(user.getEmail());
        update_Balance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot updateQuantity: dataSnapshot.getChildren()){
                    //updates the balance and available balance fields
                    updateQuantity.getRef().child("balance").setValue(value);
                    updateQuantity.getRef().child("available_balance").setValue(s);
                }
                //updates the adapter to reflect new balances
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
    //updates the status of a request
    public void updateRequest(com.example.jepapp.Models.HR.Requests request, final String state, DatabaseReference databaseReference){
        databaseReference.child(request.getKey()).child("status").setValue(state);
        adapter.notifyDataSetChanged();
        Toast toast = Toast.makeText(getContext(),
                "Request has been processed",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void getUserData() {
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        //gets all user data from firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                //clears the list of any previous user data
                userlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials allusers = dataSnapshot.getValue(UserCredentials.class);
                    //adds all user data into list
                    userlist.add(allusers);
                }
                //reloads adapter
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //deletes any existing menu
        menu.clear();
        //inflates new menu
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
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    String userInput = newText.toLowerCase();
                    List<com.example.jepapp.Models.HR.Requests> newList = new ArrayList<>();

                    getActivity().onSearchRequested();

                    for (int i = 0; i< requestlist.size(); i++){
                        //searches for instances of the search key word in the object
                        if (requestlist.get(i).getUsername().toLowerCase().contains(userInput)|| requestlist.get(i).getdate().toLowerCase().contains(userInput)) {

                            newList.add(requestlist.get(i));

                        }

                    }
                    //updates the adapter with only objects that have the keyword
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
        if (item.getItemId() == R.id.action_search) {
            return false;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }



}
