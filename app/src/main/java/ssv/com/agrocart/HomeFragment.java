package ssv.com.agrocart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.actions.SearchIntents;

import org.w3c.dom.Text;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private View v;
    private TextView title;
    private CardView marketYard,weather,market, news,policies,credit, about,gChat;
    private ImageButton version,share,rate;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       v = inflater.inflate(R.layout.fragment_main_home,container, false);

       marketYard = v.findViewById(R.id.cvMarketYard);
       market = v.findViewById(R.id.cvMarket);
       weather =v.findViewById(R.id.cvWeather);
       news = v.findViewById(R.id.cvNews);
       policies = v.findViewById(R.id.cvPolicies);
       title = v.findViewById(R.id.title);
       version=  v.findViewById(R.id.version);
       share = v.findViewById(R.id.share);
       rate = v.findViewById(R.id.rate);
       credit = v.findViewById(R.id.cvCredit);
       about = v.findViewById(R.id.cvAbout);
       gChat = v.findViewById(R.id.cvGChat);

       Objects.requireNonNull(getActivity()).findViewById(R.id.appBar).setVisibility(View.VISIBLE);

       gChat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getContext(),Global_chats.class));
           }
       });


       credit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AlertDialog dialog = new AlertDialog.Builder(getContext())
                       .setTitle("Credit")
                       .setMessage(" Yash Patel (Beta Tester) \n Ankit Patel (Beta Tester) \n Meet Mistry (Beta Tester)")
                       .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.cancel();
                           }
                       })
                       .create();
               dialog.show();
           }
       });

       about.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                startActivity(new Intent(getContext(),About.class));
           }
       });

       share.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent sharingIntent = new Intent(Intent.ACTION_SEND);
               sharingIntent.setType("text/plain");
               String shareBody = "Join us at Telegram channel https://t.me/codeInair";
               sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "extra subject");
               sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
               startActivity(Intent.createChooser(sharingIntent, "Share via"));
           }
       });

        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater1 = getLayoutInflater();
                View dialogView = inflater1.inflate(R.layout.alert_dialog_version,null);
                dialogBuilder.setView(dialogView);

                dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                dialogBuilder.setPositiveButton("ok",null);
                dialogBuilder.show();
            }
        });

        market.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getContext(),Market.class));
           }
       });

       marketYard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(getContext(),MarketYard.class);
               startActivity(i);
           }
       });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(getContext(),Weather.class);
                startActivity(j);

            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().remove(HomeFragment.this);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment()).commit();
                try {
                    getActivity().findViewById(R.id.appBar).setVisibility(View.GONE);
                }catch (Exception ignored){
                    Toast.makeText(getContext(), ""+ignored, Toast.LENGTH_SHORT).show();
                }
            }
        });

        policies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().remove(HomeFragment.this);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new PoliciesFragment()).commit();
                try {
                    getActivity().findViewById(R.id.appBar).setVisibility(View.GONE);
                }catch (Exception ignored){
                    Toast.makeText(getContext(), ""+ignored, Toast.LENGTH_SHORT).show();
                }
            }
        });

       return  v;
    }
}
