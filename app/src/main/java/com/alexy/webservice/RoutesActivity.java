package com.alexy.webservice;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.alexy.webservice.R.id.spinnerEndStop;
import static com.alexy.webservice.R.id.spinnerStartStop;

public class RoutesActivity extends AppCompatActivity {

    Spinner spinnerStartStop, spinnerEndStop;
    private String value;
    Button btn2,btn3;
    ArrayAdapter<String> adapterStart;
    ArrayAdapter<String> adapterEnd;
    String startResult;
    String endResult;
    String end = "1";
    String start = "1";
    Boolean isEnd = false;
    TextView resultTV;
    String temps = "";
    private AsyncHttpClient client;
    private AsyncHttpClient client2;
    private AsyncHttpClient client3;
    private AsyncHttpClient client4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        spinnerStartStop = (Spinner) findViewById(R.id.spinnerStartStop);
        spinnerEndStop = (Spinner) findViewById(R.id.spinnerEndStop);
        resultTV =(TextView) findViewById(R.id.resultTV);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(RoutesActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        temps = "&hour="+i+":"+i1;
                        btn3.setText("Heure d'arrivée : "+i+":"+i1);
                    }
                }, new Date().getHours(),new Date().getMinutes(),true);
                timePickerDialog.show();
            }
        });
        client = new AsyncHttpClient();
        client.get("http://192.168.240.36/index.php/stop/list", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Map<String, Object> result = mapper.readValue(responseBody, Map.class);
                    List<String> listStops = new ArrayList<>();

                    for (String str : result.keySet()) {
                        Map<String, Object> stop = (Map<String, Object>) result.get(str);
                        String nameStop = (String) stop.get("name");
                        Log.d("Stops :",""+nameStop);
                        listStops.add(nameStop);

                    }
                    Log.d("Stops :",""+listStops);
                    adapterStart = new ArrayAdapter<>(RoutesActivity.this, android.R.layout.simple_list_item_1, listStops);
                    adapterEnd = new ArrayAdapter<>(RoutesActivity.this, android.R.layout.simple_list_item_1, listStops);
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
                Log.e("HttpError", "GET REQUEST ERROR" + error.getMessage());
            }
        });

        spinnerStartStop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                start = spinnerStartStop.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerEndStop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                end = spinnerEndStop.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterEnd.isEmpty() || adapterStart.isEmpty()) {

                } else {
                    executeAll();
                }


            }
        });


    }


    public void executeAll(){
        client2 = new AsyncHttpClient();
        Log.d("Details","http://192.168.240.36/index.php/stop?name=" + start);
        client2.get("http://192.168.240.36/index.php/stop?name=" + start, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ObjectMapper mapper = new ObjectMapper();
                try {
                    startResult = mapper.readValue(responseBody, String.class);


                    client3 = new AsyncHttpClient();
                    Log.d("Details","http://192.168.240.36/index.php/stop?name=" + end);
                    client3.get("http://192.168.240.36/index.php/stop?name=" + end, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                endResult = mapper.readValue(responseBody, String.class);

                                client4 = new AsyncHttpClient();
                                Log.d("Details","http://192.168.240.36/index.php/hour/travel/stops/id?idStartStop=" + startResult + "&idEndStop=" + endResult + temps);
                                client4.get("http://192.168.240.36/index.php/hour/travel/stops/id?idStartStop=" + startResult + "&idEndStop=" + endResult + temps, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        ObjectMapper mapper = new ObjectMapper();
                                        try {
                                            Map<String, Object> result = mapper.readValue(responseBody, Map.class);
                                            List<String> listStops = new ArrayList<>();

                                            for (String str : result.keySet()) {
                                                Map<String, Object> stop = (Map<String, Object>) result.get(str);
                                                String nameStop = (String) stop.get("hour");
                                                listStops.add(nameStop);
                                                Log.d("test",nameStop);

                                            }
                                            Log.d("tag", listStops + "");
                                            if (listStops.get(0).isEmpty() || listStops.get(1).isEmpty()){
                                                resultTV.setText("Pas d'horraire");
                                            }
                                            resultTV.setText("Départ : "+ listStops.get(0) + "\nArrivé : "+ listStops.get(1));
                                            resultTV.setVisibility(View.VISIBLE);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }



}
