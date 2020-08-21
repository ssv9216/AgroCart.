package ssv.com.agrocart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class CropDetailPublic extends AppCompatActivity {

    TextView cName, cWeight,cPrice, cDesc;
    ImageButton uProfile;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_detail_public);

        cName = findViewById(R.id.tvCnameP);
        cWeight = findViewById(R.id.cWeightP);
        cPrice = findViewById(R.id.cPriceP);
        cDesc = findViewById(R.id.cDescP);
        uProfile = findViewById(R.id.uProfileP);
        toolbar = findViewById(R.id.acpToolbar);

        //setting up button
        setActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setTitle("About Crop");
        getActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        Crop cropItem = i.getParcelableExtra("Crop item");

        cName.setText(cropItem.getName());
        cPrice.setText(cropItem.getPrice());
        cWeight.setText(cropItem.getWeight());
        cDesc.setText(cropItem.getDescription());

        final String uId = cropItem.getUid();

        // Don't show profile button when it's current user post.
        if (getCurrentUserId().equals(uId)){
            uProfile.setVisibility(View.GONE);
//            Toast.makeText(this, "It's your post", Toast.LENGTH_SHORT).show();
        }

        uProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CropDetailPublic.this,ProfileDetail.class);
                i.putExtra("UserId", uId);
                startActivity(i);
            }
        });

    }
    public String getCurrentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

}
