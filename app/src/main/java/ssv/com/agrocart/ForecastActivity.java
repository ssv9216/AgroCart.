package ssv.com.agrocart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;

public class ForecastActivity extends AppCompatActivity {


    private static final String TAG = "ForecastActivity";
    RequestQueue mRequestQueue;
    TextView today, today2, today3, today4, today5, today6, tvWeatherData, tvWeatherData2, tvWeatherData3, tvWeatherData4, tvWeatherData5, tvWeatherData6;
    ImageView icon1, icon2, icon3, icon4, icon5, icon6;
    List<String> temp_min = new ArrayList<>();
    List<String> temp_max = new ArrayList<>();
    List<String> iconNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        //Setting up for DAY_OF_WEEK
        today = findViewById(R.id.today);
        today2 = findViewById(R.id.today2);
        today3 = findViewById(R.id.today3);
        today4 = findViewById(R.id.today4);
        today5 = findViewById(R.id.today5);
        today6 = findViewById(R.id.today6);
        //For day data
        tvWeatherData = findViewById(R.id.tvWeatherData);
        tvWeatherData2 = findViewById(R.id.tvWeatherData2);
        tvWeatherData3 = findViewById(R.id.tvWeatherData3);
        tvWeatherData4 = findViewById(R.id.tvWeatherData4);
        tvWeatherData5 = findViewById(R.id.tvWeatherData5);
        tvWeatherData6 = findViewById(R.id.tvWeatherData6);
        //weather icons

        icon1 = findViewById(R.id.wIcon1);
        icon2 = findViewById(R.id.wIcon2);
        icon3 = findViewById(R.id.wIcon3);
        icon4 = findViewById(R.id.wIcon4);
        icon5 = findViewById(R.id.wIcon5);
        icon6 = findViewById(R.id.wIcon6);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("");
        getActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        settingUpTitles(dayOfWeek);

        mRequestQueue = Volley.newRequestQueue(this);
        //Retrieving data through intent and parse it.
        Intent i = getIntent();
        if (i.getExtras() != null) {
            double lon = (double) Objects.requireNonNull(i.getExtras()).get("lon");
            double lat = (double) i.getExtras().get("lat");
            Log.d(TAG, String.valueOf(lon));
            Log.d(TAG, String.valueOf(lat));

            parseJson(lon, lat);
        } else {
            Log.d(TAG, "Extras are null");
        }
    }

    private void settingUpTitles(int dayOfWeek) {
        Log.d(TAG, "DayOfWeek=" + dayOfWeek);
        switch (dayOfWeek) {
            case 1:
                today3.setText("TUE");
                today4.setText("WED");
                today5.setText("THU");
                today6.setText("FRI");
                break;
            case 2:
                today3.setText("WED");
                today4.setText("THU");
                today5.setText("FRI");
                today6.setText("SAT");
                break;
            case 3:

                today3.setText("THU");
                today4.setText("FRI");
                today5.setText("SAT");
                today6.setText("SUN");
                break;
            case 4:

                today3.setText("FRI");
                today4.setText("SAT");
                today5.setText("SUN");
                today6.setText("MON");
                break;
            case 5:

                today3.setText("SAT");
                today4.setText("SUN");
                today5.setText("MON");
                today6.setText("TUE");
                break;
            case 6:

                today3.setText("SUN");
                today4.setText("MON");
                today5.setText("TUE");
                today6.setText("WED");
                break;
            case 7:

                today3.setText("MON");
                today4.setText("TUE");
                today5.setText("WED");
                today6.setText("THU");
                break;
        }
    }

    public void parseJson(double longitude, double latitude) {

        String apiOpeWeather = "93a57e004c94dcf75b3067a3aa2542e4";
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiOpeWeather;
        Log.d(TAG, "Inside parseJson");
        Log.d(TAG, url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray listArray = response.getJSONArray("list");
                            temp_min.clear();
                            temp_max.clear();

                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject object = listArray.getJSONObject(i);

                                //inside main object find temp_min and temp_max
                                JSONObject main = object.getJSONObject("main");

                                //Getting temp_min and storing to temp_min array
                                String first_min = main.get("temp_min").toString();
                                int minInCelsius1 = (int) (Double.parseDouble(first_min) - 273.15);
                                temp_min.add(String.valueOf(minInCelsius1));

                                //Getting temp_max and storing to temp_max array
                                String first_max = main.get("temp_max").toString();
                                int maxInCelsius2 = (int) (Double.parseDouble(first_max) - 273.15);
                                temp_max.add(String.valueOf(maxInCelsius2));

                                //Getting icon name..
                                JSONArray weather = object.getJSONArray("weather");

                                JSONObject zero = (JSONObject) weather.get(0);
                                String iconName = zero.getString("icon");
                                iconNames.add(iconName);

                            }
                            Log.d(TAG, "TODAY= " + temp_min.get(0) + " " + temp_max.get(0));
                            Log.d(TAG, "TOMORROW= " + temp_min.get(6) + " " + temp_max.get(6));
                            Log.d(TAG, "OVER MORROW= " + temp_min.get(14) + " " + temp_max.get(14));

                            //setting temp values to text
                            settingUpData(temp_max, temp_min);

                            settingUpIcons(iconNames);

                        } catch (JSONException e) {
                            Log.d(TAG, "JSOn Error: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Response Error" + error);
            }
        });
        mRequestQueue.add(request);
    }


    @SuppressLint("SetTextI18n")
    private void settingUpData(List<String> temp_max, List<String> temp_min) {
        if (temp_min.isEmpty() || temp_max.isEmpty()) {
            Log.d(TAG, "data is empty");
        } else {
            Log.d(TAG, "at 0: " + temp_min.get(0));
            tvWeatherData.setText(temp_max.get(0) + "°C / " + temp_min.get(0) + "°C");
            tvWeatherData2.setText(temp_max.get(6) + "°C / " + temp_min.get(6) + "°C");
            tvWeatherData3.setText(temp_max.get(14) + "°C / " + temp_min.get(14) + "°C");
            tvWeatherData4.setText(temp_max.get(22) + "°C / " + temp_min.get(22) + "°C");
            tvWeatherData5.setText(temp_max.get(30) + "°C / " + temp_min.get(30) + "°C");
            tvWeatherData6.setText(temp_max.get(38) + "°C / " + temp_min.get(38) + "°C");
        }
    }

    private void settingUpIcons(List<String> icon_names) {

        for (int i = 0; i < icon_names.size(); i++) {
            switch (iconNames.get(i)) {
                case "01d":
                case "01n":
                    if (i == 0) {
                        icon1.setImageResource(R.drawable.d1);
                    }
                    if (i == 6) {
                        icon2.setImageResource(R.drawable.d1);
                    }
                    if (i == 14) {
                        icon3.setImageResource(R.drawable.d1);
                    }
                    if (i == 22) {
                        icon4.setImageResource(R.drawable.d1);
                    }
                    if (i == 30) {
                        icon5.setImageResource(R.drawable.d1);
                    }
                    if (i == 38) {
                        icon6.setImageResource(R.drawable.d1);
                    }
                    break;
                case "02n":
                case "02d":
                    if (i == 0) {
                        icon1.setImageResource(R.drawable.d2);
                    }
                    if (i == 6) {
                        icon2.setImageResource(R.drawable.d2);
                    }
                    if (i == 14) {
                        icon2.setImageResource(R.drawable.d2);
                    }
                    if (i == 22) {
                        icon2.setImageResource(R.drawable.d2);
                    }
                    if (i == 30) {
                        icon2.setImageResource(R.drawable.d2);
                    }
                    if (i == 38) {
                        icon2.setImageResource(R.drawable.d2);
                    }
                    break;
                case "03n":
                case "03d":
                    if (i == 0) {
                        icon1.setImageResource(R.drawable.d3);
                    }
                    if (i == 6) {
                        icon2.setImageResource(R.drawable.d3);
                    }
                    if (i == 14) {
                        icon3.setImageResource(R.drawable.d3);
                    }
                    if (i == 22) {
                        icon4.setImageResource(R.drawable.d3);
                    }
                    if (i == 30) {
                        icon5.setImageResource(R.drawable.d3);
                    }
                    if (i == 38) {
                        icon6.setImageResource(R.drawable.d3);
                    }
                    break;
                case "04n":
                case "04d":
                    if (i == 0) {
                        icon1.setImageResource(R.drawable.d4);
                    }
                    if (i == 6) {
                        icon2.setImageResource(R.drawable.d4);
                    }
                    if (i == 14) {
                        icon3.setImageResource(R.drawable.d4);
                    }
                    if (i == 22) {
                        icon4.setImageResource(R.drawable.d4);
                    }
                    if (i == 30) {
                        icon5.setImageResource(R.drawable.d4);
                    }
                    if (i == 38) {
                        icon6.setImageResource(R.drawable.d4);
                    }
                    break;
                case "09d":
                case "09n":
                    if (i == 0) {
                        icon1.setImageResource(R.drawable.d9);
                    }
                    if (i == 6) {
                        icon2.setImageResource(R.drawable.d9);
                    }
                    if (i == 14) {
                        icon3.setImageResource(R.drawable.d9);
                    }
                    if (i == 22) {
                        icon4.setImageResource(R.drawable.d9);
                    }
                    if (i == 30) {
                        icon5.setImageResource(R.drawable.d9);
                    }
                    if (i == 38) {
                        icon6.setImageResource(R.drawable.d9);
                    }
                    break;
                case "10n":
                case "10d":
                    if (i == 0) {
                        icon1.setImageResource(R.drawable.d10);
                    }
                    if (i == 6) {
                        icon2.setImageResource(R.drawable.d10);
                    }
                    if (i == 14) {
                        icon3.setImageResource(R.drawable.d10);
                    }
                    if (i == 22) {
                        icon4.setImageResource(R.drawable.d10);
                    }
                    if (i == 30) {
                        icon5.setImageResource(R.drawable.d10);
                    }
                    if (i == 38) {
                        icon6.setImageResource(R.drawable.d10);
                    }
                    break;
                case "11n":
                case "11d":
                    if (i == 0) {
                        icon1.setImageResource(R.drawable.d11);
                    }
                    if (i == 6) {
                        icon2.setImageResource(R.drawable.d11);
                    }
                    if (i == 14) {
                        icon3.setImageResource(R.drawable.d11);
                    }
                    if (i == 22) {
                        icon4.setImageResource(R.drawable.d11);
                    }
                    if (i == 30) {
                        icon5.setImageResource(R.drawable.d11);
                    }
                    if (i == 38) {
                        icon6.setImageResource(R.drawable.d11);
                    }
                    break;
                case "13n":
                case "13d":
                    if (i == 0) {
                        icon1.setImageResource(R.drawable.d13);
                    }
                    if (i == 6) {
                        icon2.setImageResource(R.drawable.d13);
                    }
                    if (i == 14) {
                        icon3.setImageResource(R.drawable.d13);
                    }
                    if (i == 22) {
                        icon4.setImageResource(R.drawable.d13);
                    }
                    if (i == 30) {
                        icon5.setImageResource(R.drawable.d13);
                    }
                    if (i == 38) {
                        icon6.setImageResource(R.drawable.d13);
                    }
                    break;
                case "50n":
                case "50d":
                    if (i == 0) {
                        icon1.setImageResource(R.drawable.d50);
                    }
                    if (i == 6) {
                        icon2.setImageResource(R.drawable.d50);
                    }
                    if (i == 14) {
                        icon3.setImageResource(R.drawable.d50);
                    }
                    if (i == 22) {
                        icon4.setImageResource(R.drawable.d50);
                    }
                    if (i == 30) {
                        icon5.setImageResource(R.drawable.d50);
                    }
                    if (i == 38) {
                        icon6.setImageResource(R.drawable.d50);
                    }
                    break;
                default:
                    if (i == 0) {
                        icon1.setImageResource(R.drawable.ic_weather);
                    }
                    if (i == 6) {
                        icon2.setImageResource(R.drawable.ic_weather);
                    }
                    if (i == 14) {
                        icon3.setImageResource(R.drawable.ic_weather);
                    }
                    if (i == 22) {
                        icon4.setImageResource(R.drawable.ic_weather);
                    }
                    if (i == 30) {
                        icon5.setImageResource(R.drawable.ic_weather);
                    }
                    if (i == 38) {
                        icon6.setImageResource(R.drawable.ic_weather);
                    }

            }
        }
    }

}
