package com.example.zouxu_zhang_103703431.dailyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/13.
 */

public class DetailNext1Day extends Activity {

    String state_Name;
    String city_Name;
    String country_Name;
    String temps;
    String temp_min;
    String date1;
    String date2;
    String date3;
    String temp_max;
    String weather_inf_data;
    String weather_descriptions;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);




        city_Name= getIntent().getStringExtra("city_Name");
        state_Name= getIntent().getStringExtra("state_Name");
        country_Name= getIntent().getStringExtra("country_Name");
        temps = getIntent().getStringExtra("temps");
        temp_min= getIntent().getStringExtra("temp_min");
        temp_max= getIntent().getStringExtra("temp_max");
        date1 = getIntent().getStringExtra("date1");
        date2= getIntent().getStringExtra("date2");
        date3= getIntent().getStringExtra("date3");
        weather_inf_data= getIntent().getStringExtra("weather_inf");
        weather_descriptions= getIntent().getStringExtra("weather_descriptions");


        TextView title_city = (TextView) findViewById(R.id.title_city);
        TextView title_time = (TextView)findViewById(R.id.title_update_time);
        TextView degree = (TextView)findViewById(R.id.degree);
        TextView weather_inf = (TextView)findViewById(R.id.weather_inf);
        LinearLayout forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);

        title_city.setText(city_Name);
        degree.setText(temps);
        weather_inf.setText(weather_inf_data);


        forecastLayout.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
        TextView date_text = (TextView) view.findViewById(R.id.date);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView max = (TextView) view.findViewById(R.id.temp_max);
        TextView min = (TextView) view.findViewById(R.id.temp_min);
        date_text.setText(date2);
        description.setText(weather_descriptions);
        max.setText(temp_max);
        min.setText(temp_min);
        forecastLayout.addView(view);

        ListView lv = (ListView)findViewById(R.id.list_of_next_3_days);
        String[] s = {"Back to Today(" + date1 + ")'s weather"};
        // ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, R.layout.textview, s);
        ForeCastAdapter adapter = new ForeCastAdapter(this, s);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    finish();
                }
            }
        });

       /* Intent intent = new Intent(Weather.this, DetailNext1Day.class);
        intent.putExtra("city_Name",city_Name);
        intent.putExtra("state_Name",state_Name);
        intent.putExtra("country_Name",country_Name);
        intent.putExtra("temps", String.valueOf(df.format(temps.get(index_of_courrent_weather+8)-275.13)) + "°C");
        intent.putExtra("temp_min", "max temp : " + String.valueOf(df.format(temp_max_of_next_five_days.get(1)-273.15)) + "°C");
        intent.putExtra("temp_max","min temp : " + String.valueOf(df.format(temp_min_of_next_five_days.get(1)-273.15)) + "°C");
        intent.putExtra("date1",weather_forecast_date.get(0));
        intent.putExtra("date2",weather_forecast_date.get(8));
        intent.putExtra("date3",weather_forecast_date.get(16));
        intent.putExtra("weather_inf",main_weathers.get(index_of_courrent_weather+8));
        intent.putExtra("weather_descriptions",weather_descriptions.get(index_of_courrent_weather+8));*/
    }
}
