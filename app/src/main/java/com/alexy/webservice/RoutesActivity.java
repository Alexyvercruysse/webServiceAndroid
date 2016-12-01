package com.alexy.webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

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

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.google.fr/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("HEy","success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("HEy","failure");
            }
        });

    }
}
