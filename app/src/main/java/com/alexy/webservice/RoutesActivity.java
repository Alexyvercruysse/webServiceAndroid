package com.alexy.webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import java.util.List;

import cz.msebera.android.httpclient.Header;

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
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {


                    try {
                        List<String> startStops = new ArrayList<String>();
                        List<String> endStops = new ArrayList<String>();
                        value = new String(bytes, "UTF-8");
                        JSONObject obj = new JSONObject(value);
                        for (int i = 1; i<obj.length(); i++){
                            JSONObject stop = obj.getJSONObject(""+i);
                            String name = stop.getString("name");
                            startStops.add(name);
                            endStops.add(name);
                            ArrayAdapter<String> adapterStart = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_list_item_1,startStops);
                            ArrayAdapter<String> adapterEnd = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_list_item_1,endStops);
                            adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            adapterEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerStartStop.setAdapter(adapterStart);
                            spinnerEndStop.setAdapter(adapterEnd);

                        }


                    } catch (Throwable tx) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + value + "\"");
                    }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("HEy","failure");
            }
        });

    }
}
