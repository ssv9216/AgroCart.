package ssv.com.agrocart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class ProfileImageDetail extends AppCompatActivity {

    private HomePage2 mainActivity;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image_detail);

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
        ImageView profileImage = findViewById(R.id.profileImage);
        Toolbar toolbar = findViewById(R.id.toolBar);
        backButton = findViewById(R.id.backButton);



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileImageDetail.super.onBackPressed();
            }
        });



        Intent i = getIntent();
        if (i.getExtras() != null){
            try {

                    String imageUrl = i.getStringExtra("imageUrl");

                    Picasso.with(this)
                            .load(imageUrl)
                            .into(profileImage);

            }catch (Exception e){
                Log.d("TAG",""+e);
            }
        }
    }
}
