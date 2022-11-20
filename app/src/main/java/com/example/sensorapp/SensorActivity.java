package com.example.sensorapp;

import static com.example.sensorapp.SensorDetailsActivity.EXTRA_SENSOR_TYPE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SensorActivity extends AppCompatActivity {
    private static final String SENSOR_APP_TAG = "SENSOR_APP_TAG";
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;
    private SensorAdapter sensorAdapter;
    private final List<Integer> sensors =new ArrayList(Arrays.asList(Sensor.TYPE_LIGHT,Sensor.TYPE_AMBIENT_TEMPERATURE));
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorList.forEach(sensor -> {
            Log.d(SENSOR_APP_TAG, "Sensor name:" + sensor.getName());
            Log.d(SENSOR_APP_TAG, "Sensor vendor:" + sensor.getVendor());
            Log.d(SENSOR_APP_TAG, "Sensor max range:" + sensor.getMaximumRange());
        });

        if(sensorAdapter == null) {
            sensorAdapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(sensorAdapter);
        }
        else {
            sensorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String string = getString(R.string.sensor_count, sensorList.size());
        getSupportActionBar().setSubtitle(string);
        return true;
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder>{

        private final List<Sensor> list;

        public SensorAdapter(List<Sensor> items){
            list = items;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflate = LayoutInflater.from(parent.getContext());
            return new SensorHolder(inflate, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = list.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }
    private class SensorHolder extends RecyclerView.ViewHolder{

        private final TextView sensorName;

        public SensorHolder (LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.sensor_list_item, parent,false));
            sensorName = itemView.findViewById(R.id.sensorName);
        }

        public void bind(Sensor sensor){

            sensorName.setText(sensor.getName());
            View Container = itemView.findViewById(R.id.sensor_list_item);
            if(sensors.contains(sensor.getType())){
                Container.setBackgroundResource(R.color.color1);
                Container.setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), SensorDetailsActivity.class);
                    intent.putExtra(EXTRA_SENSOR_TYPE, sensor.getType());
                    startActivity(intent);
                });
            }
            if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                Container.setBackgroundResource(R.color.color2);
                Container.setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                    startActivity(intent);
                });
            }


        }
    }
}