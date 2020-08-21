package ssv.com.agrocart;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SellFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    View view;
    private EditText price,weight,description;
    private Spinner spinner;
//database reference
    private DatabaseReference reff;
// import class Crop
    private Crop crop;
//counter crop increase
    private long maxid =0;
    private long publicMaxid = 0;
    public static String TAG ="SellFrament";
    private RelativeLayout rl1;
    private ImageView wifiOff;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sell, container, false);
        weight = view.findViewById(R.id.etWeight);
        price = view.findViewById(R.id.etPrice);
        description = view.findViewById(R.id.etDesc);
        Button post = view.findViewById(R.id.btnPost);

//Spinner for select crop
        final Spinner spinner = view.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()), R.array.crops_names, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        rl1 =view.findViewById(R.id.rl1);
        wifiOff = view.findViewById(R.id.ivOff);

        if (isInternetConnection() ){
            rl1.setVisibility(View.VISIBLE);
            wifiOff.setVisibility(View.GONE);
        }else{
            rl1.setVisibility(View.GONE);
            wifiOff.setVisibility(View.VISIBLE);
        }


        crop= new Crop();
//Get current user id

        getFirebaseUserId();
//        set reference to child "Crops"
        reff = FirebaseDatabase.getInstance().getReference();

        reff.child("Crops").child(getFirebaseUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                    if already crop available then count child and store to maxid
                    maxid=dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", "ERor:"+databaseError);
            }
        });

        reff.child("PublicPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    publicMaxid = dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("SellFragment", "public listener");
            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetConnection()) {
                    if (weight.getText().toString().isEmpty() || price.getText().toString().isEmpty() || description.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Enter All Details", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "inside else");
                        postToFirebase();
                        weight.getText().clear();
                        price.getText().clear();
                        description.getText().clear();
                        Toast.makeText(getContext(), "adding", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d(TAG, "inside big else");
                    Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;

    }

    private String getFirebaseUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        return currentUser. getUid();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getContext(), "Nothing Selected!", Toast.LENGTH_SHORT).show();
    }
    private void postToFirebase () {
        EditText weight = view.findViewById(R.id.etWeight);
        price= view.findViewById(R.id.etPrice);
        spinner=view.findViewById(R.id.spinner1);
        String name = spinner.getSelectedItem().toString();

            crop.setName(name);
            crop.setWeight(weight.getText().toString());
            crop.setPrice(price.getText().toString());
            crop.setUid(FirebaseUserId());
            crop.setDescription(description.getText().toString());


            try {
                reff.child("Crops").child(getFirebaseUserId()).child(String.valueOf(maxid + 1)).setValue(crop);

                reff.child("Crops").child("PublicPost").push().setValue(crop);


                Toast.makeText(getContext(), "Posted", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getContext(), ""+e, Toast.LENGTH_LONG).show();
            }
    }

//    checking internet connection

    private boolean isInternetConnection()
    {
        ConnectivityManager connectivityManager =  (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();

    }

    private String FirebaseUserId(){
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

}
