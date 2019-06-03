package com.example.youngjungoo.warningapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);


        final Button callButton = findViewById(R.id.callButton);
        TextView titleText = findViewById(R.id.titleText);
        TextView dscText = findViewById(R.id.dscText);
        ImageView imageView = findViewById(R.id.violenceImage);

        callButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent();
                callIntent.setAction(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:01043868572"));
                startActivity(callIntent);
            }
        });

        Intent intent = getIntent();
        titleText.setText(intent.getStringExtra("title"));
        dscText.setText(intent.getStringExtra("dsc"));

        Glide.with(getBaseContext()).load(intent.getStringExtra("img")).into(imageView);

    }

}
