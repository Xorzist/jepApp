package com.example.jepapp.Activities;

import android.app.Activity;

import com.example.jepapp.R;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.user_name);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
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
public class LoginActivity extends Activity {
}
