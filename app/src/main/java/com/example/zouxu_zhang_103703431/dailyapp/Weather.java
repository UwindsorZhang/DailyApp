package com.example.zouxu_zhang_103703431.dailyapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import static android.os.SystemClock.sleep;

/**
 * Created by Administrator on 2017/4/12.
 */

public class Weather extends Activity {
    private ScrollView weather_Layout;
    private TextView title_city;
    private TextView title_time;
    private TextView degree;
    private TextView weather_inf;
    private LinearLayout forecastLayout;
    private TextView cloud;
    private TextView wind_speed;

    int id_of_gps=-1;
    String testoutput="";
    String state_Name;
    String city_Name;
    String country_Name;
    String weather_forecast_data_String = "";
    int index_of_courrent_weather;
    List<Double> temps= new ArrayList<>();
    List<Double> temp_mins= new ArrayList<>();
    List<Double> temp_maxs= new ArrayList<>();
    List<Double> temp_min_of_next_five_days= new ArrayList<>();
    List<Double> temp_max_of_next_five_days= new ArrayList<>();
    List<String> main_weathers = new ArrayList<>();
    List<String> weather_descriptions = new ArrayList<>();
    List<Integer> wind_degrees = new ArrayList<>();
    List<Integer> wind_speeds = new ArrayList<>();
    List<Integer> clouds = new ArrayList<>();
    List<Double> rain_possiblitys= new ArrayList<>();
    List<String> weather_forecast_date = new ArrayList<>();
    List<String> weather_forecast_time = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);

        //weather_Layout =(ScrollView)findViewById(R.id.weather_layout);
        title_city= (TextView)findViewById(R.id.title_city);
        title_time = (TextView)findViewById(R.id.title_update_time);
        degree = (TextView)findViewById(R.id.degree);
        weather_inf = (TextView)findViewById(R.id.weather_inf);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
       // cloud = (TextView)findViewById(R.id.cloud);
       // wind_speed = (TextView)findViewById(R.id.wind_speed);

        id_of_gps= getIntent().getIntExtra("id_of_gps",-1);
        city_Name= getIntent().getStringExtra("city_Name");
        state_Name= getIntent().getStringExtra("state_Name");
        country_Name= getIntent().getStringExtra("country_Name");

        write_Data_ToFile();
        readFromFile();
        get_weather_fore_cast();
        get_temp_MinandMax();
        showWeatherInf();

        ListView lv = (ListView)findViewById(R.id.list_of_next_3_days);
        String[] s = {weather_forecast_date.get(0) +" (Today,Refreash)",weather_forecast_date.get(8) +" (Tomorrow)" ,weather_forecast_date.get(16)};
       // ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, R.layout.textview, s);
        ForeCastAdapter adapter = new ForeCastAdapter(this, s);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Integer[] myTaskParams = { id_of_gps};
                    RefreashTask refreashTask = new RefreashTask(getApplication().getBaseContext());
                    refreashTask.execute(myTaskParams);
                    readFromFile();
                    get_weather_fore_cast();
                    get_temp_MinandMax();
                    showWeatherInf();
                }
                if(position==1){
                    Intent intent = new Intent(Weather.this, DetailNext1Day.class);
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
                    intent.putExtra("weather_descriptions",weather_descriptions.get(index_of_courrent_weather+8));

                    startActivity(intent);
                }

                if(position==2){
                    Intent intent = new Intent(Weather.this, DetailNext2Day.class);
                    intent.putExtra("city_Name",city_Name);
                    intent.putExtra("state_Name",state_Name);
                    intent.putExtra("country_Name",country_Name);
                    intent.putExtra("temps", String.valueOf(df.format(temps.get(index_of_courrent_weather+16)-275.13)) + "°C");
                    intent.putExtra("temp_min", "max temp : " + String.valueOf(df.format(temp_max_of_next_five_days.get(2)-273.15)) + "°C");
                    intent.putExtra("temp_max","min temp : " + String.valueOf(df.format(temp_min_of_next_five_days.get(2)-273.15)) + "°C");
                    intent.putExtra("date1",weather_forecast_date.get(0));
                    intent.putExtra("date2",weather_forecast_date.get(8));
                    intent.putExtra("date3",weather_forecast_date.get(16));
                    intent.putExtra("weather_inf",main_weathers.get(index_of_courrent_weather+16));
                    intent.putExtra("weather_descriptions",weather_descriptions.get(index_of_courrent_weather+16));

                    startActivity(intent);
                }
            }
        });
       setListViewHeightBasedOnChildren(lv);
    }

    //reference to https://kennethflynn.wordpress.com/2012/09/12/putting-android-listviews-in-scrollviews/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 10;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private  void showWeatherInf(){
        Date d = new Date();
        String dt  = String.valueOf(DateFormat.format("yyyy-MM-dd HH:mm:ss", d.getTime()));
        String current_date=null,current_time = null;
        StringTokenizer st = new StringTokenizer(dt," ");
        for (int knt =0;st.hasMoreTokens();knt++) {

            current_date=st.nextToken();


            current_time=st.nextToken();

        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);

        get_current_index(current_time);

        title_city.setText(city_Name);
        title_time.setText(current_time);
        degree.setText(String.valueOf(df.format(temps.get(index_of_courrent_weather)-275.13)) + "°C");
        weather_inf.setText(main_weathers.get(index_of_courrent_weather));


        forecastLayout.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
        TextView date_text = (TextView) view.findViewById(R.id.date);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView max = (TextView) view.findViewById(R.id.temp_max);
        TextView min = (TextView) view.findViewById(R.id.temp_min);
        date_text.setText(current_date);
        description.setText(weather_descriptions.get(index_of_courrent_weather));
        max.setText("max temp : " + String.valueOf(df.format(temp_max_of_next_five_days.get(0)-273.15)) + "°C");
        min.setText("min temp : " + String.valueOf(df.format(temp_min_of_next_five_days.get(0)-273.15)) + "°C");
        forecastLayout.addView(view);





    }




    private void get_current_index(String time) {
        StringTokenizer st = new StringTokenizer(time,":");
        int current_hour=Integer.valueOf(st.nextToken());



        for(int i=0;i<weather_forecast_time.size();i++){
            StringTokenizer st2 = new StringTokenizer(weather_forecast_time.get(i),":");
            int compare_hour=Integer.valueOf(st2.nextToken());
            if(current_hour>=(compare_hour-3)){
                index_of_courrent_weather=i;
                break;
            }
            index_of_courrent_weather=-1;
        }
    }


    private void readFromFile() {
        runOnUiThread(new Runnable(){
            @Override
            public void run() {


                try {
                    String baseFolder = getApplication().getBaseContext().getExternalFilesDir(null).getAbsolutePath();
                    File file = new File(baseFolder + "/weather_forecast.json");
                    InputStream inputStream = new FileInputStream(file);

                    if ( inputStream != null ) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        String string_of_line_weather = "";

                        while ( (receiveString = reader.readLine()) != null ) {
                            string_of_line_weather+= receiveString;
                        }
                        weather_forecast_data_String = string_of_line_weather;

                    }
                    else{

                    }
                    inputStream.close();
                }
                catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }

            }

        });

    }


    private void write_Data_ToFile() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String baseFolder,data = "";
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try{
                    baseFolder = getApplication().getBaseContext().getExternalFilesDir(null).getAbsolutePath();
                    File file = new File(baseFolder + "/weather_forecast.json");
                    if(file.exists()==true){
                        URL weather_URL= new URL("http://api.openweathermap.org/data/2.5/forecast?id=" + id_of_gps + "&APPID=942a438dfad8aa4a25a5618ca61c36dd");
                        connection = (HttpURLConnection)weather_URL.openConnection();
                        connection.setConnectTimeout(6666);
                        connection.setReadTimeout(6666);
                        InputStream inputstream = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(inputstream));
                        StringBuilder respoonse_string = new StringBuilder();
                        String respones_text;
                        while ((respones_text = reader.readLine())!=null){
                            data += (respones_text);
                        }

                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(data.getBytes());
                        fos.close();
                    }else{
                        URL weather_URL= new URL("http://api.openweathermap.org/data/2.5/forecast?id=" + id_of_gps + "&APPID=942a438dfad8aa4a25a5618ca61c36dd");
                        connection = (HttpURLConnection)weather_URL.openConnection();
                        connection.setConnectTimeout(6666);
                        connection.setReadTimeout(6666);
                        InputStream inputstream = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(inputstream));
                        StringBuilder respoonse_string = new StringBuilder();
                        String respones_text;
                        while ((respones_text = reader.readLine())!=null){
                            data += (respones_text);
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(data.getBytes());
                        fos.close();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally{
                    if(reader!=null){
                        try{
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();;
                    }
                }
            }
        }).start();

    }
    public void get_weather_fore_cast()  {
        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                try {
                    JSONObject weather_forecast_obg = new JSONObject(weather_forecast_data_String);
                    JSONArray list_weather_Array = weather_forecast_obg.getJSONArray("list");
                    for (int i = 0; i < list_weather_Array.length(); i++) {
                        JSONObject weather_inf = list_weather_Array.getJSONObject(i);
                        JSONObject temp_inf = weather_inf.getJSONObject("main");
                        JSONArray weather_Array = weather_inf.getJSONArray("weather");
                        JSONObject sub_weather_inf = weather_Array.getJSONObject(0);
                        JSONObject cloud_inf= weather_inf.getJSONObject("clouds");
                        JSONObject wind_inf= weather_inf.getJSONObject("wind");
                        JSONObject rain_inf= weather_inf.getJSONObject("rain");


                        String dt_String = weather_inf.getString("dt_txt");

                        DecimalFormat df = new DecimalFormat("#.###");
                        df.setRoundingMode(RoundingMode.FLOOR);
                        double rain_possiblity;
                        if(rain_inf.has("3h")){
                            rain_possiblity = rain_inf.getDouble("3h");
                        }
                        else{
                            rain_possiblity = 0;

                        }

                        int wind_speed = wind_inf.getInt("speed");
                        int wind_degree = wind_inf.getInt("deg");
                        int cloud = cloud_inf.getInt("all");
                        String main_weather = sub_weather_inf.getString("main");
                        String weather_description = sub_weather_inf.getString("description");
                        double temp = temp_inf.getDouble("temp");
                        double temp_min = temp_inf.getDouble("temp_min");
                        double temp_max = temp_inf.getDouble("temp_max");

                        rain_possiblity = Double.parseDouble(df.format(rain_possiblity));

                        temps.add(temp);
                        temp_mins.add(temp_min);
                        temp_maxs.add(temp_max);
                        main_weathers.add(main_weather);
                        weather_descriptions.add(weather_description);
                        clouds.add(cloud);
                        wind_degrees.add(wind_degree);
                        wind_speeds.add(wind_speed);
                        rain_possiblitys.add(rain_possiblity);


                        testoutput += "Weather: " + main_weather + "\n";
                        testoutput += "Cloud level: " + cloud + "\n";
                        testoutput += "Wind degree: " + wind_degree + "\n";
                        testoutput += "Wind speed: " + wind_speed + "\n";
                        testoutput += "Rain level: " + rain_possiblity + "\n";

                        StringTokenizer st = new StringTokenizer(dt_String," ");
                        for (int knt =0;st.hasMoreTokens();knt++) {
                            if(knt==0){
                                weather_forecast_date.add(st.nextToken());
                            }
                            if(knt==1){
                                weather_forecast_time.add(st.nextToken());
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void get_temp_MinandMax()  {
        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                sleep(1000);
                double compare_Min,compare_Max;
                compare_Min=temp_mins.get(0);
                compare_Max=temp_maxs.get(0);
                for (int j = 1; j <= 5; j++) {
                    compare_Min=temp_mins.get(0+(j-1)*8);
                    compare_Max=temp_maxs.get(0+(j-1)*8);
                    for (int i = 1; i <= 8; i++) {
                        if((i*j-1)<temp_mins.size()){
                            if(temp_mins.get(i*j-1)<compare_Min){
                                compare_Min=temp_mins.get(i*j-1);
                            }
                            if(temp_maxs.get(i*j-1)>compare_Max){
                                compare_Max=temp_maxs.get(i*j-1);
                            }
                        }

                    }
                    DecimalFormat df = new DecimalFormat("#.###");
                    df.setRoundingMode(RoundingMode.FLOOR);
                    temp_min_of_next_five_days.add(compare_Min);
                    temp_max_of_next_five_days.add(compare_Max);
                    testoutput += "tmep_min_of_days: " + df.format(temp_min_of_next_five_days.get(j-1)-273.15) + "\n";
                    testoutput += "tmep_max_of_days: " + df.format(temp_max_of_next_five_days.get(j-1)-273.15) + "\n";
                }
            }
        });
    }
}
