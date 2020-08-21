package ssv.com.agrocart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PoliciesAdapter extends RecyclerView.Adapter<PoliciesAdapter.ViewHolder> {

    private List<Policies> mPolicies;
    private Context mContext;

    public PoliciesAdapter(Context context, List<Policies> policies){
        mContext = context;
        mPolicies = policies;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.policies_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Policies currentItem = mPolicies.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.desc.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return mPolicies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.psTitle);
            desc = itemView.findViewById(R.id.psDesc);
        }
    }
}
