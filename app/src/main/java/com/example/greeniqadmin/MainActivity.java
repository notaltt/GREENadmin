package com.example.greeniqadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout username, password;
    private MaterialButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.userEmailLog);
        password = findViewById(R.id.userPasswordLog);

        login = findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAdmin();
            }
        });
    }

    private void loginAdmin() {
        String fetchUsername = username.getEditText().getText().toString();
        String fetchPassword = password.getEditText().getText().toString();

        if(fetchUsername.equals("admin") && fetchPassword.equals("123123")){
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }else if(fetchUsername.equals("") || fetchPassword.equals("")) {
            username.setError("All fields must not empty.");
            password.setError("All fields must not empty.");
        }else{
            username.setError("Wrong username or password");
            password.setError("Wrong username or password");
        }
    }
}