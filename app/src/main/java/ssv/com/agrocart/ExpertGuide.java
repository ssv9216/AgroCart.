package ssv.com.agrocart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExpertGuide extends AppCompatActivity {

    final private static String TAG = "ExpertGuide";
    private RecyclerView mRecyclerView;
    private ExpertGuideAdapter mAdapter;
    private List<ExpertGuideItems> mGuides;
    //    Toolbar toolbar;
    CoordinatorLayout relativeLayout;
    TextView greetings;
    Calendar calendar;
    SimpleDateFormat formatter;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_guide);


//        toolbar = findViewById(R.id.egToolbar);
        greetings = findViewById(R.id.greetings);
        relativeLayout = findViewById(R.id.relativeLayout);

        //setting up greeting by time
        calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("HH");
        String hour = formatter.format(calendar.getTime());
        showGreetings(hour);

        //Actionbar
//        setActionBar(toolbar);
//        Objects.requireNonNull(getActionBar()).setTitle("Guides");

//        getActionBar().setDisplayHomeAsUpEnabled(true);

        //recyclerView
        mRecyclerView = findViewById(R.id.rvGuides);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mGuides = new ArrayList<>();
        getDataFromFirebase();

        //Snackbar
        final Snackbar snackbar = Snackbar.make(relativeLayout, "Syncing...", Snackbar.LENGTH_LONG);
        snackbar.setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();

            }
        });
        snackbar.show();


    }

    public void showGreetings(String hour) {

        int timeHour = Integer.parseInt(hour);

        if (timeHour > 6 && timeHour < 12) {
            greetings.setText(getString(R.string.greetingtext, "good morning"));
        } else if (timeHour >= 12 && timeHour <= 16) {
            greetings.setText(getString(R.string.greetingtext, "good afternoon"));
        } else if (timeHour > 16 && timeHour <= 19) {
            greetings.setText(getString(R.string.greetingtext, "good evening"));
        } else if (timeHour >= 20 && timeHour <= 23) {
            greetings.setText(getString(R.string.greetingtext, "good night"));
        } else if (timeHour <= 5) {
            greetings.setText(getString(R.string.greetingtext, "good night"));
        }

    }

    private void getDataFromFirebase() {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Guides");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    ExpertGuideItems guide = postSnapshot.getValue(ExpertGuideItems.class);
                    mGuides.add(guide);
                    Log.d(TAG, "guide:" + guide);
                }
                try {
                    mAdapter = new ExpertGuideAdapter(ExpertGuide.this, mGuides);
                    mRecyclerView.setAdapter(mAdapter);
                } catch (Exception e) {
                    Log.d(TAG, "Error: " + e);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Database Error:" + databaseError);
            }
        });
    }

}
