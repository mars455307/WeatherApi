package com.example.weatherapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapi.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ArrayList<String> recordList;
    private RecyclerView recyclerView;
    private ArrayList<model> arrayList = new ArrayList<>();
    Handler handler = new Handler();
    private Boolean checkStart = true;

    private static final String FILE_NAME = "personal_settings";
    private static final String SP_KEY_FIRST_RUN = "APP_FIRST_RUN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        checkAppFirstRun();
        initView();
        new getData().start();
    }

    private void initView() {
        recordList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
    }

    class getData extends Thread {

        String data = "";

        @Override
        public void run() {

            try {

                URL url = new URL("https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-8BE05EB2-F2B7-4281-98CD-AE018E8A2704&locationName=%E8%87%BA%E5%8C%97%E5%B8%82&elementName=MinT");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                recordList.clear();

                while ((line = bufferedReader.readLine()) != null ) {

                    data = data + line;
                }

                if (!data.isEmpty()) {

                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject records = jsonObject.getJSONObject("records");
                    JSONArray location = records.getJSONArray("location");

                    for (int i = 0 ; i < location.length() ; i++) {
                        JSONObject data = location.getJSONObject(i);
                        JSONArray weatherElement = data.getJSONArray("weatherElement");
                        for (int j = 0 ; j < weatherElement.length() ; j++) {
                            JSONObject data1 = weatherElement.getJSONObject(j);
                            JSONArray time = data1.getJSONArray("time");
                            for (int k = 0; k < time.length(); k++) {
                                JSONObject data3 = time.getJSONObject(k);
                                String startTime = data3.getString("startTime");
                                String endTime = data3.getString("endTime");
                                JSONObject parameter = data3.getJSONObject("parameter");
                                String parameterName = parameter.getString("parameterName");
                                String parameterUnit = parameter.optString("parameterUnit");

                               arrayList.add(new model(startTime,endTime,parameterName,parameterUnit));
                            }

                        }
                    }

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    RecyclerAdapter adapter = new RecyclerAdapter(arrayList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                }
            });
        }
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        private ArrayList<model> arrayList;


        public RecyclerAdapter(ArrayList<model> arrayList) {
            this.arrayList = arrayList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView time;
            ConstraintLayout constraintLayout;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                time = itemView.findViewById(R.id.time);
                constraintLayout = itemView.findViewById(R.id.layout_click);
            }
        }

        @NonNull
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
            return new ViewHolder(inflate);
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
            model model = arrayList.get(position);
            holder.time.setText(model.getStartTime() + "\n" + model.getEndTime()+ "\n" + model.getParameterName() + model.getParameterUnit());

            String startTime = model.getStartTime();
            String endTime = model.getEndTime();
            String parameterName = model.getParameterName();
            String parameterUnit = model.getParameterUnit();

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("startTime",startTime);
                    intent.putExtra("endTime",endTime);
                    intent.putExtra("parameterName",parameterName);
                    intent.putExtra("parameterUnit",parameterUnit);
                    v.getContext().startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            if (this.arrayList != null){
                return this.arrayList.size();
            }
            return 0;
        }

    }

    private void checkAppFirstRun() {
        SharedPreferences sp = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        boolean isAPPFirstRun = sp.getBoolean(SP_KEY_FIRST_RUN,false);
        if(isAPPFirstRun){
            Toast.makeText(MainActivity.this,"歡迎回來",Toast.LENGTH_SHORT).show();
        }else{
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(SP_KEY_FIRST_RUN,true);
            editor.commit();
        }
    }
}
