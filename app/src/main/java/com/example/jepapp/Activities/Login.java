package com.example.jepapp.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jepapp.Activities.Admin.OrdersViewPager;
import com.example.jepapp.Activities.Admin.MenuCreationViewPager;
import com.example.jepapp.Activities.HR.HrViewPager;
import com.example.jepapp.Activities.Users.CustomerViewPager;
import com.example.jepapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends AppCompatActivity {

    EditText uname, pass;
    ImageView login,signup,forgot;
    ProgressDialog progress;
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
        forgot = findViewById(R.id.forgotpassword);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassword();
            }
        });

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Determines the current user's log in state and redirects them based on their login credentials
        if (currentUser != null && currentUser.getEmail().equalsIgnoreCase("admin@admin.com")) {
            //Subscribes admin user to a firebase topic
            mMessaging.subscribeToTopic("Orders");
            Intent intent = new Intent(getApplicationContext(), MenuCreationViewPager.class);
            startActivity(intent);
            finish();
        } else if (currentUser != null && currentUser.getEmail().equalsIgnoreCase("hr@hr.com")) {

            Intent intent = new Intent(getApplicationContext(), HrViewPager.class);
            startActivity(intent);
            finish();
        } else if (currentUser != null && currentUser.getEmail().equalsIgnoreCase("canteenstaff@cf.com")) {

            Intent intent = new Intent(getApplicationContext(), OrdersViewPager.class);
            startActivity(intent);
            finish();
        } else if (currentUser != null && !currentUser.getEmail().equalsIgnoreCase("admin@admin.com") && !currentUser.getEmail().equalsIgnoreCase("hr@hr.com")
                && !currentUser.getEmail().equalsIgnoreCase("canteenstaff@cf.com") && currentUser.isEmailVerified()) {
            //Unsubscribe a customer user from the firebase topic
            mMessaging.unsubscribeFromTopic("Orders");
            Intent intent = new Intent(getApplicationContext(), CustomerViewPager.class);
            startActivity(intent);
            finish();

        }

        //Checks if user has entered correct credentials and redirects them to the corresponding interface
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if the user has entered any text into the input fields
                if (!uname.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()) {
                    //Checks if the user enters credentials for  admin
                    if (uname.getText().toString().trim().equalsIgnoreCase("admin@admin.com")) {
                        //Attempt to log the user into the system
                        mAuth.signInWithEmailAndPassword(uname.getText().toString().trim(), pass.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //If admin user is logged in successfully
                                        if (task.isSuccessful()) {
                                            //Attempt to subscript to channel
                                            mMessaging.subscribeToTopic("/topics/Orders")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            String msg = ("Subscribed!");
                                                            //If admin trying to subscribe ran into an error
                                                            if (!task.isSuccessful()) {
                                                                msg = ("Subscription Error");
                                                                Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                                                            }


                                                        }
                                                    });
                                            // Sign in success, update UI with the signed-in user's information
                                            //Launches interface
                                            Intent intent = new Intent(getApplicationContext(), MenuCreationViewPager.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                    //Checks if the user enters credentials for Human Resources
                    else if (uname.getText().toString().trim().equalsIgnoreCase("hr@hr.com")) {
                        //Attempt to log the user into the system
                        mAuth.signInWithEmailAndPassword(uname.getText().toString().trim(), pass.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //If user is logged in successfully
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            //Launches interface
                                            Intent intent = new Intent(getApplicationContext(), HrViewPager.class);
                                            startActivity(intent);
                                            finish();


                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                    //Checks if the user enters credentials for  Canteen Staff
                    else if (uname.getText().toString().trim().equalsIgnoreCase("canteenstaff@cf.com")) {
                        //Attempt to log the user into the system
                        mAuth.signInWithEmailAndPassword(uname.getText().toString().trim(), pass.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //If user is logged in successfully
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            //Launches interface
                                            Intent intent = new Intent(getApplicationContext(), OrdersViewPager.class);
                                            startActivity(intent);
                                            finish();


                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                    //Checks if the user enters credentials for customer
                    else {
                        final ProgressDialog VerifyCredentialsdialog = new ProgressDialog(Login.this);
                        VerifyCredentialsdialog.setMessage("Verifying Credentials...");
                        VerifyCredentialsdialog.show();
                        //Attempt to log the user into the system
                        mAuth.signInWithEmailAndPassword(uname.getText().toString().trim(), pass.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                                VerifyCredentialsdialog.cancel();
                                                //Attempt to subscript to channel
                                                mMessaging.unsubscribeFromTopic("/topics/Orders")
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                String msg = ("UnSubscribed!");
                                                                if (!task.isSuccessful()) {

                                                                    //If trying to subscribe ran into an error

                                                                    msg = ("Subscription Error");
                                                                    Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                                                                }


                                                            }
                                                        });
                                                // Sign in success, update UI with the signed-in user's information
                                                //Launches interface
                                                Intent intent = new Intent(getApplicationContext(), CustomerViewPager.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                VerifyCredentialsdialog.cancel();
                                                Toast.makeText(getApplicationContext(), "Your email has not been verified,please check your " +
                                                        "email's inbox or spam folder", Toast.LENGTH_LONG).show();
                                            }

                                        } else {
                                            VerifyCredentialsdialog.cancel();
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getApplicationContext(), "Incorrect Credentials,are you sure this user is registered?", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your credentials", Toast.LENGTH_LONG).show();

                }
            }
        });
        //Launches the interface for a user to Register
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }
        //Function to send user an email to reset their password
        private void ResetPassword() {
            androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder1.setTitle("Reset Password");
            builder1.setMessage("Would you like to reset your Password");
            //builder1.setCancelable(true);
            final EditText input = new EditText(getApplicationContext());
            input.setTransformationMethod(PasswordTransformationMethod.getInstance());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setHint("Please enter your email address");
            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            builder1.setView(input);
            builder1.setIcon(R.drawable.person);
            builder1.setPositiveButton(
                    "Submit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int id) {
                           mAuth.sendPasswordResetEmail(input.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       Toast.makeText(Login.this, "Reset Email has been sent successfully", Toast.LENGTH_SHORT).show();
                                   }else{
                                       Toast.makeText(Login.this, "Reset Email could not be sent at this time", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
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


}
