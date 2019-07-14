package com.parse.starter;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class UpdateProfile extends AppCompatActivity {

    Button cancel;
    EditText nameView;
    EditText emailView;
    EditText usernameView;
    ImageView profilePicture;
    Button selectButton;
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
                selectButton.setText("Photo selected");
                profilePicture.setImageBitmap(Bitmap.createScaledBitmap(userPhoto, 300, 300, false));

            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }
    }

    public void updateUser(View view){

        if(nameView.getText().toString().replaceAll("\\s+","").equals("") ||
                emailView.getText().toString().replaceAll("\\s+","").equals("") ||
                usernameView.getText().toString().replaceAll("\\s+","").equals("")){

            Toast.makeText(this, "You can not leave any empty fields", Toast.LENGTH_SHORT).show();

        }else {

            ParseQuery<ParseUser> query = ParseUser.getQuery();

            query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(final ParseUser user, ParseException e) {

                    if (userPhoto == null) {
                        userPhoto = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.hastorlogo);
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    userPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    byte[] byteArray = stream.toByteArray();

                    final ParseFile file = new ParseFile("image.png", byteArray);

                    file.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (null == e) {

                                user.put("profilePicture", file);
                                user.put("name", nameView.getText().toString());
                                user.setEmail(emailView.getText().toString());
                                user.setUsername(usernameView.getText().toString());


                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "The update was successful", Toast.LENGTH_SHORT).show();
                                            setResult(Activity.RESULT_OK);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        selectButton = findViewById(R.id.selectPhotoButton);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });

        cancel = findViewById(R.id.logoutButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameView = findViewById(R.id.nameEditText);
        emailView = findViewById(R.id.emailEditText);
        usernameView = findViewById(R.id.usernameEditText);
        profilePicture = findViewById(R.id.userPicture);


        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        nameView.setText(user.getString("name"));
                        emailView.setText(user.getEmail());
                        usernameView.setText(user.getUsername());

                        ParseFile file = user.getParseFile("profilePicture");

                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null) {
                                    userPhoto = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    profilePicture.setImageBitmap(Bitmap.createScaledBitmap(userPhoto, 300, 300, false));
                                }
                            }
                        });
                    }
                });
    }
}
