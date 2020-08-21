package ssv.com.agrocart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewPosts extends RecyclerView.Adapter<RecyclerViewPosts.ViewHolder> implements Filterable {

    private static final String TAG = "RecyclerViewPosts";

    private Context mContext;
    private List<Crop> mCrop;
    private List<Crop> cropFull;
    private  OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = (OnItemClickListener) listener;
    }

    public RecyclerViewPosts(List<Crop> crop, Context context) {

        mContext = context;
        mCrop = crop;
        cropFull = new ArrayList<>(mCrop);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.crops_post_sample, parent,false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "OnbindViewHoder");
        final Crop cropCurrent = mCrop.get(position);
        holder.crop_name.setText(cropCurrent.getName());
        holder.crop_price.setText(cropCurrent.getPrice());
        holder.crop_weight.setText(cropCurrent.getWeight());


    }

    @Override
    public int getItemCount() {
        return  mCrop.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView crop_name;
        TextView crop_price;
        TextView crop_weight;
        CardView parent_layout;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            crop_name = itemView.findViewById(R.id.tvCropName);
            crop_price = itemView.findViewById(R.id.tvCropPrice);
            crop_weight = itemView.findViewById(R.id.tvCropWeight);
            parent_layout = itemView.findViewById(R.id.parent_layout);

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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Crop> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(cropFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Crop  item: cropFull){
                    if (item.name.toLowerCase().contains(filterPattern) || item.price.toLowerCase().contains(filterPattern) || item.weight.toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mCrop.clear();
            mCrop.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

}
