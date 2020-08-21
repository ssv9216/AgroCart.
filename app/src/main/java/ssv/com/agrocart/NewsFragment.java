package ssv.com.agrocart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import static android.content.Intent.ACTION_VIEW;
import static ssv.com.agrocart.R.drawable.default_profile_pic;
import static ssv.com.agrocart.R.drawable.ic_keyboard_backspace;
import static ssv.com.agrocart.R.raw.crop_tractor;

public class NewsFragment extends Fragment implements HomePage2.OnBackPressedListener {

    private static final String TAG = "NewsFragment";

    public NewsFragment() {
    }

    View view;
    public ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private ArrayList<NewsItem> mNewsList;
    private RequestQueue mRequestQueue;
    private LottieAnimationView laLoader;
    private SearchView searchView;
    private int counter = 0;
    private String queryText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);



        laLoader = view.findViewById(R.id.laLoader);
        laLoader.bringToFront();
        laLoader.setVisibility(View.VISIBLE);


        mRecyclerView = view.findViewById(R.id.recycler_view);
        //snapHelper
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mNewsList = new ArrayList<>();

        final String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String url = "https://newsapi.org/v2/top-headlines?country=in&apiKey="+getResources().getString(R.string.news_api_key);
//        String url = "http://newsapi.org/v2/everything?q=agriculture&from="+currentDate+"&sortBy=publishedAt&apiKey=";
        mRequestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        parseJSON(url);




        return view;
    }


    private void parseJSON(final String url) {
        mNewsList.clear();
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("articles");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject articles = jsonArray.getJSONObject(i);

                                    String title = articles.getString("title");
                                    String imageUrl = articles.getString("urlToImage");
                                    String desc = articles.getString("description");
                                    String url = articles.getString("url");

                                    mNewsList.add(new NewsItem(imageUrl, title, desc, url));
                                }

                                mNewsAdapter = new NewsAdapter(getContext(), mNewsList);
                                mRecyclerView.setAdapter(mNewsAdapter);
                                mNewsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(int position) {
//
                                        Intent i = new Intent(ACTION_VIEW, Uri.parse(mNewsList.get(position).getUrl()));
                                        startActivity(i);
                                    }
                                });


                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                mRecyclerView.setLayoutManager(layoutManager);
                                laLoader.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mRequestQueue.add(request);
        } catch (Exception e) {
            Log.d(TAG, "NetWork Error");
        }
    }


    @Override
    public boolean onBackPressed() {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.fragment_container, new HomeFragment()).commit();
        return true;
    }

    private boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();

    }

}
