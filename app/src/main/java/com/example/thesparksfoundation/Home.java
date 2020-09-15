package com.example.thesparksfoundation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    TextView name;
    TextView email;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       name = findViewById(R.id.username);
       email = findViewById(R.id.email);
       imageView = findViewById(R.id.imageView);




        String username = getIntent().getStringExtra("username");
        String imagefa = getIntent().getStringExtra("imagefa");
        String emailfa = getIntent().getStringExtra("emafa");



      name.setText(username);


        email.setText(emailfa);

        Picasso.get().load(imagefa).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(imageView);


    }
}

