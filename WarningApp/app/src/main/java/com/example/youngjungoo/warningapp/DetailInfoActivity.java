package com.example.youngjungoo.warningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        TextView titleText = findViewById(R.id.titleText);
        TextView dscText = findViewById(R.id.dscText);
        ImageView imageView = findViewById(R.id.violenceImage);

        Intent intent = getIntent();
        titleText.setText(intent.getStringExtra("title"));
        dscText.setText(intent.getStringExtra("dsc"));

        Glide.with(getBaseContext()).load(intent.getStringExtra("img")).into(imageView);

    }
}
