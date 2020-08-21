package ssv.com.agrocart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Intent.ACTION_VIEW;
import static com.google.firebase.database.util.JsonMapper.parseJson;

public class PoliciesFragment extends Fragment implements HomePage2.OnBackPressedListener {

    private static final String TAG = "PoliciesFragment";
    private RecyclerView recyclerView;
    private PoliciesAdapter mAdapter;
    private List<Policies> mList;
    private RequestQueue mRequestQueue;
    private LottieAnimationView laLoader;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_policies,container, false);

       recyclerView = v.findViewById(R.id.fpRecyclerView);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       laLoader = v.findViewById(R.id.laLoader);
       laLoader.bringToFront();
       laLoader.setVisibility(View.VISIBLE);

       mList =  new ArrayList<>();
       mRequestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

       String url = "http://ssv.ueuo.com/policies.json" ;
        parseJSON(url);

//        mList.add(new Policies("Information on cultivation of wheat by Department of Agriculture of Meghalaya","Information on cultivation of wheat is provided by the Agriculture Department of Meghalaya. Get details of recommended varieties of wheat such as Sonalika, Kalyan Sona, Girija etc. Details about the selection and preparation of land are also provided. Information on time of sowing, seed treatment, seed rate etc. is available. Get information on manures, fertilizers, weed control, irrigation,...","https://megagriculture.gov.in/PUBLIC/crops_wheat.aspx"));
//       mList.add(new Policies("Information on cultivation of wheat by Department of Agriculture of Meghalaya","Information on cultivation of wheat is provided by the Agriculture Department of Meghalaya. Get details of recommended varieties of wheat such as Sonalika, Kalyan Sona, Girija etc. Details about the selection and preparation of land are also provided. Information on time of sowing, seed treatment, seed rate etc. is available. Get information on manures, fertilizers, weed control, irrigation,...","https://megagriculture.gov.in/PUBLIC/crops_wheat.aspx"));



       return v;
    }

    private void parseJSON(String url){

        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("policies");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject policie = jsonArray.getJSONObject(i);

                                    String title = policie.getString("title");
                                    String desc = policie.getString("Description");
                                    String url = policie.getString("url");


                                    mList.add(new Policies(title, desc, url));
                                }

                                mAdapter = new PoliciesAdapter(getContext(),mList);
                                recyclerView.setAdapter(mAdapter);
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
        }catch(Exception e){
            Log.d(TAG, "NetWork Error");
        }
    }


    @Override
    public boolean onBackPressed() {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        return true;
    }
}
