package com.example.jepapp.Fragments.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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
import com.google.firebase.auth.AuthCredential;
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
    private EditText Balance,Contact,Department,usernamefield,fullnamefield,emailfield,employeeidfield,availableBalance;

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
    private LinearLayout submitcancelayout,fullnamelayout,usernamelayout,departmentlayout,contactlayout;
    private Button request,submitedit,canceledit,updatepassword,
            tile100,tile500,tile1000,tile2000,tile5000,tileother;
    private ImageView deleteprofile;
    private boolean response,success;
    private DatabaseReference databaseReferenceusers;
    private MyTask task;
    private int fixup=1;
    boolean passwordresult = false;



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
        databaseReferenceusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DoEmailquery();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        SimpleDateFormater = new SimpleDateFormat("dd/MM/yyyy");
        datenow = new Date();
        currentemail = mAuth.getCurrentUser().getEmail();
        Balance= rootView.findViewById(R.id.Balanceinfo);
        Department=rootView.findViewById(R.id.departmentinfo);
        Contact=rootView.findViewById(R.id.contactinfo);
        usernamefield = rootView.findViewById(R.id.usernamefield);
        availableBalance = rootView.findViewById(R.id.availbalanceinfo);
        fullnamefield = rootView.findViewById(R.id.fullnamefield);
        emailfield = rootView.findViewById(R.id.emailfield);
        employeeidfield = rootView.findViewById(R.id.employeeidfield);
        submitcancelayout = rootView.findViewById(R.id.submitcanclelayout);
        deleteprofile = rootView.findViewById(R.id.deleteprofile);
        submitedit = rootView.findViewById(R.id.submiteditprofile);
        updatepassword = rootView.findViewById(R.id.updatepassword);
        tile100 = rootView.findViewById(R.id.tile100);
        tile100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmountRequest("100");
            }
        });
        tile500 = rootView.findViewById(R.id.tile500);
        tile500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmountRequest("500");
            }
        });
        tile1000 = rootView.findViewById(R.id.tile1000);
        tile1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmountRequest("1000");
            }
        });
        tile2000 = rootView.findViewById(R.id.tile2000);
        tile2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmountRequest("2000");
            }
        });
        tile5000 = rootView.findViewById(R.id.tile5000);
        tile5000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmountRequest("5000");
            }
        });
        tileother = rootView.findViewById(R.id.tileother);
        tileother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomRequest();
            }
        });

        updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordupdate();
            }
        });


        submitcancelayout.setVisibility(View.GONE);

        response = false;
        success = false;



        deleteprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                builder1.setTitle("Delete Your Profile!");
                builder1.setMessage("Enter your password to delete your profile");
                //builder1.setCancelable(true);
                final EditText input = new EditText(getContext());
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder1.setView(input);
                builder1.setIcon(R.drawable.denied);
                builder1.setPositiveButton(
                        "Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                if(input.getText().length()==0){
                                    Toast.makeText(getContext(), "The password is empty", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    AuthCredential credential = EmailAuthProvider.getCredential(currentemail,input.getText().toString());
                                    mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                DeleteUser(currentemail);
                                                dialog.cancel();

                                            }
                                            else{
                                                Toast.makeText(getContext(), "Password is Incorrect", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
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

        userreference = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        //Query to update the user information once it has been changed
        userreferenceQuery();

        //Query to get email of current user
        DoEmailquery();

        requestreference = FirebaseDatabase.getInstance().getReference("JEP").child("Requests");
        requestreferenceQuery();

        canceledit = rootView.findViewById(R.id.canceleditprofile);
        fullnamelayout = rootView.findViewById(R.id.fullnameeditlayout);
        fullnamelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatepassword.setVisibility(View.GONE);
                submitcancelayout.setVisibility(View.VISIBLE);
                fullnamefield.setEnabled(true);
                Snackbar snackbar = Snackbar.make(getView(), "Full name field  now editable", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        usernamelayout = rootView.findViewById(R.id.usernameeditlayout);
        usernamelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatepassword.setVisibility(View.GONE);
                submitcancelayout.setVisibility(View.VISIBLE);
                usernamefield.setEnabled(true);
                Snackbar snackbar = Snackbar.make(getView(), "username field  now editable", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        departmentlayout = rootView.findViewById(R.id.departmenteditlayout);
        departmentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatepassword.setVisibility(View.GONE);
                submitcancelayout.setVisibility(View.VISIBLE);
                Department.setEnabled(true);
                Snackbar snackbar = Snackbar.make(getView(), "Department field now editable", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        contactlayout = rootView.findViewById(R.id.Contacteditlayout);
        contactlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatepassword.setVisibility(View.GONE);
                submitcancelayout.setVisibility(View.VISIBLE);
                Contact.setEnabled(true);
                Snackbar snackbar = Snackbar.make(getView(), "Contact field now editable", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        submitedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkfields(fullnamefield.getText().toString().trim(),usernamefield.getText().toString().trim(),
                        Contact.getText().toString().trim(),Department.getText().toString().trim(),emailfield.getText().toString().trim(),
                        employeeidfield.getText().toString().trim());

            }
        });
        canceledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatepassword.setVisibility(View.VISIBLE);
                submitcancelayout.setVisibility(View.GONE);
                Contact.setEnabled(false);
                Department.setEnabled(false);
                usernamefield.setEnabled(false);
                fullnamefield.setEnabled(false);



            }
        });


        return rootView;
    }

    private void CustomRequest() {
        //Create Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Send A Custom Request");
        //Add Custom Layout
        final View customLayout = getLayoutInflater().inflate(R.layout.customrequestlayout, null);
        builder.setView(customLayout);
        builder.setIcon(R.drawable.adminprofile);

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
                final EditText customrequests = customLayout.findViewById(R.id.customrequest);
                EditText custompassword = customLayout.findViewById(R.id.customrequestpassword);
                setbalanceRequest(customrequests.getText().toString());
                //This statement will prompt the user is the field is empty
                if (customrequests.getText().length()==0 || Integer.valueOf(customrequests.getText().toString())<=0 ||
                        custompassword.getText().toString().length()==0){
                    Toast.makeText(getContext(), "Please check input fields", Toast.LENGTH_LONG).show();
                }

                //This statement will push the request to the db if the field is not empty
                else  {
                    mAuth.signInWithEmailAndPassword(emailfield.getText().toString(),custompassword.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task2) {
                                    if (task2.isSuccessful()) {//If users password is correct
                                        setbalanceRequest(customrequests.getText().toString());
                                        task = new MyTask();
                                        task.execute();
                                        Toast.makeText(getContext(),"Request Submitted",Toast.LENGTH_SHORT).show();
                                        DoEmailquery();
                                        setPasswordresult(false);
                                        dialog.dismiss();
                                        Log.d("Success", "Successful");
                                    }
                                    else {
                                        // If password entry fails, display a message to the user.
                                        Log.w("huh", "signInWithEmail:failure", task2.getException());
                                        Toast.makeText(getContext(), "Your Password is Incorrect", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });


    }



    private void AmountRequest(final String amount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("$"+amount+" Request!");
        builder.setMessage("Are you sure you want to send a request for $"+amount+" ?");

        final View customLayout = getLayoutInflater().inflate(R.layout.genericrequestlayout, null);
        builder.setView(customLayout);
        builder.setIcon(R.drawable.adminprofile);

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
                EditText genericpassword = customLayout.findViewById(R.id.genericPassword);
                //This statement will prompt the user is the field is empty
                if (genericpassword.getText().length()==0 ){
                    Toast.makeText(getContext(), "Please check input fields", Toast.LENGTH_LONG).show();
                }

                //This statement will push the request to the db if the field is not empty
                else  {
                    mAuth.signInWithEmailAndPassword(emailfield.getText().toString(),genericpassword.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task2) {
                                    if (task2.isSuccessful()) {//If users password is correct
                                        setbalanceRequest(amount);
                                        task = new MyTask();
                                        task.execute();
                                        Toast.makeText(getContext(),"Request Submitted",Toast.LENGTH_SHORT).show();
                                        DoEmailquery();
                                        setPasswordresult(false);
                                        dialog.dismiss();
                                        Log.d("Success", "Successful");
                                    }
                                    else {
                                        // If password entry fails, display a message to the user.
                                        Log.w("huh", "signInWithEmail:failure", task2.getException());
                                        Toast.makeText(getContext(), "Your Password is Incorrect", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });


    }

    private void sendPasswordupdate() {
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder1.setMessage("Are you sure you wish to update your password?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAuth.sendPasswordResetEmail(currentemail );
                        Snackbar snackbar = Snackbar.make(getView(), "Please check your email to update your password", Snackbar.LENGTH_LONG);
                        snackbar.show();
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


    private void checkfields(final String fullnames, final String usernames, final String contactnums, final String Departments,
                             final String emailfields, final String employeeidfields) {

        //Check if username is unique
        if (checkusername(usernames) || variableChecker(usernames)){
            usernamefield.setError("Please correct this field");
        }
        else if (variableChecker(fullnames)){
            fullnamefield.setError("This field cannot be empty");
        }
        else if (contactnums.length()<=10||variableChecker(contactnums)){
            Contact.setError("Please ensure a area code is entered");
        }
        else if (variableChecker(Departments)){
            Department.setError("Please correct this field");
        }

        else {
            Updateuser(fullnames,usernames,contactnums,Departments,emailfields,employeeidfields);

        }
        }

    private void Updateuser(String fullnames, String usernames, String contactnums, String departments,
                            String emailfields, String employeeidfields) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Updating Profile");
        progressDialog.show();
            UserCredentials updateuser= new UserCredentials(mAuth.getUid(),usernames,emailfields,employeeidfields,contactnums,
                    departments,Balance.getText().toString(),fullnames, Balance.getText().toString());
            myDBRef.child("Users")
                .child(emailfields.toLowerCase().replace(".",""))
                .setValue(updateuser);
        Snackbar snackbar = Snackbar.make(getView(), "Your details have been updated", Snackbar.LENGTH_SHORT);
        snackbar.show();
        progressDialog.dismiss();
        Intent inside = new Intent(getContext(), CustomerViewPager.class);
        startActivity(inside);
        getActivity().finish();

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
        builder.setTitle("View All Requests");
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
        builder.setPositiveButton("Close",null) ;
        final AlertDialog dialog = builder.create();
        dialog.show();




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
        //Function to send a request with the entered amount
        String key =getDb().child("Requests").push().getKey();
        Requests userrequest = new Requests(key,mAuth.getUid(),getUsername(),requestamount,SimpleDateFormater.format(datenow),"pending",
                employeeidfield.getText().toString());
        getDb().child("Requests")
                .child(key)
                .setValue(userrequest);
        Log.d("Start Adding","START!");
        balancerequestAdapter.notifyDataSetChanged();
    }

    public void DoEmailquery(){
//        final ProgressDialog progressDialog1 = new ProgressDialog(getContext());
//        progressDialog1.setMessage("Getting Email");
//        progressDialog1.show();
        //Function to get the details of the current user
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
                availableBalance.setText(Requestmatch.get(0).getAvailable_balance());

                //progressDialog1.cancel();


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
        //Helper function to ensure username is assigned
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
        final ProgressDialog progressDialog2 = new ProgressDialog(getContext());
        progressDialog2.setMessage("Getting My Details");
        progressDialog2.show();
        //Query to find all requests for the current user
        Query requestreference = myDBRef.child("Requests").orderByChild("userID").equalTo(mAuth.getCurrentUser().getUid());

        requestreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                requestsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Requests requests = dataSnapshot.getValue(Requests.class);

                    //Add to the list that will be used for the adapter
                    requestsList.add(requests);
                }
                Collections.reverse(requestsList);
                //update recycler view
                balancerequestAdapter.notifyDataSetChanged();
                progressDialog2.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }
    private void DeleteUser(final String deletecurrentemail) {

        final ProgressDialog progressDialog3 = new ProgressDialog(getContext());
        progressDialog3.setMessage("Deleting Profile");
        progressDialog3.show();

        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    myDBRef.child("Users").child(deletecurrentemail.replace(".","")).removeValue();
                    Intent i = new Intent(getActivity(), Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().finish();
                    startActivity(i);
                    progressDialog3.cancel();
                    //FirebaseAuth.getInstance().signOut();
                    Log.e("Signout", "Done ");


                }else{
                    Toast.makeText(getContext(), "Unable to delete profile at this time, please sign out and sign in once again", Toast.LENGTH_SHORT).show();
                    progressDialog3.cancel();
                }
            }
        });

    }

    public boolean isPasswordresult() {
        return passwordresult;
    }

    public void setPasswordresult(boolean passwordresult) {
        this.passwordresult = passwordresult;
    }
}




