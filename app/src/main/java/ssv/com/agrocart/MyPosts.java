package ssv.com.agrocart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyPosts extends AppCompatActivity {

    private static final String TAG ="MyPosts" ;
    private RecyclerView mRecyclerView;
    private MyPostsRecyclerViewAdapter mAdapter;
    private List<MyPostsGetterSetter> mCrop;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        toolbar = findViewById(R.id.mpToolbar);

        setActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setTitle("My Posts");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.rvPosts);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mCrop = new ArrayList<>();
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Crops").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    MyPostsGetterSetter crop = postSnapshot.getValue(MyPostsGetterSetter.class);
                    assert crop != null;
                    crop.setmKey(postSnapshot.getKey());
                    mCrop.add(crop);
                    Log.d(TAG, ""+crop);
                }
                try {

                    mAdapter = new MyPostsRecyclerViewAdapter((Context)MyPosts.this, mCrop);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new MyPostsRecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(int position) {

                            Intent i = new Intent(MyPosts.this, CropDetail.class);
                            String c_name = mCrop.get(position).getName();
                            String c_price = mCrop.get(position).getPrice();
                            String c_weight = mCrop.get(position).getWeight();
                            String c_desc = mCrop.get(position).getDescription();

                            i.putExtra("c_name", c_name);
                            i.putExtra("c_price", c_price);
                            i.putExtra("c_weight", c_weight);
                            i.putExtra("c_desc", c_desc);

                            startActivity(i);
                        }
                    });

                }catch (Exception e){
                    Log.d(TAG, "Error from Catch:"+ e);
                    Toast.makeText(MyPosts.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyPosts.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }
}
