package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        Bundle bundle = getIntent().getExtras();
        Log.e("TTTEEESSSS",bundle.toString());



        String startTime = bundle.getString("startTime");
        String endTime = bundle.getString("endTime");
        String parameterName = bundle.getString("parameterName");
        String parameterUnit = bundle.getString("parameterUnit");

        textView.setText(startTime+ "\n" + endTime + "\n" + parameterName + parameterUnit);

    }

    private void initView() {
        textView = findViewById(R.id.tv2);
    }


}