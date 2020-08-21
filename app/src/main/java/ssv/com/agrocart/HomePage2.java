package ssv.com.agrocart;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigator;
import androidx.navigation.ui.AppBarConfiguration;

public class HomePage2 extends AppCompatActivity {
    protected Navigator.OnNavigatorBackPressListener onBackPress;
    private AppBarConfiguration mAppBarConfiguration;
    private AppBarLayout appBarLayout;
    Button btnExpert;
    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_page2);



        //bottomNavigationBar
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);

        btnExpert = findViewById(R.id.btnExpert);
        appBarLayout = findViewById(R.id.appBar);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        btnExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage2.this, ExpertGuide.class));
            }
        });

//        IbOptionMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final PopupMenu popupMenu = new PopupMenu(HomePage2.this, IbOptionMenu);
//                popupMenu.inflate(R.menu.activity_home_page2_drawer);
//                popupMenu.show();
//
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
//                            case R.id.about:
//                                PopUpDialog();
//                                return true;
//                            case R.id.nav_share:
//
//                                shareLink();
//                                return true;
//                            case R.id.nav_rate_app:
//                                Toast.makeText(HomePage2.this, "Rate out App", Toast.LENGTH_SHORT).show();
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                });
//            }
//        });

    }


    private void shareLink() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Join us at Telegram channel https://t.me/codeInair";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "extra subject");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "More Apps", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_rate_app:
                Toast.makeText(this, "Rate out App", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof OnBackPressedListener) || !((OnBackPressedListener) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }



    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {

                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            appBarLayout.setVisibility(View.VISIBLE);
                            bottomNav.getMenu().getItem(0).setChecked(true);
                            break;


                        case R.id.nav_sell:
                            selectedFragment = new SellFragment();
                            appBarLayout.setVisibility(View.GONE);
                            break;


                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            appBarLayout.setVisibility(View.GONE);
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.fragment_container, selectedFragment).commit();
                    updateNavigationBarState(menuItem.getItemId());
                    return true;

                }

            };


    private void updateNavigationBarState(int itemId) {

        Menu menu = bottomNav.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);

            item.setChecked(item.getItemId() == R.drawable.ic_home_inverse);
        }

    }
}
