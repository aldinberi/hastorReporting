package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {

    EditText nameView;
    EditText emailView;
    EditText usernameView;
    EditText passwordView;
    Button photoButton;
    Bitmap userPhoto;

    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = data.getData();

        if(requestCode == 1 && resultCode == RESULT_OK && data != null ){
            try{
                userPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                photoButton.setText("Photo selected");

            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }
    }
    public void registerUser(View view){

        final ParseUser user = new ParseUser();

        if(userPhoto == null){
           userPhoto = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.hastorlogo);
        }


        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        userPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] byteArray = stream.toByteArray();

        final ParseFile file = new ParseFile("image.png", byteArray);

        file.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if(null == e)
                    user.put("profilePicture", file);
                    user.put("name", nameView.getText().toString());
                    user.setEmail(emailView.getText().toString());
                    user.setUsername(usernameView.getText().toString());
                    user.setPassword(passwordView.getText().toString());

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if( e == null){
                                Toast.makeText(RegisterActivity.this, "The registration was successful", Toast.LENGTH_SHORT).show();
                                finish();
                            } else{
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        }
                    });
            }
        });






    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameView = findViewById(R.id.nameEditText);
        emailView = findViewById(R.id.emailEditText);
        usernameView = findViewById(R.id.usernameEditText);
        passwordView = findViewById(R.id.passwordEditText);

        photoButton = findViewById(R.id.photoButton);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }
}
