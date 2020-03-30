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
import com.example.jepapp.Activities.Admin.ItemsPageforViewPager;
import com.example.jepapp.Activities.HR.HrPageForViewPager;
import com.example.jepapp.Activities.HR.Hrjob;
import com.example.jepapp.Activities.Users.CustomerViewPager;
import com.example.jepapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends AppCompatActivity {
    private static final Object TAG = "Login Class";
    EditText uname, pass;
    ImageView login,signup;
    ProgressDialog progress;
    String loginurl = "http://legacydevs.com/Login.php";

    private com.android.volley.RequestQueue mRequestq;
    private static Login logininstance;

    private FirebaseAuth mAuth;
    private FirebaseMessaging mMessaging;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mMessaging = FirebaseMessaging.getInstance();

        progress = new ProgressDialog(this);
        uname = findViewById(R.id.contact);
        pass = findViewById(R.id.passwordfield);
        login = findViewById(R.id.loginimg);
        signup = findViewById(R.id.registerimg);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null && currentUser.getEmail().equalsIgnoreCase("admin@admin.com")){
            Log.e("Email :",currentUser.getEmail());
            mMessaging.subscribeToTopic("Orders");
            Intent intent = new Intent(getApplicationContext(), AdminPageforViewPager.class);
            startActivity(intent);
            finish();
        }
        if (currentUser!=null && currentUser.getEmail().equalsIgnoreCase("hr@hr.com")){
            Log.e("Email :",currentUser.getEmail());
            mMessaging.subscribeToTopic("Orders");
            Intent intent = new Intent(getApplicationContext(), Hrjob.class);
            startActivity(intent);
            finish();
        }
        else if (currentUser!=null && !currentUser.getEmail().equalsIgnoreCase("admin@admin.com") && !currentUser.getEmail().equalsIgnoreCase("hr@hr.com")){
            Log.e("Email :",currentUser.getEmail());
            mMessaging.unsubscribeFromTopic("Orders");
            Intent intent = new Intent(getApplicationContext(), CustomerViewPager.class);
            startActivity(intent);
            finish();

            }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uname.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()) {
                    if(uname.getText().toString().trim().equalsIgnoreCase("admin@admin.com")){
                        //Attempt to log the user into the system
                        mAuth.signInWithEmailAndPassword(uname.getText().toString().trim(), pass.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {//If admin user is logged in successfully

                                            //Attempt to subscript to channel
                                            mMessaging.subscribeToTopic("/topics/Orders")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            String msg = ("Subscribed!");
                                                            if (!task.isSuccessful()) {//If admin trying to subscribe ran into an error
                                                                msg = ("Subscription Error");
                                                            }
                                                            Log.e("Subscription", msg);
                                                        }
                                                    });
                                            Intent intent = new Intent(getApplicationContext(), ItemsPageforViewPager.class);
                                            startActivity(intent);
                                            finish();
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("huh", "signInWithEmail:success for admin");

                                        }
                                        else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("huh", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                    else if(uname.getText().toString().trim().equalsIgnoreCase("hr@hr.com")){
                        //Attempt to log the user into the system
                        mAuth.signInWithEmailAndPassword(uname.getText().toString().trim(), pass.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {//If admin user is logged in successfully

                                            //Attempt to subscript to channel
                                            mMessaging.subscribeToTopic("/topics/Orders")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            String msg = ("Subscribed!");
                                                            if (!task.isSuccessful()) {//If admin trying to subscribe ran into an error
                                                                msg = ("Subscription Error");
                                                            }
                                                            Log.e("Subscription", msg);
                                                        }
                                                    });
                                            Intent intent = new Intent(getApplicationContext(), Hrjob.class);
                                            startActivity(intent);
                                            finish();
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("huh", "signInWithEmail:success for HR");

                                        }
                                        else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("huh", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                        else{
                        mAuth.signInWithEmailAndPassword(uname.getText().toString().trim(), pass.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mMessaging.unsubscribeFromTopic("/topics/Orders")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            String msg = ("UnSubscribed!");
                                                            if (!task.isSuccessful()) {//If admin trying to subscribe ran into an error
                                                                msg = ("Subscription Error");
                                                            }
                                                            Log.e("Subscription", msg);
                                                        }
                                                    });
                                            Intent intent = new Intent(getApplicationContext(), CustomerViewPager.class);
                                            startActivity(intent);
                                            finish();
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("huh", "signInWithEmail:success");

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("fail", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(getApplicationContext(), "Incorrect Credentials,are you sure this user is registered?", Toast.LENGTH_LONG).show();
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
