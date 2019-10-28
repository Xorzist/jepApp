package com.example.jepapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jepapp.Activities.Users.PageforViewPager;
import com.example.jepapp.R;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.username);
        password=findViewById(R.id.passwordfield);
        //login=findViewById(R.id.logbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(username.getText().toString(), "admin")&&Objects.equals(password.getText().toString(),"1234"))
                {
                    opennewActivity();

                }else
                {
                    Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void opennewActivity() {
        Intent intent = new Intent(this, PageforViewPager.class);
        startActivity(intent);
    }
}
