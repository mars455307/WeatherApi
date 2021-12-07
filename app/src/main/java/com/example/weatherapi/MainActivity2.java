package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();
        initView();

        Intent intent = getIntent();
        String startTime = intent.getStringExtra("startTime");
        String endTime = intent.getStringExtra("endTime");
        String parameterName = intent.getStringExtra("parameterName");
        String parameterUnit = intent.getStringExtra("parameterUnit");

        textView.setText(startTime+ "\n" + endTime + "\n" + parameterName + parameterUnit);
    }

    private void initView() {
        textView = findViewById(R.id.tv2);
    }


}