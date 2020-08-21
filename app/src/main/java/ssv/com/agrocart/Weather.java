package ssv.com.agrocart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static ssv.com.agrocart.R.drawable.d1;
import static ssv.com.agrocart.R.drawable.d10;
import static ssv.com.agrocart.R.drawable.d11;
import static ssv.com.agrocart.R.drawable.d13;
import static ssv.com.agrocart.R.drawable.d2;
import static ssv.com.agrocart.R.drawable.d3;
import static ssv.com.agrocart.R.drawable.d4;
import static ssv.com.agrocart.R.drawable.d50;
import static ssv.com.agrocart.R.drawable.d9;
import static ssv.com.agrocart.R.drawable.ic_weather;


public class Weather extends AppCompatActivity {

    final public static String TAG = "Weather";
    private static final int LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    private RequestQueue mRequestQueue;
    private RequestQueue mRequestQueue1;
    public double lat = 22.554029;
    public double lon = 72.948936;
    private TextView tvTemp, tvCityName, tvDesc;
    RelativeLayout rlWeather;
    LottieAnimationView loading;
    RelativeLayout relativeLayout1;
    TextView today, tomorrow, overMorrow, overMorrowTitle;
    ImageView iv,iv1,iv2;
    public List<String> iconNames = new ArrayList<>();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvTemp = findViewById(R.id.tvTemp);
        tvCityName = findViewById(R.id.tvCityName);
        tvDesc = findViewById(R.id.tvDescW);
        rlWeather = findViewById(R.id.rlWeather);
        relativeLayout1 = findViewById(R.id.rlWeather1);
        loading = findViewById(R.id.lavLoading);
        today = findViewById(R.id.textView4);
        tomorrow = findViewById(R.id.textView5);
        overMorrow = findViewById(R.id.textView6);
        overMorrowTitle = findViewById(R.id.textView3);
        iv =findViewById(R.id.iv);
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);




        Toolbar toolbar = findViewById(R.id.wrToolbar);
        setActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setTitle("");

        //Before retrieve data all view will hidden and loading is visible
        relativeLayout1.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue1 = Volley.newRequestQueue(this);

        findOverMorrow();

    }

    private void findOverMorrow() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "" + dayOfWeek);

        switch (dayOfWeek) {
            case 1:
                overMorrowTitle.setText("TUE");
                break;
            case 2:
                overMorrowTitle.setText("WED");
                break;
            case 3:
                overMorrowTitle.setText("THU");
                break;
            case 4:
                overMorrowTitle.setText("FRI");
                break;
            case 5:
                overMorrowTitle.setText("SAT");
                break;
            case 6:
                overMorrowTitle.setText("SUN");
                break;
            case 7:
                overMorrowTitle.setText("MON");
                break;
            default:
                overMorrowTitle.setText("OverMorrow");

        }
    }



    //parse json for current weather data
    private void parseJson(Double longitude, Double latitude) {

        String apiOpeWeather = "93a57e004c94dcf75b3067a3aa2542e4";
        final String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiOpeWeather;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject main = response.getJSONObject("main");
                            JSONArray weatherArray = response.getJSONArray("weather");

                            Log.d(TAG, ""+url);

                            for (int i = 0; i < weatherArray.length(); i++) {
                                JSONObject description = weatherArray.getJSONObject(i);
                                String desc = description.get("main").toString();
                                tvDesc.setText(desc);

                                String iconName = description.get("icon").toString();

                                settingUpIconsCurrent(iconName);
                            }


                            String cityName = response.get("name").toString();

                            String temp = main.getString("temp");
                            int tempC = (int) (Double.parseDouble(temp) - 273.15);

                            tvTemp.setText(tempC + "");
                            tvCityName.setText(cityName);
                            Log.d(TAG, String.valueOf(tempC));


                        } catch (JSONException e) {
                            Log.d(TAG, "" + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "" + error);
            }
        });
        mRequestQueue.add(request);

    }

    private void settingUpIconsCurrent(String iconName) {
        switch (iconName){
            case "01d":
            case "01n":
                iv.setImageResource(d1);
                break;
            case "02d":
            case "02n":
                iv.setImageResource(d2);
                break;
            case "03d":
            case "03n":
                iv.setImageResource(d3);
                break;
            case "04d":
            case "04n":
                iv.setImageResource(d4);
                break;
            case "09d":
            case "09n":
                iv.setImageResource(d9);
                break;
            case "10d":
            case "10n":
                iv.setImageResource(d10);
                break;
            case "11d":
            case "11n":
                iv.setImageResource(d11);
                break;
            case "13d":
            case "13n":
                iv.setImageResource(d13);
                break;
            case "50d":
            case "50n":
                iv.setImageResource(d50);
                break;
            default:
                iv.setImageResource(ic_weather);
        }
    }

    //On app start check permission
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            askLocationPermission();
        }
    }

    //Get last location using fused
    private void getLastLocation() {

        //current Location
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3800);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationServices.getFusedLocationProviderClient(Weather.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(Weather.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;

                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            Log.d(TAG, "Latitude=" + latitude);
                            lat = latitude;
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            Log.d(TAG, "longitude=" + longitude);
                            lon = longitude;
                            parseJson(longitude, latitude);
                            parsJsonForecast(longitude, latitude);
                        }
                    }
                }, Looper.getMainLooper());

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //We have a location
                    Log.d(TAG, "onSuccess: " + location.toString());
                    Log.d(TAG, "onSuccess: " + location.getLatitude());
                    lon = location.getLongitude();
                    Log.d(TAG, "onSuccess: " + location.getLongitude());
                    lat = location.getLatitude();
                    //show relative layout
                    relativeLayout1.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);


                    parseJson(lon, lat);


                } else {
                    Log.d(TAG, "onSuccess: Location was null...");
//                    parseJson(lon, lat);
//                    Toast.makeText(Weather.this, "Didn't get location ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getLastLocation();
            } else {
                //Permission not granted
            }
        }
    }

    public void parsJsonForecast(double lon, double lat) {

        String apiOpeWeather = "93a57e004c94dcf75b3067a3aa2542e4";
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + apiOpeWeather;
        Log.d(TAG, url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray listArray = response.getJSONArray("list");
                            List<String> temp_min = new ArrayList<>();
                            List<String> temp_max = new ArrayList<>();
                            temp_min.clear();
                            temp_max.clear();
                            iconNames.clear();

                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject object = listArray.getJSONObject(i);

                                JSONObject main = object.getJSONObject("main");

                                //Getting temp_min and storing to temp_min array
                                String first_min = main.get("temp_min").toString();
                                int minInCelsius = (int) (Double.parseDouble(first_min) - 273.15);
                                temp_min.add(String.valueOf(minInCelsius));

                                //Getting temp_max and storing to temp_max array
                                String first_max = main.get("temp_max").toString();
                                int maxInCelsius = (int) (Double.parseDouble(first_max) - 273.15);
                                temp_max.add(String.valueOf(maxInCelsius));

                                //getting icon names
                                JSONArray weather = object.getJSONArray("weather");

                                JSONObject zero = (JSONObject) weather.get(0);
                                String iconName = zero.getString("icon");
                                iconNames.add(iconName);


                            }
//                            Log.d(TAG,"TODAY= "+ temp.get(0)+" "+temp.get(1)+" "+temp.get(2));
//                            Log.d(TAG,"TOMORROW= "+ temp.get(3)+" "+temp.get(4)+" "+temp.get(5));
//                            Log.d(TAG,"OVER MORROW= "+ temp.get(6)+" "+temp.get(7)+" "+temp.get(8));

                            //setting temp values to text

                            today.setText(temp_max.get(0) + "°c / " + temp_min.get(0) + "°c");
                            tomorrow.setText(temp_max.get(6) + "°c / " + temp_min.get(6) + "°c");
                            overMorrow.setText(temp_max.get(14) + "°c / " + temp_min.get(14) + "°c");
                            settingUpIcons(iconNames);

                        } catch (JSONException e) {
                            Log.d(TAG, "JSOn Error: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "" + error);
            }
        });
        mRequestQueue1.add(request);

    }

    private void settingUpIcons(List<String> iconNames) {

        //for tomorrow and overMorrow
        for(int i = 0;i< iconNames.size(); i++){
            switch (iconNames.get(i)){
                case "01d":
                case "01n":
                    if (i==6){
                        iv1.setImageResource(d1);
                    }
                    if (i==14){
                        iv2.setImageResource(d1);
                    }
                    break;
                case "02d":
                case "02n":
                    if (i==6){
                        iv1.setImageResource(d2);
                    }
                    if (i==14){
                        iv2.setImageResource(d2);
                    }
                case "03d":
                case "03n":
                    if (i==6){
                        iv1.setImageResource(d3);
                    }
                    if (i==14){
                        iv2.setImageResource(d3);
                    }
                    break;
                case "04d":
                case "04n":
                    if (i==6){
                        iv1.setImageResource(d4);
                    }
                    if (i==14){
                        iv2.setImageResource(d4);
                    }
                    break;
                case "09d":
                case "09n":
                    if (i==6){
                        iv1.setImageResource(d9);
                    }
                    if (i==14){
                        iv2.setImageResource(d9);
                    }
                    break;
                case "10d":
                case "10n":
                    if (i==6){
                        iv1.setImageResource(d10);
                    }
                    if (i==14){
                        iv2.setImageResource(d10);
                    }
                    break;
                case "11d":
                case "11n":
                    if (i==6){
                        iv1.setImageResource(d11);
                    }
                    if (i==14){
                        iv2.setImageResource(d11);
                    }
                    break;
                case "13d":
                case "13n":
                    if (i==6){
                        iv1.setImageResource(d13);
                    }
                    if (i==14){
                        iv2.setImageResource(d13);
                    }
                    break;
                case "50d":
                case "50n":
                    if (i==6){
                        iv1.setImageResource(d50);
                    }
                    if (i==14){
                        iv2.setImageResource(d50);
                    }
                    break;
                default:
                    iv1.setImageResource(ic_weather);
                    iv2.setImageResource(ic_weather);
            }
        }
    }

    //onClick constraint layout
    public void gotoForecast(View view) {

        Intent i = new Intent(this, ForecastActivity.class);
        i.putExtra("lon", lon);
        i.putExtra("lat", lat);
        startActivity(i);
    }
    //check weather internet is available or not



}
