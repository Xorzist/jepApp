package com.example.jepapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Signup extends AppCompatActivity {
    String TAG = "Signup Class";
    ProgressDialog progress;
    Spinner departmentspinner;
    EditText reguname, regpass, regemail, regconfirmpass, contactnum, regfullname, regempid;
    ImageView register, returner;
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private DatabaseReference databaseReferenceusers;
    private boolean response;
    private DatabaseReference requestreference;
    


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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        progress = new ProgressDialog(this);
        databaseReferenceusers = FirebaseDatabase.getInstance().getReference("JEP").child("Users");
        requestreference = FirebaseDatabase.getInstance().getReference("JEP").child("Requests");
        requestreferenceQuery();


        if (currentUser != null && currentUser.isEmailVerified()) {
            // User is already logged in. Take him to main activity
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

                if (uname.isEmpty() || checkusername(uname)) {
                    reguname.setError("Please enter a username");
                } else if (password.isEmpty() || password.length() < 6) {
                    regpass.setError("At least 6 characters in length");
                } else if (fullname.isEmpty()) {
                    regfullname.setError("Please enter your name");
                } else if (empID.isEmpty()) {
                    regempid.setError("Please enter your employee ID");
                } else if (email.isEmpty() || !email.matches(emailPattern)) {
                    regemail.setError("Please enter a valid e-mail");
                } else if (passwordconfirmation.isEmpty() || !passwordconfirmation.equals(password)) {
                    regconfirmpass.setError("Passwords do not match");
                } else if (mcontactnum.isEmpty() || mcontactnum.length() < 10) {
                    contactnum.setError("Please enter a valid contact number including area-code");
                } else {
                    final ProgressDialog progressDialog1 = new ProgressDialog(Signup.this);
                    progressDialog1.setMessage("Creating your Account...");
                    progressDialog1.show();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog1.cancel();
                                        // Sign in success, update UI with the signed-in user's information
                                        UserCredentials newuser1,newuser2;
                                        String key = db.child("NewUserBalance").push().getKey();
                                        String balance = "0";
                                        newuser1 = new UserCredentials(mAuth.getUid(), uname, email.toLowerCase(), empID, mcontactnum, mdepartment, balance, fullname, balance);
                                        newuser2 = new UserCredentials(mAuth.getUid(), uname, email.toLowerCase(), empID, mcontactnum, mdepartment, "new", fullname, "new");

                                        db.child("Users")
                                                .child(email.toLowerCase().replace(".", ""))
                                                .setValue(newuser1);
                                        db.child("NewUserBalance")
                                                .child(key)
                                                .setValue(newuser2);
                                        Log.d(TAG, "createUserWithEmail:success");
                                        SendVerificationEmail();
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        Intent inside = new Intent(Signup.this, Login.class);
                                        startActivity(inside);
                                        finish();

                                    } else {
                                        progressDialog1.cancel();
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Signup.this, task.getException().toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }
            }


        });

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

    private void SendVerificationEmail() {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(Signup.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(getCurrentFocus(), "A verification email has been sent to your email,verify your account" +
                            " in order to login", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Signup.this, task.getException().toString(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void requestreferenceQuery() {
    }

    private void showDialog() {
        if (!progress.isShowing())
            progress.show();
    }

    private void hideDialog() {
        if (progress.isShowing())
            progress.dismiss();
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
}
