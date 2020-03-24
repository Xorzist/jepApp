package com.example.jepapp.Fragments.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jepapp.Activities.Login;
import com.example.jepapp.Activities.Signup;
import com.example.jepapp.Activities.Users.CustomerViewPager;
import com.example.jepapp.Adapters.Users.BalancerequestAdapter;
import com.example.jepapp.Adapters.Users.MyOrdersAdapter;
import com.example.jepapp.Models.Comments;
import com.example.jepapp.Models.HR.Requests;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class profilepage extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference myDBRef;
    private List<UserCredentials> Requestmatch = new ArrayList<>();
    private List<UserCredentials> Alluserslist = new ArrayList<>();
    private List<Comments> myCommentslist = new ArrayList<>();
    private List<Requests> requestsList;
    private EditText Balance,Contact,Department,usernamefield,fullnamefield,emailfield,employeeidfield,
            passwordoldfield,passwordnewfield;
    private String balanceRequest,username,currentemail;
    private RecyclerView recyclerView;
    public MyOrdersAdapter adapter;
    private  BalancerequestAdapter balancerequestAdapter;
    private SimpleDateFormat SimpleDateFormater;
    private Date datenow;
    private DatabaseReference userreference;
    private DatabaseReference requestreference;
    private DividerItemDecoration dividerItemDecoration;
    private TextView passwordoldtitle,passwordnewtitle;
    private LinearLayout submitcancelayout;
    private Button request,submitedit,canceledit;
    private ImageView editprofile,deleteprofile;
    private boolean canedit,response,success;
    private DatabaseReference databaseReferenceusers;
    private MyTask task;
    private int fixup=1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.customer_profilepage, container, false);
        mAuth = FirebaseAuth.getInstance();
        myDBRef = FirebaseDatabase.getInstance().getReference().child("JEP");
        Requestmatch = new ArrayList<UserCredentials>();
        myCommentslist = new ArrayList<Comments>();
        requestsList = new ArrayList<>();
        balancerequestAdapter = new BalancerequestAdapter(getContext(),requestsList);
        databaseReferenceusers = FirebaseDatabase.getInstance().getReference("JEP").child("Users");


//        TODO Recycler view gives error,address in the future
//        recyclerView = rootView.findViewById(R.id.pastbalancerequests);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
//        recyclerView.setLayoutManager(linearLayoutManager);
//        //calling adapter
//        recyclerView.setAdapter(balancerequestAdapter);


        SimpleDateFormater = new SimpleDateFormat("dd/MM/yyyy");
        datenow = new Date();
        currentemail = mAuth.getCurrentUser().getEmail();
        Balance= rootView.findViewById(R.id.Balanceinfo);
        Department=rootView.findViewById(R.id.departmentinfo);
        Contact=rootView.findViewById(R.id.contactinfo);
        usernamefield = rootView.findViewById(R.id.usernamefield);
        fullnamefield = rootView.findViewById(R.id.fullnamefield);
        emailfield = rootView.findViewById(R.id.emailfield);
        employeeidfield = rootView.findViewById(R.id.employeeidfield);
        passwordoldtitle = rootView.findViewById(R.id.passwordoldtitle);
        passwordoldfield = rootView.findViewById(R.id.passwordoldfield);
        passwordnewtitle = rootView.findViewById(R.id.passwordnewtitle);
        passwordnewfield = rootView.findViewById(R.id.passwordnewfield);
        submitcancelayout = rootView.findViewById(R.id.submitcanclelayout);
        deleteprofile = rootView.findViewById(R.id.deleteprofile);
        submitedit = rootView.findViewById(R.id.submiteditprofile);
        canceledit = rootView.findViewById(R.id.canceleditprofile);
        editprofile = rootView.findViewById(R.id.editprofile);
        passwordoldfield.setVisibility(View.GONE);
        passwordnewfield.setVisibility(View.GONE);
        passwordoldtitle.setVisibility(View.GONE);
        passwordnewtitle.setVisibility(View.GONE);
        submitcancelayout.setVisibility(View.GONE);
        canedit=false;
        response = false;
        success = false;


        deleteprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you wish to delete your profile?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DeleteUser(currentemail);
                                mAuth.signOut();
                                Intent i = new Intent(getContext(), Login.class);
                                startActivity(i);
                                getActivity().finish();

                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                androidx.appcompat.app.AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        request = rootView.findViewById(R.id.requeststitle);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestmethod();

            }
        });
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editprofileoperation();
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

    private void editprofileoperation() {

        if(!canedit) {
            Snackbar snackbar = Snackbar.make(getView(), "Profile Details are now editable", Snackbar.LENGTH_SHORT);
            snackbar.show();
            canedit=true;
        }
        else{
            Snackbar snackbar = Snackbar.make(getView(), "PROFILE DETAILS ALREADY EDITABLE", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        passwordoldfield.setVisibility(View.VISIBLE);
        passwordnewfield.setVisibility(View.VISIBLE);
        passwordoldtitle.setVisibility(View.VISIBLE);
        passwordnewtitle.setVisibility(View.VISIBLE);
        submitcancelayout.setVisibility(View.VISIBLE);
        Contact.setEnabled(true);
        Department.setEnabled(true);
        usernamefield.setEnabled(true);
        fullnamefield.setEnabled(true);
        employeeidfield.setEnabled(true);
        emailfield.setEnabled(true);

        submitedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkfields(fullnamefield.getText().toString().trim(),usernamefield.getText().toString().trim(),
                        Contact.getText().toString().trim(),Department.getText().toString().trim(),emailfield.getText().toString().trim(),
                        employeeidfield.getText().toString().trim(),passwordoldfield.getText().toString().trim(),passwordnewfield.getText().toString().trim());

            }
        });

        canceledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canedit=false;
                passwordoldfield.setVisibility(View.GONE);
                passwordnewfield.setVisibility(View.GONE);
                passwordoldtitle.setVisibility(View.GONE);
                passwordnewtitle.setVisibility(View.GONE);
                submitcancelayout.setVisibility(View.GONE);
                Contact.setEnabled(false);
                Department.setEnabled(false);
                usernamefield.setEnabled(false);
                fullnamefield.setEnabled(false);
                emailfield.setEnabled(false);
                employeeidfield.setEnabled(false);
                emailfield.setEnabled(false);
                employeeidfield.setEnabled(false);
            }
        });
    }

    private void checkfields(final String fullnames, final String usernames, final String contactnums, final String Departments,
                             final String emailfields, final String employeeidfields, String oldpasswords, String newpasswords) {
        oldpasswords= passwordoldfield.getText().toString().trim();
        //Check if username is unique
        if (checkusername(usernames) || variableChecker(usernames)){
            usernamefield.setError("Please correct this field");
        }
        else if (variableChecker(fullnames)){
            fullnamefield.setError("This field cannot be empty");
        }
        else if (variableChecker(contactnums)){
            Contact.setError("Please correct this field");
        }
        else if (variableChecker(Departments)){
            Department.setError("Please correct this field");
        }
        else if (variableChecker(emailfields)){
            emailfield.setError("Please correct this field");
        }
        else if(variableChecker(employeeidfields)) {
            employeeidfield.setError("Please correct this field");
        }
        else if (passwordchecker(oldpasswords)==true){
            passwordoldfield.setError("This password is incorrect");

        }
        else {
            //Check to see if the user wants to change his password
            if(newpasswords.isEmpty()) {
                //Check to see if the user wants to change his email
                if(!emailfields.toLowerCase().equals(mAuth.getCurrentUser().getEmail().toLowerCase())) {
                    NewUser(fullnames,usernames,contactnums,Departments,emailfields,employeeidfields,oldpasswords);
                }
                else {
                    Updateuser(fullnames,usernames,contactnums,Departments,emailfields,employeeidfields,oldpasswords);
                }

        }
            else{
                //Check to see if the user wants to change his email
                if(!emailfields.toLowerCase().equals(mAuth.getCurrentUser().getEmail().toLowerCase())) {
                    NewUser(fullnames,usernames,contactnums,Departments,emailfields,employeeidfields,newpasswords);
                }
                else {
                    Updateuser(fullnames,usernames,contactnums,Departments,emailfields,employeeidfields,newpasswords);
                }
            }

        }



    }

    private void NewUser(final String fullnames, final String usernames, final String contactnums, final String departments,
                         final String emailfields, final String employeeidfields, String oldpasswords) {
        mAuth.createUserWithEmailAndPassword(emailfields, oldpasswords)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            UserCredentials newuser;
                            newuser = new UserCredentials(mAuth.getUid(),usernames,emailfields,employeeidfields,contactnums,
                                    departments,Balance.getText().toString(),fullnames);
                            myDBRef.child("Users")
                                    .child(emailfields.toLowerCase().replace(".",""))
                                    .setValue(newuser);
                            Snackbar snackbar = Snackbar.make(getView(), "Please Sign in Once more", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            DeleteUser(currentemail);
                            Intent inside = new Intent(getContext(), Login.class);
                            startActivity(inside);
                            getActivity().finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar snackbar = Snackbar.make(getView(), "The email already exists", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }

                });

    }




    private void Updateuser(String fullnames, String usernames, String contactnums, String departments,
                            String emailfields, String employeeidfields, String oldpasswords) {

            UserCredentials updateuser= new UserCredentials(mAuth.getUid(),usernames,emailfields,employeeidfields,contactnums,
                    departments,Balance.getText().toString(),fullnames);
            myDBRef.child("Users")
                .child(emailfields.toLowerCase().replace(".",""))
                .setValue(updateuser);
        Snackbar snackbar = Snackbar.make(getView(), "Your details have been updated", Snackbar.LENGTH_SHORT);
        snackbar.show();
        Intent inside = new Intent(getContext(), Login.class);
        startActivity(inside);
        getActivity().finish();



    }
    private boolean passwordchecker(final String oldpasswords) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Checking Password");

            boolean idk;
            if (oldpasswords.isEmpty()){
                success=true;
                Log.e("emptycheck?",String.valueOf(success));
            }
            else {
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(currentemail, oldpasswords).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                                Log.e("signer",String.valueOf(task.isSuccessful()));
                                fixup=fixup-1;
                                success = false;
                                while(fixup>=1){
                                    submitedit.callOnClick();
                                }

                                progressDialog.dismiss();

                        } else {
                            success = true;
                            Log.e("signerelse",String.valueOf(success));
                            progressDialog.dismiss();

                        }

                    }

                });
            }
            idk = success;


        return idk;
    }

    //Method to run a check on entered varialbe
    private boolean variableChecker(String variable){
        if (variable.isEmpty() || variable.length() > 30) {
            return true;
        }
        else {
            return false;
        }

    }

    private boolean checkusername(final String usernames) {
        databaseReferenceusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child(usernames).exists()) {
                        response = true;
                    } else {
                        response = false;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return response;
    }


    private void requestmethod() {
        //Create Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Send A Request");
        //Add Custom Layout
        final View customLayout = getLayoutInflater().inflate(R.layout.customer_balance_request, null);
        builder.setView(customLayout);
        recyclerView = customLayout.findViewById(R.id.pastbalancerequests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        //calling adapter
        recyclerView.setAdapter(balancerequestAdapter);
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
                    Toast.makeText(getContext(), "The field is empty", Toast.LENGTH_SHORT).show();
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

        String key =getDb().child("Requests").push().getKey();
        Requests userrequest = new Requests(key,mAuth.getUid(),getUsername(),requestamount,SimpleDateFormater.format(datenow),"pending");
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
                Contact.setText(Requestmatch.get(0).getContactnumber());
                usernamefield.setText(Requestmatch.get(0).getUsername());
                fullnamefield.setText(Requestmatch.get(0).getName());
                emailfield.setText(Requestmatch.get(0).getEmail());
                employeeidfield.setText(Requestmatch.get(0).getEmpID());


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
        //Query to find all requests for the current user
        Query requestreference = myDBRef.child("Requests").orderByChild("userID").equalTo(mAuth.getCurrentUser().getUid());

        requestreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Requests requests = dataSnapshot.getValue(Requests.class);

                    //Add to the list that will be used for the adapter
                    requestsList.add(requests);
                }
                Collections.reverse(requestsList);
                //update recycler view
                balancerequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }
    private void DeleteUser(String deletecurrentemail) {
        myDBRef.child("Users").child(deletecurrentemail.replace(".","")).removeValue();
        mAuth.getCurrentUser().delete();

    }
}




