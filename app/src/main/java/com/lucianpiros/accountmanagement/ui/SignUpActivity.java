package com.lucianpiros.accountmanagement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucianpiros.accountmanagement.R;
import com.lucianpiros.accountmanagement.model.UserAccountModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public class SignUpActivity extends AppCompatActivity {

    private UserAccountModel userAccountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText name = findViewById(R.id.name);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final EditText password2 = findViewById(R.id.password2);
        final ProgressBar progressBar = findViewById(R.id.progressbar);
        Button signUp = findViewById(R.id.signup);

        userAccountModel = UserAccountModel.getUserAccountModel(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                userAccountModel.signup(name.getText().toString(), email.getText().toString(), password.getText().toString(), password2.getText().toString());
            }
        });

        userAccountModel.loggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                progressBar.setVisibility(View.GONE);
                if (aBoolean) {
                    Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                    startActivity(intent);
                } else {
                    // TODO: add more detailed error and negative use cases handling
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.invalid_login), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
