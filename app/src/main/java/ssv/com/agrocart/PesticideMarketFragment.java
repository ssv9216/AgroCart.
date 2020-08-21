package ssv.com.agrocart;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ssv.com.agrocart.adapters.PesticideAdapter;

public class PesticideMarketFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private PesticideAdapter mAdapter;
    private List<PesticideSample> mSample;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_pesticide_market, container,false);

        mRecyclerView = view.findViewById(R.id.rvPesticide);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSample = new ArrayList<>();
        //Adding Data Manually
        final PesticideSample pesticideSample = new PesticideSample("green dews oil cake fertilizer integrated & decomposed ", "5000 g Powder", "₹949", "5", R.drawable.pest1);
        PesticideSample pesticideSample1 = new PesticideSample("Midgules Enriched Vermicompost – 100% Organic ( With Neem Cake & Compost) b Soil Manure  (5 kg Powder)", "5 kg Powder", "₹949", "3.7", R.drawable.pest2);
        PesticideSample pesticideSample2 = new PesticideSample("Green Ivy Neem oil for plants Soil Manure", "250 ml Liquid", "₹319", "4", R.drawable.pest3);
        //Adding to List
        mSample.add(pesticideSample);
        mSample.add(pesticideSample1);
        mSample.add(pesticideSample2);
        mSample.add(pesticideSample);
        mSample.add(pesticideSample1);
        mSample.add(pesticideSample2);



        //setting adapter to recyclerView
        mAdapter = new PesticideAdapter(getContext(), mSample);
        mRecyclerView.setAdapter(mAdapter);
        //OnClickOnItem
        mAdapter.setOnItemClickListener(new PesticideAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int Position) {
                Intent i = new Intent(getContext(), PesticideDetail.class);

                i.putExtra("title", mSample.get(Position).getTitle());
                i.putExtra("desc", mSample.get(Position).getDesc());
                i.putExtra("rating",mSample.get(Position).getRating());
                i.putExtra("Image", mSample.get(Position).getThumbnail());
                i.putExtra("price", mSample.get(Position).getPrice());
                startActivity(i);

            }
        });





        return view;
    }
}
