package com.example.jepapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jepapp.Activities.Users.PageforViewPager;
import com.example.jepapp.Login;
import com.example.jepapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
    String TAG="Signup Class";
    ProgressDialog progress;
    EditText reguname,regpass;
    ImageView register,returner;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_signup);
//        getSupportActionBar().setTitle("Register");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reguname=findViewById(R.id.suname);
        regpass=findViewById(R.id.spassword);
        returner=findViewById(R.id.returner);
        register=findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        progress=new ProgressDialog(this);


        if (currentUser!=null) {
            // User is already logged in. Take him to main activity
            Intent inside=new Intent(Signup.this, PageforViewPager.class);
            startActivity(inside);
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = reguname.getText().toString().trim();
                String password = regpass.getText().toString().trim();

                if (!uname.isEmpty() && !password.isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(uname, password)
                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(Signup.this, "Authentication Success.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent inside=new Intent(Signup.this, PageforViewPager.class);
                                        startActivity(inside);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Signup.this, "Registration failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });

                }
                else if(password.length()<6){
                    Toast.makeText(getApplicationContext(),
                            "Password length must be more than 6 characters", Toast.LENGTH_LONG)
                            .show();

                }
                 else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
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
