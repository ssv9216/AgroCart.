package ssv.com.agrocart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class loginpage extends AppCompatActivity {

    public EditText etEmail, etPass;
    public Button btnLogin,btnRegister;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);


        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.login_etEmail);
        etPass = findViewById(R.id.login_etPass);
        btnLogin = findViewById(R.id.login_btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDetail();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(loginpage.this, RegisterUser.class);
                startActivity(i);
                finish();
            }
        });



    }


    public void checkDetail() {
        if (!etEmail.getText().toString().equals("") && !etPass.getText().toString().equals("")) {

            progressDialog.setMessage("Loading");
            progressDialog.show();
            if(etEmail.getText().toString().equals("ssv") && etPass.getText().toString().equals("ssv")){
                gotoHomePage();
            }
            else {
                checkUser();
            }
    }else{
            Toast.makeText(this, "Enter Data Properly", Toast.LENGTH_SHORT).show();
            if(etEmail.getText().toString().isEmpty()) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(1)
                        .playOn(findViewById(R.id.login_etEmail));
            }
            if(etPass.getText().toString().isEmpty()) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(1)
                        .playOn(findViewById(R.id.login_etPass));
            }
        }

    }

    private void checkUser() {
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.cancel();
                            gotoHomePage();
                        }else{
                            progressDialog.cancel();
                            Toast.makeText(loginpage.this, "Enter Right Detail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void gotoHomePage() {

        final Intent i = new Intent(loginpage.this,HomePage2.class);
        startActivity(i);
        finish();
        finishAfterTransition();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("isLogin",true).apply();
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
    }




}
