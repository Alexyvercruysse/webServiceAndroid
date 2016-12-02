package com.alexy.webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alexy.webservice.Services.HttpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.alexy.webservice.R.id.spinnerEndStop;
import static com.alexy.webservice.R.id.spinnerStartStop;

public class RoutesActivity extends AppCompatActivity {

    Spinner spinnerStartStop,spinnerEndStop;
    private String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        spinnerStartStop = (Spinner) findViewById(R.id.spinnerStartStop);
        spinnerEndStop = (Spinner) findViewById(R.id.spinnerEndStop);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.240.36/index.php/Stop/All", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Map<String, Object> result  = mapper.readValue(responseBody, Map.class);
                    List<String> listStops = new ArrayList<>();

                    for(String str : result.keySet()){
                        Map<String, Object> stop = (Map<String,Object>)result.get(str);
                        String nameStop = (String) stop.get("name");
                        listStops.add(nameStop);
                    }

                    ArrayAdapter<String> adapterStart = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, listStops);
                    ArrayAdapter<String> adapterEnd = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, listStops);
                    adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapterEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStartStop.setAdapter(adapterStart);
                    spinnerEndStop.setAdapter(adapterEnd);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Log.e("HttpError","GET REQUEST ERROR" + error.getMessage());
            }
        });




    }
}
