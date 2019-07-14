package com.parse.starter;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText newPasswordView;
    EditText confirmPasswordView;
    Button cancel;

    public void updatePassword(View view){
        if(newPasswordView.getText().toString().equals("") || confirmPasswordView.getText().toString().equals("")){

            Toast.makeText(this, "You can not leave any empty fields", Toast.LENGTH_SHORT).show();

        }else {
           if(newPasswordView.getText().toString().equals(confirmPasswordView.getText().toString())){
               ParseUser user = ParseUser.getCurrentUser();

               user.setPassword(newPasswordView.getText().toString());

               user.saveInBackground(new SaveCallback() {
                   @Override
                   public void done(ParseException e) {
                       if(e == null){
                           Toast.makeText(ChangePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                       }else{
                           Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });

           }else{
               Toast.makeText(this, "The passwords must be same", Toast.LENGTH_SHORT).show();
           }
       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPasswordView = findViewById(R.id.newPasswordEditText);
        confirmPasswordView = findViewById(R.id.confirmPasswordEditText);
        cancel = findViewById(R.id.cancelButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
