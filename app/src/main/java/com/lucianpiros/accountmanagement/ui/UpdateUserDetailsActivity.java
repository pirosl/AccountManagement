package com.lucianpiros.accountmanagement.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lucianpiros.accountmanagement.R;
import com.lucianpiros.accountmanagement.model.UserAccountModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public class UpdateUserDetailsActivity extends AppCompatActivity {

    private UserAccountModel userAccountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateuserdetails);

        final EditText name = findViewById(R.id.name);
        final EditText location = findViewById(R.id.location);
        final EditText birthdate = findViewById(R.id.birthdate);
        Button update = findViewById(R.id.updatedetails);

        userAccountModel = UserAccountModel.getUserAccountModel(this);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAccountModel.update(name.getText().toString(), location.getText().toString(), birthdate.getText().toString());
            }
        });

        userAccountModel.updateResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    UpdateUserDetailsActivity.this.finish();
                }
                else {
                    // TODO: add more detailed error and negative use cases handling
                    Toast toast=Toast.makeText(getApplicationContext(),"Error updating details",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
