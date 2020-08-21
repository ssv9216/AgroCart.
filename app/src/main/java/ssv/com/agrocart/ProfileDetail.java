package ssv.com.agrocart;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import ssv.com.agrocart.adapters.ProfilePostsAdapter;

import static androidx.core.view.ViewCompat.getTransitionName;
import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static ssv.com.agrocart.R.id.about;
import static ssv.com.agrocart.R.id.apdIvProfile;
import static ssv.com.agrocart.R.id.ivProfile;
import static ssv.com.agrocart.R.id.profileImage;

public class ProfileDetail extends AppCompatActivity {

    private static final String TAG = "ProfileDetail";
    TextView tvEmail, tvUserName;
    CollapsingToolbarLayout ctLayout;
    ImageButton ibMail, ibPhone, ibSms, ibStar;
    ImageView ivProfile;
    String UserNumber;
    String profileUrl;
    Bitmap bmProfileImage;
    //horizontal-List of user post
    RecyclerView recyclerView;
    ProfilePostsAdapter mAdapter;
    String uid;
    List<Crop> mList;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        tvEmail = findViewById(R.id.userEmail);
        ibMail = findViewById(R.id.ibMailUser);
        tvUserName = findViewById(R.id.userName);
        ibPhone = findViewById(R.id.ibPhoneUser);
        ibStar = findViewById(R.id.ibStar);
        ibSms = findViewById(R.id.ibSms);
        ivProfile = findViewById(R.id.apdIvProfile);
        recyclerView = findViewById(R.id.apdRecyclerView);

        //snapHelper to jump to next item specially use pagerSnaphelper
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        mList = new ArrayList<>();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                uid = null;
            } else {
                uid = extras.getString("UserId");
            }
        } else {
            uid = (String) savedInstanceState.getSerializable("UserId");
        }


        //getting user's detail
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("User").child(uid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = (String) dataSnapshot.child("name").getValue();
                tvUserName.setText(name);
                String address = (String) dataSnapshot.child("address").getValue();
                String email = (String) dataSnapshot.child("email").getValue();
                tvEmail.setText(email);
                UserNumber = (String) dataSnapshot.child("number").getValue();
                profileUrl = (String) dataSnapshot.child("ProfileImageUrl").getValue();
                if (profileUrl != null) {
                    Picasso.with(ProfileDetail.this)
                            .load(profileUrl)
                            .fit()
                            .centerInside()
                            .into(ivProfile);
                } else {

                    ivProfile.setImageResource(R.drawable.default_profile_pic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        retrievePosts();

        ibMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", tvEmail.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        ibPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + UserNumber));
                startActivity(intent);
            }
        });

        ibSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (UserNumber == null) {
                    return;
                }
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", UserNumber, null)));

            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileUrl == null) {
                    return;
                }
                Intent i = new Intent(getApplicationContext(), ProfileImageDetail.class);
                i.putExtra("imageUrl",profileUrl);
                i.putExtra("imageUrl", profileUrl);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity )ProfileDetail.this,(ImageView)ivProfile,"profileImage");
                startActivity(i,options.toBundle());

            }
        });
    }

    private void retrievePosts() {

        final DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference().child("Crops").child(uid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                        Crop crop = postSnapShot.getValue(Crop.class);
                        mList.add(crop);

                    }
                    mAdapter = new ProfilePostsAdapter(ProfileDetail.this, mList);
                    LinearLayoutManager llm = new LinearLayoutManager(ProfileDetail.this, HORIZONTAL, false);
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new ProfilePostsAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(int position) {
                            Intent i = new Intent(ProfileDetail.this, CropDetailPublic.class);
                            i.putExtra("Crop item",  mList.get(position));

                            Log.d(TAG, "Serializing...");
                            startActivity(i);
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "insideDataSnapShot error::" + e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "OnCancelled::" + databaseError);
            }
        });
    }


}
