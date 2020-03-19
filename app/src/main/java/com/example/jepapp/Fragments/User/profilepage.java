package com.example.jepapp.Fragments.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Adapters.Users.BalancerequestAdapter;
import com.example.jepapp.Adapters.Users.MyOrdersAdapter;
import com.example.jepapp.Models.Comments;
import com.example.jepapp.Models.HR.Requests;
import com.example.jepapp.Models.MItems;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class profilepage extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference myDBRef;
    private List<UserCredentials> Requestmatch = new ArrayList<>();
    private List<Comments> myCommentslist = new ArrayList<>();
    private List<Requests> requestsList;
    private TextView Balance,Ordertitle,Contact,Department,request,usernamefield;
    private String balanceRequest,username;
    private RecyclerView recyclerView;
    public MyOrdersAdapter adapter;
    private  BalancerequestAdapter balancerequestAdapter;
    private MyTask task;
    private SimpleDateFormat SimpleDateFormater;
    private Date datenow;
    private DatabaseReference userreference;
    private DatabaseReference requestreference;
    private DividerItemDecoration dividerItemDecoration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.customer_profilepage, container, false);
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        Requestmatch = new ArrayList<UserCredentials>();
        myCommentslist = new ArrayList<Comments>();
        requestsList = new ArrayList<>();
        balancerequestAdapter = new BalancerequestAdapter(getContext(),requestsList);
//        TODO Recycler view gives error,address in the future
//        recyclerView = rootView.findViewById(R.id.pastbalancerequests);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
//        recyclerView.setLayoutManager(linearLayoutManager);
//        //calling adapter
//        recyclerView.setAdapter(balancerequestAdapter);
        mAuth = FirebaseAuth.getInstance();
        SimpleDateFormater = new SimpleDateFormat("dd/MM/yyyy");
        datenow = new Date();
        Balance= rootView.findViewById(R.id.Balanceinfo);
        Department=rootView.findViewById(R.id.departmentinfo);
        Contact=rootView.findViewById(R.id.contactinfo);
        Ordertitle=rootView.findViewById(R.id.ordercount);
        usernamefield = rootView.findViewById(R.id.usernamefield);


        request = rootView.findViewById(R.id.requeststitle);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestmethod();

            }
        });

        userreference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        //Query to update the user information once it has been changed
        userreferenceQuery();

        //Query to get email of current user
        DoEmailquery();

        requestreference = FirebaseDatabase.getInstance().getReference("JEP").child("Requests");
        requestreferenceQuery();


        return rootView;
    }




    private void requestmethod() {
        //Create Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Send A Request");
        //Add Custom Layout
        final View customLayout = getLayoutInflater().inflate(R.layout.customer_balance_request, null);
        builder.setView(customLayout);
        //Setup button to handle the request
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //Setup button to terminated the dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean closeDialog = false;
                EditText requestfield = customLayout.findViewById(R.id.balance_requestinfo);
                setbalanceRequest(requestfield.getText().toString());
                //This statement will prompt the user is the field is empty
                if (getBalanceRequest().isEmpty()){
                    Toast.makeText(getContext(), "The filed is empty", Toast.LENGTH_SHORT).show();
                }
                //This statement will push the request to the db if the field is not empty
                else if (!getBalanceRequest().isEmpty()){
                    task = new MyTask();
                    task.execute();
                    Toast.makeText(getContext(),"Request Submitted",Toast.LENGTH_SHORT).show();
                    DoEmailquery();
                    dialog.dismiss();


                }

            }
        });

    }

    private class MyTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            if (isCancelled()) {
                return null;
            }
            try {

                RequestCreator(getBalanceRequest());
                //Thread.sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == null) {

            }
        }

    }

    private void RequestCreator(String requestamount) {

        //Query to find the email of the  current user in the Users table in the db

        String key =getDb().child("Comments").push().getKey();
        Requests userrequest = new Requests(key,mAuth.getUid(),getUsername(),requestamount,SimpleDateFormater.format(datenow),"Pending");
        getDb().child("Requests")
                .child(key)
                .setValue(userrequest);
        Log.d("Start Adding","START!");
    }

    public void DoEmailquery(){
        Query emailquery = myDBRef.child("Users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

        emailquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials userCredentials = dataSnapshot.getValue(UserCredentials.class);
                    //Log.e("onDataChange: ", allmyorders.getTitle().toString());

                    //Add the user that matches to a list
                    Requestmatch.add(userCredentials);

                }
                //Set the username of the user with the  corresponding email
                setUsername(Requestmatch.get(0).getUsername());
                Balance.setText(Requestmatch.get(0).getBalance());
                Department.setText(Requestmatch.get(0).getDepartment());
                Contact.setText(Requestmatch.get(0).getContact());
                usernamefield.setText(Requestmatch.get(0).getUsername());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }
    private DatabaseReference getDb() {
        return myDBRef;
    }

    private String getBalanceRequest() {
        return balanceRequest;
    }

    private void setbalanceRequest(String balanceRequest) {
        this.balanceRequest = balanceRequest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    private void userreferenceQuery() {
        userreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DoEmailquery();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void requestreferenceQuery() {
        requestreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Requests requests = dataSnapshot.getValue(Requests.class);

                    requestsList.add(requests);
                }
                //update recycler view
                balancerequestAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}




