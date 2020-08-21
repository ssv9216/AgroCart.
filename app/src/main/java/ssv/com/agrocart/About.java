package ssv.com.agrocart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Objects;

import static android.content.Intent.ACTION_VIEW;

public class About extends AppCompatActivity {
    private Toolbar toolbar;
    private CardView cv1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = findViewById(R.id.toolBar);
        cv1= findViewById(R.id.cv1);

        setActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setTitle("");
        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://t.me/detroitdeveloper";
                Intent i = new Intent(ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }
        });

    }


    public void intentToTwitter(View view) {
        String url = "https://www.twitter.com/sohanvahora?s=09";
        Intent i = new Intent(ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }

    public void intentToInstagram(View view) {
        String url = "https://www.instaram.com/vhorasohan_";
        Intent i = new Intent(ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }

    public void intentToMyTelgram(View view) {
        String url = "https://www.t.me/ssv9216";
        Intent i = new Intent(ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }


}
