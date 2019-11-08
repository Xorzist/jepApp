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

import com.example.jepapp.Activities.Admin.AdminPageforViewPager;
import com.example.jepapp.Activities.Users.PageforViewPager;
import com.example.jepapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private static final Object TAG = "Login Class";
    EditText uname, pass;
    ImageView login,signup;
    ProgressDialog progress;
    String loginurl = "http://legacydevs.com/Login.php";

    private com.android.volley.RequestQueue mRequestq;
    private static Login logininstance;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        progress = new ProgressDialog(this);
        uname = findViewById(R.id.username);
        pass = findViewById(R.id.passwordfield);
        login = findViewById(R.id.loginimg);
        signup = findViewById(R.id.registerimg);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Intent intent = new Intent(getApplicationContext(), AdminPageforViewPager.class);
            startActivity(intent);
            finish();


        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uname.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()) {
                    if(uname.getText().toString().equals("Admin@admin.com")){
                        mAuth.signInWithEmailAndPassword(uname.getText().toString(), pass.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(getApplicationContext(),AdminPageforViewPager.class);
                                            startActivity(intent);
                                            finish();
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("huh", "signInWithEmail:success for admin");

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("huh", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                        else{
                        mAuth.signInWithEmailAndPassword(uname.getText().toString(), pass.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(getApplicationContext(),PageforViewPager.class);
                                            startActivity(intent);
                                            finish();
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("huh", "signInWithEmail:success");

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("huh", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Please enter your credentials", Toast.LENGTH_LONG).show();

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });




    }
}
