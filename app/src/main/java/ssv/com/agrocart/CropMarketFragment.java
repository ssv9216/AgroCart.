package ssv.com.agrocart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.Gravity.RIGHT;

public class CropMarketFragment extends Fragment {

    public static final String TAG = "CropMarketFragment";

    //vars
    private RecyclerView mRecyclerView;
    private RecyclerViewPosts mAdapter;
    private List<Crop> mCrop;
    private SearchView searchView;
    DatabaseReference reff;
    private LottieAnimationView laLoader;

    public CropMarketFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop_market, container, false);

        Log.d(TAG, "Oncreate Started");

        searchView = view.findViewById(R.id.searchView);
        searchView.setVisibility(View.GONE);
        laLoader = view.findViewById(R.id.laLoader);
        laLoader.bringToFront();

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(getResources().getColor(R.color.mentCream));

        Log.d(TAG, "InitRecyclerView");
        mRecyclerView = view.findViewById(R.id.cropRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCrop = new ArrayList<>();
        getDataFromFirebase();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return view;
    }

    private void getDataFromFirebase(){
        reff = FirebaseDatabase.getInstance().getReference().child("Crops").child("PublicPost");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "On DataChange");
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String name = Objects.requireNonNull(postSnapshot.child("name").getValue()).toString();
                        String weight = Objects.requireNonNull(postSnapshot.child("weight").getValue()).toString();
                        String price = Objects.requireNonNull(postSnapshot.child("price").getValue()).toString();
                        String uid = Objects.requireNonNull(postSnapshot.child("uid").getValue()).toString();
                        String desc = Objects.requireNonNull(postSnapshot.child("description").getValue()).toString();
                        if (!uid.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                            mCrop.add(new Crop(weight, name, price, uid, desc));
                            Log.d(TAG, "data added to mCrop");
                        }
                    }
                    try {

                        mAdapter = new RecyclerViewPosts(mCrop,(Context)getContext());
                        mRecyclerView.setAdapter(mAdapter);
                        laLoader.setVisibility(View.GONE);
                        searchView.setVisibility(View.VISIBLE);
                        mAdapter.setOnItemClickListener(new RecyclerViewPosts.OnItemClickListener() {
                            @Override
                            public void OnItemClick(int position) {
                                Intent i = new Intent(getContext(), CropDetailPublic.class);

                                i.putExtra("Crop item", mCrop.get(position));


                                Log.d(TAG, "putted");
                                startActivity(i);
                            }
                        });

                    }catch (Exception e){
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, String.valueOf(e));
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Called OnCancelled");
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
