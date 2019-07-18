package com.example.jepapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jepapp.Login;
import com.example.jepapp.R;
import com.example.jepapp.SessionPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    String TAG="Signup Class";
    ProgressDialog progress;
    EditText reguname,regpass;
    Button register,returner;
    SessionPref session;
    String registerurl = "http://legacydevs.com/Signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reguname=findViewById(R.id.suname);
        regpass=findViewById(R.id.spassword);
        returner=findViewById(R.id.returner);
        register=findViewById(R.id.register);
        progress=new ProgressDialog(this);

        session=new SessionPref(getApplicationContext());
        if (session.isLoggedIn()) {
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
                    registerUser(uname,password);

                } else {
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
                finish();
            }
        });
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser( final String uname,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progress.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                registerurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                Signup.this,
                                Login.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uname", uname);

                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        Login.getInstance().addToRequestQueue(strReq, tag_string_req);
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

