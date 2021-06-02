package com.example.climate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {
    private TextView placeName, cTemp, cMaxTemp, cMinTemp,cCondition,cSunRise,cSunSet,windSpeed,humidity,pressure,cloud;
    private ImageView imageView2;
    private FloatingActionButton actionButton;
    String location;
    JSONArray forecastRaw;
    String imgUrl = null;
    private ImageButton imageButton;
    private EditText editText;
    private ListView listView;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        actionButton = findViewById(R.id.actionButton);
        placeName = findViewById(R.id.textView);
        cTemp = findViewById(R.id.textView2);
        cCondition = findViewById(R.id.textView4);
        cMaxTemp = findViewById(R.id.textView40);
        cMinTemp = findViewById(R.id.textView41);
        cSunRise = findViewById(R.id.textView8);
        cSunSet = findViewById(R.id.textView10);
        windSpeed = findViewById(R.id.textView12);
        humidity = findViewById(R.id.textView14);
        pressure = findViewById(R.id.textView16);
        cloud = findViewById(R.id.textView18);
        imageView2 = findViewById(R.id.imageView2);
        imageButton = findViewById(R.id.imageButton);
        editText = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);

        SharedPreferences sp = getSharedPreferences("SEARCH",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        location = sp.getString("city","delhi");

        setData();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = editText.getText().toString();
                ed.putString("city",editText.getText().toString());
                ed.apply();
                setData();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject[] raw = new JSONObject[3];
                for(int i =0; i<3;i++){
                    try {
                        raw[i] = forecastRaw.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                customAdapter cd = new customAdapter(MainActivity2.this,R.layout.sample,raw);
                listView.setAdapter(cd);
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareUrl = "https://drive.google.com/file/d/1N11ggTpOc8J6z1zimm-DpYMMUNhmu8MU/view?usp=sharing";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(intent.EXTRA_TEXT, "Hey checkout this cool Weather Stats application: "+shareUrl);
//                intent.putExtra(Intent.EXTRA_STREAM, attachment);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });




    }
    public void setData(){
            String url = "http://api.weatherapi.com/v1/forecast.json?key=eeee903b06b64b45a18102247213005&q="+location+"&days=7";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("STAGE 1", "Into the response section ");
                        try {
                            Log.d("STAGE 2", "Into the try ");
                            forecastRaw = response.getJSONObject("forecast").getJSONArray("forecastday");
                            placeName.setText(response.getJSONObject("location").getString("name")+", "+response.getJSONObject("location").getString("region"));
                            cTemp.setText(response.getJSONObject("current").getString("temp_c"));
                            cCondition.setText(response.getJSONObject("current").getJSONObject("condition").getString("text"));
                            imgUrl = "https://"+response.getJSONObject("current").getJSONObject("condition").getString("icon");
                            Glide.with(MainActivity2.this).load(imgUrl).into(imageView2);
                            windSpeed.setText(response.getJSONObject("current").getString("wind_mph")+"m/s");
                            humidity.setText(response.getJSONObject("current").getString("humidity")+"%");
                            pressure.setText(response.getJSONObject("current").getString("pressure_in")+"-in");
                            cloud.setText(response.getJSONObject("current").getString("cloud")+"%");
                            cMaxTemp.setText(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getString("maxtemp_c")+"°C");
                            cMinTemp.setText(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getString("mintemp_c")+"°C");
                            cSunRise.setText(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("sunrise"));
                            cSunSet.setText(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("sunset"));

                        } catch (JSONException e) {
                            Log.d("STAGE 2", "Into the catch error ");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("STAGE 1", "Into the response error ");
                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}