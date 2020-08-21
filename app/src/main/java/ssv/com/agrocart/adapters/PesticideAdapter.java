package ssv.com.agrocart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ssv.com.agrocart.PesticideSample;
import ssv.com.agrocart.R;

public class PesticideAdapter extends RecyclerView.Adapter<PesticideAdapter.PesticideViewHolder> {

    Context mContext;
    List<PesticideSample> mSamples;
    OnItemClickListener mListener;

    public PesticideAdapter(Context context, List<PesticideSample> sample){
        mSamples = sample;
        mContext = context;
    }

    public interface OnItemClickListener{
        void OnItemClick(int Position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener =(OnItemClickListener) listener;
    }

    @NonNull
    @Override
    public PesticideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pesticide_sample, parent, false);
        return new PesticideViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PesticideViewHolder holder, int position) {
        PesticideSample current = mSamples.get(position);
        holder.title.setText(current.getTitle());
        holder.desc.setText(current.getDesc());
        holder.price.setText(current.getPrice());
        holder.rating.setText(current.getRating());
        holder.thumbnail.setImageResource(current.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return mSamples.size();
    }

    static class PesticideViewHolder extends RecyclerView.ViewHolder {

        TextView title, desc, rating, price;
        ImageView thumbnail;

        PesticideViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tvTitle);
            desc = (TextView) itemView.findViewById(R.id.tvDesc);
            rating = (TextView)itemView.findViewById(R.id.tvRating);
            price = (TextView)itemView.findViewById(R.id.tvPrice);
            thumbnail = itemView.findViewById(R.id.ivThumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position =getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);
                        }
                    }
                }
            });

        }
    }


}
