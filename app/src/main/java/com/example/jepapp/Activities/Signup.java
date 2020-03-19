package com.example.jepapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jepapp.Activities.Users.CustomerViewPager;
import com.example.jepapp.Models.Admin;
import com.example.jepapp.Models.UserCredentials;
import com.example.jepapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    String TAG="Signup Class";
    ProgressDialog progress;
    EditText reguname,regpass,regemail,regconfirmpass,contactnum,department;
    ImageView register,returner;
    private FirebaseAuth mAuth;
    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_signup);
//        getSupportActionBar().setTitle("Register");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reguname=findViewById(R.id.fullname);
        regpass=findViewById(R.id.spassword);
        returner=findViewById(R.id.returner);
        register=findViewById(R.id.register);
        regemail = findViewById(R.id.email);
        regconfirmpass = findViewById(R.id.confirmpassword);
        department = findViewById(R.id.department);
        contactnum = findViewById(R.id.contact);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        progress=new ProgressDialog(this);


        if (currentUser!=null) {
            // User is already logged in. Take him to main activity
            Intent inside=new Intent(Signup.this, CustomerViewPager.class);
            startActivity(inside);
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String uname = reguname.getText().toString().trim();
                String password = regpass.getText().toString().trim();
                final String email = regemail.getText().toString().trim();
                final String mdepartment = department.getText().toString().trim();
                final String mcontactnum = contactnum.getText().toString().trim();
                String passwordconfirmation = regconfirmpass.getText().toString().trim();
                db =FirebaseDatabase.getInstance().getReference().child("JEP");

                if (uname.isEmpty()) {
                    reguname.setError("Please enter a username");
                } else if (password.isEmpty() || password.length() < 6) {
                    regpass.setError("At least 6 characters in length");
                } else if (email.isEmpty() || !email.matches(emailPattern)) {
                    regemail.setError("Please enter a valid e-mail");
                } else if (passwordconfirmation.isEmpty() || !passwordconfirmation.equals(password)) {
                    regconfirmpass.setError("Passwords do not match");
                }
                else if (mdepartment.isEmpty()){
                    department.setError("Please enter your department");
                }
                else if (mcontactnum.isEmpty()){
                    contactnum.setError("Please enter a contact number");
                }

                else  {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        UserCredentials newuser;
                                        String key = db.child("Users").push().getKey();
                                        String balance = "0";
                                        newuser = new UserCredentials(key,mAuth.getUid(),uname,email.toLowerCase(),"0",mdepartment,mcontactnum);
                                        db.child("Users")
                                                .child(email.toLowerCase().replace(".",""))
                                                .setValue(newuser);
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(Signup.this, "Authentication Success.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent inside = new Intent(Signup.this, CustomerViewPager.class);
                                        startActivity(inside);
                                        finish();

                                    } else {
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
                Intent backtolog=new Intent(Signup.this, Login.class);
                startActivity(backtolog);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }

    private void showDialog() {
        if (!progress.isShowing())
            progress.show();
    }

    private void hideDialog() {
        if (progress.isShowing())
            progress.dismiss();
    }
}
