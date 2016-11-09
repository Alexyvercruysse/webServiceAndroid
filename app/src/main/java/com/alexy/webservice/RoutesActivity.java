package com.alexy.webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivity extends AppCompatActivity {

    Spinner spinnerStartStop,spinnerEndStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        spinnerStartStop = (Spinner) findViewById(R.id.spinnerStartStop);
        spinnerEndStop = (Spinner) findViewById(R.id.spinnerEndStop);
        List<String> startStops = new ArrayList<String>(){{add("Choisir le point de départ");add("La raie des fesses");add("L'arrêt de travail");add("La reine des neiges");add("L'arrêt cardiaque");add("L'arection");}};
        List<String> endStops = new ArrayList<String>(){{add("Choisir le point d'arriver");add("La raie des fesses");add("L'arrêt de travail");add("La reine des neiges");add("L'arrêt cardiaque");add("L'arection");}};
        ArrayAdapter<String> adapterStart = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,startStops);
        ArrayAdapter<String> adapterEnd = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,endStops);
        adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartStop.setAdapter(adapterStart);
        spinnerEndStop.setAdapter(adapterEnd);
    }
}
