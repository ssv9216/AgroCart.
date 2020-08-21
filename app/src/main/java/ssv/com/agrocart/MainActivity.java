package ssv.com.agrocart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import androidx.appcompat.app.AppCompatActivity;

import static ssv.com.agrocart.R.id.ivAppIcon;

public class MainActivity extends AppCompatActivity {

    private ImageView appIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appIcon = findViewById(ivAppIcon);

        final Intent i = new Intent(this,RegisterUser.class);
        final Intent j = new Intent(this, loginpage.class);
        final Intent k = new Intent(this,HomePage2.class);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean IsLogin = prefs.getBoolean("isLogin",false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(IsLogin){
                    startActivity(k);
                    finish();
                }else{
                    startActivity(j);
                    finish();
                }
            }
        }, 2000);

        YoYo.with(Techniques.FadeIn)
                .duration(2000)
                .playOn(findViewById(R.id.ivAppIcon));


    }
}
