package com.lucianpiros.accountmanagement.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.lucianpiros.accountmanagement.R;
import com.lucianpiros.accountmanagement.aidl.Me;
import com.lucianpiros.accountmanagement.model.UserAccountModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class UserDetailsActivity extends AppCompatActivity {

    private UserAccountModel userAccountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);

        final TextView name = findViewById(R.id.name);
        final TextView location = findViewById(R.id.location);
        final TextView birthdate = findViewById(R.id.birthdate);

        userAccountModel = UserAccountModel.getUserAccountModel(this);

        LiveData<Me> me =  userAccountModel.userInfo();

        me.observe(this, new Observer<Me>() {
            @Override
            public void onChanged(Me me) {
                if(me != null) {
                    name.setText(me.getName());
                    location.setText(me.getLocation());
                    birthdate.setText(me.getBirthdate());
                }
            }
        });
    }
}
