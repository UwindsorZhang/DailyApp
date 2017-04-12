package com.example.zouxu_zhang_103703431.dailyapp;

import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

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
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import static android.content.ContentValues.TAG;
import static android.os.SystemClock.sleep;
import static java.lang.Math.round;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    List<String> permissionList = new ArrayList<>();
    TextView responseText;
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    TextView tv1;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    String testoutput="";
    List<JSONObject> obj_List = new ArrayList<>();
    List<JSONObject> weather_forecast_List = new ArrayList<>();
    String stateName;
    String cityName;
    String countryName;
    int id_db;
    String city_Name_db;
    String country_Name_db;
    double lat_db ;
    double lon_db;
    int id_of_gps=-1;
    String weather_forecast_data_String = "";
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // responseText = (TextView) findViewById(R.id.response_text);
        // sendRequestWithHttpURLConnection();
        //openGPSSettings();
        //getLocation();

        tv1 = (TextView) this.findViewById(R.id.response_text);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {

        }

        //loadJSONFromAsset();
        loadJSONFromAsset();


        //if(!s.isEmpty() ) {
        // try {

        //    JSONObject obj = new JSONObject(loadJSONFromAsset());
        //JSONArray id_jsonArray = obj.getJSONArray("_id");
        // JSONArray city_jsonArray = obj.getJSONArray("name");
        // JSONArray country_jsonArray = obj.getJSONArray("country");
        //JSONArray coordinate_jsonObj = obj.getJSONArray("coord");
        //  ArrayList cityIDList = new ArrayList();
        // HashMap<String, String> m_li;

         /*  for (int i = 0; i < 1; i++) {
               JSONObject subObject = id_jsonArray.getJSONObject(i);
               JSONObject coordinate_Obj = subObject.getJSONObject("coord");
                Log.d("Details-->", coordinate_Obj.getString("lat"));
                String lat_value = subObject.getString("lat");
                String lon_value = subObject.getString("lon");

                //Add your values in your `ArrayList` as below:
               testoutput += ("\nlat=" + lat_value);
               testoutput += ("\nlon=" + lon_value);


            }*/

                     /* JsonArray array = object.getAsJsonArray();
            for (int i = 0; i < 2 ; i++) {
                //System.out.println("---------------");
                JsonObject subObject = array.get(i).getAsJsonObject();
                testoutput += ("\nid=" + subObject.get("_id").getAsInt());
                testoutput += ("\nname=" + subObject.get("name").getAsString());
                testoutput += ("\ncountry=" + subObject.get("country").getAsString());
                JsonObject coordinate_Obj = subObject.get("coord").getAsJsonObject();
                testoutput += ("\nlat=" + coordinate_Obj.get("lat").getAsInt());
                testoutput += ("\nlon=" + coordinate_Obj.get("lon").getAsInt());
            }*/
        //  } catch (JSONException e) {
        //      testoutput += "233333333333\n";
        //       e.printStackTrace();
        //   }
        //  }



        Find_Location();




       // tv1.setText(testoutput);
    }

    protected void startLocationUpdates() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        tv1.setText("Location received: " + location.toString());
    }


    public void loadJSONFromAsset() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String json = null;

                try {
                    //AssetFileDescriptor descriptor = getAssets().openFd("cityID.json");
                    InputStream is = MainActivity.this.getBaseContext().getAssets().open("cityID.json");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    String repStr = null;
                    while ((repStr = reader.readLine()) != null) {
                        JSONObject obj = new JSONObject(repStr);
                        obj_List.add(obj);
                    }
           /* int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");*/
                    //JsonParser parser = new JsonParser();
                    //FileReader reader = new FileReader(descriptor.getFileDescriptor());
                    //JsonObject object = (JsonObject) parser.parse(reader);


           /* JsonArray array = object.getAsJsonArray();
            for (int i = 0; i < 2 ; i++) {
                //System.out.println("---------------");
                JsonObject subObject = array.get(i).getAsJsonObject();
                testoutput += ("\nid=" + subObject.get("_id").getAsInt());
                testoutput += ("\nname=" + subObject.get("name").getAsString());
                testoutput += ("\ncountry=" + subObject.get("country").getAsString());
                JsonObject coordinate_Obj = subObject.get("coord").getAsJsonObject();
                testoutput += ("\nlat=" + coordinate_Obj.get("lat").getAsInt());
                testoutput += ("\nlon=" + coordinate_Obj.get("lon").getAsInt());
            }*/
                } catch (IOException ex) {
                    testoutput += "12342352345\n";
                    ex.printStackTrace();

                } catch (JSONException e) {
                    testoutput += "88888888888\n";
                    e.printStackTrace();
                }

            }
        });
    }




    public void Find_Location(){
        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }

                                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                        mGoogleApiClient);



                                if (mLastLocation != null) {
                                    tv1.setText("1");
                                    testoutput += String.valueOf(mLastLocation.getLatitude()) + "\n" + String.valueOf(mLastLocation.getLongitude());

                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                    double MyLat = mLastLocation.getLatitude();
                                    double MyLon = mLastLocation.getLongitude();
                                    List<Address> addresses = null;
                                    try {
                                        addresses = geocoder.getFromLocation(MyLat, MyLon, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String countryName = addresses.get(0).getAddressLine(2);
                                    String cityName = addresses.get(0).getLocality();
                                    String stateName = addresses.get(0).getAdminArea();

                                    FindCityID(cityName, countryName,MyLat,MyLon);

                                    testoutput += "\n" + cityName;
                                    testoutput += "\n" + stateName;
                                    testoutput += "\n" + countryName;
                                    testoutput += "\n" + id_of_gps + "\n";

                                }
                                else{
                                    tv1.setText("2");
                                }

/*
                                try {
                                    String data = "";
                                    String baseFolder = MainActivity.this.getBaseContext().getExternalFilesDir(null).getAbsolutePath();
                                    File file = new File(baseFolder + "/weather_forecast.json");
                                    FileOutputStream fos = new FileOutputStream(file);
                                    fos.write(data.getBytes());
                                    fos.close();
                                    testoutput += "\n" + baseFolder;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }



                                try {
                                    String baseFolder = MainActivity.this.getBaseContext().getExternalFilesDir(null).getAbsolutePath();
                                    File file = new File(baseFolder + "/weather_forecast.json");
                                    InputStream inputStream = new FileInputStream(file);

                                    if ( inputStream != null ) {
                                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                                        BufferedReader reader = new BufferedReader(inputStreamReader);
                                        String receiveString = "";
                                        StringBuilder stringBuilder = new StringBuilder();

                                        while ( (receiveString = reader.readLine()) != null ) {
                                        }

                                    }
                                    inputStream.close();
                                }
                                catch (FileNotFoundException e) {
                                    Log.e("login activity", "File not found: " + e.toString());
                                } catch (IOException e) {
                                    Log.e("login activity", "Can not read file: " + e.toString());
                                }
*/










                                write_Data_ToFile();
                                readFromFile();
                                get_weather_fore_cast();
                                get_temp_MinandMax();

                                tv1.setText(testoutput);

                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                startLocationUpdates();
                            }

                        })
                        .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                if (connectionResult.hasResolution()) {
                                    try {
                                        // Start an Activity that tries to resolve the error
                                        connectionResult.startResolutionForResult(MainActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                                    } catch (IntentSender.SendIntentException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
                                }
                            }
                        })
                        .addApi(LocationServices.API)
                        .build();
            }
        });

    }

    public void FindCityID(final String name_Of_City, final String name_Of_Country, final double lat_gps, final double lon_gps){
        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                try {
                    for (int i = 0; i < obj_List.size(); i++) {
                        JSONObject city_Obj = obj_List.get(i);
                        // testoutput += ( obj_List.size() + "\n");
                        if(city_Obj.getString("name").equals(name_Of_City)){
                                JSONObject coordinate_Obj = city_Obj.getJSONObject("coord");
                            double temp_lat =coordinate_Obj.getDouble("lat");
                            double temp_lon =coordinate_Obj.getDouble("lon");
                                if(((lat_gps-1)<temp_lat)&&(temp_lat<(lat_gps+1))){
                                    if(((lon_gps-1)<temp_lon)&&(temp_lon<(lon_gps+1))){
                                        id_of_gps = city_Obj.getInt("_id") ;
                                        break;
                                    }
                                }

                        }
               /* city_Name_db = city_Obj.getString("name");
                String country_Name_db = city_Obj.getString("country");
                JSONObject coordinate_Obj = city_Obj.getJSONObject("coord");
                double lat_db = coordinate_Obj.getDouble("lat") ;
                double lon_db = coordinate_Obj.getDouble("lon") ;
                int id_db = city_Obj.getInt("_id") ;*/

                      /*  testoutput +=( id_db + "\n");
                        testoutput +=( city_Name _db+ "\n");
                        testoutput +=( country_Name_db + "\n");
                        testoutput +=( lat_db + "\n");
                        testoutput +=( lon _db+ "\n");*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void readFromFile() {
        runOnUiThread(new Runnable(){
            @Override
            public void run() {


                try {
                    String baseFolder = MainActivity.this.getBaseContext().getExternalFilesDir(null).getAbsolutePath();
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
                    baseFolder = MainActivity.this.getBaseContext().getExternalFilesDir(null).getAbsolutePath();
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


    /*  private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=87af097933a54e7faa3bf56ebe71bc57");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(6666);
                    connection.setReadTimeout(6666);
                    InputStream inputstream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputstream));
                    StringBuilder respoonse_string = new StringBuilder();
                    String respones_text;
                    while ((respones_text = reader.readLine()) != null) {
                        respoonse_string.append(respones_text);
                    }
                    showResponse(respoonse_string.toString());
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
            }
        }).start();
    }

    private void showResponse(final String respoonse_string) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                responseText.setText(respoonse_string);
            }
        });
    }*/
/*
    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent, 0); //此为设置完成后返回到获取界面

    }

    private void getLocation() {
        // 获取位置管理服务
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this.getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗


        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        //LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }


};

        TextView tv1;
        tv1 = (TextView) this.findViewById(R.id.response_text);
        if ( ActivityCompat.checkSelfPermission(this,android. Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            tv1.setText("access denied1");
        }
        else{
            tv1.setText("access denied2");
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions , 1);
        }
        else{

        }
        // 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置

        updateToNewLocation(location);
        locationManager.requestLocationUpdates(provider, 10 * 1000, 10, locationListener);

    }

    private void updateToNewLocation(Location location) {

        TextView tv1;
        tv1 = (TextView) this.findViewById(R.id.response_text);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            tv1.setText("lat：" + latitude + "\nlou:" + longitude);
        } else {
            tv1.setText("access denied");
        }


    }*/


