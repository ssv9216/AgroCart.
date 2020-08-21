package ssv.com.agrocart;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ssv.com.agrocart.adapters.MarketYardAdapter;

public class MarketYard extends AppCompatActivity {
    private static final String TAG = "MarketYard";
    //    private WebView webView;
    private Spinner spinner1;
    private Spinner spinner2;
    private String cropName;
    private String cropNo;
    private Button search;
    private TextView variety;
    private String url;
    private RecyclerView recyclerView;
    private MarketYardAdapter mAdapter;
    List<CropRatesSample> sample ;

    private String updateDate;
    private String updateTime;

    private RelativeLayout rl2;
    private ImageButton open;

    private LottieAnimationView laLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_yard);

//        webView = findViewById(R.id.webView);
        search = findViewById(R.id.btnSearch);
        spinner1 = findViewById(R.id.srCrops);
        spinner2 = findViewById(R.id.srStates);
        recyclerView = findViewById(R.id.amyRecyclerView);
        rl2 = findViewById(R.id.rl2);
        open = findViewById(R.id.imgBtnOpen);
        variety = findViewById(R.id.variety);

        laLoader = findViewById(R.id.laLoader);
        laLoader.bringToFront();

        sample = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        //Up (back button) <-
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.crops_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);


        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int vis = rl2.getVisibility();
                if (vis == 8){
                    rl2.setVisibility(View.VISIBLE);
                    open.setImageResource(R.drawable.ic_keyboard_arrow_up);
                    YoYo.with(Techniques.SlideInLeft)
                            .duration(500)
                            .repeat(0)
                            .playOn(rl2);

                }else{

                    rl2.setVisibility(View.GONE);
                    open.setImageResource(R.drawable.ic_keyboard_arrow_down);
                }
                        //visible =0
                    //gone =8
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open.setImageResource(R.drawable.ic_keyboard_arrow_down);
                rl2.setVisibility(View.GONE);
                laLoader.setVisibility(View.VISIBLE);
                int position = spinner1.getSelectedItemPosition();
                switch (position) {
                    case 0:
                        cropName = "Brinjal";
                        cropNo = "247";
                        loadWebView();
                        break;
                    case 1:
                        cropName = "Bhindi";
                        cropNo = "202";
                        loadWebView();
                        break;
                    case 2:
                        cropName = "Cabbage";
                        cropNo = "203";
                        loadWebView();
                        break;
                    case 3:
                        cropName = "Wheat";
                        cropNo = "120";
                        loadWebView();
                        break;
                    case 4:
                        cropName = "Rice";
                        cropNo = "119";
                        loadWebView();
                        break;
                    case 5:
                        cropName = "Tomato";
                        cropNo = "169";
                        loadWebView();
                        break;

                }
            }
        });

    }

    private void loadWebView() {
        url = "https://www.commodityonline.com/mandiprices/" + cropName + "/gujarat/" + cropNo + "/12";
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.requestFocus(View.FOCUS_DOWN);
//        webView.loadUrl(url);
        new Content().execute();
    }

    public class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(url).get();
                //number of classes with same name
                int size = doc.select("div.dt_ta_09").size();
                sample.clear();

                updateDate = doc.getElementsByClass("la_update-style as_padd").text();
                updateTime = doc.getElementsByClass("la_date-style as_padd").text();

                for (int i = 0; i < size; i++) {

                    String cityName = doc.getElementsByClass("dt_ta_09").get(i).select("div.dt_ta_11").text();
                    String variety = doc.getElementsByClass("dt_ta_09").get(i).select("div.dt_ta_12").text();
                    String arrivals = doc.getElementsByClass("dt_ta_09").get(i).select("div.dt_ta_13").text();
                    String m_price = doc.getElementsByClass("dt_ta_09").get(i).select("div.dt_ta_14").text();
                    String MinMax = doc.getElementsByClass("dt_ta_09").get(i).select("div.dt_ta_10s").text();

                    sample.add(new CropRatesSample(cityName, variety, arrivals, m_price, MinMax));
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mAdapter = new MarketYardAdapter(sample);
            variety.setText(cropName);
            recyclerView.setAdapter(mAdapter);
            laLoader.setVisibility(View.GONE);
            Toast.makeText(MarketYard.this, ""+updateDate+" \n "+updateTime, Toast.LENGTH_SHORT).show();
        }
    }
}
