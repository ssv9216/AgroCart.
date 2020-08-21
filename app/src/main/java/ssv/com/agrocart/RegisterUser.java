package ssv.com.agrocart;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class RegisterUser extends AppCompatActivity{
//    protected static long ROW = 0;


    public EditText etName,etState,etPhone, etDist, etTaluka, etVillage, etPass, etConfPass, etEmail;
    public Button btnSignUp,btnLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    private int INTERNET_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);

        etPass = findViewById(R.id.etPass);
        etConfPass = findViewById(R.id.etConfPass);

        btnSignUp = findViewById(R.id.signUp);
        btnLogin = findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();


        requestInternetPermission();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterUser.this,loginpage.class);
                startActivity(i);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = etName.getText().toString();
                final String phone = etPhone.getText().toString();
                final String pass = etPass.getText().toString();
                final String confPass = etConfPass.getText().toString();
                final String Email = etEmail.getText().toString();

//                if(ContextCompat.checkSelfPermission(RegisterUser.this,
//                        Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(RegisterUser.this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                    requestInternetPermission();
//                }else{
//                                requestInternetPermission();
//                }

                if (!Email.equals("") && !Email.equals(".") && !name.equals("") && !name.equals(".") && !phone.equals("") &&  !pass.equals("") && !confPass.equals("") ) {
                    if (phone.length() != 10) {

                        Toast.makeText(RegisterUser.this, "number should be in 10 digit ", Toast.LENGTH_SHORT).show();
                    }else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        Toast.makeText(RegisterUser.this, "Enter valid email address", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (!pass.equals(confPass) && pass.length() <= 8) {
                            Toast.makeText(RegisterUser.this, "password must be same and greater than 6 letter", Toast.LENGTH_SHORT).show();
                        } else {


                            registerUser();

                        }
                    }
                } else {
                    Toast.makeText(RegisterUser.this, "Enter Detail Properly", Toast.LENGTH_SHORT).show();
                }
            }



        });
    }



    private void requestInternetPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.INTERNET )){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("It's required")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(RegisterUser.this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == INTERNET_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
            else{
                Toast.makeText(this, "Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUser() {
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        final String email = etEmail.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterUser.this, "Login now", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterUser.this,loginpage.class));
                            finish();
                            progressDialog.cancel();
                        }
                        else{
                            Toast.makeText(RegisterUser.this, "Try again", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                });
    }


}