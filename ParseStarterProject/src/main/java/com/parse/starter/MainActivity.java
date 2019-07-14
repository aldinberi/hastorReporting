/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity  {
  EditText usernameView;
  EditText passwordView;


  public void reportList() {
    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
    startActivity(intent);
    finish();
  }

  public void onClickLogin(View v){
      String username = usernameView.getText().toString();
      String password = passwordView.getText().toString();

    ParseUser.logInInBackground(username, password, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if(user != null){
          Toast.makeText(MainActivity.this, "Login is successful", Toast.LENGTH_SHORT).show();
          reportList();
          finish();
      }else{
          Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          e.printStackTrace();
        }

      }
    });

  }

  public  void onClickRegister(View v){
        Intent intent  = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    usernameView = findViewById(R.id.usernameEditText);
    passwordView = findViewById(R.id.passwordEditText);

    if(ParseUser.getCurrentUser() != null){
        reportList();
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

}