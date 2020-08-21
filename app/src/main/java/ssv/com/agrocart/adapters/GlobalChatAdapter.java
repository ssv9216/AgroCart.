package ssv.com.agrocart.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ssv.com.agrocart.GlobalChat;
import ssv.com.agrocart.R;

public class GlobalChatAdapter  extends RecyclerView.Adapter {
    public static final int VIEW_TYPE_MESSAGE_SENT = 1;
    public static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private List<GlobalChat> globalChat;
    private Context mContext;
    private String name;


    public GlobalChatAdapter(Context context, List<GlobalChat> chat){
        globalChat = chat;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return globalChat.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (globalChat.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())){
            return VIEW_TYPE_MESSAGE_SENT;
        }else{
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sent,parent,false);
            return new SentChatHolder(view);
        }else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_received,parent,false);
            return new ReceivedChatHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GlobalChat currentChat = (GlobalChat) globalChat.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentChatHolder) holder).bind(currentChat);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedChatHolder) holder).bind(currentChat);
                break;
        }
    }




    static class ReceivedChatHolder extends RecyclerView.ViewHolder{

        TextView received,senderName,time;

        public ReceivedChatHolder(@NonNull View itemView) {
            super(itemView);
            received = itemView.findViewById(R.id.msgReceived);
            senderName = itemView.findViewById(R.id.tvSenderName);
            time = itemView.findViewById(R.id.mrTime);
        }
        void bind(GlobalChat chat){
            received.setText(chat.getMessage());
            time.setText(chat.getTime());
            DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("User").child(chat.getUserId());
            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                        senderName.setText(name);

                    }catch (Exception e){
                        Log.d("adapter",""+e);}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


    static class SentChatHolder extends RecyclerView.ViewHolder{

        TextView send,time;

        public SentChatHolder(@NonNull View itemView) {
            super(itemView);

            send = itemView.findViewById(R.id.msgSent);
            time = itemView.findViewById(R.id.msTime);
        }
        void bind(GlobalChat chat){
            send.setText(chat.getMessage());
            time.setText(chat.getTime());
        }
    }

}
