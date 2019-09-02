package com.lucianpiros.accountmanagement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lucianpiros.accountmanagement.R;
import com.lucianpiros.accountmanagement.model.UserAccountModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends AppCompatActivity {

    private UserAccountModel userAccountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final ProgressBar progressBar = findViewById(R.id.progressbar);
        TextView signup = findViewById(R.id.signup);
        Button logIn = findViewById(R.id.login);

        userAccountModel = ViewModelProviders.of(this).get(UserAccountModel.class);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                userAccountModel.login(email.getText().toString(), password.getText().toString());
            }
        });

        userAccountModel.loggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                progressBar.setVisibility(View.GONE);
                if(aBoolean) {

                }
                else {
                    // TODO: add more detailed error and negative use cases handling
                    Toast toast=Toast.makeText(getApplicationContext(),getString(R.string.invalid_login),Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
