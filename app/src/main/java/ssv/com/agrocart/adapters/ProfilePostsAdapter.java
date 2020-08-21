package ssv.com.agrocart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ssv.com.agrocart.Crop;
import ssv.com.agrocart.MyPostsGetterSetter;
import ssv.com.agrocart.R;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.ViewHolder>{

    Context mContext;
    List<Crop> mList;
    private OnItemClickListener mListener;

    public ProfilePostsAdapter(Context context, List<Crop> list){
        mContext = context;
        mList = list;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener ){
        mListener = (OnItemClickListener)listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crops_post_sample,parent,false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cropName.setText(mList.get(position).getName());
        holder.cropWeight.setText(mList.get(position).getWeight());
        holder.cropPrice.setText(mList.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView cropName, cropPrice,cropWeight;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            cropName = itemView.findViewById(R.id.tvCropName);
            cropPrice = itemView.findViewById(R.id.tvCropPrice);
            cropWeight = itemView.findViewById(R.id.tvCropWeight);

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
