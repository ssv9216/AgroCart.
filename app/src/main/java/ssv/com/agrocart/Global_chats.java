package ssv.com.agrocart;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.internal.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ssv.com.agrocart.adapters.GlobalChatAdapter;


public class Global_chats extends AppCompatActivity {

    RecyclerView recyclerView;
    GlobalChatAdapter mAdapter;
    List<GlobalChat> globalChat;
    DatabaseReference reff;
    FloatingActionButton fab;
    EditText messageInput;
    public static String TAG = "GlobalChat";
    private String currentTime;
    private String currentDate;
    private String msgKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chats);

         currentTime = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
         currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());




        try {
            Objects.requireNonNull(getActionBar()).setTitle("Global Chats");
            getSupportActionBar().setTitle("Global Chats");
        }catch (Exception e){Log.d(TAG,""+e);}
        fab = findViewById(R.id.fabSend);
        messageInput = findViewById(R.id.etMsgInput);

        globalChat = new ArrayList<>();
        recyclerView = findViewById(R.id.agcRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //SnapHelper
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        mAdapter = new GlobalChatAdapter(getApplicationContext(), globalChat);
        recyclerView.setAdapter(mAdapter);

//        globalChat.add(new GlobalChat(""+currentTime, "56546848", currentTime,currentDate));

        reff = FirebaseDatabase.getInstance().getReference();
        reff.child("GlobalMessages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                globalChat.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    try {
                        String msg = postSnapShot.child("message").getValue().toString();
                        String uid = postSnapShot.child("userId").getValue().toString();
                        String time = postSnapShot.child("time").getValue().toString();
                        msgKey = postSnapShot.getKey();
                        Log.d(TAG,"Keys"+msgKey);
                        globalChat.add(new GlobalChat(msg,uid, time, currentDate));

                        Log.d(TAG, msg);
                    }catch (Exception e){
                        Log.d(TAG,""+e);
                    }
                }
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(globalChat.size()-1);



            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reff = reff.child("GlobalMessages");
        final List<GlobalChat> newList = new ArrayList<>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageInput.getText().toString().equals(" ") || messageInput.getText().toString().trim().equals("")) {
                    Toast.makeText(Global_chats.this, "Type something", Toast.LENGTH_SHORT).show();
                } else {
                    messageInput.clearFocus();
                    globalChat.clear();
                    reff.push().setValue(new GlobalChat(messageInput.getText().toString(), FirebaseAuth.getInstance().getUid(), currentTime,currentDate));
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(messageInput.getWindowToken(), 0);
                    recyclerView.scrollToPosition(globalChat.size()-1);
                    messageInput.setText(null);
                }
            }
        });


    }



}
