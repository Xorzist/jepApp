package com.example.jepapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jepapp.Activities.Users.CustomerViewPager;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Signup extends AppCompatActivity {
    ProgressDialog progress;
    Spinner departmentspinner;
    EditText reguname, regpass, regemail, regconfirmpass, contactnum, regfullname, regempid;
    ImageView register, returner;
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private DatabaseReference databaseReferenceusers;
    private DatabaseReference requestreference;
    private ArrayList<UserCredentials> userCredentialsArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_signup);

        reguname = findViewById(R.id.username);
        regfullname = findViewById(R.id.fullname);
        regempid = findViewById(R.id.empID);
        regpass = findViewById(R.id.spassword);
        returner = findViewById(R.id.returner);
        register = findViewById(R.id.register);
        regemail = findViewById(R.id.email);
        regconfirmpass = findViewById(R.id.confirmpassword);
        departmentspinner = findViewById(R.id.department);
        contactnum = findViewById(R.id.contact);
        mAuth = FirebaseAuth.getInstance();
        userCredentialsArrayList = new ArrayList<>();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        progress = new ProgressDialog(this);
        databaseReferenceusers = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        requestreference = FirebaseDatabase.getInstance().getReference("JEP").child("Requests");
        requestreferenceQuery();
        getAllusers();


        if (currentUser != null && currentUser.isEmailVerified()) {
            // User is already logged in. Take him to appropriate interface
            Intent inside = new Intent(Signup.this, CustomerViewPager.class);
            startActivity(inside);
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String uname = reguname.getText().toString().trim();
                final String fullname = regfullname.getText().toString().trim();
                final String empID = regempid.getText().toString().trim();
                String password = regpass.getText().toString().trim();
                final String email = regemail.getText().toString().trim();
                final String mdepartment = departmentspinner.getSelectedItem().toString().trim();
                final String mcontactnum = contactnum.getText().toString().trim();
                String passwordconfirmation = regconfirmpass.getText().toString().trim();
                db = FirebaseDatabase.getInstance().getReference().child("JEP");

                //Determine if the user has entered valid user information
                if (uname.isEmpty() || checkusername(uname)) {
                    reguname.setError("Please check the username you have entered");
                } else if (password.isEmpty() || password.length() < 6) {
                    regpass.setError("At least 6 characters in length");
                } else if (fullname.isEmpty()) {
                    regfullname.setError("Please enter your name");
                } else if (empID.isEmpty() || checkEmpID(empID)) {
                    regempid.setError("Please check the employee ID you have entered");
                } else if (email.isEmpty() || !email.matches(emailPattern)) {
                    regemail.setError("Please enter a valid e-mail");
                } else if (passwordconfirmation.isEmpty() || !passwordconfirmation.equals(password)) {
                    regconfirmpass.setError("Passwords do not match");
                } else if (mcontactnum.isEmpty() || mcontactnum.length() < 10) {
                    contactnum.setError("Please enter a valid contact number including area-code");
                } else {
                    final ProgressDialog CreateAccountDialog = new ProgressDialog(Signup.this);
                    CreateAccountDialog.setMessage("Creating your Account...");
                    CreateAccountDialog.show();
                    //Attempt to add user into the database
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        CreateAccountDialog.cancel();
                                        // Sign in success, update UI with the signed-in user's information


                                        UserCredentials newuser1;
                                        String key = db.child("NewUserBalance").push().getKey();
                                        String balance = "new";
                                        newuser1 = new UserCredentials(mAuth.getUid(), uname, email.toLowerCase(), empID, mcontactnum, mdepartment, balance, fullname, balance);
                                        // Add user credentials to firebase
                                        db.child("Users")
                                                .child(email.toLowerCase().replace(".", ""))
                                                .setValue(newuser1);

                                        SendVerificationEmail();
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //Launches interface
                                        Intent inside = new Intent(Signup.this, Login.class);
                                        startActivity(inside);
                                        finish();

                                    }
                                    else {
                                        CreateAccountDialog.cancel();
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Signup.this, task.getException().toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }
            }


        });
        // Function to return the user to the log in page
        returner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtolog = new Intent(Signup.this, Login.class);
                startActivity(backtolog);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }

    private boolean checkEmpID(String empID) {
        Boolean employeeid= false;
        for (int i = 0; i < userCredentialsArrayList.size(); i++) {
            if (userCredentialsArrayList.get(i).getEmpID().toLowerCase().equals(empID.toLowerCase())){
                employeeid = true;
            }
        }
        return  employeeid;
    }

    //Function to retrieve all users in the database
    private void getAllusers() {
        final ProgressDialog UsersDialog = new ProgressDialog(Signup.this);
        UsersDialog.setMessage("Preparing registration Interface");
        UsersDialog.show();
       userCredentialsArrayList.clear();
        databaseReferenceusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserCredentials userdetails = dataSnapshot.getValue(UserCredentials.class);
                    userCredentialsArrayList.add(userdetails);
                }
                UsersDialog.cancel();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //Function to send a verification email to the user
    private void SendVerificationEmail() {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(Signup.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Determine if the email was sent
                if (task.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(getCurrentFocus(), "A verification email has been sent to your email,verify your account" +
                            " in order to login", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(Signup.this, task.getException().toString(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void requestreferenceQuery() {
    }

    //Function used to check if the username the user desires is already taken
    private boolean checkusername(final String username) {
        Boolean usernameresponse= false;
        for (int i = 0; i < userCredentialsArrayList.size(); i++) {
            if (userCredentialsArrayList.get(i).getUsername().toLowerCase().equals(username.toLowerCase())){
                usernameresponse = true;
            }
        }
       return  usernameresponse;
    }
}
