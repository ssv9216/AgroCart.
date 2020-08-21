package ssv.com.agrocart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class CropDetail extends AppCompatActivity {

    public static String TAG = "CropDetail";
    ImageButton IBLike;
    TextView   cName, cPrice, cWeight, cDesc;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_detail);

        Log.d(TAG, "Inside CropDetail");
        IBLike = findViewById(R.id.ibLike);
        cName = findViewById(R.id.tvCname);
        cPrice = findViewById(R.id.cPrice);
        cWeight = findViewById(R.id.cWeight);
        cDesc = findViewById(R.id.cDesc);
        toolbar = findViewById(R.id.cdToolbar);

        setActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setTitle("About Crop");
        getActionBar().setDisplayHomeAsUpEnabled(true);


        String c_name;
        String c_price = null;
        String c_weight = null;
        String c_desc = null;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                c_name = null;
            } else {
                c_name = extras.getString("c_name");
                c_price = extras.getString("c_price");
                c_weight = extras.getString("c_weight");
                c_desc = extras.getString("c_desc");
            }
        } else {
            c_name = (String) savedInstanceState.getSerializable("c_name");
            c_price = (String) savedInstanceState.getSerializable("c_price");
            c_weight = (String) savedInstanceState.getSerializable("c_weight");
            c_desc = (String) savedInstanceState.getSerializable("c_desc");
        }

        cName.setText(c_name);
        cPrice.setText(c_price);
        cWeight.setText(c_weight);
        cDesc.setText(c_desc);


    }

    public void liked(View view) {

        IBLike.setImageResource(R.drawable.ic_liked);

    }

}
