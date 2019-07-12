package com.example.jepapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private static final Object TAG = "Login Class";
    EditText uname, pass;
    Button login;
    TextView  signup;
    ProgressDialog progress;
    String loginurl = "http://legacydevs.com/Login.php";

    private com.android.volley.RequestQueue mRequestq;
    private static Login logininstance;
    private SessionPref session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logininstance = this;
        progress = new ProgressDialog(this);
        session = new SessionPref(getApplicationContext());
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Login.this, LunchMenu.class);
            startActivity(intent);
            finish();
        }
        uname = findViewById(R.id.uname);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.logbtn);
        signup = findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = uname.getText().toString().trim();
                String password = pass.getText().toString().trim();

                // Check for empty data in the form
                if (!username.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(username, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter a username and password", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        Signup.class);
                startActivity(i);
                finish();
            }
        });


    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String uname, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        progress.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, loginurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Login Class", "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Store user id as global identifier
                        String globaluid = jObj.getString("uid");
                        session.setUID(globaluid);
//
                        // Launch main activity
                        Intent intent = new Intent(Login.this,
                                LunchMenu.class);
                        //Send User ID through intents
                        //intent.putExtra("EXTRA_UID", globaluid);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Log.d("Error Message", errorMsg);
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Login Class", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
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


    public static synchronized Login getInstance() {
        return logininstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestq == null) {
            mRequestq = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestq;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestq != null) {
            mRequestq.cancelAll(tag);
        }

    }
}


