package com.example.zouxu_zhang_103703431.dailyapp;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/4/13.
 */

public class RefreashTask extends AsyncTask<Integer, String, Boolean> {
    Context app_context;

    public RefreashTask(Context context) {
        super();
        app_context = context;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        int id_of_gps = params[0];
        String baseFolder, data = "";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            baseFolder = app_context.getExternalFilesDir(null).getAbsolutePath();
            File file = new File(baseFolder + "/weather_forecast.json");
            if (file.exists() == true) {
                URL weather_URL = new URL("http://api.openweathermap.org/data/2.5/forecast?id=" + id_of_gps + "&APPID=942a438dfad8aa4a25a5618ca61c36dd");
                connection = (HttpURLConnection) weather_URL.openConnection();
                connection.setConnectTimeout(6666);
                connection.setReadTimeout(6666);
                InputStream inputstream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputstream));
                StringBuilder respoonse_string = new StringBuilder();
                String respones_text;
                while ((respones_text = reader.readLine()) != null) {
                    data += (respones_text);
                }

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data.getBytes());
                fos.close();
            } else {
                URL weather_URL = new URL("http://api.openweathermap.org/data/2.5/forecast?id=" + id_of_gps + "&APPID=942a438dfad8aa4a25a5618ca61c36dd");
                connection = (HttpURLConnection) weather_URL.openConnection();
                connection.setConnectTimeout(6666);
                connection.setReadTimeout(6666);
                InputStream inputstream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputstream));
                StringBuilder respoonse_string = new StringBuilder();
                String respones_text;
                while ((respones_text = reader.readLine()) != null) {
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
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
                ;
            }
        }
        return null;
    }
}
