package ssv.com.agrocart;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    public static String TAG = "ProfileFragment";
    private DatabaseReference reff;
    private User user;
    private Button cart;
    private EditText etName, etPhone, etAddress, etEmail;
    private ImageView profileImage;
    private ImageButton profileButton;
    private Uri imageUri;
    StorageReference mStorageRef;
    private static String imageUrl = null;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        LinearLayout ll2 = view.findViewById(R.id.ll2);
        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        etAddress = view.findViewById(R.id.etAddress);
        final Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnSave = view.findViewById(R.id.btnSave);
        final Button btnLogout = view.findViewById(R.id.btnLogout);
        etEmail = view.findViewById(R.id.etEmail);
        Button myPosts = view.findViewById(R.id.ibMyPosts);
        profileImage = view.findViewById(R.id.ivProfile);
        profileButton = view.findViewById(R.id.ibProfile);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading");
        //firebase storage
        mStorageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(getFirebaseUid());

        etName.setEnabled(false);
        etPhone.setEnabled(false);
        etEmail.setEnabled(false);
        etAddress.setEnabled(false);

        ll2.isVerticalScrollBarEnabled();

        displayCurrentUserData();

        etEmail.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setEnabled(false);
                etPhone.setEnabled(false);
                etEmail.setEnabled(false);
                etAddress.setEnabled(false);

                if (isInternetConnection()) {

                    if (!etName.getText().toString().equals("") && !etEmail.getText().toString().equals("") && !etPhone.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
                        user.setName(etName.getText().toString().trim());
                        user.setEmail(etEmail.getText().toString().trim());
                        user.setAddress(etAddress.getText().toString());
                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(etEmail.getText().toString());

                        if (etPhone.getText().toString().length() == 10) {
                            user.setNumber(etPhone.getText().toString());
                            reff.child(getFirebaseUid()).setValue(user);
                            Toast.makeText(getContext(), "Data Updated ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Phone number should have 10 digit", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Enter all data properly", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "check Internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setEnabled(true);
                etPhone.setEnabled(true);
                etEmail.setEnabled(true);
                etAddress.setEnabled(true);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                prefs.edit().putBoolean("isLogin", false).apply();
                Toast.makeText(getContext(), "Logging out", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
                Objects.requireNonNull(getActivity()).finish();

            }
        });

        //Cart (My Posts)
        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyPosts.class));
            }
        });

        myPosts.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getContext(), "My Posts", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChooser();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDetailActivity();
            }
        });

        return view;
    }

    private void startDetailActivity() {
        if (imageUrl == null) {
            Toast.makeText(getContext(), "Add photo", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(getContext(), ProfileImageDetail.class);
            i.putExtra("imageUrl", imageUrl);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(), profileImage, "profileImage");
            startActivity(i, options.toBundle());
        }

    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

//            AsyncTask.execute(new Runnable() {
//                @Override
//                public void run() {
//                    uploader();
//                }
//            });
            uploader();

        }
    }

    //    private String getExtension(Uri uri){
//        ContentResolver cr = null;
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        assert false;
//        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
//    }
    private void uploader() {
        progressDialog.show();
        final StorageReference Ref = mStorageRef.child("Profile.jpg");
        Ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        //createNewPost(imageUrl);

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(getFirebaseUid());
                                        ref.child("ProfileImageUrl").setValue(imageUrl);
                                    }
                                });
                            }
                        }

                        Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void displayCurrentUserData() {
        try {

            // Get current user id
            FirebaseUser currentFirebaseuser = FirebaseAuth.getInstance().getCurrentUser();
            assert currentFirebaseuser != null;
            String userid = currentFirebaseuser.getUid();
            getFirebaseUid();
            etEmail.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
            user = new User();
            reff = FirebaseDatabase.getInstance().getReference().child("User");

            reff.child(getFirebaseUid()).child("ProfileImageUrl").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        imageUrl = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                        Picasso.with(getContext())
                                .load(imageUrl)
                                .fit()
                                .centerInside()
                                .into(profileImage);

                    } catch (Exception e) {
                        Log.d(TAG, "" + e);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            if (isInternetConnection()) {
                try {
                    reff.child(userid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                User user = dataSnapshot.getValue(User.class);
                                assert user != null;
                                String name = user.getName();
                                String email = user.getEmail();
                                String phone = user.getNumber();
                                String address = user.getAddress();

                                etEmail.setText(email);
                                etName.setText(name);
                                etPhone.setText(phone);
                                etAddress.setText(address);

                            } else {
                                Toast.makeText(getContext(), "Add detail", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFirebaseUid() {
        FirebaseUser currentFirebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        return currentFirebaseuser.getUid();
    }

    private boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }


}
