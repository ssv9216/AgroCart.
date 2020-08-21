package ssv.com.agrocart;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class Market extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem tabCrops;
    TabItem tabPesticide;
    Toolbar toolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        tabLayout = findViewById(R.id.tabs);
        tabCrops = findViewById(R.id.tiCrop);
        tabPesticide = findViewById(R.id.tiPesticide);
        toolbar = findViewById(R.id.mtToolbar);

        setActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setTitle(R.string.market);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //Up(back Button) <-
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_market, new CropMarketFragment()).commit();
        
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    Log.d("position", "cropmarket"+tab.getPosition());
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_market, new CropMarketFragment()).commit();
                }
                if (tab.getPosition() ==1){
                    Log.d("position", "pestMarket"+tab.getPosition());
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_market, new PesticideMarketFragment()).commit();
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });;



    }
}