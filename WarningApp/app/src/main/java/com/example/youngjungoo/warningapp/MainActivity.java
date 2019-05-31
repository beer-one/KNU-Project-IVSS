package com.example.youngjungoo.warningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button signUp,violenceList;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUp = findViewById(R.id.signUp);
        violenceList = findViewById(R.id.violenceList);

        signUp.setOnClickListener(this);
        violenceList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signUp :
                intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.violenceList:
                intent = new Intent(MainActivity.this, ViolenceLogActivity.class);
                startActivity(intent);
                break;
        }
    }
}