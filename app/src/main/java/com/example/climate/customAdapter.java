package com.example.climate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class customAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private JSONObject[] objects;
    public customAdapter(@NonNull Context context, int resource, @NonNull JSONObject[] objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    @Nullable
    @Override
    public JSONObject getItem(int position) {
        return objects[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.sample, parent, false);
        ImageView img = convertView.findViewById(R.id.imageListView);
        TextView date,maxTemp,minTemp,sunRise,sunSet,condition;
        date = convertView.findViewById(R.id.date);
        maxTemp = convertView.findViewById(R.id.maxTemp);
        minTemp = convertView.findViewById(R.id.minTemp);
        sunRise = convertView.findViewById(R.id.sunRise);
        sunSet = convertView.findViewById(R.id.sunSet);
        condition = convertView.findViewById(R.id.condition);
        String imgUrl;
        try {
            date.setText(objects[position].getString("date"));
            condition.setText(objects[position].getJSONObject("day").getJSONObject("condition").getString("text"));
            imgUrl = "https://"+objects[position].getJSONObject("day").getJSONObject("condition").getString("icon");
            Glide.with(context).load(imgUrl).into(img);
            maxTemp.setText("Max Temp: "+objects[position].getJSONObject("day").getString("maxtemp_c") + "°C");
            minTemp.setText("Min Temp: "+objects[position].getJSONObject("day").getString("mintemp_c")+ "°C");
            sunRise.setText("Sun Rise: "+objects[position].getJSONObject("astro").getString("sunrise"));
            sunSet.setText("Sun Set"+objects[position].getJSONObject("astro").getString("sunset"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
