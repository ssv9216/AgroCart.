package ssv.com.agrocart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ssv.com.agrocart.CropRatesSample;
import ssv.com.agrocart.R;

public class MarketYardAdapter extends RecyclerView.Adapter<MarketYardAdapter.ViewHolder> {

    private List<CropRatesSample> mList ;

    public MarketYardAdapter(List<CropRatesSample> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_yard_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.marketCenter.setText(String.format(": %s", mList.get(position).getCity()));
        holder.arrival.setText(String.format((": %s"), mList.get(position).getArrivals()));
        holder.modalPrice.setText(String.format(": %s",mList.get(position).getModelPrice()));
        holder.MinMaxPrice.setText(String.format((": %s"),mList.get(position).getMinMaxPrice()));


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView marketCenter, arrival, modalPrice, MinMaxPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            marketCenter = itemView.findViewById(R.id.mcData);
            arrival = itemView.findViewById(R.id.arrData);
            modalPrice = itemView.findViewById(R.id.modelData);
            MinMaxPrice = itemView.findViewById(R.id.MinMaxData);
        }
    }
}
